# programmatic

Run a WireMock server programmatically via Java code and with various configurations.


## Overview

At it's core, WireMock is a Java library. It's distributed as a `.jar` and so you can depend on the library and code to
the Java APIs directly. Alternatively, you might consider running WireMock in a Docker container, but in any case you
should know your options.


## Instructions

Follow these instructions to run the example program.

1. Use Java 17
2. Build and run the program:
    * ```shell
      ./gradlew run
      ```
3. Make requests to the stubbed endpoints:
    * ```shell
      curl --request GET --url http://localhost:8070/message
      ```
    * ```shell
      curl --request GET --url http://localhost:8070/random-integer
      ```
4. Observe the Jetty server statistics by going to <http://localhost:8070/stats/> in your browser
    * Statistics include things like the number of responses with 200/300/400/500 status codes, the number of connections,
      and the amount of memory used by the underlying Jetty server.
