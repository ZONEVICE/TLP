# TLP CPU Frequency Manager

Python script to manage and apply `CPU_SCALING_MAX_FREQ_ON_BAT` and `CPU_BOOST_ON_BAT` options in TLP configuration file (`/etc/tlp.conf`) using simple commands.

# Setup

0. Have Python.
1. Place the Python project files (`src/core`) inside the path `/usr/local/bin/tlp-zv`.
2. Create a file without extension in the path `/usr/local/bin/` with the name `tlpv` and add the following content:

```bash
#!/bin/bash
sudo python /usr/local/bin/tlp-zv/src/main.py "$@"
```

Now you can run the script using the command `tlpv` from any terminal.

# Usage

**Commands available**

| Command                 | Description                           |
| ----------------------- | ------------------------------------- |
| `bf <frequency as kHz>` | Set CPU maximum frequency on battery  |
| `af <frequency as kHz>` | Set CPU maximum frequency on AC       |
| `cf`                    | Display current running CPU frequency |

To set the CPU maximum frequency on battery (in kHz):

```bash
sudo tlp-zv bf <frequency_in_kHz>
```

Take into consideration that the value passed in `<frequency as kHz>` should be the value without five zeros. For example, to set the frequency to 1.6 GHz, you should pass `16`. The system adds the five zeros automatically.

# Design

This project has not been tested on other hardware or software configurations.

- CPU: Intel i5-10210U
- OS: Linux Mint 21.2
- TLP version: 1.5.0
- Python 3.10.12
- Java 11 (OpenJDK 11)

# GUI

The GUI is a Java Swing + Apache Netbeans IDE 25 project located in the `src/gui` folder.

The python project `src/core` must be installed as stated at Setup section.

The GUI must be ran as superuser to have permission to modify the TLP configuration file.

The GUI contains a total of 5 commands to be executed with ease by pressing buttons:

`tlpv bf 4`

`tlpv bf 8`

`tlpv bf 16`

`tlpv bf 22`

`tlpv bf 42`
