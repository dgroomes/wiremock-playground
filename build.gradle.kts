plugins {
    java
}

val wireMockVersion = "2.26.3"
val junitJupiterVersion = "5.6.0"
val assertJVersion = "3.15.0"
val slf4jVersion = "1.7.30"

allprojects {
    // Do we really need to declare this java plugin here *and* at the top of this file?
    apply(plugin = "java")
    apply(plugin = "application")

    repositories {
        mavenCentral()
    }

    dependencies {
        constraints {
            implementation(group = "com.github.tomakehurst", name = "wiremock-jre8", version = wireMockVersion)
            implementation(group = "org.slf4j", name = "slf4j-api", version = slf4jVersion)
            implementation(group = "org.slf4j", name = "slf4j-simple", version = slf4jVersion)
            implementation(group = "com.github.tomakehurst", name = "wiremock-standalone", version = wireMockVersion)

            testImplementation(group = "org.assertj", name = "assertj-core", version = assertJVersion)
            testImplementation(group = "org.junit.jupiter", name = "junit-jupiter-api", version = junitJupiterVersion)
            testRuntimeOnly(group = "org.junit.jupiter", name = "junit-jupiter-engine", version = junitJupiterVersion)
        }
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_15
        targetCompatibility = JavaVersion.VERSION_15
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
            useJUnitPlatform()
        }

        named<CreateStartScripts>("startScripts") {
            defaultJvmOpts = listOf("--enable-preview")
        }

        named<JavaExec>("run") {
            jvmArgs = listOf("--enable-preview")
        }
    }
}

