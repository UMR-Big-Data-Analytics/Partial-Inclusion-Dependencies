# TPC-H Dataset
The TPC-H (Transaction Processing Performance Council Benchmark H) dataset is a decision support benchmark that simulates a data warehouse and is used to evaluate the performance of database systems. It consists of a set of business-oriented ad hoc queries and concurrent data modifications, chosen to have broad industry-wide relevance.

## Generation
Download the provided generation tools from the [TPC Website](https://www.tpc.org/). Complie the dbgen tool.
Using the command `dbgen -vf -s <SIZE>` an instance of TPC-H of size `<SIZE>`GB is created. The generated `.tbl` files are `|` seperated. They do not have a header.

As a last step, we use `remove_tail_seperator.py` to remove the tailing seperator which is present in the file.

## Usage within project
I use a 1GB and a 5GB version of TPC-H for performance measurements.

## Config for TPC-H
```java
case TPCH_1:
    this.databaseName = "TPCH_<SIZE>";
    this.tableNames = new String[]{"customer", "lineitem", "nation", "orders", "part", "partsupp", "region" "supplier"};
    this.inputFileSeparator = '|';
    this.inputFileHasHeader = false;
    this.inputFileEnding = ".tbl";
```
The `<SIZE>` varibale should be equal to the generated size.