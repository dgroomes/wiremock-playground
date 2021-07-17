dependencies {
    implementation(group = "com.github.tomakehurst", name = "wiremock-jre8")
    implementation(group = "org.slf4j", name = "slf4j-api")
    implementation(group = "org.slf4j", name = "slf4j-simple")
}

application {
    mainClass.set("dgroomes.wiremock.ProgrammaticMain")
}
