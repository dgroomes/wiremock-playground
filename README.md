# wiremock-playground UNIMPLEMENTED 

Learning and exploring WireMock.

This is an example Gradle-based Java application that showcases the excellent HTTP mock server _WireMock_ 
<http://wiremock.org/docs/>.

## Instructions

* Use Java 14
* Run with `./gradlew run`
* Run the tests with `./gradlew test`

## Notes

* Why does the program take around 60 seconds to exit after the WireMock server is stopped? Are there threads keeping 
  the process alive? 