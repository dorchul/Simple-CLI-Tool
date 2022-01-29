# Discount exercise

This project implements a CLI tool for replacing a string in a file with the current time in ISO format, obtained by calling the [timeanddate.com API](https://dev.timeanddate.com/).

## Table of contents

- [Table of contents](#table-of-contents)
- [Building with Maven](#building-with-maven)
- [Running](#running)
- [Generating an API key and secret](#generating-an-api-key-and-secret)

## Building with Maven

- [Install Maven](https://maven.apache.org/install.html)
- Clone the project
- Run `mvn clean install` from the project's root directory

## Running

- Create a json config file `config.json` containing the required parameters. See [this file](./config_example.json) for an example 
  - Replace the API keys with [your own keys](#generating-an-api-key-and-secret)
  - Replace `textToUpdate` with the template string you want to replace with the current time
  - Replace `fileToUpdate` with the file you want to make the replacements in
- Execute `mvn exec:java -Dexec.args="config.json"`

## Generating an API key and secret

- Create a [timeanddate.com account](https://dev.timeanddate.com/account)
- Browse to the [access keys](https://dev.timeanddate.com/account/accesskey)
- You should see one key that was automatically generated for you 
- Click "edit"
- Copy the access key and secret key which will be needed later
- Check "Allow insecure methods"
- Click "Create new policy for this access key"
- Select "service (Allowed services)" in the "Add Parameter" drop-down and click "Add"
- Set the default value to `"timeservice"` (including the double quotes)
- Click "Save policy"
