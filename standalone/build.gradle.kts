dependencies {
    implementation(group = "com.github.tomakehurst", name = "wiremock-jre8-standalone")
}

application {
    mainClass.set("com.github.tomakehurst.wiremock.standalone.WireMockServerRunner")
}
