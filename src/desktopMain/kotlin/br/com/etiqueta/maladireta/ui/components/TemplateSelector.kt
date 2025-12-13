package br.com.etiqueta.maladireta.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.etiqueta.maladireta.models.EtiquetaTemplate
import br.com.etiqueta.maladireta.models.Registro
import br.com.etiqueta.maladireta.models.formatarComTemplate

@Composable
fun TemplateSelectorSection(
    templates: List<EtiquetaTemplate>,
    selectedTemplate: EtiquetaTemplate,
    sampleRegistro: Registro?,
    onSelectTemplate: (EtiquetaTemplate) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "📝 Layout da Etiqueta",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = "Selecione como as informações serão exibidas nas etiquetas",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.secondary
        )

        // Cards de templates em horizontal scroll
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 4.dp)
        ) {
            items(templates) { template ->
                TemplateCard(
                    template = template,
                    isSelected = template.id == selectedTemplate.id,
                    sampleRegistro = sampleRegistro,
                    onClick = { onSelectTemplate(template) }
                )
            }
        }

        // Preview do template selecionado
        if (sampleRegistro != null) {
            TemplatePreviewBox(
                template = selectedTemplate,
                registro = sampleRegistro
            )
        }
    }
}

@Composable
private fun TemplateCard(
    template: EtiquetaTemplate,
    isSelected: Boolean,
    sampleRegistro: Registro?,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) 
                MaterialTheme.colorScheme.primaryContainer 
            else 
                MaterialTheme.colorScheme.surface
        ),
        border = if (isSelected) 
            androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
        else 
            androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = template.nome,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) 
                        MaterialTheme.colorScheme.onPrimaryContainer 
                    else 
                        MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                
                if (isSelected) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = "Selecionado",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Text(
                text = template.descricao,
                fontSize = 10.sp,
                color = if (isSelected) 
                    MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                else 
                    MaterialTheme.colorScheme.secondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // Mini preview
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(Color.White, RoundedCornerShape(4.dp))
                    .border(0.5.dp, Color(0xFFE0E0E0), RoundedCornerShape(4.dp))
                    .padding(4.dp)
            ) {
                if (sampleRegistro != null) {
                    Text(
                        text = sampleRegistro.formatarComTemplate(template),
                        fontSize = 5.sp,
                        lineHeight = 6.sp,
                        color = Color.Black,
                        maxLines = 5,
                        overflow = TextOverflow.Ellipsis
                    )
                } else {
                    Text(
                        text = "Carregue dados\npara preview",
                        fontSize = 6.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
private fun TemplatePreviewBox(
    template: EtiquetaTemplate,
    registro: Registro
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surfaceVariant,
                RoundedCornerShape(8.dp)
            )
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Preview com dados reais:",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(4.dp))
                .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(4.dp))
                .padding(12.dp)
        ) {
            Text(
                text = registro.formatarComTemplate(template),
                fontSize = 11.sp,
                lineHeight = 14.sp,
                color = Color.Black
            )
        }

        // Info do template
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TemplateInfoItem("Fonte", "${template.tamanhoFonte}pt")
            TemplateInfoItem("Campos", "${template.campos.count { it.exibir }}")
            TemplateInfoItem("Entrelinhas", "${template.entrelinhas}")
        }
    }
}

@Composable
private fun TemplateInfoItem(label: String, value: String) {
    Column {
        Text(
            text = label,
            fontSize = 9.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
