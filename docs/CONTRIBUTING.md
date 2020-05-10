# Contributing

To contriubte, first see the project page <https://github.com/dgroomes/wiremock-playground/projects/1>.

## Notes

* TODO add some stubs
* Create a JDK Flight Recording
  * (see `java` options at <https://docs.oracle.com/en/java/javase/14/docs/specs/man/java.html> and scroll down to `-XX:StartFlightRecording`)
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