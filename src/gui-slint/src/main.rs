//! Compact Slint GUI for tlpv (src/core). It replaces the Java Swing GUI of src/gui with the same
//! look and a fraction of its memory. Like the Java GUI, it has to run as root: tlpv rewrites
//! /etc/tlp.conf and restarts TLP.

mod idle;
mod tlp;

use std::cell::RefCell;
use std::error::Error;
use std::fs;
use std::io;
use std::path::{Path, PathBuf};
use std::rc::{Rc, Weak};
use std::time::{Duration, Instant, SystemTime};

use slint::{ComponentHandle, Timer, TimerMode};

use idle::{IdleEvent, IdleWatcher, Pointer, Position};
use tlp::{Job, MaxFreqs, Mode, Runner};

slint::include_modules!();

/// Paths used by the GUI, overridable through environment variables to try it without root.
const TLPV: (&str, &str) = ("TLP_GUI_TLPV", "/usr/local/bin/tlpv");
const TLP_CONF: (&str, &str) = ("TLP_GUI_CONF", "/etc/tlp.conf");
/// Manual value restored after idle when /etc/tlp.conf has none (the Java GUI's default).
const DEFAULT_MANUAL_VALUE: i32 = 22;
const WATCH_INTERVAL: Duration = Duration::from_secs(1);
const CONF_CHECK_INTERVAL: Duration = Duration::from_secs(5);

fn main() -> Result<(), slint::PlatformError> {
    let tlpv = env_path(TLPV);
    let conf = env_path(TLP_CONF);

    let ui = AppWindow::new()?;
    // Needs the platform, which creating the window initializes; must happen before showing it.
    slint::set_xdg_app_id("tlp-gui-slint")?;

    let freqs = tlp::read_max_freqs(&conf);
    let current = freqs.as_ref().copied().unwrap_or_default();
    let on_ac = tlp::on_ac_power();
    // Start in the mode of the power source in use: its setting is the one that takes effect.
    let mode = if on_ac == Some(true) {
        Mode::Ac
    } else {
        Mode::Bat
    };
    ui.set_ac_mode(mode == Mode::Ac);
    show_freqs(&ui, current);
    let (title, text) = startup_status(&conf, &freqs, on_ac);
    ui.set_status_title(title.into());
    ui.set_status_text(text.into());

    let runner = {
        let ui = ui.as_weak();
        Runner::spawn(tlpv, conf.clone(), move |title, text, freqs| {
            let _ = ui.upgrade_in_event_loop(move |ui| {
                ui.set_status_title(title.into());
                ui.set_status_text(text.into());
                if let Some(freqs) = freqs {
                    show_freqs(&ui, freqs);
                }
            });
        })
    };

    let controller = Rc::new_cyclic(|this| {
        RefCell::new(Controller {
            this: this.clone(),
            ui: ui.as_weak(),
            runner,
            mode,
            manual: [
                current.bat.unwrap_or(DEFAULT_MANUAL_VALUE),
                current.ac.unwrap_or(DEFAULT_MANUAL_VALUE),
            ],
            pointer: None,
            watcher: None,
            watch_timer: Timer::default(),
            delayed: None,
            delay_timer: Timer::default(),
        })
    });

    ui.on_option_clicked({
        let controller = controller.clone();
        move |value| controller.borrow_mut().option_clicked(value)
    });
    ui.on_toggle_mode({
        let controller = controller.clone();
        move || controller.borrow_mut().toggle_mode()
    });
    ui.on_watch_clicked({
        let controller = controller.clone();
        move |value, seconds| controller.borrow_mut().watch_clicked(value, seconds)
    });
    ui.on_delay_clicked({
        let controller = controller.clone();
        move |value, minutes| controller.borrow_mut().delay_clicked(value, minutes)
    });

    // Keeps the highlighted button right when /etc/tlp.conf changes from outside (tlpv in a terminal).
    let _conf_timer = watch_conf(ui.as_weak(), conf);

    ui.run()
}

struct Controller {
    this: Weak<RefCell<Controller>>,
    ui: slint::Weak<AppWindow>,
    runner: Runner,
    mode: Mode,
    /// Value last chosen by hand (big button or timer) for each mode, indexed by `Mode`: what the
    /// idle watcher restores. The Java GUI kept one value for both modes.
    manual: [i32; 2],
    pointer: Option<Pointer>,
    watcher: Option<IdleWatcher>,
    watch_timer: Timer,
    /// Pending timed change: value and due time. Wall-clock time, so that a timer that expires
    /// while the laptop is suspended goes off right after resuming.
    delayed: Option<(i32, SystemTime)>,
    delay_timer: Timer,
}

impl Controller {
    fn apply(&self, mode: Mode, value: i32, reason: &str) {
        let target = format!("{} {value}", mode.label());
        let title = match reason {
            "" => format!("{} · {target}", clock()),
            reason => format!("{} · {reason} → {target}", clock()),
        };
        self.runner.run(Job { mode, value, title });
    }

    /// Sets `value` for the current mode as the user's choice: a big button, or the timer.
    fn set_manual(&mut self, value: i32, reason: &str) {
        let mode = self.mode;
        self.manual[mode as usize] = value;
        if let Some(watcher) = &mut self.watcher {
            watcher.manual_change(mode, Instant::now());
        }
        self.apply(mode, value, reason);
    }

    fn option_clicked(&mut self, value: i32) {
        // Like the Java GUI, get out of the way right away.
        minimize(self.ui.unwrap().window());
        self.set_manual(value, "");
    }

    /// The watcher and the timer follow the mode too: they apply to the mode current when they act.
    fn toggle_mode(&mut self) {
        self.mode = self.mode.toggled();
        self.ui.unwrap().set_ac_mode(self.mode == Mode::Ac);
    }

    fn watch_clicked(&mut self, value: i32, seconds: i32) {
        if self.watcher.is_some() {
            self.stop_watcher();
            return;
        }
        let ui = self.ui.unwrap();
        let position = match self.pointer_position() {
            Ok(position) => position,
            Err(err) => {
                show_error(&ui, "Mouse watcher", &err.to_string());
                return;
            }
        };
        let idle_after = Duration::from_secs(seconds.max(1).unsigned_abs().into());
        self.watcher = Some(IdleWatcher::new(
            value,
            idle_after,
            position,
            Instant::now(),
        ));
        let this = self.this.clone();
        self.watch_timer
            .start(TimerMode::Repeated, WATCH_INTERVAL, move || {
                if let Some(this) = this.upgrade() {
                    this.borrow_mut().poll_watcher();
                }
            });
        ui.set_watch_armed(true);
        minimize(ui.window());
    }

    fn stop_watcher(&mut self) {
        self.watch_timer.stop();
        self.ui.unwrap().set_watch_armed(false);
        // Don't leave the idle value behind.
        if let Some(mode) = self.watcher.take().and_then(|watcher| watcher.applied()) {
            self.apply(mode, self.manual[mode as usize], "Watcher off");
        }
    }

    fn poll_watcher(&mut self) {
        let position = match self.pointer_position() {
            Ok(position) => position,
            Err(err) => {
                show_error(&self.ui.unwrap(), "Mouse watcher stopped", &err.to_string());
                self.pointer = None;
                self.stop_watcher();
                return;
            }
        };
        let Some(watcher) = &mut self.watcher else {
            return;
        };
        match watcher.poll(position, Instant::now(), self.mode) {
            Some(IdleEvent::Idle { mode, value }) => self.apply(mode, value, "Mouse idle"),
            Some(IdleEvent::Moved { mode }) => {
                self.apply(mode, self.manual[mode as usize], "Mouse moved")
            }
            None => {}
        }
    }

    fn pointer_position(&mut self) -> Result<Position, Box<dyn Error>> {
        if self.pointer.is_none() {
            self.pointer = Some(Pointer::connect()?);
        }
        self.pointer.as_ref().expect("connected above").position()
    }

    /// Starts the timer, or cancels it when it is already running.
    fn delay_clicked(&mut self, value: i32, minutes: i32) {
        let ui = self.ui.unwrap();
        if self.delayed.take().is_some() {
            self.delay_timer.stop();
            ui.set_delay_armed(false);
            return;
        }
        let delay = Duration::from_secs(u64::from(minutes.max(1).unsigned_abs()) * 60);
        self.delayed = Some((value, SystemTime::now() + delay));
        ui.set_delay_remaining(countdown(delay).into());
        ui.set_delay_armed(true);
        let this = self.this.clone();
        self.delay_timer
            .start(TimerMode::Repeated, Duration::from_secs(1), move || {
                if let Some(this) = this.upgrade() {
                    this.borrow_mut().delay_tick();
                }
            });
        minimize(ui.window());
    }

    fn delay_tick(&mut self) {
        let Some((value, due)) = self.delayed else {
            return;
        };
        let ui = self.ui.unwrap();
        let remaining = due.duration_since(SystemTime::now()).unwrap_or_default();
        if remaining >= Duration::from_millis(500) {
            ui.set_delay_remaining(countdown(remaining).into());
            return;
        }
        self.delayed = None;
        self.delay_timer.stop();
        ui.set_delay_armed(false);
        // A delayed click on a big button, in the mode current now.
        self.set_manual(value, "Timer");
    }
}

/// Minimizes the window, as the Java GUI does with `setState(ICONIFIED)`.
fn minimize(window: &slint::Window) {
    // Slint caches the minimized state and only refreshes it on some window events, which Muffin
    // (Cinnamon) doesn't send when the window is restored. While the cache still says
    // "minimized", set_minimized(true) does nothing, so reset it first.
    window.set_minimized(false);
    window.set_minimized(true);
}

fn env_path((var, default): (&str, &str)) -> PathBuf {
    std::env::var_os(var).map_or_else(|| PathBuf::from(default), PathBuf::from)
}

fn clock() -> String {
    chrono::Local::now().format("%H:%M:%S").to_string()
}

/// "m:ss", or "h:mm:ss" from one hour on.
fn countdown(remaining: Duration) -> String {
    let seconds = (remaining.as_millis() + 500) / 1000;
    let (hours, minutes, seconds) = (seconds / 3600, seconds / 60 % 60, seconds % 60);
    if hours > 0 {
        format!("{hours}:{minutes:02}:{seconds:02}")
    } else {
        format!("{minutes}:{seconds:02}")
    }
}

fn show_error(ui: &AppWindow, what: &str, error: &str) {
    ui.set_status_title(format!("{} · {what}", clock()).into());
    ui.set_status_text(error.into());
}

fn show_freqs(ui: &AppWindow, freqs: MaxFreqs) {
    ui.set_active_bat(freqs.bat.unwrap_or(-1));
    ui.set_active_ac(freqs.ac.unwrap_or(-1));
}

/// Title and text of the status area at startup.
fn startup_status(
    conf: &Path,
    freqs: &io::Result<MaxFreqs>,
    on_ac: Option<bool>,
) -> (String, String) {
    let mut lines = Vec::new();
    let title = match freqs {
        Ok(freqs) => {
            let value =
                |value: Option<i32>| value.map_or("?".to_owned(), |value| value.to_string());
            let source = match on_ac {
                Some(true) => " (on AC power)",
                Some(false) => " (on battery)",
                None => "",
            };
            format!(
                "{} · Current: BF {}, AC {}{source}",
                clock(),
                value(freqs.bat),
                value(freqs.ac)
            )
        }
        Err(err) => {
            lines.push(err.to_string());
            format!("{} · Cannot read {}", clock(), conf.display())
        }
    };
    if !tlp::is_root() {
        lines.push(
            "Not running as root: tlpv will ask for the sudo password in the terminal.".to_owned(),
        );
    }
    (title, lines.join("\n"))
}

fn watch_conf(ui: slint::Weak<AppWindow>, conf: PathBuf) -> Timer {
    let modified = |path: &Path| {
        fs::metadata(path)
            .and_then(|metadata| metadata.modified())
            .ok()
    };
    let mut last = modified(&conf);
    let timer = Timer::default();
    timer.start(TimerMode::Repeated, CONF_CHECK_INTERVAL, move || {
        let now = modified(&conf);
        if now != last {
            last = now;
            if let (Some(ui), Ok(freqs)) = (ui.upgrade(), tlp::read_max_freqs(&conf)) {
                show_freqs(&ui, freqs);
            }
        }
    });
    timer
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn formats_countdown() {
        assert_eq!(countdown(Duration::from_secs(60)), "1:00");
        assert_eq!(countdown(Duration::from_millis(58_999)), "0:59");
        assert_eq!(countdown(Duration::from_secs(3 * 3600 + 5)), "3:00:05");
    }
}
