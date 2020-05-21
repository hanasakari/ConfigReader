plugins {
    java
}

group = "cn.jamci"
version = "0.0.1"

repositories {
    mavenCentral()
}

val jackson_yaml_version = "2.11.0"
val jackson_version = "2.11.0"


dependencies {
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:$jackson_yaml_version")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jackson_version")
    testImplementation("junit", "junit", "4.12")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}