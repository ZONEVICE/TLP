# CLAUDE.md

Personal utility that caps the CPU frequency of the owner's laptop through TLP, to save battery
and keep temperatures down. The owner writes in Spanish; code, comments and docs are in English.

| Path | What | Status |
| --- | --- | --- |
| `src/core` | Python CLI (`tlpv`) that edits `/etc/tlp.conf` and restarts TLP | The contract: don't modify unless asked |
| `src/gui` | Java Swing GUI (NetBeans project) | Legacy, kept as is |
| `src/gui-slint` | Rust + Slint GUI with the same layout and a modern look, replacing the Java one | Current GUI |
| `dev/` | Local material, git-ignored: `gui-java-example.png` (screenshot of the Java GUI), `gui-slint-test/` (test tools) | Not in git |

## Host

- Linux Mint 21.2 (Ubuntu jammy), Cinnamon on **X11**, 1920x1080 at 96 DPI (scale factor 1), touch
  screen. Intel i5-10210U (0.4 to 4.2 GHz). TLP 1.5.0, Python 3.10, Rust stable, OpenJDK 21.
- Only this Linux setup matters: no Windows/macOS concerns.
- The host is also the test machine; the owner may be using it at the same time. Changing the TLP
  configuration is fine; don't synthesize mouse/keyboard input on the real display (`:0`).
- `sudo` needs a password, so Claude can't run anything as root: test with the fakes below.
- The repo lives in `~/sync/git`, a **Syncthing** folder whose `.stignore` excludes `node_modules/`,
  `bin/`, `obj/`, `.vs/` but not `target/`. Build Rust with `CARGO_TARGET_DIR` outside of it
  (unless the owner adds `target/` to `~/sync/git/.stignore`), or ~2.5 GB of build output gets synced.
- Git is the owner's business: commit, rebase or move branches only when asked, and never push
  (the owner pushes). Work happens on branches (`dev-*`) that the owner later moves `main` to.

## Core (`src/core`)

- Installed copy in `/usr/local/bin/tlp-zv/src/` (identical files). Launcher `/usr/local/bin/tlpv`:
  `sudo python /usr/local/bin/tlp-zv/src/main.py "$@"`.
- `tlpv bf N`: `CPU_SCALING_MAX_FREQ_ON_BAT=N00000` (kHz, capped at 4200000) and
  `CPU_BOOST_ON_BAT=1` if above 1.6 GHz else `0`, then `sudo tlp start`. `tlpv af N`: same with
  `_ON_AC`. `tlpv cf`: current MHz of each core.
- It rewrites every line containing the key, commented ones included.
- When its output is piped, TLP's messages come first and Python's
  `CPU battery frequency set to ...` last (Python buffers stdout until exit). Both GUIs show
  stdout and stderr merged in that order.
- The GUIs call `/usr/local/bin/tlpv <bf|af> <N>` with N in 4, 8, 16, 22, 42 and must run as root
  (root runs `sudo` without a password).

## Java GUI (`src/gui`)

- NetBeans/Ant project, Swing Metal look ("Ocean" theme). The owner starts it from a terminal:
  `cd src/gui/dist && sudo java -jar gui.jar` (~115 MB RSS).
- Known limitation, kept on purpose: its combo boxes always say "BF", it keeps one "last manual
  value" for both modes, and the AC/BATTERY toggle is not reflected in the mouse idle and timer
  rows. Fixed only in the Slint GUI.

## Slint GUI (`src/gui-slint`)

### Build, run, test

```sh
cd src/gui-slint
export CARGO_TARGET_DIR=~/.cache/tlp-gui-slint-target  # outside Syncthing
cargo build --release          # -> $CARGO_TARGET_DIR/release/tlp-gui-slint (~10 MB)
sudo "$CARGO_TARGET_DIR/release/tlp-gui-slint"         # root, like the Java GUI
cargo test
cargo clippy --all-targets
```

`TLP_GUI_TLPV` and `TLP_GUI_CONF` override `/usr/local/bin/tlpv` and `/etc/tlp.conf`, to run it
without root against a fake (see Testing). Without root the status area says so at startup.

### Files

- `ui/widgets.slint`: the `Theme` global (colors, fonts, radius) and the flat widgets: `Label`,
  `Button`, `OptionButton` (big buttons), `SegmentedControl`, `Select`, `NumberField`,
  `StatusArea`.
- `ui/app.slint`: `AppWindow`, the layout, and the properties/callbacks used by Rust.
- `src/main.rs`: `Controller`, all state on the UI thread with `slint::Timer`s: mode, manual
  value per mode, idle watcher, timed change, status title and text.
- `src/tlp.rs`: `Mode` (bf/af), `Runner` (worker thread running one tlpv job at a time, reports
  the status title/text and the re-read `/etc/tlp.conf`), config parsing, power source, root check.
- `src/idle.rs`: mouse idle state machine (pure, unit-tested) and the X11 pointer query (x11rb,
  own connection).
- `build.rs`: compiles `ui/app.slint` with the `fluent-light` style (only so nothing follows the
  desktop's dark theme; no std-widgets are used).

### Behavior

- Same window and controls as the Java GUI. A big button runs `tlpv bf|af N` and minimizes the
  window, like Java (every time: see the minimize gotcha below).
- Battery | AC switch (Java's "Toggle AC/BATTERY mode" button): changes the labels of the big
  buttons **and** of both drop-downs; the idle watcher and the timer act on the mode that is
  current when they fire (the fix requested over the Java GUI).
- Mouse idle watcher (its OK toggles it; filled dark while on; its drop-down and number field are
  locked): polls the global pointer position every second; after N seconds without movement it
  sets the drop-down's value in the current mode; when the pointer moves it restores the manual
  value of the mode it changed. Turning it off while the idle value is applied also restores.
- Manual values are kept per mode (Java had one for both): they start from `/etc/tlp.conf`
  (fallback 22) and are updated by the big buttons and by the timer.
- Timer (its OK starts it and shows the countdown; click again to cancel; drop-down and number
  field are locked): after N minutes it acts like a click on the big button, in the mode current
  then. It uses wall-clock time, so a timer that expires during suspend fires right after resume.
- Status area: title `HH:MM:SS · [reason →] BF 8` (reasons: Timer, Mouse idle, Mouse moved,
  Watcher off) above tlpv's output in monospace; `Running…` meanwhile; `[tlpv exit status: N]` on
  failure; a thin scrollbar appears when the text doesn't fit. At startup the title is
  `Current: BF 22, AC 42 (on AC power)`.
- The big button matching the value in `/etc/tlp.conf` for the current mode is filled dark;
  the file is re-read after every tlpv run and when its mtime changes (checked every 5 s).
- Starts in AC mode when a "Mains" power supply is online (`/sys/class/power_supply`), battery
  mode otherwise.
- tlpv runs never block the UI and never overlap (they can race on `/etc/tlp.conf`).
- Keyboard: Tab moves focus (a ring shows it; clicks don't), Space/Enter press buttons, arrows
  change drop-downs, number fields and the Battery | AC switch. Holding − or + repeats; the mouse
  wheel over a number field steps it.

### Design rules

- Compact, sober and modern: the owner keeps the window in a screen corner; the big buttons are
  large for the touch screen. The owner rejected a pixel copy of the Java Swing look ("Swing del
  2000") and wants the same sizes with a modern style, but no colors or complex styles: flat
  white controls with 6px corners (8px for the big buttons) on a light grey window, neutral
  greys only (Tailwind "zinc"), "on" states (active value, running watcher/timer) filled dark.
  The software renderer draws no shadows: a 1px translucent line under raised controls stands
  in for them. Font: Ubuntu 13px (the desktop's UI font), Ubuntu Mono 12px for tlpv's output.
- The geometry copies the bounds Swing computes for `Home.java`: 559x282 by default (also the
  minimum size), 12px margins, 25px rows, 12px around labels with Swing's label widths as
  `min-width`, an 18px "unrelated" gap before the minutes field, 6px between rows, 102x58 big
  buttons, a 101px status area. `dev/gui-slint-test/compare.py` diffs against the Java
  screenshot (useful for the geometry only now).
- Slint gotcha: an element with `width`/`height` but no `x`/`y` is centered in its parent; set
  `x`/`y` on every partial-size element or it lands half a pixel off (rounded to 1px). The root
  of a component can't use `parent`.
- Minimize gotcha: Slint caches the minimized state and only refreshes it on `Resized`/`Occluded`
  winit events, which Muffin (compositing) never sends when the window is restored, so a second
  `set_minimized(true)` was ignored. `minimize()` in `main.rs` resets it with
  `set_minimized(false)` first (Muffin logs a harmless "Buggy client ... timestamp of 0").
- Memory is the reason for this GUI (~15 MB RSS vs ~115 MB for Java): keep the Slint features
  minimal (X11 winit backend + software renderer; no OpenGL, Wayland or accessibility) and
  `opt-level = "s"`. The winit backend always starts a zbus thread for the XDG portal and links
  `image`; that can't be turned off through features. Idle CPU use is zero (timers only run
  while the watcher/timer are on, plus a stat() of `/etc/tlp.conf` every 5 s).

### Testing without root and without touching the desktop

`dev/gui-slint-test/`:

- `fake-tlpv`: runs the real `src/core/main.py` with `/etc/tlp.conf` redirected to
  `$TLP_GUI_CONF` and `sudo tlp start` replaced by TLP-like output; logs calls to `calls.log`
  next to that file.
- `xt.py`: drives a virtual display (`geom`, `click`, `press`/`release`, `move`, `key`, `shot`,
  `state`), coordinates relative to the window titled `TLP`.
- `compare.py`: side-by-side and diff against `dev/gui-java-example.png`.

```sh
Xvfb :99 -screen 0 1280x800x24 -dpi 96 -nolisten tcp &
DISPLAY=:99 metacity --replace &   # or: dbus-run-session -- muffin --replace (screenshots come out blank)
W=<scratch dir>; cp /etc/tlp.conf "$W/"
DISPLAY=:99 TLP_GUI_TLPV=dev/gui-slint-test/fake-tlpv TLP_GUI_CONF="$W/tlp.conf" \
    "$CARGO_TARGET_DIR/release/tlp-gui-slint" &
python3 dev/gui-slint-test/xt.py click 387 241 TLP     # 4th big button (minimizes the window)
DISPLAY=:99 wmctrl -a TLP                               # restore it
python3 dev/gui-slint-test/xt.py shot "$W/shot.png" TLP
cat "$W/calls.log"                                      # e.g. "13:56:59 af 22 -> sudo tlp start"
```

Useful click targets (window coordinates): watcher OK (519, 24), its seconds −/+ (337, 24) /
(385, 24); timer OK (473, 55); Battery (145, 86) / AC (412, 86); big buttons at y 241,
x 60 + 108·i. To reproduce window-manager behavior like the minimize bug, use Muffin: its
compositing keeps minimized windows mapped.
