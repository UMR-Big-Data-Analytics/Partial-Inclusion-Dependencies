# ⚙️ Utils

This folder contains utility files used to run the `spind.jar` JAR application on multiple datasets in batch mode.

## 📄 Files

### `run_jar.bat`

A Windows batch script to execute the `spind.jar` file on a list of datasets specified in the `config.txt`. The script reads each configuration line, extracts parameters, and invokes the JAR with the appropriate arguments.

#### How to Run

```bash
cd utils
run_jar.bat
```

> Make sure `spind.jar` is located in the same directory or adjust the `JAR_NAME` path in the script accordingly.


### `config.txt`

Specifies datasets and their parameters for execution. Each line represents a dataset with semicolon (`;`) delimiters, following the format:

```text
DatasetName;Separator;FileExtension;HasHeader;Threshold;MaxNary;ValidationSize;MergeSize;ChunkSize;SortSize;Parallel;NumFiles
```

#### Example Entry

```text
Cars;,;.csv;true;1.0;-1;50000;1000;100000;2000000;-1;-1
```

#### Parameter Explanation

| Field            | Description                                                     |          
| ---------------- | --------------------------------------------------------------- | 
| `DatasetName`    | Name of the dataset                                             |           
| `Separator`      | Field separator (e.g., `,`,`\|`, `\t`) |
| `FileExtension`  | File format (e.g., `.csv`, `.tsv`, `.tbl`)                      |           
| `HasHeader`      | Whether the file includes a header row (`true`/`false`)         |           
| `Threshold`      | Partial degree threshold value (e.g., between `0` and `1.0`) |           
| `MaxNary`        | Maximum arity of considered dependencies (`-1` for no limit)    |           
| `ValidationSize` | Size of validation sample                                       |           
| `MergeSize`      | Merge buffer size                                               |           
| `ChunkSize`      | Number of records per processing chunk                          |           
| `SortSize`       | Buffer size for sorting                                         |           
| `Parallel`       | Whether to run in parallel (`-1` = default)                     |           
| `NumFiles`       | Number of input files to use                                    |           

