plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("io.github.cdimascio:java-dotenv:5.2.2")
    testImplementation("com.fasterxml.jackson.core:jackson-databind:2.15.0")
}

tasks.test {
    useJUnitPlatform()
}