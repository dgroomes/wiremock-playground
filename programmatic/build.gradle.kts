plugins {
    java
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.wiremock)
    implementation(libs.slf4j.api)
    implementation(libs.slf4j.simple)
}

application {
    mainClass.set("dgroomes.wiremock.ProgrammaticMain")
}
