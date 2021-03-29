dependencies {
    implementation(group = "com.github.tomakehurst", name = "wiremock-jre8")
    implementation(group = "org.slf4j", name = "slf4j-api")
    implementation(group = "org.slf4j", name = "slf4j-simple")

    testImplementation(group = "org.assertj", name = "assertj-core")
    testImplementation(group = "org.junit.jupiter", name = "junit-jupiter-api")
    testRuntimeOnly(group = "org.junit.jupiter", name = "junit-jupiter-engine")
}

application {
    mainClass.set("dgroomes.wiremock.ProgrammaticMain")
}
