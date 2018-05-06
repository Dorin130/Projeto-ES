# Adventure Builder [![Build Status](https://travis-ci.com/tecnico-softeng/es18al_15-project.svg?token=5FaChxQjpMkkzdVVADUP&branch=develop)](https://travis-ci.com/tecnico-softeng/es18al_15-project) [![codecov](https://codecov.io/gh/tecnico-softeng/es18al_15-project/branch/develop/graph/badge.svg?token=7RY2x2UONX)](https://codecov.io/gh/tecnico-softeng/es18al_15-project)

To run tests execute: mvn clean install

To see the coverage reports, go to <module name>/target/site/jacoco/index.html.


|   Number   |          Name           |            Email                     |   GitHub Username  | Group |
| ---------- | ----------------------- | -------------------------------------| -------------------| ----- |
|  83509     | Marco Silva             | marcofsilva@tecnico.ulisboa.pt       | MarcofSilva        |   1   |
|  83475     | Hugo Guerreiro          | hugo.guerreiro@tecnico.ulisboa.pt    | hugosilvaguerreiro |   1   |
|            |                         |                                      |                    |   1   |
|  83504     | Manuel Vidigueira       | manuel.vidigueira@tecnico.ulisboa.pt | SemperDarky        |   2   |
|  83534     | Nádia Fernandes         | nadia.sofia@tecnico.ulisboa.pt       | NadiaSofia         |   2   |
|            |                         |                                      |                    |   2   |

- **Group 1: tax, broker, bank
- **Group 2: car, hotel, activity

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

And create the 7 databases for the project as specified in
the `resources/fenix-framework.properties`.
