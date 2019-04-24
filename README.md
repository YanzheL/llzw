<div align="center">
  <img src="logo.png"><br><br>
</div>

-----------------

Simple full-stack shopping site powered by [Spring](https://spring.io/) and [Angular](https://angular.io/)

This is a Spring demo site designated for beginners, not suitable for production.

Current project status: **Still in development**

## Continuous build status

| Branch     | Status                                                    |
| ---------- | --------------------------------------------------------- |
| **master** | ![](https://travis-ci.org/YanzheL/llzw.svg?branch=master) |
| **dev**    | ![](https://travis-ci.org/YanzheL/llzw.svg?branch=dev)    |


## Prerequisites

- **JRE Runtime:** 8 or later
- **MySQL:** 8 or later, running on [localhost:3306](localhost:3306),  otherwise you should modify related information in [application-dev.yml](api-gate/src/main/resources/application-dev.yml)

## Getting Started

Two options

#### Use Prebuilt JAR Package

Follow the intructions on [Release Page](https://github.com/YanzheL/llzw/releases)

#### From Source

To compile the source, you need a valid JDK environment with version 8 or later.

Clone this project

```shell
git clone https://github.com/YanzheL/llzw.git
```

Install maven dependencies

```shell
cd api-gate
./mvnw install -DskipTests
```

Build the package

```shell
./mvnw package -DskipTests
```

Run

```shell
java "-Dspring.profiles.active=dev" -jar target/*.jar
```

You may also run the test version with in-memory database if you do not have an external database.

```shell
java "-Dspring.profiles.active=test" -jar target/*.jar
```

## Features

* RESTful API Backend, integrated with spring security
* Angular Single Page Application

## Contributing



## Documentation

- [API Documentation](https://llzw.readthedocs.io)

## Licensing

[Apache License 2.0](LICENSE)
