# anstoss-transfer-market-quarkus project

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```
./mvnw quarkus:dev
```

## Packaging and running the application

The application is packageable using `./mvnw package`.
It produces the executable `anstoss-transfer-market-quarkus-1.0.0-SNAPSHOT-runner.jar` file in `/target` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/lib` directory.

The application is now runnable using `java -jar target/anstoss-transfer-market-quarkus-1.0.0-SNAPSHOT-runner.jar`.

## Creating a native executable

You can create a native executable using: `./mvnw package -Pnative`.

Or you can use Docker to build the native executable using: `./mvnw package -Pnative -Dquarkus.native.container-build=true`.

You can then execute your binary: `./target/anstoss-transfer-market-quarkus-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/building-native-image-guide .

## Run

Development mode

```
mvn quarkus:dev
```

Run from artifact

```
mvn clean package
java -jar target/anstoss-transfer-market-quarkus-1.0.0-SNAPSHOT-runner.jar
```

Run with docker jvm mode

```
docker build -f src/main/docker/Dockerfile.jvm -t quarkus/anstoss-transfer-market-quarkus-jvm .
docker run -i --rm -p 8080:8080 quarkus/anstoss-transfer-market-quarkus-jvm
```

Run with docker native

```
docker build -f src/main/docker/Dockerfile.multistage -t quarkus/anstoss-transfer-market-quarkus-native .
docker run -i --rm -p 8080:8080 quarkus/anstoss-transfer-market-quarkus-native
```