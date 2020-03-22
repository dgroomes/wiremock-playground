dependencies {
    implementation(group = "com.github.tomakehurst", name = "wiremock-standalone")
}

application {
    mainClassName = "com.github.tomakehurst.wiremock.standalone.WireMockServerRunner"
}