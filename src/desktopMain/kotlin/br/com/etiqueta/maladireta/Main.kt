package br.com.etiqueta.maladireta

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import br.com.etiqueta.maladireta.ui.screens.MainScreen
import br.com.etiqueta.maladireta.ui.theme.EtiquetasMalaDiretaTheme
import br.com.etiqueta.maladireta.viewmodel.MainViewModel

fun main() = application {
    val windowState = rememberWindowState(width = 750.dp, height = 650.dp)
    val viewModel = MainViewModel()

    Window(
        onCloseRequest = ::exitApplication,
        state = windowState,
        title = "Gerador de Etiquetas Pimaco - PDF",
        resizable = true
    ) {
        EtiquetasMalaDiretaTheme {
            MainScreen(viewModel)
        }
    }
}
