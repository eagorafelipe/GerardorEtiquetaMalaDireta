package br.com.etiqueta.maladireta.models

/**
 * Template para customização do layout das etiquetas.
 * Permite definir quais campos exibir e como formatá-los.
 */
data class EtiquetaTemplate(
    val id: String,
    val nome: String,
    val descricao: String,
    val campos: List<CampoTemplate>,
    val tamanhoFonte: Float = 7.5f,
    val entrelinhas: Float = 1.2f,
    val paddingInterno: Float = 0.95f
) {
    override fun toString(): String = nome

    companion object {
        val DEFAULT = EtiquetaTemplate(
            id = "default",
            nome = "Padrão - Completo",
            descricao = "Exibe todos os campos: síndico, condomínio, endereço, bairro, cidade e CEP",
            campos = listOf(
                CampoTemplate(TipoCampo.SINDICO, "", 40, true),
                CampoTemplate(TipoCampo.CONDOMINIO, "", 45, true),
                CampoTemplate(TipoCampo.ENDERECO, "", 45, true),
                CampoTemplate(TipoCampo.BAIRRO, "", 30, true),
                CampoTemplate(TipoCampo.CIDADE_CEP, "", 50, true)
            )
        )

        val TEMPLATES_PADRAO = listOf(DEFAULT)
    }
}

/**
 * Define um campo individual no template da etiqueta
 */
data class CampoTemplate(
    val tipo: TipoCampo,
    val prefixo: String = "",
    val limiteCaracteres: Int = 45,
    val exibir: Boolean = true
)

/**
 * Tipos de campos disponíveis para as etiquetas
 */
enum class TipoCampo(val label: String) {
    SINDICO("Nome do Síndico"),
    CONDOMINIO("Nome do Condomínio"),
    ENDERECO("Endereço"),
    BAIRRO("Bairro"),
    CIDADE("Cidade"),
    CEP("CEP"),
    CIDADE_CEP("Cidade + CEP")
}

/**
 * Extensão para formatar um Registro usando um template específico
 */
fun Registro.formatarComTemplate(template: EtiquetaTemplate): String {
    val linhas = mutableListOf<String>()

    for (campo in template.campos) {
        if (!campo.exibir) continue

        val valor = when (campo.tipo) {
            TipoCampo.SINDICO -> if (nomeSindico.isNotBlank()) "${campo.prefixo}${
                truncar(
                    nomeSindico,
                    campo.limiteCaracteres
                )
            }" else null

            TipoCampo.CONDOMINIO -> if (nomeCondominio.isNotBlank()) "${campo.prefixo}${
                truncar(
                    nomeCondominio,
                    campo.limiteCaracteres
                )
            }" else null

            TipoCampo.ENDERECO -> if (enderecoCondominio.isNotBlank()) "${campo.prefixo}${
                truncar(
                    enderecoCondominio,
                    campo.limiteCaracteres
                )
            }" else null

            TipoCampo.BAIRRO -> if (bairroCondominio.isNotBlank()) "${campo.prefixo}${
                truncar(
                    bairroCondominio,
                    campo.limiteCaracteres
                )
            }" else null

            TipoCampo.CIDADE -> if (cidadeCondominio.isNotBlank()) "${campo.prefixo}${
                truncar(
                    cidadeCondominio,
                    campo.limiteCaracteres
                )
            }" else null

            TipoCampo.CEP -> if (cepCondominio.isNotBlank()) "${campo.prefixo}CEP: $cepCondominio" else null
            TipoCampo.CIDADE_CEP -> {
                val partes = mutableListOf<String>()
                if (cidadeCondominio.isNotBlank()) partes.add(truncar(cidadeCondominio, 30))
                if (cepCondominio.isNotBlank()) partes.add("CEP: $cepCondominio")
                if (partes.isNotEmpty()) "${campo.prefixo}${partes.joinToString(" - ")}" else null
            }
        }

        valor?.let { linhas.add(it) }
    }

    return linhas.joinToString("\n")
}

private fun truncar(texto: String, limite: Int): String {
    return if (texto.length <= limite) texto
    else texto.take(limite - 3) + "..."
}