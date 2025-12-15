import sys
import file_handler
import option
import os

# Validate arguments
if sys.argv[1] != 'bf' and sys.argv[1] != 'af' and sys.argv[1] != 'cf':
    print('Invalid parameter to update')
    exit()

if sys.argv[1] == 'bf' or sys.argv[1] == 'af':
    if not sys.argv[2]:
        print('Invalid value for parameter to update')
        exit()

    try:
        int(sys.argv[2])
    except ValueError:
        print('Value must be an integer')
        exit()

    # Validate file existence
    if not file_handler.file_exists('/etc/tlp.conf'):
        print('Configuration file /etc/tlp.conf does not exist')
        exit()

# Update the configuration file based on option
if sys.argv[1] == 'bf':
    frequency = sys.argv[2] + '00000'
    option.update_cpu_battery_freq(frequency)
elif sys.argv[1] == 'af':
    frequency = sys.argv[2] + '00000'
    option.update_cpu_ac_freq(frequency)
elif sys.argv[1] == 'cf':
    option.display_current_cpu_frequency()
    exit()
else:
    print('Unsupported option')
    exit()

# TLP restart to apply changes (requires sudo privileges)
os.system('sudo tlp start')
