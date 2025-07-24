# CCB Daily File Pipeline

## 1. Project Overview

This project automates the processing of daily backend reports in a banking environment. Originally implemented as a shell script, the logic has since been refactored into a structured Java program. 

The system is built around a unified interface that each module implements to handle its specific type of report for a given date, enabling clean separation of concerns and extensibility.

Since v1.3, common CLI and handler execution logic is centralized in an abstract base class provided by the `abstract-handler-launcher` module, which all handler entry points now extend.

---

## 2. Structured Shell Replacement (v1.0)

Version 1.0 marks the initial transformation of a monolithic shell script into a structured, object-oriented Java application.

- Business logic was encapsulated into reusable classes and methods
- Introduced a unified entry point to coordinate report processing
- Established a clean codebase that laid the groundwork for future modularization

---

## 3. Modular Architecture (v1.1+)

Starting from version 1.1, the project evolves into a fully modular architecture.

- Each report type is implemented as an independent handler module
- All handler modules implement a shared interface:
  ```java
  void handle(ReportDateContext context);
  ```
- Common functionality is extracted into a shared `shared-core` module, including:
  - Date context parsing
  - File operations
  - Logging and constants
 
Starting from version 1.3:
- Introduced `abstract-handler-launcher` module with a shared `Launcher` base class.
- Removed deprecated `app-main` module (previous unified entry point).

This modular design allows each handler to be developed, deployed, and authorized independently, improving maintainability and flexibility.

---

## 4. Current Modules

The project currently includes the following modules:

- **handler-mx-gmps**  
  Handles the extraction and processing of GMPS MX `.tar` archives for both income and outgo data

- **handler-efs** (renamed from handler-mt-efs) 
  Fetches and renames `.xlsx` files for EFS reports from a structured source directory

- **handler-mt-gmps** (New in v1.3)  
  Extracts MT GMPS `.tar` archives, decrypts files, renames them, and replaces SN/ISN placeholders with random values

- **handler-smis** (New in v1.4)  
  Extracts the SMIS ZIP archive into a target folder named after the specified date

- **abstract-handler-launcher**  
  Provides an abstract `Launcher` class to unify argument parsing and handler execution

- **shared-core**  
  Provides shared utilities, such as report date context, file helpers, path construction, and logging

This architecture allows for new handler modules to be added or replaced with minimal impact to existing components.

---

## 5. Usage

Each module in this project accepts a unified input format for dates and paths:

- **Required arguments:** `<Source_Dir> <Target_Dir>`
- **Optional arguments:** `[siradt] [tmdt]` (format: `yyyymmdd`)
- If no dates are provided, the system will default to using today's date and yesterday's date
- The CLI format is consistent across all modules and entry points, but each handler may use different paths as needed
- `siradt` is used for GMPS module, `tmdt` is used for EFS and SMIS modules  

Each handler (e.g., GMPS or EFS) can be run independently for targeted processing or debugging.
```bash
java -jar handler-mx-gmps-all-1.4-SNAPSHOT.jar <Source_Dir> <Target_Dir> [siradt] [tmdt]
```
```bash
java -jar handler-efs-all-1.4-SNAPSHOT.jar <Source_Dir> <Target_Dir> [siradt] [tmdt]
```
```bash
java -jar handler-mt-gmps-all-1.4-SNAPSHOT.jar <Source_Dir> <Target_Dir> [siradt] [tmdt]
```
```bash
java -jar handler-smis-all-1.4-SNAPSHOT.jar <Source_Dir> <Target_Dir> [siradt] [tmdt]
```
This consistent input behavior ensures that all entry points follow the same convention, simplifying scheduling and automation.

---

## 6. How to Add a New Handler Module

To extend this project with a new handler:

1. **Create a new Gradle module**  
   Add a new module under the project root (e.g., `handler-<name>`), and include it in `settings.gradle`.

2. **Implement the `Handler` interface**  
   Define the business logic inside a class that implements:
   ```java
   void handle(ReportDateContext context);
   ```
3. **Provide an entry point**  
   Create a `Launcher` subclass in your module to reuse argument parsing and execution logic (replace `NewMain` with your new main class name):
   ```java
   public class NewMain extends Launcher {
      public static void main(String[] args) throws Exception {
        new NewMain().launch(args);
      }

      @Override
      protected Handler createHandler(String sourceDir, String targetDir) {
        return new NewMain(sourceDir, targetDir);
      }
   }
   ```

4. **Update build.gradle**  
   Firstly, modify version number is all build.gradle.
   In build.gradle of the new module:
   - Add dependencies
     ```gradle
     dependencies {
         testImplementation platform('org.junit:junit-bom:5.10.0')
         testImplementation 'org.junit.jupiter:junit-jupiter'
         implementation project(':abstract-handler-launcher')
         implementation project(':shared-core')
     }
     ```
   - Add required plugins and implement corresponding configuration
     ```gradle
     plugins {
         id 'java'
         id 'application'
         id 'com.github.johnrengelman.shadow' version '8.1.1'
     }
     ```
     ```gradle
     application {
         mainClass = 'com.ccb.daily.file.pipeline.smis.SMISMain' //replace content in '' with your main entry class
     }
     ```
     ```gradle
     shadowJar {
         archiveBaseName.set('handler-smis-all') //replace content in '' with your moudle name + "-all"
         archiveClassifier.set('')
     }
     ```
   - Since the program is initially run with jdk1.8, you might need set up java compatibility:
     ```gradle
     java {
         sourceCompatibility = JavaVersion.VERSION_1_8
         targetCompatibility = JavaVersion.VERSION_1_8
     }
     ```

---

## Changelog

### v1.1 (2025-06-26)
- Refactored into a **modular architecture**: each handler now has an independent executable
- Introduced **separate entry points**:
  - `MXGMPSMain` – extracts GMPS MX message archives
  - `EFSMain` – fetches EFS MT files
  - `App` – processes unified execution
- Enabled **assignment of different service accounts** for permission-sensitive tasks
- Extracted shared logic into a **core module** for reuse and cleaner structure
- Maintains **backward compatibility** with version 1.0

### v1.2 (2025-07-10)
- Introduced CLI arguments for **input/output path** configuration per handler
- Removed hardcoded paths in `EFSFileHandler` and `MXMessageHandler`
- Updated error messages to reflect new CLI format

### v1.3 (2025-07-18)
- Added `abstract-handler-launcher` module with shared `Launcher` base class.
- Removed deprecated `app-main` module.
- Introduced new handler module: `handler-mt-gmps`.
- Renamed `handler-mt-efs` to `handler-efs`.
- Standardized package naming across all modules.

### v1.4 (2025-7-24)
- Implemented SMISHandler class for handling SMIS report extraction
- Supports locating ZIP by date, removing old directory, and extracting to a target folder named after the same date
- Integrated with existing Handler interface for pipeline consistency
