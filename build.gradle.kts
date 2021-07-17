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
