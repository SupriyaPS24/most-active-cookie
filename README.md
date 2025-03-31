# Most Active Cookie

This is a command-line application that processes a cookie log file and determines the most active cookie(s) for a given date.

## Features
- Reads a `.csv` log file containing cookie activity timestamps.
- Identifies the most frequently occurring cookie(s) on a specified date.
- Outputs one or more cookies that appeared the most times.

#### Log File Format
The log file should be a .csv file with the following format:

```
cookie,timestamp
AtY0laUfhglK3lC7,2018-12-09T14:19:00+00:00
SAZuXPGUrfbcn5UA,2018-12-09T10:13:00+00:00
5UAVanZf6UtGyKVS,2018-12-09T07:25:00+00:00
AtY0laUfhglK3lC7,2018-12-09T06:19:00+00:00
SAZuXPGUrfbcn5UA,2018-12-08T22:03:00+00:00
4sMM2LxV07bPJzwf,2018-12-08T21:30:00+00:00
fbcn5UAVanZf6UtG,2018-12-08T09:30:00+00:00
4sMM2LxV07bPJzwf,2018-12-07T23:30:00+00:00
```
- cookie: The cookie identifier.
- timestamp: The timestamp of the cookie activity in ISO 8601 format (e.g., 2018-12-09T14:19:00+00:00).

### Usage Example
To run the application, use the following command format:
#### Example Command:
```sh
  java -cp target/most-active-cookie-1.0-SNAPSHOT.jar  com.app.cli.CommandLineApp -f test-data/cookie_log.csv -d 2018-12-09
```

#### Example Output:
`5UAVanZf6UtGyKVS`

### Tech Stack
- Java 21
- Apache Maven 

## Setup Instructions
### Prerequisites

Ensure you have:

- Java installed and configured (java -version).
- Apache Maven installed (mvn -version).

## Installation and Execution

### Installation
You can install the application by either cloning the repository or importing the project if it is archive file.

#### Method 1: Cloning the Repository
Clone the repository and navigate to the project folder:

```sh
  git clone git@github.com:SupriyaPS24/most-active-cookie.git
```

```cd most-active-cookie```

#### Method 2: Import from an Archive
1.  Extract the archive to a folder.
2. Import the project into your IDE:

- IntelliJ IDEA: Open the project containing pom.xml.
- Eclipse: Import as an Existing Maven Project.

### Build the project using Maven:

```sh
  mvn clean package
```
This command will use Maven to compile the source code, run any tests, and package the compiled code into a jar file.

### Run the Application
Once the project is built, execute the application using:

```sh
  java -cp target/most-active-cookie-1.0-SNAPSHOT.jar com.app.cli.CommandLineApp -f test-data/cookie_log.csv -d 2018-12-09
```
This will output the most active cookie(s) for the given date.

### Notes
- The log file must be in .csv format.
- If multiple cookies have the same highest occurrence on the specified date, they will be printed on separate lines.
- Make sure the file path provided for the log file is correct.

