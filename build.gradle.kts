val wireMockVersion = "2.26.3"
val junitJupiterVersion = "5.6.0"
val assertJVersion = "3.15.0"
val slf4jVersion = "1.7.30"

val dependencyConstraints = project(":dependency-constraints")
configure(allprojects.minus(dependencyConstraints)) {
    apply(plugin = "java")
    apply(plugin = "application")

    repositories {
        mavenCentral()
    }

    dependencies {
        "implementation"(platform(dependencyConstraints))
    }
}
