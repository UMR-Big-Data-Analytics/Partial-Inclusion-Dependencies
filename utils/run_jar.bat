@echo off
setlocal enabledelayedexpansion

REM Define the JAR file name
set JAR_NAME=spind.jar

REM Define the input file
set CONFIG_FILE=config.txt

REM Define timeout in seconds (6 hours = 21600 seconds)
set TIMEOUT_SECONDS=0

REM Read the config file line by line
for /f "tokens=1-12 delims=;" %%A in (%CONFIG_FILE%) do (
    set "DATASET_NAME=%%A"
    set "SEPARATOR=%%B"
    set "FILE_ENDING=%%C"
    set "INPUT_FILE_HAS_HEADER=%%D"
    set "THRESHOLD=%%E"
	set "MAX_NARY=%%F"
	set "VALIDATION_SIZE=%%G"
	set "MERGE_SIZE=%%H"
	set "CHUNK_SIZE=%%I"
	set "SORT_SIZE=%%J"
	set "PARALLEL=%%K"
	set "NUMFILES=%%L"
	
    echo Running JAR for dataset: "!DATASET_NAME!" with "!THRESHOLD!" Threshold

    REM Properly escape the separator | and wrap arguments in quotes
    set "CMD_ARGS="!DATASET_NAME!" "!SEPARATOR!" "!FILE_ENDING!" "!INPUT_FILE_HAS_HEADER!" "!THRESHOLD!" "!MAX_NARY!" "!VALIDATION_SIZE!" "!MERGE_SIZE!" "!CHUNK_SIZE!" "!SORT_SIZE!" "!PARALLEL!" "!NUMFILES!""

    REM Run the JAR with parameters and set a timeout
    start /wait cmd /c java -Xmx20g -Xms20g -jar %JAR_NAME% !CMD_ARGS!

    REM Check if the process completed within the timeout
    if errorlevel 1 (
        echo Process for dataset "!DATASET_NAME!" did not complete within the timeout period.
        REM Optionally, you can handle the timeout scenario here
    ) else (
        echo Process for dataset "!DATASET_NAME!" completed successfully.
    )
)

echo All tasks finished.
