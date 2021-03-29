dependencies {
    implementation(group = "com.github.tomakehurst", name = "wiremock-standalone")
}

application {
    mainClass.set("com.github.tomakehurst.wiremock.standalone.WireMockServerRunner")
}
