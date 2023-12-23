# programmatic

Run a WireMock server programmatically via Java code and with various mapping features.


## Overview

At it's core, WireMock is a Java library. It's distributed as a `.jar` and so you can depend on the library and code to
the Java APIs directly. Alternatively, you might consider running WireMock in a Docker container, but in any case you
should know your options.


## Instructions

Follow these instructions to run the example program.

1. Pre-requisite: Java 21
2. Build the program distribution
    * ```shell
      ./gradlew installDist
      ```
3. Run the program
    * ```shell
      build/install/programmatic/bin/programmatic
      ```
    * It will output something like the following.
    * ```text
      13:00:38 [main] DEBUG dgroomes.wiremock.ProgrammaticMain - Starting the WireMock server
      ...
      13:00:38 [main] INFO org.eclipse.jetty.server.Server - Started Server@75f2099{STARTING}[11.0.18,sto=10000] @523ms
      13:00:38 [main] INFO dgroomes.wiremock.ProgrammaticMain - WireMock server started!
      13:00:38 [main] DEBUG dgroomes.wiremock.ProgrammaticMain - Boot up time was PT0.422965S
      13:00:38 [main] DEBUG dgroomes.wiremock.ProgrammaticMain - View statistics at http://localhost:8070/stats/
      ...
      ```
4. Make requests to the stubbed endpoints:
    * ```shell
      curl --request GET --url http://localhost:8070/message
      ```
    * ```shell
      curl --request GET --url http://localhost:8070/random-integer
      ```
    * ```shell
      curl http://localhost:8070/message-templated?name=Bluey
      ```
    * ```shell
      curl http://localhost:8070/message-delayed?delay=2
      ```
    * ```shell
      curl --header 'X-Scenario: occasional-failure' http://localhost:8070/message
      curl --header 'X-Scenario: occasional-failure' http://localhost:8070/message
      curl --header 'X-Scenario: occasional-failure' http://localhost:8070/message
      curl --header 'X-Scenario: occasional-failure' http://localhost:8070/message
      ```
5. Observe the Jetty server statistics by going to <http://localhost:8070/stats/> in your browser
    * Statistics include things like the number of responses with 200/300/400/500 status codes, the number of connections,
      and the amount of memory used by the underlying Jetty server.


## Reference

* [WireMock docs: *Admin API*](https://wiremock.org/docs/standalone/admin-api-reference/)
