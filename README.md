Auth Service: A microservice to be used for authentication purpose
============
This service is used to provide other service with authentication and user management features.
It also includes privileges and permission management


Requirements
============
* Java >= 17 (OpenJDK JVM is tested by our CI on Travis)
* MySql 8

You can run the required version of the database server in a container, instead of having to install it, like this:

    docker run --name mysql-8 -p 3306:3306 -e MYSQL_ROOT_PASSWORD_FILE=mysql -d mysql-8

and stop and destroy it like this:

    docker rm -f mysql-8

Beware that this database container database keeps its state inside the container and not on the host filesystem.  It is lost when you destroy (rm) this container.  This is typically fine for development.  See [Caveats: Where to Store Data on the database container documentation](https://hub.docker.com/_/mariadb) re. how to make it persistent instead of ephemeral.

Tomcat v9 is only required if you wish to deploy a  WAR to a separate external servlet container.  Note that you do not require to install Tomcat to develop it, or to run it in production if you use the self-contained JAR, which transparently embeds a servlet container using Spring Boot.


Instructions how to run for local development
============

Run the following commands:
1. `./mvnw clean install`
2`./mvnw spring-boot:run`



Instructions to build the JAR file
============
1. Clone the repository or download and extract the archive file to your local directory.
2. Run `./mvnw clean package` to build a modern cloud native fully self contained JAR file which will be created at `gpj-auth-service/target/` directory.
3Start the jar and pass the directory where you have downloaded the JDBC driver as loader.path, for example: `java -Dloader.path=. -jar target/gpj-auth-service-2.7.12.jar` (does not require external Tomcat)

    ...
    java -jar gpj-auth-service-2.7.12.jar


Instructions to build a WAR file
============
1. Clone the repository or download and extract the archive file to your local directory.
2. Run `./gradlew :fineract-war:clean :fineract-war:war` to build a traditional WAR file which will be created at `fineract-war/build/libs` directory.
3. Deploy this WAR to your Tomcat v9 Servlet Container.

We recommend using the JAR instead of the WAR file deployment, because it's much easier.

Note that with the 1.4 release the tenants database pool configuration changed from Tomcat DBCP in XML to an embedded Hikari, configured by environment variables, see above.


Instructions to execute Integration Tests
============

1. `./gradlew clean test`


Instructions to run Using Docker
============
As Prerequisites, you must have `docker`  installed on your machine;
1. `docker build -t auth-service .`
2. `docker run -p 8081:8081 auth-service`


Running Integration tests with test Coverage Reports
============

The project uses Jacoco to measure unit tests code coverage, to generate a report run the following command:

    `./mvnw clean test jacoco:report`

Generated reports can be found in target/site/jacoco directory.


Contributors
============
Judiao Mbaua
