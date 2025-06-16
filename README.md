# Encrypting-Decrypting

## Introduction

This Java application encrypts and decrypts text files using the **ADFGVX cipher algorithm** in combination with a **Polybius square** and columnar transposition based on a user-defined keyword.

---

## Project Description

This project was developed for a programming assessment and features a command-line application capable of:

- Encrypting text files from a specified directory.
- Decrypting encrypted files to their original form.
- Handling files using the ADFGVX cipher implemented with 2D arrays (Polybius square).

---

## Compile and Run

From the `src` directory, run the following commands in the terminal:

```bash
javac -d out src/ie/atu/sw/*.java src/ie/atu/sw/exceptions/*.java
java -cp ./out ie.atu.sw.Runner
