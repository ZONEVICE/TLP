# TLP CPU Frequency Manager

Small tools to cap the CPU frequency of a laptop through [TLP](https://linrunner.de/tlp/), to save
battery and keep temperatures down. They edit `CPU_SCALING_MAX_FREQ_ON_BAT` / `_ON_AC` (and the
matching `CPU_BOOST_ON_*`) in `/etc/tlp.conf` and restart TLP.

| Part | Path | Description |
| --- | --- | --- |
| Core | `src/core` | Python command line tool, `tlpv`. Does the actual work. |
| GUI (Slint) | `src/gui-slint` | Compact Rust + Slint window with big, touch-friendly buttons. Recommended. |
| GUI (Java) | `src/gui` | Previous Java Swing version of the same window. |

Both GUIs call `tlpv`, so the core must be installed first.

Tested on Linux Mint 21.2 (X11) with an Intel i5-10210U, TLP 1.5.0 and Python 3.10.

## Core

### Install

1. Copy the files of `src/core` to `/usr/local/bin/tlp-zv/src/`.
2. Create the launcher `/usr/local/bin/tlpv` and make it executable (`sudo chmod +x /usr/local/bin/tlpv`):

   ```bash
   #!/bin/bash
   sudo python /usr/local/bin/tlp-zv/src/main.py "$@"
   ```

### Usage

| Command | Description |
| --- | --- |
| `tlpv bf <N>` | Set the maximum CPU frequency on battery |
| `tlpv af <N>` | Set the maximum CPU frequency on AC |
| `tlpv cf` | Show the current frequency of each core |

`N` is in hundreds of MHz: `tlpv bf 16` sets 1.6 GHz on battery. Turbo boost is enabled above
1.6 GHz, and the maximum is 4.2 GHz.

## GUIs

Both have the same layout and controls:

- **Big buttons** (4, 8, 16, 22, 42): set that frequency, then minimize the window.
- **Battery / AC mode** (a `Battery | AC` switch in the Slint GUI, a toggle button in the Java
  one): choose whether the buttons change the battery (`BF`) or the AC (`AC`) setting.
- **Change to … after mouse idle for … seconds**: while the mouse doesn't move, use the chosen
  frequency; restore the previous one when it moves again. `OK` turns it on and off.
- **Change to … after … minutes**: apply a frequency once the time is up.
- The text area shows the output of the last `tlpv` call.

The GUIs must run as root, because `tlpv` modifies `/etc/tlp.conf`.

### Slint GUI (`src/gui-slint`)

A modern, flat look that uses about 15 MB of RAM (the Java GUI about 115 MB). Compared to the
Java version, the idle and timer settings follow the AC/BATTERY mode, the button of the active
frequency is highlighted, and a running timer shows a countdown and can be cancelled with a
second click.

#### Build

1. Install Rust 1.92 or later with [rustup](https://rustup.rs).
2. Build:

   ```bash
   cd src/gui-slint
   cargo build --release
   ```

   The first build takes a few minutes and downloads the dependencies. The program ends up in
   `target/release/tlp-gui-slint`. To keep the build files (several GB) out of the project folder,
   set `CARGO_TARGET_DIR`, e.g. `CARGO_TARGET_DIR=~/.cache/tlp-gui-slint-target cargo build --release`.

3. Run it:

   ```bash
   sudo target/release/tlp-gui-slint
   ```

   Optionally, install it: `sudo install -m 755 target/release/tlp-gui-slint /usr/local/bin/`.

### Java GUI (`src/gui`)

A Java Swing project for Apache NetBeans (Java 11). Build it from NetBeans, then run:

```bash
cd src/gui/dist
sudo java -jar gui.jar
```
