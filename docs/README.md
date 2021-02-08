# wiremock-playground 

Learning and exploring WireMock.

This is a collection of Gradle sub-projects that showcase the excellent HTTP mock server _WireMock_ 
<http://wiremock.org/docs/>.

There are two sub-projects:

* [`programmatic/`](#programmatic)
* [`standalone/`](#standalone)

## `programmatic/`

The `programmatic/` sub-project showcases how to run the WireMock server programmatically via Java code and with 
various configurations. Use it like a recipe book for when you write your own WireMock code! 

Instructions:

* Use Java 15
* Run with `./gradlew programmatic:run`
* Make a request to a stubbed endpoint with `curl --request GET --url http://localhost:8070/message`
* Observe the Jetty server statistics by going to <http://localhost:8070/stats/> in your browser
  * Statistics include things like the number of responses with 200/300/400/500 status codes, the number of connections,
    and the amount of memory used by the underlying Jetty server.

## `standalone/`

The `standalone/` sub-project showcases how to run WireMock as a standalone process by downloading the
executable jar and running it. See the official docs at <http://wiremock.org/docs/running-standalone/>.

Instructions:

* Use Java 15
* Run with `./gradlew standalone:run`
* Exit with `Ctrl + C`
