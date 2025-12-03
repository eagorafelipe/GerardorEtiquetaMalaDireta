package br.com.etiqueta.maladireta

import br.com.etiqueta.maladireta.models.ModeloEtiqueta
import br.com.etiqueta.maladireta.models.Registro
import br.com.etiqueta.maladireta.services.PDFGenerator
import br.com.etiqueta.maladireta.services.PlanilhaReader
import javafx.geometry.Pos
import javafx.scene.control.*
import javafx.scene.layout.Priority
import javafx.scene.text.FontPosture
import javafx.scene.text.FontWeight
import javafx.stage.FileChooser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tornadofx.*
import java.io.File

class MainView : View("Gerador de Etiquetas Pimaco - PDF") {

    private val planilhaReader = PlanilhaReader()
    private val pdfGenerator = PDFGenerator()

    private var arquivoPlanilha: File? = null
    private var registros: List<Registro> = emptyList()

    private lateinit var comboModelo: ComboBox<ModeloEtiqueta>
    private lateinit var txtArquivo: TextField
    private lateinit var btnSelecionar: Button
    private lateinit var btnGerar: Button
    private lateinit var lblStatus: Label
    private lateinit var lblTotal: Label
    private lateinit var progressBar: ProgressBar

    override val root = vbox(spacing = 20) {
        setPrefSize(700.0, 550.0)
        padding = insets(30)
        style {
            backgroundColor += c("#f9fafb")
        }

        // Cabeçalho
        vbox(spacing = 5) {
            label("Gerador de Etiquetas Pimaco") {
                addClass(Styles.heading)
            }
            label("Crie etiquetas profissionais a partir de planilhas Excel ou CSV") {
                style {
                    fontSize = 14.px
                    textFill = c("#6b7280")
                }
            }
        }

        // Card - Seleção de Modelo
        vbox(spacing = 15) {
            addClass(Styles.card)

            label("📋 Modelo de Etiqueta") {
                style {
                    fontSize = 16.px
                    fontWeight = FontWeight.BOLD
                    textFill = c("#374151")
                }
            }

            hbox(spacing = 10) {
                label("Selecione o modelo Pimaco:") {
                    alignment = Pos.CENTER_LEFT
                    minWidth = 180.0
                }

                comboModelo = combobox {
                    items = ModeloEtiqueta.MODELOS_PIMACO.asObservable()
                    selectionModel.selectFirst()
                    prefWidth = 400.0

                    valueProperty().addListener { _, _, novoModelo ->
                        atualizarInfoModelo(novoModelo)
                    }
                }
            }

            // Informações do modelo
            hbox(spacing = 20) {
                style {
                    backgroundColor += c("#f3f4f6")
                    borderRadius += box(6.px)
                    padding = box(12.px)
                }

                vbox(spacing = 5) {
                    label("Dimensões") {
                        style {
                            fontSize = 11.px
                            textFill = c("#6b7280")
                            fontWeight = FontWeight.BOLD
                        }
                    }
                    label {
                        textProperty().bind(comboModelo.valueProperty().stringBinding {
                            it?.let { "${it.larguraMM}mm × ${it.alturaMM}mm" } ?: "-"
                        })
                        style {
                            fontSize = 13.px
                            textFill = c("#111827")
                        }
                    }
                }

                vbox(spacing = 5) {
                    label("Layout") {
                        style {
                            fontSize = 11.px
                            textFill = c("#6b7280")
                            fontWeight = FontWeight.BOLD
                        }
                    }
                    label {
                        textProperty().bind(comboModelo.valueProperty().stringBinding {
                            it?.let { "${it.colunas} colunas × ${it.linhas} linhas" } ?: "-"
                        })
                        style {
                            fontSize = 13.px
                            textFill = c("#111827")
                        }
                    }
                }

                vbox(spacing = 5) {
                    label("Total por Página") {
                        style {
                            fontSize = 11.px
                            textFill = c("#6b7280")
                            fontWeight = FontWeight.BOLD
                        }
                    }
                    label {
                        textProperty().bind(comboModelo.valueProperty().stringBinding {
                            it?.let { "${it.etiquetasPorPagina} etiquetas" } ?: "-"
                        })
                        style {
                            fontSize = 13.px
                            textFill = c("#111827")
                        }
                    }
                }
            }
        }

        // Card - Seleção de Arquivo
        vbox(spacing = 15) {
            addClass(Styles.card)

            label("📂 Arquivo de Dados") {
                style {
                    fontSize = 16.px
                    fontWeight = FontWeight.BOLD
                    textFill = c("#374151")
                }
            }

            label("Formato esperado: 5 colunas (Nome_Sindico, Nome_Condominio, End_Condominio, Bairro_Condominio, Cidade_Condominio, Cep_Condominio)") {
                style {
                    fontSize = 12.px
                    textFill = c("#6b7280")
                }
            }

            hbox(spacing = 10) {
                txtArquivo = textfield {
                    promptText = "Nenhum arquivo selecionado"
                    isEditable = false
                    hgrow = Priority.ALWAYS
                    style {
                        backgroundColor += c("#f9fafb")
                    }
                }

                btnSelecionar = button("Selecionar Arquivo") {
                    addClass(Styles.secondaryButton)
                    action { selecionarArquivo() }
                }
            }

            lblTotal = label {
                style {
                    fontSize = 13.px
                    textFill = c("#059669")
                    fontWeight = FontWeight.BOLD
                }
                isVisible = false
            }
        }

        // Card - Geração
        vbox(spacing = 15) {
            addClass(Styles.card)

            label("🖨️ Geração do PDF") {
                style {
                    fontSize = 16.px
                    fontWeight = FontWeight.BOLD
                    textFill = c("#374151")
                }
            }

            hbox(spacing = 10) {
                alignment = Pos.CENTER_LEFT

                btnGerar = button("Gerar PDF") {
                    addClass(Styles.primaryButton)
                    isDisable = true
                    prefWidth = 150.0
                    action { gerarPDF() }
                }

                lblStatus = label {
                    style {
                        fontSize = 13.px
                        textFill = c("#6b7280")
                    }
                }
            }

            progressBar = progressbar {
                prefWidth = Double.MAX_VALUE
                progress = 0.0
                isVisible = false
            }
        }

        // Rodapé
        hbox {
            alignment = Pos.CENTER
            vgrow = Priority.ALWAYS

            label("💡 Dica: Use filtros no Excel antes de salvar a planilha para gerar etiquetas de cidades específicas") {
                style {
                    fontSize = 12.px
                    textFill = c("#9ca3af")
                    fontStyle = FontPosture.ITALIC
                }
            }
        }
    }

    private fun atualizarInfoModelo(modelo: ModeloEtiqueta?) {
        // Info já está vinculada via bindings
    }

    private fun selecionarArquivo() {
        val fileChooser = FileChooser()
        fileChooser.title = "Selecionar Planilha"
        fileChooser.extensionFilters.addAll(
            FileChooser.ExtensionFilter("Planilhas", "*.xlsx", "*.xls", "*.csv"),
            FileChooser.ExtensionFilter("Excel", "*.xlsx", "*.xls"),
            FileChooser.ExtensionFilter("CSV", "*.csv")
        )

        val arquivo = fileChooser.showOpenDialog(currentWindow)

        if (arquivo != null) {
            arquivoPlanilha = arquivo
            txtArquivo.text = arquivo.name
            lblStatus.text = "Carregando dados..."
            lblStatus.textFill = c("#6b7280")

            GlobalScope.launch(Dispatchers.IO) {
                try {
                    registros = planilhaReader.lerArquivo(arquivo)

                    withContext(Dispatchers.JavaFx) {
                        lblTotal.text = "✓ ${registros.size} registro(s) carregado(s)"
                        lblTotal.isVisible = true
                        lblStatus.text = ""
                        btnGerar.isDisable = false
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.JavaFx) {
                        lblStatus.text = "Erro ao carregar arquivo"
                        lblStatus.textFill = c("#dc2626")
                        lblTotal.isVisible = false

                        alert(
                            Alert.AlertType.ERROR,
                            "Erro ao Carregar Arquivo",
                            "Não foi possível ler o arquivo: ${e.message}"
                        )
                    }
                }
            }
        }
    }

    private fun gerarPDF() {
        val modelo = comboModelo.value ?: return

        val fileChooser = FileChooser()
        fileChooser.title = "Salvar PDF"
        fileChooser.initialFileName = "Etiquetas_${modelo.codigo}_${System.currentTimeMillis()}.pdf"
        fileChooser.extensionFilters.add(
            FileChooser.ExtensionFilter("PDF", "*.pdf")
        )

        val arquivoSaida = fileChooser.showSaveDialog(currentWindow) ?: return

        btnGerar.isDisable = true
        btnSelecionar.isDisable = true
        progressBar.isVisible = true
        progressBar.progress = 0.0
        lblStatus.text = "Gerando PDF..."
        lblStatus.textFill = c("#2563eb")

        GlobalScope.launch(Dispatchers.IO) {
            try {
                pdfGenerator.gerarPDF(registros, modelo, arquivoSaida) { atual, total ->
                    GlobalScope.launch(Dispatchers.JavaFx) {
                        progressBar.progress = atual.toDouble() / total
                        lblStatus.text = "Gerando PDF... Página $atual de $total"
                    }
                }

                withContext(Dispatchers.JavaFx) {
                    progressBar.isVisible = false
                    lblStatus.text = "✓ PDF gerado com sucesso!"
                    lblStatus.textFill = c("#059669")
                    btnGerar.isDisable = false
                    btnSelecionar.isDisable = false

                    alert(
                        Alert.AlertType.INFORMATION,
                        "Sucesso!",
                        "PDF gerado com sucesso em:\n${arquivoSaida.absolutePath}"
                    )
                }
            } catch (e: Exception) {
                withContext(Dispatchers.JavaFx) {
                    progressBar.isVisible = false
                    lblStatus.text = "Erro ao gerar PDF"
                    lblStatus.textFill = c("#dc2626")
                    btnGerar.isDisable = false
                    btnSelecionar.isDisable = false

                    alert(
                        Alert.AlertType.ERROR,
                        "Erro ao Gerar PDF",
                        "Não foi possível gerar o PDF: ${e.message}"
                    )
                }
            }
        }
    }
}