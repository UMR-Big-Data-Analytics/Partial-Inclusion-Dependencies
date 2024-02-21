# partial-inclusion-dependencies


## Algorithm Output
Each of the connected Algorithms outputs the same format. The structure is as follows:
```json
{
    "execution_name": <SOME UNIQUE NAME>,
    "algorithm": "pSPIDER"/"pBINDER"/"SPIND",
    "threshold": <THRESHOLD AS DOUBLE>,
    "null_handling": "aware"/"unaware",
    "duplicate_handling": "subset"/"foreign"/"equality"/"inequality",
    "total_time": <TIME IN MS>,
    "execution_date": <DATETIME OF EXECUTION>,
    "metadata" : <ALGORITHM SPECIFIC METADATA>,
    "pINDs": [
        {
            <DEPENDANT AS STRING>: [<REFERRED AS STING>, ...],
            ...
        },
        <DICTIONARY FOR THE NEXT LEVEL>,
        ...
    ]
}
```
Using this unified structure it becomes easy to compare the algorithms. The algorithm specific metadata is defined in the repository of each algorithm and depends on the specific strategies used.