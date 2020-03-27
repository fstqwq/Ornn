# Compiler 2020 local judge

## How to use?

1. Clone this repo recursively and install the requirements:`PyYAML, json, subprocess` with Python 3.
2. Modify the config file(config.yaml):
    - path/compiler: path to your compiler root, the folder should contain `build.sh`, `semantic.sh`(for semantic test), `codegen.sh`(for codegen test), `optimize.sh`(for optimize test)
    - path/dataset: path to dataset
    - stage: semantic, codegen, optimize
3. You can read your brief result from the screen and more information in corresponding yaml file.
