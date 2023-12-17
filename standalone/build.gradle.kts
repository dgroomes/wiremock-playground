plugins {
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.wiremock.standalone)
}

application {
    mainClass.set("wiremock.Run")
}
