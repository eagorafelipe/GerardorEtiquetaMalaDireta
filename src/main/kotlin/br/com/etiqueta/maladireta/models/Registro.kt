package br.com.etiqueta.maladireta.models

data class Registro(
    val nomeSindico: String,
    val nomeCondominio: String,
    val enderecoCondominio: String,
    val bairroCondominio: String,
    val cidadeCondominio: String,
    val cepCondominio: String
) {
    fun formatarParaEtiqueta(): String {
        val linhas = mutableListOf<String>()

        if (nomeSindico.isNotBlank()) {
            linhas.add("Síndico(a): ${truncar(nomeSindico, 40)}")
        }

        if (nomeCondominio.isNotBlank()) {
            linhas.add(truncar(nomeCondominio, 45))
        }

        if (enderecoCondominio.isNotBlank()) {
            linhas.add(truncar(enderecoCondominio, 45))
        }

        if(bairroCondominio.isNotBlank()){
            linhas.add(truncar(bairroCondominio, 30))
        }

        val cidadeCep = mutableListOf<String>()
        if (cidadeCondominio.isNotBlank()) {
            cidadeCep.add(truncar(cidadeCondominio, 30))
        }
        if (cepCondominio.isNotBlank()) {
            cidadeCep.add("CEP: $cepCondominio")
        }

        if (cidadeCep.isNotEmpty()) {
            linhas.add(cidadeCep.joinToString(" - "))
        }

        return linhas.joinToString("\n")
    }

    private fun truncar(texto: String, limite: Int): String {
        return if (texto.length <= limite) texto
        else texto.take(limite - 3) + "..."
    }
}