# Simple CLI Tool

This project implements a CLI tool for replacing a string in a file with the
current time in ISO format, obtained by calling the
[timeanddate.com API](https://dev.timeanddate.com/).

## Table of contents

- [Table of contents](#table-of-contents)
- [Requirements](#requirements)
- [Building with Maven](#building-with-maven)
- [Running locally](#running-locally)
- [Building with Docker](#building-with-docker)
- [Running with Docker](#running-with-docker)
- [Create a config file](#create-a-config-file)
- [Generating an API key and secret](#generating-an-api-key-and-secret)

## Requirements

- Linux
  - MacOS and Windows should also work but were not tested
- One of:
  - [Docker](https://docs.docker.com/get-docker/)
  - [Maven](https://maven.apache.org/install.html) and
    [JDK 17](https://openjdk.java.net/projects/jdk/17/)

## Building with Maven

Clone the project and run from the project's root directory:

```sh
mvn clean install
```

## Running locally

- [Create a config file](#create-a-config-file) and put it in `run/config.json`
- Execute `mvn exec:java -Dexec.args="run/config.json"`
  - If running on Windows, replace `mvn` with `mvn.cmd`

## Building with Docker

Clone the project and run from the project's root directory:

```sh
docker build -t discount-ex .
```

## Running with Docker

- [Create a config file](#create-a-config-file) and put it in `run/config.json`
  - Set `fileToUpdate` to `run/file.txt`.
- Execute the following command:

  ```sh
  docker run --rm -it -v "${PWD}/run:/app/run" discount-ex
  ```

## Create a config file

See [this file](./config_example.json) for an example. Important parameters:

- Replace the API keys with [your own keys](#generating-an-api-key-and-secret)
- Replace `textToUpdate` with the template string you want to replace with the
  current time
- Replace `fileToUpdate` with the file you want to make the replacements in

## Generating an API key and secret

- Create a [timeanddate.com account](https://dev.timeanddate.com/account)
- Browse to the [access keys](https://dev.timeanddate.com/account/accesskey)
- You should see one key that was automatically generated for you
- Click "edit"
- Copy the access key and secret key which will be needed later
- Check "Allow insecure methods"
- Click "Create new policy for this access key"
- Select "service (Allowed services)" in the "Add Parameter" drop-down and click
  "Add"
- Set the default value to `"timeservice"` (including the double quotes)
- Click "Save policy"
