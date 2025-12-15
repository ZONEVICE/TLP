import os

def file_exists(filepath):
    return os.path.isfile(filepath)

def print_file_contents(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        contents = f.read()
        print(contents)
