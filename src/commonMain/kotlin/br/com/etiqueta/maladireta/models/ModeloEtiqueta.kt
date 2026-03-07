package br.com.etiqueta.maladireta.models

import com.itextpdf.kernel.geom.PageSize

data class ModeloEtiqueta(
    val codigo: String,
    val nome: String,
    val colunas: Int,
    val linhas: Int,
    val larguraMM: Double,
    val alturaMM: Double,
    val margemSuperiorMM: Double = 5.0,
    val margemInferiorMM: Double = 5.0,
    val margemEsquerdaMM: Double = 5.0,
    val margemDireitaMM: Double = 5.0,
    val espacamentoHorizontalMM: Double = 0.0,
    val espacamentoVerticalMM: Double = 0.0,
    val tamanhoPapel: PageSize = PageSize.A4
) {
    val etiquetasPorPagina: Int = colunas * linhas

    override fun toString(): String = "$codigo - $nome ($colunas×$linhas)"

    companion object {
        val MODELOS_PIMACO = listOf(
            ModeloEtiqueta(
                codigo = "6280",
                nome = "Etiqueta Remetente",
                colunas = 3,
                linhas = 10,
                larguraMM = 66.7 + 4.8, // Largura da etiqueta + espaçamento horizontal
                alturaMM = 25.4,
                margemSuperiorMM = 12.7,
                margemInferiorMM = 10.5,
                margemEsquerdaMM = 4.8,
                margemDireitaMM = 4.8,
                espacamentoHorizontalMM = 4.8,
                espacamentoVerticalMM = 0.0,
                tamanhoPapel = PageSize.LETTER
            ),
        )
    }
}