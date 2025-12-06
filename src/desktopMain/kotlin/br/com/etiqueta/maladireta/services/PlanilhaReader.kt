package br.com.etiqueta.maladireta.services

import br.com.etiqueta.maladireta.models.Registro
import com.opencsv.CSVReaderBuilder
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.File
import java.io.FileReader

class PlanilhaReader {

    fun lerArquivo(arquivo: File): List<Registro> {
        return when (arquivo.extension.lowercase()) {
            "xlsx", "xls" -> lerExcel(arquivo)
            "csv" -> lerCSV(arquivo)
            else -> throw IllegalArgumentException("Formato não suportado: ${arquivo.extension}")
        }
    }

    private fun lerExcel(arquivo: File): List<Registro> {
        val registros = mutableListOf<Registro>()

        arquivo.inputStream().use { inputStream ->
            val workbook = WorkbookFactory.create(inputStream)
            val sheet = workbook.getSheetAt(0)

            // Pular cabeçalho (linha 0)
            for (i in 1 until sheet.physicalNumberOfRows) {
                val row = sheet.getRow(i) ?: continue

                val nomeSindico = row.getCell(0)?.toString()?.trim() ?: ""
                val nomeCondominio = row.getCell(1)?.toString()?.trim() ?: ""
                val endereco = row.getCell(2)?.toString()?.trim() ?: ""
                val bairro = row.getCell(3)?.toString()?.trim() ?: ""
                val cidade = row.getCell(4)?.toString()?.trim() ?: ""
                val cep = row.getCell(5)?.toString()?.trim() ?: ""

                // Pular linhas vazias
                if (nomeSindico.isBlank() && nomeCondominio.isBlank()) continue

                registros.add(Registro(nomeSindico, nomeCondominio, endereco, bairro, cidade, cep))
            }

            workbook.close()
        }

        return registros
    }

    private fun lerCSV(arquivo: File): List<Registro> {
        val registros = mutableListOf<Registro>()

        FileReader(arquivo).use { reader ->
            val csvReader = CSVReaderBuilder(reader)
                .withSkipLines(1) // Pular cabeçalho
                .build()

            var linha: Array<String>?
            while (csvReader.readNext().also { linha = it } != null) {
                val campos = linha!!

                if (campos.size < 6) continue

                val nomeSindico = campos[0].trim()
                val nomeCondominio = campos[1].trim()
                val endereco = campos[2].trim()
                val bairro = campos[3].trim()
                val cidade = campos[4].trim()
                val cep = campos[5].trim()

                if (nomeSindico.isBlank() && nomeCondominio.isBlank()) continue

                registros.add(Registro(nomeSindico, nomeCondominio, endereco, bairro, cidade, cep))
            }

            csvReader.close()
        }

        return registros
    }
}
