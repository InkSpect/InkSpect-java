# InkSpect-java

InkSpect for Java

InkSpect is a prototype Static Application Security Testing (SAST) tool built on **[Joern](https://github.com/joernio/joern)**, focused on Java.  
**Status:** Prototype ; not yet operational.
It offers Taint Analysis based on a Code Property Graph.
Please build once before modifying the code.

## Prerequisite

Install development tools:
- sbt
- java 

Or enable Flake for Nix environment:

```bash
direnv allow
```

## Build

- Build Native Image

```
sbt clean compile graalvm-native-image:packageBin
```

- Build Java App Packaging

```
sbt clean stage
```

## Run

- Run CLI (Command Line Interface)

```
./inkspect_java_cli -s testprogram/
```

- Run REPL (Read-Eval-Print-Loop) for interactive usage

```
./inkspect_java_repl
```