plugins {
    /*
     * Using the Gradle "platform" feature to declare dependency version constraints that will be used by the other sub-projects
     * so that we only ever have to declare version information in one place instead of across all sub-projects. See the
     * Gradle docs about "platform": https://docs.gradle.org/current/userguide/platforms.html
     */
    `java-platform`
}

val wireMockVersion = "2.26.3"
val junitJupiterVersion = "5.6.0"
val assertJVersion = "3.15.0"
val slf4jVersion = "1.7.30"

dependencies {
    constraints {
        /**
         * For some reason, using the overloaded method `api(group: String, name: String, version: String)` causes the
         * Gradle configuration phase to fail with the error "Adding dependencies to platforms is not allowed by default."
         * So instead I am using the method `api(constraintNotation: String)`. But shouldn't I be able to use the former
         * method overload? Is this a defect? From the JavaDoc, it actually suggests that the latter "adds a dependency"
         * while the former "adds a dependency constraint" so that suggests it is not a defect. But from a user perspective,
         * the method calls convey the same information so I would guess they should do the same thing. But they don't.
         */
     // api("com.github.tomakehurst", "wiremock-jre8", wireMockVersion) // This form causes Gradle to fail
        api("com.github.tomakehurst:wiremock-jre8:$wireMockVersion")    // This form works (and is indeed the form that the official Gradle example uses)
        api("org.slf4j:slf4j-api:$slf4jVersion")
        api("org.slf4j:slf4j-simple:$slf4jVersion")
        api("com.github.tomakehurst:wiremock-standalone:$wireMockVersion")
        api("org.assertj:assertj-core:$assertJVersion")
        api("org.junit.jupiter:junit-jupiter-api:$junitJupiterVersion")
        api("org.junit.jupiter:junit-jupiter-engine:$junitJupiterVersion")
    }
}
