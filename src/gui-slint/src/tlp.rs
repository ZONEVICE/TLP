//! Everything that touches the system: tlpv, /etc/tlp.conf and the power supply.

use std::fs;
use std::io::{self, Read};
use std::path::{Path, PathBuf};
use std::process::{Command, Stdio};
use std::sync::mpsc;
use std::thread;

/// The TLP setting that `tlpv` changes.
#[derive(Clone, Copy, Debug, PartialEq, Eq)]
pub enum Mode {
    /// `tlpv bf`: CPU_SCALING_MAX_FREQ_ON_BAT
    Bat = 0,
    /// `tlpv af`: CPU_SCALING_MAX_FREQ_ON_AC
    Ac = 1,
}

impl Mode {
    pub fn toggled(self) -> Mode {
        match self {
            Mode::Bat => Mode::Ac,
            Mode::Ac => Mode::Bat,
        }
    }

    /// First argument of tlpv.
    pub fn arg(self) -> &'static str {
        match self {
            Mode::Bat => "bf",
            Mode::Ac => "af",
        }
    }

    /// Prefix of the option labels ("BF 8", "AC 8").
    pub fn label(self) -> &'static str {
        match self {
            Mode::Bat => "BF",
            Mode::Ac => "AC",
        }
    }
}

/// CPU_SCALING_MAX_FREQ_ON_* of /etc/tlp.conf in tlpv units (kHz / 100000).
#[derive(Clone, Copy, Debug, Default, PartialEq, Eq)]
pub struct MaxFreqs {
    pub bat: Option<i32>,
    pub ac: Option<i32>,
}

pub fn read_max_freqs(path: &Path) -> io::Result<MaxFreqs> {
    Ok(parse_max_freqs(&fs::read_to_string(path)?))
}

/// As in TLP, the last assignment of a parameter wins.
pub fn parse_max_freqs(conf: &str) -> MaxFreqs {
    let mut freqs = MaxFreqs::default();
    for line in conf.lines() {
        let Some((key, value)) = line.trim().split_once('=') else {
            continue;
        };
        let slot = match key.trim_end() {
            "CPU_SCALING_MAX_FREQ_ON_BAT" => &mut freqs.bat,
            "CPU_SCALING_MAX_FREQ_ON_AC" => &mut freqs.ac,
            _ => continue,
        };
        *slot = to_tlpv_units(value);
    }
    freqs
}

fn to_tlpv_units(value: &str) -> Option<i32> {
    let value = value.split('#').next()?.trim().trim_matches('"');
    let khz: i64 = value.parse().ok()?;
    (khz > 0 && khz % 100_000 == 0)
        .then(|| i32::try_from(khz / 100_000).ok())
        .flatten()
}

/// Whether a "Mains" power supply (the AC adapter) is online; `None` if there is no such supply.
pub fn on_ac_power() -> Option<bool> {
    let mut found = None;
    for entry in fs::read_dir("/sys/class/power_supply").ok()?.flatten() {
        let path = entry.path();
        if read_trimmed(&path.join("type")).as_deref() == Some("Mains") {
            let online = read_trimmed(&path.join("online")).as_deref() == Some("1");
            found = Some(found.unwrap_or(false) || online);
        }
    }
    found
}

fn read_trimmed(path: &Path) -> Option<String> {
    fs::read_to_string(path)
        .ok()
        .map(|text| text.trim().to_owned())
}

pub fn is_root() -> bool {
    // SAFETY: geteuid has no preconditions and cannot fail.
    unsafe { libc::geteuid() == 0 }
}

/// Runs `tlpv <bf|af> <value>` and returns what it printed, stdout and stderr interleaved the
/// same way the Java GUI shows them.
pub fn run_tlpv(tlpv: &Path, mode: Mode, value: i32) -> String {
    run(tlpv, &[mode.arg(), &value.to_string()])
        .unwrap_or_else(|err| format!("Error: cannot run {}: {err}", tlpv.display()))
}

fn run(program: &Path, args: &[&str]) -> io::Result<String> {
    let (mut reader, writer) = io::pipe()?;
    let mut command = Command::new(program);
    command
        .args(args)
        .stdin(Stdio::null())
        .stdout(writer.try_clone()?)
        .stderr(writer);
    let mut child = command.spawn()?;
    // Closes our copies of the pipe's write end, otherwise the read below never ends.
    drop(command);
    let mut bytes = Vec::new();
    reader.read_to_end(&mut bytes)?;
    let status = child.wait()?;
    let mut output = String::from_utf8_lossy(&bytes).trim_end().to_owned();
    if !status.success() {
        if !output.is_empty() {
            output.push('\n');
        }
        output.push_str(&format!("[tlpv {status}]"));
    }
    Ok(output)
}

pub struct Job {
    pub mode: Mode,
    pub value: i32,
    /// Title of the status area, e.g. "12:00:00 · Mouse idle → BF 8".
    pub title: String,
}

/// Runs the jobs one at a time on a worker thread: the GUI never blocks, and two tlpv never
/// rewrite /etc/tlp.conf at the same time.
pub struct Runner {
    jobs: mpsc::Sender<Job>,
}

impl Runner {
    /// `report` receives the title and text for the status area and, once a job is done, the
    /// values then written in `conf`.
    pub fn spawn(
        tlpv: PathBuf,
        conf: PathBuf,
        report: impl Fn(String, String, Option<MaxFreqs>) + Send + 'static,
    ) -> Runner {
        let (jobs, queue) = mpsc::channel::<Job>();
        thread::Builder::new()
            .name("tlpv".into())
            .spawn(move || {
                for job in queue {
                    report(job.title.clone(), "Running…".into(), None);
                    let output = run_tlpv(&tlpv, job.mode, job.value);
                    report(job.title, output, read_max_freqs(&conf).ok());
                }
            })
            .expect("cannot start the tlpv thread");
        Runner { jobs }
    }

    pub fn run(&self, job: Job) {
        // Sending only fails once the worker is gone, which happens only when the Runner is dropped.
        let _ = self.jobs.send(job);
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn parses_max_freqs() {
        let conf = "\
#CPU_SCALING_MAX_FREQ_ON_AC=0
CPU_SCALING_MAX_FREQ_ON_AC=4200000
#CPU_SCALING_MIN_FREQ_ON_BAT=400000
CPU_SCALING_MAX_FREQ_ON_BAT=\"800000\"
CPU_BOOST_ON_BAT=0
";
        assert_eq!(
            parse_max_freqs(conf),
            MaxFreqs {
                bat: Some(8),
                ac: Some(42)
            }
        );
    }

    #[test]
    fn last_assignment_wins() {
        let conf = "CPU_SCALING_MAX_FREQ_ON_BAT=800000\nCPU_SCALING_MAX_FREQ_ON_BAT=2200000\n";
        assert_eq!(parse_max_freqs(conf).bat, Some(22));
    }

    #[test]
    fn values_tlpv_cannot_set_are_unknown() {
        assert_eq!(
            parse_max_freqs("CPU_SCALING_MAX_FREQ_ON_BAT=1650000").bat,
            None
        );
        assert_eq!(parse_max_freqs("CPU_SCALING_MAX_FREQ_ON_AC=").ac, None);
        assert_eq!(
            parse_max_freqs("CPU_SCALING_MAX_FREQ_ON_AC=1600000  # comment").ac,
            Some(16)
        );
    }

    #[test]
    fn interleaves_stdout_and_stderr_and_reports_failures() {
        let sh = Path::new("/bin/sh");
        let output = run(sh, &["-c", "echo out; echo err >&2; echo end"]).unwrap();
        assert_eq!(output, "out\nerr\nend");
        let output = run(sh, &["-c", "echo nope; exit 3"]).unwrap();
        assert_eq!(output, "nope\n[tlpv exit status: 3]");
        let output = run_tlpv(Path::new("/nonexistent/tlpv"), Mode::Bat, 8);
        assert!(
            output.starts_with("Error: cannot run /nonexistent/tlpv"),
            "{output}"
        );
    }

    #[test]
    fn runner_reports_progress_and_config() {
        use std::os::unix::fs::PermissionsExt;

        let dir = std::env::temp_dir().join(format!("tlp-gui-slint-test-{}", std::process::id()));
        fs::create_dir_all(&dir).unwrap();
        let tlpv = dir.join("tlpv");
        let conf = dir.join("tlp.conf");
        let script = "#!/bin/sh\necho \"CPU_SCALING_MAX_FREQ_ON_BAT=${2}00000\" > \"$(dirname \"$0\")/tlp.conf\"\necho \"args: $*\"\n";
        fs::write(&tlpv, script).unwrap();
        fs::set_permissions(&tlpv, fs::Permissions::from_mode(0o755)).unwrap();

        let (tx, rx) = mpsc::channel();
        let runner = Runner::spawn(tlpv, conf, move |title, text, freqs| {
            tx.send((title, text, freqs)).unwrap()
        });
        runner.run(Job {
            mode: Mode::Bat,
            value: 16,
            title: "T".into(),
        });
        assert_eq!(
            rx.recv().unwrap(),
            ("T".to_owned(), "Running…".to_owned(), None)
        );
        assert_eq!(
            rx.recv().unwrap(),
            (
                "T".to_owned(),
                "args: bf 16".to_owned(),
                Some(MaxFreqs {
                    bat: Some(16),
                    ac: None
                })
            )
        );
        fs::remove_dir_all(dir).unwrap();
    }
}
