# CCB Daily File Pipeline

## 1. Project Overview

This project automates the processing of daily backend reports in a banking environment.

Originally implemented as a shell script, the logic has since been refactored into a structured Java program.  
The system is built around a unified interface that each module implements to handle its specific type of report for a given date, enabling clean separation of concerns and extensibility.

---

## 2. Structured Shell Replacement (v1.0)

Version 1.0 marks the initial transformation of a monolithic shell script into a structured, object-oriented Java application.

- Business logic was encapsulated into reusable classes and methods
- Introduced a unified entry point to coordinate report processing
- Established a clean codebase that laid the groundwork for future modularization

---

## 3. Modular Architecture (v1.1+)

Starting from version 1.1, the project evolves into a fully modular architecture.

- Each report type (e.g., GMPS, EFS) is implemented as an independent handler module
- All handler modules implement a shared interface:
  ```java
  void handle(ReportDateContext context);
  ```
- Common functionality is extracted into a shared `shared-core` module, including:
  - Date context parsing
  - File operations
  - Logging and constants

- A centralized `app-main` module retains the v1.0-style unified entry point, supporting backward-compatible execution

This modular design allows each handler to be developed, deployed, and authorized independently, improving maintainability and flexibility.

---

## 4. Current Modules

The project currently includes the following modules:

- **handler-mx-gmps**  
  Handles the extraction and processing of GMPS MX `.tar` archives for both income and outgo data

- **handler-mt-efs**  
  Fetches and renames `.xlsx` files for EFS MT reports from a structured source directory

- **shared-core**  
  Provides shared utilities, such as report date context, file helpers, path construction, and logging

- **app-main**  
  Implements a unified dispatcher that sequentially executes all registered handlers, maintaining compatibility with the original v1.0 behavior

This architecture allows for new handler modules to be added or replaced with minimal impact to existing components.

---

## 5. ⚙️ Usage

All modules in this project accept the same input format:

- Optional arguments: `[siradt] [tmdt]`
- If no arguments are provided, the system defaults to using today's date and yesterday's date
- The input format is consistent regardless of which module or entry point is used

### Run unified entry (`app-main`)
Use this to execute all available handler modules in sequence.
```bash
java -jar ccb-daily-file-pipeline-1.1-SNAPSHOT.jar
```

### Run individual modules
Each handler (e.g., GMPS or EFS) can also be run independently for targeted processing or debugging.
```bash
java -jar handler-mx-gmps-all-1.1-SNAPSHOT.jar
```
```bash
java -jar handler-mt-efs-all-1.1-SNAPSHOT.jar
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

