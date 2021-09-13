# README #

This README would normally document whatever steps are necessary to get your application up and running.

### What is this repository for? ###

* Quick Summary: Client application to send requests to IRT apis
* Version
* [Learn Markdown](https://bitbucket.org/tutorials/markdowndemo)

### How do I get set up? ###

* Summary of set up

1. Clone the repository on your local machine. "git clone https://jjuachon@bitbucket.org/biobank/irt-client.git"
2. Go to the root directory: "cd irt-client"
3. Acquire the production persistence settings here: https://stanfordmedicine.app.box.com/folder/145402666704
4. Configure your Oracle DB connection by replacing ./persistence.xml  with the file in Box.
5. Build the source code: "./gradlew clean build". This will create an irt-client.war in the ./build/libs directory.
6. Deploy the war file to your tomcat webapps folder.

* Configuration

After a successful deployment, a new table called irt_properties will be created in your oracle database.
Insert a valid IRT refresh token to be used for authenticating to IRT apis:
eg. insert into irt_properties (name, value) values ('refreshToken', '1kzn7ncrsxt4q1vlvxzciivx8nawcshj28w4xp1kmbmpe99ouqe175qlcc9a0a2g');

* Dependencies

* Database configuration

You may later change DB connection settings by doing the following steps:

1. Stop tomcat
2. Delete irt-client war file from webapps folder
3. Modify persistence.xml which is in <tomcat_home>/webapps/WEB-INF/classes/META-INF
4. Restart tomcat.
* How to run tests
* Deployment instructions

### Contribution guidelines ###

* Writing tests
* Code review
* Other guidelines

### Who do I talk to? ###

* Repo owner or admin
* Other community or team contact