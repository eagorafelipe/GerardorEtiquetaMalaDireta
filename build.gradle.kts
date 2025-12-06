import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform") version "2.0.21"
    id("org.jetbrains.compose") version "1.7.1"
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.21"
}

group = "br.com.etiqueta.maladireta"
version = "1.0.0"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    jvm("desktop")

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.components.resources)

                // Kotlin Coroutines
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
            }
        }

        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)

                // Kotlin Coroutines para Swing
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.8.1")

                // Apache POI para leitura de Excel
                implementation("org.apache.poi:poi:5.2.5")
                implementation("org.apache.poi:poi-ooxml:5.2.5")

                // iText PDF para geração de PDFs
                implementation("com.itextpdf:itext7-core:8.0.2")

                // OpenCSV para leitura de CSV
                implementation("com.opencsv:opencsv:5.9")
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "br.com.etiqueta.maladireta.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb, TargetFormat.Rpm, TargetFormat.AppImage)

            packageName = "GeradorEtiquetasMalaDireta"
            packageVersion = "1.0.0"
            description = "Gerador de Etiquetas Pimaco para Mala Direta"
            vendor = "Etiqueta MalaDireta"

            linux {
                iconFile.set(project.file("src/desktopMain/resources/icon.png"))
                debMaintainer = "eagorafelipe@gmail.com"
                menuGroup = "Office"
                appRelease = "1"
                appCategory = "Office"
            }

            windows {
                iconFile.set(project.file("src/desktopMain/resources/icon.ico"))
                menuGroup = "Etiqueta MalaDireta"
                upgradeUuid = "8e7c9f3a-5b2d-4e1a-9f8c-7d6b5a4e3c2b"
                perUserInstall = true
                dirChooser = true
            }

            macOS {
                iconFile.set(project.file("src/desktopMain/resources/icon.icns"))
                bundleID = "br.com.etiqueta.maladireta"
            }
        }

        buildTypes.release.proguard {
            configurationFiles.from(project.file("proguard-rules.pro"))
            isEnabled.set(false)
        }
    }
}
