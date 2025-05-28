# Partial Inclusion Dependency Discovery

This repository contains the implementations of the algorithms **SPIND**, **pSPIDER**, and **pBINDER**, along with the datasets used in the paper _"SPIND: Scalable Partial Inclusion Dependency Discovery."_ These algorithms are designed to discover **partial inclusion dependencies**, data dependencies that hold for a specified portion of a dataset (e.g., 90%).

An **inclusion dependency (IND)** asserts that all values from one set of columns are contained in the values of another set of columns. For example, the values in a foreign key column should be included in the values of a corresponding primary key column.

The original **SPIDER** and **BINDER** algorithms are available in the [Metanome algorithms repository](https://github.com/HPI-Information-Systems/metanome-algorithms).



## 🚀 Installation

All algorithms in this repository are implemented in Java and built using Maven. To build the project, ensure the following tools are installed:

- Java JDK 1.18 or later
- Maven
- Git

### Steps to Build

1. Clone the repository and initialize submodules:


```bash
git submodule init
git submodule update
```

2. If not already done, clone and build the [Metanome repository](https://github.com/HPI-Information-Systems/Metanome). This provides required dependencies for the algorithms.

3. Build the algorithms using Maven to generate the JAR files.

## 🧪 Usage & Examples

You can run the algorithms in two ways:

1. **Via Metanome:**  
   Follow the instructions in the [Metanome repository](https://github.com/HPI-Information-Systems/Metanome) to run the algorithms within the Metanome framework.

2. **Standalone Execution:**  
   Each algorithm includes a `Runner` class that supports standalone execution either via an IDE or directly from the JAR file.  
   For experiments, we used shell scripts to execute the JAR files. These scripts are located in the `Utils` folder.

> **Note**: An older, non-Metanome version of this code exists, but it is deprecated and not recommended for use.

## 📂 Data

Instructions on how the datasets were retrieved and prepared for the experiments in the paper can be found in the `Data` folder.

If you encounter any issues or have questions, feel free to reach out.

**ORCID:** [0009-0001-8483-2212](https://orcid.org/0009-0001-8483-2212)
