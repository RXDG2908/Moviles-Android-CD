plugins {
    kotlin("jvm") version "2.2.10"
    application
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("junit:junit:4.13.2")
}

kotlin {
    jvmToolchain(21)
}

application {
    // Punto de entrada: fun main() en Main.kt
    mainClass.set("com.leon.s02parking.MainKt")
}

tasks.test {
    useJUnit()
}

tasks.named<JavaExec>("run") {
    // Conecta la entrada estándar de la terminal con el proceso de la app,
    // para que readLine() funcione al ejecutar "./gradlew run".
    standardInput = System.`in`
}
