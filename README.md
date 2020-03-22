# wiremock-playground 

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
* TODO add some stubs
* Create a JDK Flight Recording for the `programmatic` program by setting `export PROGRAMMATIC_OPTS="-XX:StartFlightRecording,dumponexit=true"`
  * After the program exits, the .jfr file will be captured. Visualize it using Mission Control <https://github.com/openjdk/jmc>
  * Clone Mission Control and build it
  * You must run it with a JDK 8 (and it must be installed at `/Library/Java/JavaVirtualMachines...` for some reason. I couldn't get an SDKMAN installed Java to work)
    * E.g. `JAVA_HOME=/Library/Java/JavaVirtualMachines/adoptopenjdk-8.jdk/Contents/Home/ target/products/org.openjdk.jmc/macosx/cocoa/x86_64/JDK\ Mission\ Control.app/Contents/MacOS/jmc -vm $JAVA_HOME/bin`