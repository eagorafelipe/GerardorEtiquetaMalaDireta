# ProGuard rules for GeradorEtiquetasMalaDireta
# Keep main class
-keep class br.com.etiqueta.maladireta.MainKt { *; }

# Keep Compose runtime
-keep class androidx.compose.** { *; }
-keep class org.jetbrains.skia.** { *; }
-keep class org.jetbrains.skiko.** { *; }

# Keep iText PDF
-keep class com.itextpdf.** { *; }
-dontwarn com.itextpdf.**

# Keep Apache POI
-keep class org.apache.poi.** { *; }
-dontwarn org.apache.poi.**

# Keep OpenCSV
-keep class com.opencsv.** { *; }
-dontwarn com.opencsv.**

# Keep Kotlin metadata
-keep class kotlin.Metadata { *; }

# Keep coroutines
-keep class kotlinx.coroutines.** { *; }
-dontwarn kotlinx.coroutines.**

# Don't obfuscate model classes
-keep class br.com.etiqueta.maladireta.models.** { *; }
