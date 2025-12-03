plugins {
    kotlin("jvm") version "1.9.22"
    application
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "br.com.etiqueta.maladireta"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Kotlin
    implementation(kotlin("stdlib"))

    // JavaFX (TornadoFX para facilitar desenvolvimento)
    implementation("no.tornado:tornadofx:1.7.20")

    // Apache POI para leitura de Excel
    implementation("org.apache.poi:poi:5.2.5")
    implementation("org.apache.poi:poi-ooxml:5.2.5")

    // iText PDF para geração de PDFs
    implementation("com.itextpdf:itext7-core:8.0.2")

    // OpenCSV para leitura de CSV
    implementation("com.opencsv:opencsv:5.9")

    // Kotlin Coroutines para operações assíncronas
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-javafx:1.7.3")
}

javafx {
    version = "21"
    modules = listOf("javafx.controls", "javafx.fxml")
}

application {
    mainClass.set("br.com.etiqueta.maladireta.MainKtKt")
}