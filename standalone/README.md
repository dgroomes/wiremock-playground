# standalone

Run WireMock as a standalone process by downloading the executable jar and running it.


## Instructions

Follow these instructions to run the standalone WireMock server.

1. Use Java 17
2. Build the program distribution
    * ```shell
      ./gradlew installDist
      ```
3. Run the standalone server
    * ```shell
      build/install/standalone/bin/standalone
      ```
4. When you're done, stop the program with `Ctrl + C`


## Reference

* See the official docs at <http://wiremock.org/docs/running-standalone/>.
