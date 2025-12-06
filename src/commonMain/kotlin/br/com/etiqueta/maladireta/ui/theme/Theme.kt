package br.com.etiqueta.maladireta.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF2563EB),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF1D4ED8),
    onPrimaryContainer = Color.White,
    secondary = Color(0xFF6B7280),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFF3F4F6),
    onSecondaryContainer = Color(0xFF374151),
    tertiary = Color(0xFF059669),
    onTertiary = Color.White,
    error = Color(0xFFDC2626),
    onError = Color.White,
    background = Color(0xFFF9FAFB),
    onBackground = Color(0xFF111827),
    surface = Color.White,
    onSurface = Color(0xFF111827),
    surfaceVariant = Color(0xFFF3F4F6),
    onSurfaceVariant = Color(0xFF6B7280),
    outline = Color(0xFFE5E7EB)
)

@Composable
fun EtiquetasMalaDiretaTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}
