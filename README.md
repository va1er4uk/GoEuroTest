GoEuroTest
==========
To run application:
java -jar GoEuroTest.jar "string"

To specify external config:
java -jar -Dlocation=test-config.properties GoEuroTest.jar "someString"

Results will be saved in 'results.csv' located in same directory where GoEuroTest.jar was started,
if remote service is reachable and no error occured.

Application log are saved to 'go-euro.log' file, application flow and errors are logged there.
