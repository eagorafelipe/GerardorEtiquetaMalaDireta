package br.com.etiqueta.maladireta.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import br.com.etiqueta.maladireta.models.EtiquetaTemplate
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

    // Filtros
    var cidadesDisponiveis by mutableStateOf<List<String>>(emptyList())
        private set

    var cidadesSelecionadas by mutableStateOf<Set<String>>(emptySet())
        private set

    var filtroAtivo by mutableStateOf(false)
        private set

    // Templates customizados
    var selectedTemplate by mutableStateOf(EtiquetaTemplate.DEFAULT)
        private set

    var templatesDisponiveis by mutableStateOf(EtiquetaTemplate.TEMPLATES_PADRAO)
        private set

    // Preview
    var showPreview by mutableStateOf(false)
        private set

    var previewPageIndex by mutableStateOf(0)
        private set

    val modelos = ModeloEtiqueta.MODELOS_PIMACO

    // Registros filtrados baseados na seleção de cidades
    val registrosFiltrados: List<Registro>
        get() = if (filtroAtivo && cidadesSelecionadas.isNotEmpty()) {
            registros.filter { it.cidadeCondominio.uppercase() in cidadesSelecionadas.map { c -> c.uppercase() } }
        } else {
            registros
        }

    fun selectModelo(modelo: ModeloEtiqueta) {
        selectedModelo = modelo
    }

    fun setFilePath(path: String, fileName: String) {
        selectedFilePath = path
        selectedFileName = fileName
    }

    fun updateRegistros(newRegistros: List<Registro>) {
        registros = newRegistros
        // Extrair cidades únicas dos registros
        cidadesDisponiveis = newRegistros
            .map { it.cidadeCondominio }
            .filter { it.isNotBlank() }
            .distinct()
            .sorted()
        // Resetar filtros
        cidadesSelecionadas = emptySet()
        filtroAtivo = false
        appState = AppState.Success("${newRegistros.size} registro(s) carregado(s)")
    }

    // Métodos de filtro
    fun toggleCidade(cidade: String) {
        cidadesSelecionadas = if (cidade in cidadesSelecionadas) {
            cidadesSelecionadas - cidade
        } else {
            cidadesSelecionadas + cidade
        }
    }

    fun selecionarTodasCidades() {
        cidadesSelecionadas = cidadesDisponiveis.toSet()
    }

    fun limparSelecaoCidades() {
        cidadesSelecionadas = emptySet()
    }

    fun toggleFiltroAtivo() {
        filtroAtivo = !filtroAtivo
    }

    fun atualizarFiltroAtivo(ativo: Boolean) {
        filtroAtivo = ativo
    }

    // Métodos de template
    fun selectTemplate(template: EtiquetaTemplate) {
        selectedTemplate = template
    }

    fun addCustomTemplate(template: EtiquetaTemplate) {
        templatesDisponiveis = templatesDisponiveis + template
    }

    // Métodos de preview
    fun togglePreview() {
        showPreview = !showPreview
        if (showPreview) {
            previewPageIndex = 0
        }
    }

    fun setPreviewPage(index: Int) {
        previewPageIndex = index.coerceIn(0, maxPreviewPages - 1)
    }

    fun nextPreviewPage() {
        if (previewPageIndex < maxPreviewPages - 1) {
            previewPageIndex++
        }
    }

    fun previousPreviewPage() {
        if (previewPageIndex > 0) {
            previewPageIndex--
        }
    }

    val maxPreviewPages: Int
        get() {
            val total = registrosFiltrados.size
            val porPagina = selectedModelo.etiquetasPorPagina
            return if (total == 0) 1 else kotlin.math.ceil(total.toDouble() / porPagina).toInt()
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
        cidadesDisponiveis = emptyList()
        cidadesSelecionadas = emptySet()
        filtroAtivo = false
        appState = AppState.Idle
    }
}
