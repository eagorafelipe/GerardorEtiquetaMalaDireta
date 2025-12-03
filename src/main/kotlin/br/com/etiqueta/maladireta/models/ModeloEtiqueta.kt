package br.com.etiqueta.maladireta.models

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
    val espacamentoHorizontalMM: Double = 2.5,
    val espacamentoVerticalMM: Double = 0.0
) {
    val etiquetasPorPagina: Int = colunas * linhas

    override fun toString(): String = "$codigo - $nome ($colunas×$linhas)"

    companion object {
        val MODELOS_PIMACO = listOf(
            ModeloEtiqueta(
                codigo = "6081",
                nome = "Etiqueta Endereçamento",
                colunas = 3,
                linhas = 10,
                larguraMM = 66.7,
                alturaMM = 25.4,
                margemSuperiorMM = 5.0,
                margemEsquerdaMM = 5.0
            ),
            ModeloEtiqueta(
                codigo = "6082",
                nome = "Etiqueta Endereçamento",
                colunas = 2,
                linhas = 10,
                larguraMM = 99.1,
                alturaMM = 25.4,
                margemSuperiorMM = 5.0,
                margemEsquerdaMM = 5.0
            ),
            ModeloEtiqueta(
                codigo = "6083",
                nome = "Etiqueta Endereçamento",
                colunas = 2,
                linhas = 5,
                larguraMM = 99.1,
                alturaMM = 50.8,
                margemSuperiorMM = 5.0,
                margemEsquerdaMM = 5.0
            ),
            ModeloEtiqueta(
                codigo = "6280",
                nome = "Etiqueta Remetente",
                colunas = 3,
                linhas = 11,
                larguraMM = 63.5,
                alturaMM = 25.4,
                margemSuperiorMM = 2.0,
                margemEsquerdaMM = 4.8
            ),
            ModeloEtiqueta(
                codigo = "6181",
                nome = "Etiqueta Grande",
                colunas = 2,
                linhas = 7,
                larguraMM = 101.6,
                alturaMM = 38.1,
                margemSuperiorMM = 5.0,
                margemEsquerdaMM = 5.0
            ),
            ModeloEtiqueta(
                codigo = "A4049",
                nome = "Etiqueta Universal",
                colunas = 3,
                linhas = 8,
                larguraMM = 63.5,
                alturaMM = 33.9,
                margemSuperiorMM = 5.0,
                margemEsquerdaMM = 7.0
            )
        )
    }
}