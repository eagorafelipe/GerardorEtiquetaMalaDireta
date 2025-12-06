package br.com.etiqueta.maladireta.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import br.com.etiqueta.maladireta.models.ModeloEtiqueta
import br.com.etiqueta.maladireta.models.Registro

sealed class AppState {
    object Idle : AppState()
    object Loading : AppState()
    data class Success(val message: String) : AppState()
    data class Error(val message: String) : AppState()
    data class Progress(val current: Int, val total: Int, val message: String) : AppState()
}

class MainViewModel {
    var selectedModelo by mutableStateOf(ModeloEtiqueta.MODELOS_PIMACO.first())
        private set

    var selectedFilePath by mutableStateOf<String?>(null)
        private set

    var selectedFileName by mutableStateOf<String?>(null)
        private set

    var registros by mutableStateOf<List<Registro>>(emptyList())
        private set

    var appState by mutableStateOf<AppState>(AppState.Idle)
        private set

    val modelos = ModeloEtiqueta.MODELOS_PIMACO

    fun selectModelo(modelo: ModeloEtiqueta) {
        selectedModelo = modelo
    }

    fun setFilePath(path: String, fileName: String) {
        selectedFilePath = path
        selectedFileName = fileName
    }

    fun updateRegistros(newRegistros: List<Registro>) {
        registros = newRegistros
        appState = AppState.Success("${newRegistros.size} registro(s) carregado(s)")
    }

    fun setLoading() {
        appState = AppState.Loading
    }

    fun setProgress(current: Int, total: Int, message: String) {
        appState = AppState.Progress(current, total, message)
    }

    fun setSuccess(message: String) {
        appState = AppState.Success(message)
    }

    fun setError(message: String) {
        appState = AppState.Error(message)
    }

    fun resetState() {
        appState = AppState.Idle
    }

    fun clearFile() {
        selectedFilePath = null
        selectedFileName = null
        registros = emptyList()
        appState = AppState.Idle
    }
}
