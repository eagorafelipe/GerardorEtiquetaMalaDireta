package br.com.etiqueta.maladireta.services

import br.com.etiqueta.maladireta.models.EtiquetaTemplate
import br.com.etiqueta.maladireta.models.ModeloEtiqueta
import br.com.etiqueta.maladireta.models.Registro
import br.com.etiqueta.maladireta.models.formatarComTemplate
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.HorizontalAlignment
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.VerticalAlignment
import java.io.File

class PDFGenerator {

    /**
     * Gera PDF usando o template padrão (compatibilidade com código antigo)
     */
    fun gerarPDF(
        registros: List<Registro>,
        modelo: ModeloEtiqueta,
        arquivoSaida: File,
        onProgress: (Int, Int) -> Unit = { _, _ -> }
    ) {
        gerarPDFComTemplate(registros, modelo, EtiquetaTemplate.DEFAULT, arquivoSaida, onProgress)
    }

    /**
     * Gera PDF usando um template customizado
     */
    fun gerarPDFComTemplate(
        registros: List<Registro>,
        modelo: ModeloEtiqueta,
        template: EtiquetaTemplate,
        arquivoSaida: File,
        onProgress: (Int, Int) -> Unit = { _, _ -> }
    ) {
        val writer = PdfWriter(arquivoSaida)
        val pdfDoc = PdfDocument(writer)
        val document = Document(pdfDoc, PageSize.A4)

        // Configurar margens do documento
        document.setMargins(
            mmParaPontos(modelo.margemSuperiorMM).toFloat(),
            mmParaPontos(modelo.margemDireitaMM).toFloat(),
            mmParaPontos(modelo.margemInferiorMM).toFloat(),
            mmParaPontos(modelo.margemEsquerdaMM).toFloat()
        )

        val totalPaginas = kotlin.math.ceil(registros.size.toDouble() / modelo.etiquetasPorPagina).toInt()
        var paginaAtual = 0

        for (pagina in 0 until totalPaginas) {
            if (pagina > 0) {
                document.add(com.itextpdf.layout.element.AreaBreak())
            }

            // Criar tabela para esta página
            val largurasColunas = FloatArray(modelo.colunas) {
                mmParaPontos(modelo.larguraMM).toFloat()
            }
            val tabela = Table(largurasColunas)
            tabela.setHorizontalAlignment(HorizontalAlignment.CENTER)

            // Preencher tabela
            for (linha in 0 until modelo.linhas) {
                for (coluna in 0 until modelo.colunas) {
                    val indice = pagina * modelo.etiquetasPorPagina + linha * modelo.colunas + coluna

                    val celula = if (indice < registros.size) {
                        criarCelulaComTemplate(registros[indice], modelo, template)
                    } else {
                        criarCelulaVazia(modelo)
                    }

                    tabela.addCell(celula)
                }
            }

            document.add(tabela)
            paginaAtual++
            onProgress(paginaAtual, totalPaginas)
        }

        document.close()
    }

    private fun criarCelulaComTemplate(registro: Registro, modelo: ModeloEtiqueta, template: EtiquetaTemplate): Cell {
        val celula = Cell()
        celula.setWidth(mmParaPontos(modelo.larguraMM).toFloat())
        celula.setHeight(mmParaPontos(modelo.alturaMM).toFloat())
        celula.setPadding(mmParaPontos(template.paddingInterno.toDouble()).toFloat())
        celula.setBorder(null)
        celula.setVerticalAlignment(VerticalAlignment.TOP)

        val paragrafo = Paragraph(registro.formatarComTemplate(template))
        paragrafo.setFontSize(template.tamanhoFonte)
        paragrafo.setTextAlignment(TextAlignment.LEFT)
        paragrafo.setMultipliedLeading(template.entrelinhas)
        paragrafo.setMargin(0.95f)

        celula.add(paragrafo)

        return celula
    }

    private fun criarCelula(registro: Registro, modelo: ModeloEtiqueta): Cell {
        val celula = Cell()
        celula.setWidth(mmParaPontos(modelo.larguraMM).toFloat())
        celula.setHeight(mmParaPontos(modelo.alturaMM).toFloat())
        celula.setPadding(mmParaPontos(1.5).toFloat())
        celula.setBorder(null)
        celula.setVerticalAlignment(VerticalAlignment.TOP)

        val paragrafo = Paragraph(registro.formatarParaEtiqueta())
        paragrafo.setFontSize(7.5f)
        paragrafo.setTextAlignment(TextAlignment.LEFT)
        paragrafo.setMargin(0.95f)

        celula.add(paragrafo)

        return celula
    }

    private fun criarCelulaVazia(modelo: ModeloEtiqueta): Cell {
        val celula = Cell()
        celula.setWidth(mmParaPontos(modelo.larguraMM).toFloat())
        celula.setHeight(mmParaPontos(modelo.alturaMM).toFloat())
        celula.setBorder(null)
        return celula
    }

    private fun mmParaPontos(mm: Double): Double = mm * 2.834645669
}
