# backend-code

Assumption:
1. If a card hash is found to be fraudulent then it will always be fraudulent for all the future records for all the future 24 hour sliding window. 
   Example: Within a 24-hr Window if card A is found to be "fraudulent", then if the same card A appears in the following 24-hour window with amount less than    threshold it will still be considered fraudulent
   
## Gradle VERSION Gradle 6.6.1 (for compiling, unit testing and automation testing)

Run the following commands from within the project folder: /backend-code/processTrans/

<b>To compile: </b>
./gradlew build

<b>To run Unit tests: </b>
./gradlew test

<b>To run Automation testing: </b>
./gradlew cucumber


Note: In case you are specifying a path to the .csv file, on MAC you may encounter (Operation not permitted) due to MAC OS security, you can follow this <a href=https://osxdaily.com/2018/10/09/fix-operation-not-permitted-terminal-error-macos/>link</a> for a quick fix. Works on Catalina. 


<b>Execution: </b>

java -jar ./build/libs/processTrans-1.0.0.jar thresh_hold_price csv_file_path

Examples

java -jar ./build/libs/processTrans-1.0.0.jar 150 transaction.csv

java -jar ./build/libs/processTrans-1.0.0.jar 150 /User/abc/Downloads/transaction.csv


## For Gradle Version 5.0 ( as tested on ubuntu) 

Run the following commands from within the project folder: /backend-code/processTrans/

<b>To compile: </b>
gradle build

<b>To run Unit tests: </b>
gradle test

<b>To run Automation testing: </b>
gradle cucumber
   
