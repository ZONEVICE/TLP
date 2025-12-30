extends Node

# Set this to false to make local tests.
@export var execute_cmd: bool = true

# Controls the time between executions on key press commands.
var timeout := false
var timeout_timer := 0.0

#exit_code = 0 > no errors. Different means an error.

#User needs to be sudoer for tlpv to work.
#Command to add and remove sudo rights:
#sudo usermod -aG sudo v
#sudo deluser v sudo 

#It is needed to change visudo thing.
#sudo visudo
#v ALL=(ALL) NOPASSWD: /usr/local/bin/tlpv
#Remove that line later.

#Once the app is compiled, execute the bin as root.

func run_ls_command() -> void:
	var output = []
	var exit_code = OS.execute("ls", [], output, true)
	print("Exit code:", exit_code)
	for line in output[0].split("\n"):
		print(line)
		
func run_bf(freq: String) -> String:
	var msg: String = ''
	if execute_cmd == false:
		msg = 'Test mode: execute BF ' + freq + "..."
		print(msg)
		return msg
	var output = []
	var exit_code = OS.execute("sudo", ["/usr/local/bin/tlpv", "bf", freq], output, true)
	print("Exit code:", exit_code)
	for line in output[0].split("\n"):
		print(line)
		msg += line + "\n"
	return msg

func _input(event):
	if event.is_action_pressed("key_t"):
		#$TextEdit.text  = run_bf_4()
		pass

func _process(delta):
	if timeout:
		timeout_timer += delta
		if timeout_timer >= 3.5:
			timeout = false
			timeout_timer = 0.0
	else:
		if Input.is_action_pressed("key_t") and Input.is_action_pressed("key_1"):
			$TextEdit.text = run_bf("4")
			timeout = true
			timeout_timer = 0.0
		if Input.is_action_pressed("key_t") and Input.is_action_pressed("key_2"):
			$TextEdit.text = run_bf("8")
			timeout = true
			timeout_timer = 0.0
		if Input.is_action_pressed("key_t") and Input.is_action_pressed("key_3"):
			$TextEdit.text = run_bf("16")
			timeout = true
			timeout_timer = 0.0
		if Input.is_action_pressed("key_t") and Input.is_action_pressed("key_4"):
			$TextEdit.text = run_bf("22")
			timeout = true
			timeout_timer = 0.0
		if Input.is_action_pressed("key_t") and Input.is_action_pressed("key_5"):
			$TextEdit.text = run_bf("42")
			timeout = true
			timeout_timer = 0.0

func _on_button_pressed_bf_4() -> void:
	$TextEdit.text  = run_bf("4")

func _on_button_pressed_bf_8() -> void:
	$TextEdit.text  = run_bf("8")

func _on_button_pressed_bf_16() -> void:
	$TextEdit.text  = run_bf("16")

func _on_button_pressed_bf_22() -> void:
	$TextEdit.text  = run_bf("22")

func _on_button_pressed_bf_42() -> void:
	$TextEdit.text  = run_bf("42")
