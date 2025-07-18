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

Each handler (e.g., GMPS or EFS) can be run independently for targeted processing or debugging.
```bash
java -jar handler-mx-gmps-all-1.3-SNAPSHOT.jar <Source_Dir> <Target_Dir>
```
```bash
java -jar handler-efs-all-1.3-SNAPSHOT.jar <Source_Dir> <Target_Dir>
```
```bash
java -jar handler-mt-gmps-all-1.3-SNAPSHOT.jar <Source_Dir> <Target_Dir>
```
This consistent input behavior ensures that all entry points follow the same convention, simplifying scheduling and automation.

---

## 6. Permissions

To ensure proper access to source and output files, the following permissions are required:

- **GMPS Handler**
  - Read access to the GMPS shared folder, typically located at a network path
  - Write access to a local or shared output directory for extracted files

- **EFS Handler**
  - Read access to the EFS source folder, typically located under a structured daily path
  - Write access to a folder named `EFS` for storing copied report files

Each handler may be executed under a separate service account, allowing fine-grained permission control per module.

This separation supports real-world scenarios where certain reports reside on different servers or share folders with restricted access.

---

## 7. How to Add a New Handler Module

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
   Add dependencies for:
   - `abstract-handler-launcher` (for the base `Launcher` class)
   - `shared-core` (for common utilities and `Handler` interface)
   
   in build.gradle of the new module:
   ```gradle
   dependencies {
      implementation project(':abstract-handler-launcher')
      implementation project(':shared-core')
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

