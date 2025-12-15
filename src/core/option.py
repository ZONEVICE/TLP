import os

# Updates the CPU max frequency on battery in /etc/tlp.conf
#  Also enables/disables turbo boost on battery based on frequency.
def update_cpu_battery_freq(value):
    with open('/etc/tlp.conf', 'r', encoding='utf-8') as f:
        lines = f.readlines()
    new_lines = []
    enable_battery_turbo_boost = False
    try:
        frequency = int(value)
        print(f'CPU battery frequency set to {frequency}')
        if frequency > 1600000:
            enable_battery_turbo_boost = True
    except ValueError:
        pass
    for line in lines:
        added_line = False
        if 'CPU_SCALING_MAX_FREQ_ON_BAT' in line:
            value_safe = int(value)
            if value_safe > 4200000:
                value_safe = 4200000
                print('[WARNING]: Maximum allowed frequency is 4200000, setting to this value.')
            new_lines.append(f'CPU_SCALING_MAX_FREQ_ON_BAT={value_safe}\n')
            added_line = True
        if 'CPU_BOOST_ON_BAT' in line:
            if enable_battery_turbo_boost:
                new_lines.append('CPU_BOOST_ON_BAT=1\n')
            else:
                new_lines.append('CPU_BOOST_ON_BAT=0\n')
            added_line = True
        if not added_line:
            new_lines.append(line)
    with open('/etc/tlp.conf', 'w', encoding='utf-8') as f:
        f.writelines(new_lines)

# Updates the CPU max frequency on ac power in /etc/tlp.conf
def update_cpu_ac_freq(value):
    with open('/etc/tlp.conf', 'r', encoding='utf-8') as f:
        lines = f.readlines()
    new_lines = []
    enable_ac_turbo_boost = False
    try:
        frequency = int(value)
        print(f'CPU ac frequency set to {frequency}')
        if frequency > 1600000:
            enable_ac_turbo_boost = True
    except ValueError:
        pass
    for line in lines:
        added_line = False
        if 'CPU_SCALING_MAX_FREQ_ON_AC' in line:
            value_safe = int(value)
            if value_safe > 4200000:
                value_safe = 4200000
                print('[WARNING]: Maximum allowed frequency is 4200000, setting to this value.')
            new_lines.append(f'CPU_SCALING_MAX_FREQ_ON_AC={value_safe}\n')
            added_line = True
        if 'CPU_BOOST_ON_AC' in line:
            if enable_ac_turbo_boost:
                new_lines.append('CPU_BOOST_ON_AC=1\n')
            else:
                new_lines.append('CPU_BOOST_ON_AC=0\n')
            added_line = True
        if not added_line:
            new_lines.append(line)
    with open('/etc/tlp.conf', 'w', encoding='utf-8') as f:
        f.writelines(new_lines)

# Displays the current CPU frequency
def display_current_cpu_frequency():
    with os.popen("awk -F': ' '/cpu MHz/{print $2}' /proc/cpuinfo") as stream:
        print(stream.read())