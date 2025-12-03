package br.com.etiqueta.maladireta

import javafx.application.Application
import tornadofx.App
import tornadofx.reloadStylesheetsOnFocus

class EtiquetasApp : App(MainView::class, Styles::class) {
    init {
        reloadStylesheetsOnFocus()
    }
}

fun main() {
    Application.launch(EtiquetasApp::class.java)
}