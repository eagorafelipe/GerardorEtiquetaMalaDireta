package br.com.etiqueta.maladireta.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.etiqueta.maladireta.models.ModeloEtiqueta
import br.com.etiqueta.maladireta.services.PDFGenerator
import br.com.etiqueta.maladireta.services.PlanilhaReader
import br.com.etiqueta.maladireta.ui.components.AppCard
import br.com.etiqueta.maladireta.viewmodel.AppState
import br.com.etiqueta.maladireta.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import java.io.FilenameFilter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MainViewModel) {
    val scope = rememberCoroutineScope()
    val planilhaReader = remember { PlanilhaReader() }
    val pdfGenerator = remember { PDFGenerator() }

    var showModeloDropdown by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(30.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Cabeçalho
        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Text(
                text = "Gerador de Etiquetas Pimaco",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E3A8A)
            )
            Text(
                text = "Crie etiquetas profissionais a partir de planilhas Excel ou CSV",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.secondary
            )
        }

        // Card - Seleção de Modelo
        AppCard(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "📋 Modelo de Etiqueta",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Selecione o modelo Pimaco:",
                    modifier = Modifier.width(180.dp)
                )

                ExposedDropdownMenuBox(
                    expanded = showModeloDropdown,
                    onExpandedChange = { showModeloDropdown = it },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = viewModel.selectedModelo.toString(),
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showModeloDropdown) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = showModeloDropdown,
                        onDismissRequest = { showModeloDropdown = false }
                    ) {
                        viewModel.modelos.forEach { modelo ->
                            DropdownMenuItem(
                                text = { Text(modelo.toString()) },
                                onClick = {
                                    viewModel.selectModelo(modelo)
                                    showModeloDropdown = false
                                }
                            )
                        }
                    }
                }
            }

            // Informações do modelo
            ModeloInfoSection(viewModel.selectedModelo)
        }

        // Card - Seleção de Arquivo
        AppCard(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "📂 Arquivo de Dados",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "Formato esperado: 6 colunas (Nome_Sindico, Nome_Condominio, End_Condominio, Bairro_Condominio, Cidade_Condominio, Cep_Condominio)",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.secondary
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = viewModel.selectedFileName ?: "",
                    onValueChange = {},
                    readOnly = true,
                    placeholder = { Text("Nenhum arquivo selecionado") },
                    modifier = Modifier.weight(1f)
                )

                Button(
                    onClick = {
                        val fileDialog = FileDialog(null as Frame?, "Selecionar Planilha", FileDialog.LOAD)
                        fileDialog.filenameFilter = FilenameFilter { _, name ->
                            name.lowercase().endsWith(".xlsx") ||
                                    name.lowercase().endsWith(".xls") ||
                                    name.lowercase().endsWith(".csv")
                        }
                        fileDialog.isVisible = true

                        fileDialog.file?.let { fileName ->
                            val filePath = fileDialog.directory + fileName
                            viewModel.setFilePath(filePath, fileName)
                            viewModel.setLoading()

                            scope.launch(Dispatchers.IO) {
                                try {
                                    val registros = planilhaReader.lerArquivo(File(filePath))
                                    withContext(Dispatchers.Main) {
                                        viewModel.updateRegistros(registros)
                                    }
                                } catch (e: Exception) {
                                    withContext(Dispatchers.Main) {
                                        viewModel.setError("Erro ao carregar arquivo: ${e.message}")
                                        dialogMessage = "Não foi possível ler o arquivo: ${e.message}"
                                        showErrorDialog = true
                                    }
                                }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                ) {
                    Text("Selecionar Arquivo")
                }
            }

            // Status de registros carregados
            if (viewModel.registros.isNotEmpty()) {
                Text(
                    text = "✓ ${viewModel.registros.size} registro(s) carregado(s)",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF059669)
                )
            }
        }

        // Card - Geração
        AppCard(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "🖨️ Geração do PDF",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = {
                        val fileDialog = FileDialog(null as Frame?, "Salvar PDF", FileDialog.SAVE)
                        fileDialog.file = "Etiquetas_${viewModel.selectedModelo.codigo}_${System.currentTimeMillis()}.pdf"
                        fileDialog.isVisible = true

                        fileDialog.file?.let { fileName ->
                            val outputPath = fileDialog.directory + (if (fileName.endsWith(".pdf")) fileName else "$fileName.pdf")
                            viewModel.setLoading()

                            scope.launch(Dispatchers.IO) {
                                try {
                                    pdfGenerator.gerarPDF(
                                        viewModel.registros,
                                        viewModel.selectedModelo,
                                        File(outputPath)
                                    ) { atual, total ->
                                        scope.launch(Dispatchers.Main) {
                                            viewModel.setProgress(atual, total, "Gerando PDF... Página $atual de $total")
                                        }
                                    }

                                    withContext(Dispatchers.Main) {
                                        viewModel.setSuccess("PDF gerado com sucesso!")
                                        dialogMessage = "PDF gerado com sucesso em:\n$outputPath"
                                        showSuccessDialog = true
                                    }
                                } catch (e: Exception) {
                                    withContext(Dispatchers.Main) {
                                        viewModel.setError("Erro ao gerar PDF: ${e.message}")
                                        dialogMessage = "Não foi possível gerar o PDF: ${e.message}"
                                        showErrorDialog = true
                                    }
                                }
                            }
                        }
                    },
                    enabled = viewModel.registros.isNotEmpty() && viewModel.appState !is AppState.Loading && viewModel.appState !is AppState.Progress,
                    modifier = Modifier.width(150.dp)
                ) {
                    Text("Gerar PDF")
                }

                // Status
                when (val state = viewModel.appState) {
                    is AppState.Loading -> {
                        Text(
                            text = "Carregando...",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    is AppState.Progress -> {
                        Text(
                            text = state.message,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    is AppState.Success -> {
                        Text(
                            text = "✓ ${state.message}",
                            fontSize = 13.sp,
                            color = Color(0xFF059669)
                        )
                    }
                    is AppState.Error -> {
                        Text(
                            text = state.message,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    else -> {}
                }
            }

            // Progress bar
            if (viewModel.appState is AppState.Progress) {
                val progress = viewModel.appState as AppState.Progress
                LinearProgressIndicator(
                    progress = { progress.current.toFloat() / progress.total },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Rodapé
        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "💡 Dica: Use filtros no Excel antes de salvar a planilha para gerar etiquetas de cidades específicas",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.secondary,
                fontStyle = FontStyle.Italic
            )
        }
    }

    // Diálogos
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            title = { Text("Sucesso!") },
            text = { Text(dialogMessage) },
            confirmButton = {
                TextButton(onClick = { showSuccessDialog = false }) {
                    Text("OK")
                }
            }
        )
    }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text("Erro") },
            text = { Text(dialogMessage) },
            confirmButton = {
                TextButton(onClick = { showErrorDialog = false }) {
                    Text("OK")
                }
            }
        )
    }
}

@Composable
private fun ModeloInfoSection(modelo: ModeloEtiqueta) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(6.dp)
            )
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        InfoColumn(
            title = "Dimensões",
            value = "${modelo.larguraMM}mm × ${modelo.alturaMM}mm"
        )
        InfoColumn(
            title = "Layout",
            value = "${modelo.colunas} colunas × ${modelo.linhas} linhas"
        )
        InfoColumn(
            title = "Total por Página",
            value = "${modelo.etiquetasPorPagina} etiquetas"
        )
    }
}

@Composable
private fun InfoColumn(title: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary
        )
        Text(
            text = value,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
