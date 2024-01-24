# TPC-H Dataset
The TPC-H (Transaction Processing Performance Council Benchmark H) dataset is a decision support benchmark that simulates a data warehouse and is used to evaluate the performance of database systems. It consists of a set of business-oriented ad hoc queries and concurrent data modifications, chosen to have broad industry-wide relevance.

## Generation
Download the provided generation tools from the [TPC Website](https://www.tpc.org/). Complie the dbgen tool.
Using the command `dbgen -vf -s <SIZE>` an instance of TPC-H of size `<SIZE>`GB is created. The generated `.tbl` files are `|` seperated. They do not have a header.

## Usage within project
I use a 1GB and a 5GB version of TPC-H for performance measurements.

## Config for TPC-H
```java
TODO
```