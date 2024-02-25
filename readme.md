To add a README file to this project, you can create a new file named `README.md` in the root directory of the project. Here's a basic template for the README that you can expand upon based on the specific details and requirements of your project:

'''
# Email Fetcher Project

## Overview
This project is designed to fetch emails using various protocols including common email protocols (IMAP, POP3, etc.) and the Microsoft Graph API. It then stores the fetched emails into a text file for further processing or archiving.

## Features
- Fetch emails using common email protocols and Microsoft Graph API.
- Store emails in a text file.
- Configurable mail server settings through a properties file.

## Getting Started

### Prerequisites
- Java 21
- Gradle 7.4.2

### Setup
1. Clone the repository.
2. Navigate to the project directory.
3. Use the Gradle wrapper to ensure the correct Gradle version is used:
'''

shell
./gradlew build

'''
4. Configure your mail server settings in `mailserver.properties`.

### Running the Application
Execute the following command to run the application:
'''

shell
./gradlew run

'''
## Configuration
The mail server and protocol settings can be configured in `mailserver.properties`. This includes server host, port, username, password, and protocol-specific settings.

## Contributing
Contributions are welcome. Please open an issue to discuss your ideas or submit a pull request.

## License
This project is licensed under the MIT License - see the LICENSE file for details.
'''

This README provides a basic introduction to the project, instructions for setup and running the application, and placeholders for additional sections like contributing guidelines and license information. You should customize it to fit the specifics of your project, including any additional setup steps, usage examples, or external resources.