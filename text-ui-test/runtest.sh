#!/usr/bin/env bash

# Define test storage file path
TEST_STORAGE_FILE="./data/gilu_test.txt"

# Create bin directory if it doesn't exist
if [ ! -d "../bin" ]; then
    mkdir ../bin
fi

# Delete output from previous run
if [ -e "./ACTUAL.TXT" ]; then
    rm ACTUAL.TXT
fi

# Ensure the test storage file starts empty
rm -f "$TEST_STORAGE_FILE"
mkdir -p "./data"
touch "$TEST_STORAGE_FILE"

# Compile the code into the bin folder, terminate if error occurred
if ! javac -cp ../src/main/java -Xlint:none -d ../bin ../src/main/java/gilu/*.java
then
    echo "********** BUILD FAILURE **********"
    exit 1
fi

# Run the program, passing the test storage file as an argument
java -classpath ../bin gilu.Gilu "$TEST_STORAGE_FILE" < input.txt > ACTUAL.TXT

# Convert to UNIX format
cp EXPECTED.TXT EXPECTED-UNIX.TXT
dos2unix ACTUAL.TXT EXPECTED-UNIX.TXT

# Compare the output to the expected output
diff ACTUAL.TXT EXPECTED-UNIX.TXT
if [ $? -eq 0 ]; then
    echo "Test result: PASSED"
    exit 0
else
    echo "Test result: FAILED"
    exit 1
fi
