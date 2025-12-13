package br.com.etiqueta.maladireta.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import br.com.etiqueta.maladireta.models.EtiquetaTemplate
import br.com.etiqueta.maladireta.models.ModeloEtiqueta
import br.com.etiqueta.maladireta.models.Registro
import br.com.etiqueta.maladireta.models.formatarComTemplate

@Composable
fun EtiquetaPreviewDialog(
    registros: List<Registro>,
    modelo: ModeloEtiqueta,
    template: EtiquetaTemplate,
    currentPage: Int,
    totalPages: Int,
    onPageChange: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.9f),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "👁️ Preview das Etiquetas",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Modelo: ${modelo.codigo} | Template: ${template.nome}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Fechar")
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                // Info do conteúdo
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.colorScheme.primaryContainer,
                            RoundedCornerShape(8.dp)
                        )
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    InfoChip("Total", "${registros.size} registros")
                    InfoChip("Páginas", "$totalPages")
                    InfoChip("Por Página", "${modelo.etiquetasPorPagina}")
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Preview da página (simulação de folha A4)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    EtiquetaPagePreview(
                        registros = registros,
                        modelo = modelo,
                        template = template,
                        pageIndex = currentPage
                    )
                }

                // Navegação de páginas
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { onPageChange(currentPage - 1) },
                        enabled = currentPage > 0
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Página anterior"
                        )
                    }

                    Text(
                        text = "Página ${currentPage + 1} de $totalPages",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    IconButton(
                        onClick = { onPageChange(currentPage + 1) },
                        enabled = currentPage < totalPages - 1
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Próxima página"
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoChip(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
fun EtiquetaPagePreview(
    registros: List<Registro>,
    modelo: ModeloEtiqueta,
    template: EtiquetaTemplate,
    pageIndex: Int
) {
    val startIndex = pageIndex * modelo.etiquetasPorPagina
    val endIndex = minOf(startIndex + modelo.etiquetasPorPagina, registros.size)
    val pageRegistros = if (startIndex < registros.size) {
        registros.subList(startIndex, endIndex)
    } else {
        emptyList()
    }

    // Simular folha A4
    Box(
        modifier = Modifier
            .shadow(8.dp, RoundedCornerShape(4.dp))
            .background(Color.White, RoundedCornerShape(4.dp))
            .aspectRatio(210f / 297f) // Proporção A4
            .padding(8.dp)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(modelo.colunas),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(
                items = (0 until modelo.etiquetasPorPagina).toList()
            ) { index, _ ->
                val registro = pageRegistros.getOrNull(index)
                EtiquetaCell(
                    registro = registro,
                    template = template,
                    modelo = modelo
                )
            }
        }
    }
}

@Composable
fun EtiquetaCell(
    registro: Registro?,
    template: EtiquetaTemplate,
    modelo: ModeloEtiqueta
) {
    Box(
        modifier = Modifier
            .aspectRatio(modelo.larguraMM.toFloat() / modelo.alturaMM.toFloat())
            .border(
                width = 0.5.dp,
                color = if (registro != null) Color(0xFFE0E0E0) else Color(0xFFF5F5F5),
                shape = RoundedCornerShape(2.dp)
            )
            .background(
                if (registro != null) Color.White else Color(0xFFFAFAFA),
                RoundedCornerShape(2.dp)
            )
            .padding(4.dp),
        contentAlignment = Alignment.TopStart
    ) {
        if (registro != null) {
            Text(
                text = registro.formatarComTemplate(template),
                fontSize = 5.sp,
                lineHeight = 6.sp,
                color = Color.Black,
                textAlign = TextAlign.Left,
                maxLines = 6,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * Componente de preview compacto para exibir inline
 */
@Composable
fun EtiquetaPreviewCompact(
    registro: Registro,
    template: EtiquetaTemplate,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0))
    ) {
        Box(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = registro.formatarComTemplate(template),
                fontSize = 10.sp,
                lineHeight = 12.sp,
                color = Color.Black
            )
        }
    }
}
