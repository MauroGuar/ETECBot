plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version("1.8.10")
    application
}

group = "com.etec_bot"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation ("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation ("com.google.guava:guava:31.1-jre")
    implementation("net.dv8tion:JDA:5.0.0-beta.11")
    implementation("io.github.cdimascio:dotenv-java:3.0.0")
    implementation("com.google.code.gson:gson:2.10.1")
}

application {
    mainClass.set("com.etec_bot.ETECBot")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}