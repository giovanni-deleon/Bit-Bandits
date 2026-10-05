# Bit Bandits Hack

A JavaFX vault-cracking game built with Java 21 and Maven.

This README has been checked against the actual project configuration in `pom.xml`, the Java entry point in `src/main/java/vault/crack/bit_bandits_hack/Launcher.java`, and the startup resource in `src/main/resources/view/first-level.fxml`.

## What the project is

- Language: Java
- Build tool: Maven
- UI framework: JavaFX
- Java version: 21
- Entry point: `vault.crack.bit_bandits_hack.Launcher`
- Startup UI: `/view/first-level.fxml`

## Prerequisites

Before running the project, make sure you have:

- JDK 21 installed and active in your terminal
- Maven 3.9+ or the included Maven wrapper (`./mvnw`)
- A terminal with permission to execute shell scripts

Check your Java version:

```bash
java -version
```

Expected result: Java 21.x

## Project setup

Clone the repository:

```bash
git clone https://github.com/giovanni-deleon/Bit-Bandits.git
cd Bit-Bandits
```

If the wrapper is not executable on Unix-like systems, run:

```bash
chmod +x mvnw
```

## Verified build and run steps

The project configuration in `pom.xml` declares:

- `maven.compiler.source` = `21`
- `maven.compiler.target` = `21`
- JavaFX dependencies for `javafx-controls` and `javafx-fxml`
- the JavaFX Maven plugin configured with the app entry point `vault.crack.bit_bandits_hack.Launcher`

Because of that, the working commands are:

### 1) Compile the project

```bash
./mvnw clean compile
```

This verifies the project compiles successfully with the configured Java 21 setup.

### 2) Run the application

```bash
./mvnw javafx:run
```

This is the correct startup command for this project. It matches the `javafx-maven-plugin` configuration in `pom.xml` and the launcher class in `src/main/java/vault/crack/bit_bandits_hack/Launcher.java`, which then launches `HelloApplication`.

## Startup behavior

When the app starts, it loads the FXML file at:

```text
src/main/resources/view/first-level.fxml
```

This is confirmed by `HelloApplication.java`, which does:

```java
FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/view/first-level.fxml"));
```

## Notes

- This repository does not currently contain a `src/test` directory with JUnit test classes, so the reliable verification path is compile plus JavaFX launch.
- If you want to build a package instead of running directly, this is also valid:

```bash
./mvnw clean package
```

Then run the produced artifact using the JavaFX packaging workflow configured by the plugin.

## Troubleshooting

If the app does not start:

1. Confirm `java -version` shows Java 21.
2. Confirm `./mvnw -version` works.
3. Run:

```bash
./mvnw clean compile
./mvnw javafx:run
```

If you still hit a problem, check that your environment is using Java 21 rather than an older JDK.
