# backend-code

Assumption:
1. If a card hash is found to be fraudulent then it will always be fraudulent for all the future records for all the future 24 hour sliding window. 
   Example: Within a 24-hr Window if card A is found to be "fraudulent", then if the same card A appears in the following 24-hour window with amount less than    threshold it will still be considered fraudulent
   
## Gradle (for compiling, unit testing and automation testing)

Run the following commands from within the project folder: /backend-code/processTrans/

<b>To compile: </b>
./gradlew build

<b>To run Unit tests: </b>
./gradlew test

<b>To run Automation testing: </b>
./gradlew cucumber


Note: In case you are specifying a path to the .csv file, on MAC you may encounter (Operation not permitted) due to MAC OS security, you can follow this <a href=https://osxdaily.com/2018/10/09/fix-operation-not-permitted-terminal-error-macos/>link</a> for a quick fix. Works on Catalina. 
