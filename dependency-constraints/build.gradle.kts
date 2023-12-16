plugins {
    /*
     * Using the Gradle "platform" feature to declare dependency version constraints that will be used by the other subprojects
     * so that we only ever have to declare version information in one place instead of across all subprojects. See the
     * Gradle docs about "platform": https://docs.gradle.org/current/userguide/platforms.html
     */
    `java-platform`
}

val slf4jVersion = "1.7.36" // releases: http://www.slf4j.org/news.html
val wireMockVersion = "2.35.1" // releases: https://github.com/tomakehurst/wiremock/tags

dependencies {
    constraints {
        api("org.slf4j:slf4j-api:$slf4jVersion")
        api("org.slf4j:slf4j-simple:$slf4jVersion")
        api("com.github.tomakehurst:wiremock-jre8:$wireMockVersion")
        api("com.github.tomakehurst:wiremock-jre8-standalone:$wireMockVersion")
    }
}
