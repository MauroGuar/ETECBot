plugins {
    id("java")
    application
    kotlin("jvm") version "1.9.20"
}

group = "com.etec_bot"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation ("com.google.guava:guava:31.1-jre")
    implementation("net.dv8tion:JDA:5.0.0-beta.17")
    implementation("ch.qos.logback:logback-classic:1.2.9")
    implementation("io.github.cdimascio:dotenv-java:3.0.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.google.api-client:google-api-client:2.2.0")
    implementation("com.google.oauth-client:google-oauth-client-jetty:1.34.1")
    implementation("com.google.apis:google-api-services-drive:v3-rev20220815-2.0.0")
}

application {
    mainClass.set("com.etec_bot.ETECBot")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

tasks.register<Jar>("fatJar") {
    manifest {
        attributes(
            mapOf("Main-Class" to "com.etec_bot.ETECBot")
        )
    }
    archiveBaseName.set(project.rootProject.name)
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    with(tasks.jar.get())

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    from("${project.projectDir}/src/main/") {
        include("resources/**")
    }
    from("${project.projectDir}") {
        include(".env")
    }
}
