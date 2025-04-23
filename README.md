## HBV401G Software Development (2023) - Team 3D
# Day Tour Search Engine

## About
Software which searches for Day Tours.

* [Changelog](Changelog.md).

## Use
Users can:

* Search for Day Tours by location or date
* Book tours (There are only so many available spots for a given tour at a given time)
* See information on Day Tours and a photo
* Filter Day Tours with various filters
* Order Day Tours by price, popularity or time
* Leave reviews

### Installation
This Java program uses Maven to manage dependencies and build the project. The IDE should be able to handle the Maven dependencies automatically.
The Java version used is Java 17.

The main code is located in the [src/main/java/com/daytour](src/main/java/com/daytour) directory.

The tests are located in the [src/test/java/com/daytour](src/test/java/com/daytour) directory.

### Build
To build the project, run the following command in the root directory of the project:
```bash
mvn install compile
```

### Run
To run the project and start the search engine, run the following command in the root directory of the project:
```bash
mvn javafx:run
```

### Test
To run the tests for the project, run the following command in the root directory of the project:
```bash
mvn test
```

## Documentation
View the Maven site for the project to see design documents, dependencies and documentation.
It can be generated using:
```bash
mvn site
``` 
The documentation can then be viewed in the [target/site](target/site) directory, specifically by opening the [index](target/site/index.html) site.

The JavaDoc can be generated directly by using:
```bash
mvn javadoc:javadoc
```

## Design Documents
Design Documents → [Design](src/site/markdown/Design.md).

## Tech
[Maven](https://maven.apache.org/) is used to manage the project, which is written in Java and requires version 17. The frontend is built using [JavaFX](https://openjfx.io/), and [JDBC](https://docs.oracle.com/javase/8/docs/technotes/guides/jdbc/) is used to access a local [SQLite](https://sqlite.org/) database.

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE), and [SPDX](https://spdx.org/licenses/MIT.html)

