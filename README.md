[![Architecture diagram of giovanni-deleon/bit-bandits](https://gitdiagram.com/giovanni-deleon/bit-bandits/diagram.png)](https://gitdiagram.com/giovanni-deleon/bit-bandits?utm_source=readme&utm_medium=badge)

[![Architecture diagram](https://gitdiagram.com/diagram-badge.svg)](https://gitdiagram.com/giovanni-deleon/bit-bandits?utm_source=readme&utm_medium=badge)

## How to Run This Program

### Prerequisites
- Java Development Kit (JDK) 8 or higher installed
- Maven or Gradle (if the project uses one of these build tools)

### Running the Program

1. **Clone the repository** (if you haven't already):
   ```bash
   git clone https://github.com/giovanni-deleon/Bit-Bandits.git
   cd Bit-Bandits
   ```

2. **Compile the Java code**:
   - If using Maven:
     ```bash
     mvn clean compile
     ```
   - If using Gradle:
     ```bash
     gradle build
     ```
   - Or compile directly with javac:
     ```bash
     javac -d bin src/**/*.java
     ```

3. **Run the program**:
   - If using Maven:
     ```bash
     mvn exec:java -Dexec.mainClass="path.to.MainClassName"
     ```
   - If using Gradle:
     ```bash
     gradle run
     ```
   - If compiled with javac:
     ```bash
     java -cp bin MainClassName
     ```

> **Note**: Replace `MainClassName` with the actual main class name of your program.

