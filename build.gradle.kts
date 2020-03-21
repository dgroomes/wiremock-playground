plugins {
    java
    application
}

repositories {
    jcenter()
}

java {
    sourceCompatibility = JavaVersion.VERSION_14
    targetCompatibility = JavaVersion.VERSION_14
}

tasks {
    /**
     * Enable Java language preview features (so we can "records")
     */
    withType(JavaCompile::class.java) {
        options.compilerArgs.addAll(arrayOf("--enable-preview"))
    }

    withType(Test::class.java) {
        jvmArgs = listOf("--enable-preview")
    }

    named<CreateStartScripts>("startScripts") {
        defaultJvmOpts = listOf("--enable-preview")
    }

    named<JavaExec>("run") {
        jvmArgs = listOf("--enable-preview")
    }
}

val wireMockVersion = "2.26.3"
val junitJupiterVersion = "5.6.0"
val assertJVersion = "3.15.0"
val slf4jVersion = "1.7.30"

dependencies {
    implementation(group = "com.github.tomakehurst", name = "wiremock-jre8", version = wireMockVersion)
    implementation(group = "org.slf4j", name = "slf4j-api", version = slf4jVersion)
    implementation(group = "org.slf4j", name = "slf4j-simple", version = slf4jVersion)

    testImplementation(group = "org.assertj", name = "assertj-core", version = assertJVersion)
    testImplementation(group = "org.junit.jupiter", name = "junit-jupiter-api", version = junitJupiterVersion)
    testRuntimeOnly(group = "org.junit.jupiter", name = "junit-jupiter-engine", version = junitJupiterVersion)
}

application {
    mainClassName = "dgroomes.wiremock.App"
}

val test by tasks.getting(Test::class) {
    useJUnitPlatform()
}
