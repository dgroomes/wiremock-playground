plugins {
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.wire.mock.standalone)
}

application {
    mainClass.set("com.github.tomakehurst.wiremock.standalone.WireMockServerRunner")
}
