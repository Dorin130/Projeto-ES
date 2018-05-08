# Adventure Builder [![Build Status](https://travis-ci.com/tecnico-softeng/es18al_15-project.svg?token=5FaChxQjpMkkzdVVADUP&branch=develop)](https://travis-ci.com/tecnico-softeng/es18al_15-project) [![codecov](https://codecov.io/gh/tecnico-softeng/es18LL_NN-project/branch/master/graph/badge.svg?token=7RY2x2UONX)](https://codecov.io/gh/tecnico-softeng/es18al_15-project)


To see the coverage reports, go to <module name>/target/site/jacoco/index.html.


|   Number   |          Name           |            Email        |   GitHub Username  | Group |
| ---------- | ----------------------- | ----------------------- | -------------------| ----- |
|            |                         |                         |                    |   1   |
|            |                         |                         |                    |   1   |
|            |                         |                         |                    |   1   |
|            |                         |                         |                    |   2   |
|            |                         |                         |                    |   2   |
|            |                         |                         |                    |   2   |

- **Group 1:**
- **Group 2:**

### Infrastructure

This project includes the persistent layer, as offered by the FénixFramework.
This part of the project requires to create databases in mysql as defined in `resources/fenix-framework.properties` of each module.

See the lab about the FénixFramework for further details.

#### Docker (Alternative to installing Mysql in your machine)

To use a containerized version of mysql, follow these stesp:

```
docker-compose -f local.dev.yml up -d
docker exec -it mysql sh
```

Once logged into the container, enter the mysql interactive console

```
mysql --password
```

And create the 6 databases for the project as specified in
the `resources/fenix-framework.properties`.

To launch a server execute in the module's top directory: mvn clean spring-boot:run

To launch all servers execute in bin directory: startservers

To stop all servers execute: bin/shutdownservers

To run jmeter (nogui) execute in project's top directory: mvn -Pjmeter verify. Results are in target/jmeter/results/, open the .jtl file in jmeter, by associating the appropriate listeners to WorkBench and opening the results file in listener context

