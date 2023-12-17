# wiremock-playground 

📚 Learning and exploring WireMock.

> Mock the APIs You Depend On
> 
> -- <cite>http://wiremock.org</cite>


## Overview

**NOTE**: This project was developed on macOS. It is designed for my own personal use.

This is a collection of example projects that showcase the excellent HTTP mock server _WireMock_ 
<http://wiremock.org/docs/>.


## Standalone subprojects

This repository illustrates different concepts, patterns and examples via standalone subprojects. Each sub-project is
completely independent of the others and do not depend on the root project. This _standalone sub-project constraint_
forces the subprojects to be complete and maximizes the reader's chances of successfully running, understanding, and
re-using the code.

The subprojects include:


### `programmatic/`

Run a WireMock server programmatically via Java code and with various configurations.

See the README in [programmatic/](programmatic/).


### `standalone/`

Run WireMock as a standalone process by downloading the executable jar and running it.

See the README in [standalone/](standalone/).


## Wish List

General clean-ups, changes and things I wish to implement for this project:

* [x] DONE Make the subprojects completely standalone and adhere to the convention I have in my other playground repos.
* [x] DONE Add a stub with a custom handler that uses custom Java code to respond to the request.
* [ ] SKIP (WireMock has its own Docker image now; great!) Create a WireMock-in-Docker example
* [x] DONE create a subproject that declares dependency constraint versions and which will be used as a `platform(...)`
  from the other projects. For details of this feature, see the [Gradle docs about "platform"](https://docs.gradle.org/current/userguide/platforms.html)
* [x] DONE Upgrade to Gradle 8.5
* [x] DONE Upgrade dependencies
* [x] DONE Upgrade to Java 17
* [x] DONE Upgrade to WireMock 3.0
* [x] DONE Upgrade to Java 21?
* [ ] Do something with proxying. Anything I care to have a reference for? Any nice new proxy features in WireMock 3.0?
* [ ] Add some stubs to the standalone project
* [ ] Remove the JFR stuff (although interesting; that kind of stuff belongs in other playground repos)
* [ ] Consider removing the `standalone` subproject. I'm not sure if I would use this in practice anymore since there is
  now an official WireMock Docker image.


## Notes

* Create a JDK Flight Recording
  * (see `java` options at <https://docs.oracle.com/en/java/javase/15/docs/specs/man/java.html> and scroll down to `-XX:StartFlightRecording`)
  * For the `programmatic` program:
    * Build the distribution with `./gradlew programmatic:installDist`
    * Set the JRE options with `export PROGRAMMATIC_OPTS="-XX:StartFlightRecording,dumponexit=true"`
    * Run the program with `programmatic/build/install/programmatic/bin/programmatic`
  * For the `standalone` program:
    * Build the distribution with `./gradlew standalone:installDist`
    * Set the JRE options with `export STANDALONE_OPTS="-XX:StartFlightRecording,dumponexit=true"`
    * Run the program with `standalone/build/install/standalone/bin/standalone`
  * After the program exits, the .jfr file will be captured. Visualize it using Mission Control <https://github.com/openjdk/jmc>
  * Clone Mission Control and build it
  * You must run it with a JDK 8 (and it must be installed at `/Library/Java/JavaVirtualMachines...` for some reason. I couldn't get an SDKMAN installed Java to work)
    * E.g. `JAVA_HOME=/Library/Java/JavaVirtualMachines/adoptopenjdk-8.jdk/Contents/Home/ target/products/org.openjdk.jmc/macosx/cocoa/x86_64/JDK\ Mission\ Control.app/Contents/MacOS/jmc -vm $JAVA_HOME/bin`
* Can we run WireMock on Graal? If Jetty can run on Graal, then WireMock should be pretty easy to run on Graal. See an
  example for running Jetty on Graal at <https://github.com/tipsy/javalin/issues/286>
