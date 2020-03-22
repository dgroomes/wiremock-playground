# wiremock-playground UNIMPLEMENTED 

Learning and exploring WireMock.

This is a collection of Gradle sub-projects that showcase the excellent HTTP mock server _WireMock_ 
<http://wiremock.org/docs/>.

There are two sub-projects:

* [`programmatic/`](#programmatic)
* [`standalone/`](#standalone)

## Overall Instructions

Use Java 14.

## `programmatic/`

The `programmatic/` sub-project showcases how to run the WireMock server programmatically via Java code. 

Instructions:

* Run with `./gradlew programmatic:run`
* Run the tests with `./gradlew programmatic:test`

## `standalone/`

The `standalone/` sub-project showcases how to run WireMock as a standalone process by downloading the
executable jar and running it. See the official docs at <http://wiremock.org/docs/running-standalone/>.

Instructions:

* Run with `./gradlew standalone:run`
* Exit with `Ctrl + C`

## Notes

* Why does the `programmatic` program take around 60 seconds to exit after the WireMock server is stopped? Are there
  threads keeping the process alive? Why does the `standalone` program not exhibit the same behavior but instead exits
  immediately upon a `Ctrl + C`? 