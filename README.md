# Encrypting-Decrypting

# File Encryption Using 2D Arrays

**Author**: Renata Penteado  
**Version**: Java 14
---

## Description

This Java application encrypts and decrypts text using the **ADFGVX cipher algorithm** combined with a **Polybius square** and keyword-based columnar transposition. It processes text files through a clear workflow and emphasizes modularity, error handling, and ease of use.

---

## How to Compile and Run

From the `src` directory, run the following commands:

```bash
javac -d out src/ie/atu/sw/*.java src/ie/atu/sw/exceptions/*.java
java -cp ./out ie.atu.sw.Runner
```

## Directory Structure

The application uses three main file directories:

- **Plain Text File Directory**  
  Input files to be encrypted are stored here.

- **Encrypted File Directory**  
  Encrypted output files are saved here.

- **Decrypted File Directory**  
  Decrypted output files are saved here.

---

## Workflow

### Encrypting

- Reads plain text files from the **Plain Text Directory**.
- Encrypts the content using the ADFGVX algorithm.
- Saves the encrypted files in the **Encrypted Directory**.

### Decrypting

- Reads encrypted files from the **Encrypted Directory**.
- Decrypts the content.
- Saves the decrypted files in the **Decrypted Directory**.

---

## Application Components

### Runner

- Entry point of the application.
- Calls the `start()` method from the **Menu** class.

### Menu

- Displays the user interface.
- Allows users to:
    - Set directory paths.
    - Set the keyword for encryption/decryption.
    - Choose to encrypt or decrypt files.

### Parser

- Validates directory paths.
- Loads input files line by line.
- Calls `encrypt()` or `decrypt()` methods of the **Cypher** class.
- Writes output to the appropriate file directory.
- Handles custom exceptions for missing input or configuration.

### Cypher

- Implements the **ADFGVX cipher algorithm**.
- Uses a **Polybius square** and **keyword-based columnar transposition**.
- Responsible for the core encryption and decryption logic.

---

## Progress Indicator

A progress bar is shown while encrypting or decrypting to indicate progress across all files in the input directory.

---

## Features Summary

- Encrypts and decrypts text files using classical cryptographic techniques.
- Robust handling of file paths and user-defined keywords.
- Custom exceptions for enhanced resiliency.
- Directory-based workflow for structured file management.
- Real-time progress tracking with a visual indicator.

---

## Contact

Created by **Renata Penteado**  
For feedback or questions, feel free to reach out or submit an issue in the repository.
