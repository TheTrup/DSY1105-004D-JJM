package com.example.insertcode_grupojm.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class PuntoRutaSimulado(
    val numero: Int,
    val nombre: String,
    val direccion: String,
    val estado: String,
    val coordenadas: String,
    val xRatio: Float,
    val yRatio: Float,
    val colorPunto: Color
)

@Composable
fun MapaRutaGpsCard(
    puntos: List<PuntoRutaSimulado>,
    puntoSeleccionado: PuntoRutaSimulado,
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()
    val colorLineaRuta = MaterialTheme.colorScheme.primary
    val colorGrilla = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f)
    val colorTextoNodo = MaterialTheme.colorScheme.onSurface
    val colorVerdeActivo = MaterialTheme.colorScheme.secondary

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Cabecera del mapa referencial
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "Mapa de Ruta Referencial",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.MyLocation,
                        contentDescription = null,
                        tint = colorVerdeActivo,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "GPS Activo",
                        style = MaterialTheme.typography.labelSmall,
                        color = colorVerdeActivo,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // Lienzo vectorial estático
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val filas = 6
                    val columnas = 8
                    val pasoY = size.height / filas
                    val pasoX = size.width / columnas

                    // Cuadrícula cartográfica de fondo
                    for (i in 1 until filas) {
                        drawLine(
                            color = colorGrilla,
                            start = Offset(0f, pasoY * i),
                            end = Offset(size.width, pasoY * i),
                            strokeWidth = 1.dp.toPx()
                        )
                    }
                    for (j in 1 until columnas) {
                        drawLine(
                            color = colorGrilla,
                            start = Offset(pasoX * j, 0f),
                            end = Offset(pasoX * j, size.height),
                            strokeWidth = 1.dp.toPx()
                        )
                    }

                    // Línea punteada conectando los hitos de entrega
                    val coordenadasPx = puntos.map {
                        Offset(size.width * it.xRatio, size.height * it.yRatio)
                    }
                    for (i in 0 until coordenadasPx.size - 1) {
                        drawLine(
                            color = colorLineaRuta,
                            start = coordenadasPx[i],
                            end = coordenadasPx[i + 1],
                            strokeWidth = 3.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(16f, 12f), 0f)
                        )
                    }

                    // Nodos y etiquetas de cada parada
                    puntos.forEachIndexed { index, punto ->
                        val centro = coordenadasPx[index]
                        val esSeleccionado = punto.numero == puntoSeleccionado.numero

                        if (esSeleccionado) {
                            drawCircle(
                                color = punto.colorPunto.copy(alpha = 0.28f),
                                radius = 16.dp.toPx(),
                                center = centro
                            )
                        }

                        drawCircle(
                            color = punto.colorPunto,
                            radius = 8.dp.toPx(),
                            center = centro
                        )
                        drawCircle(
                            color = Color.White,
                            radius = 8.dp.toPx(),
                            center = centro,
                            style = Stroke(width = 2.dp.toPx())
                        )

                        val medicionTexto = textMeasurer.measure(
                            text = punto.nombre,
                            style = TextStyle(
                                color = colorTextoNodo,
                                fontSize = 11.sp,
                                fontWeight = if (esSeleccionado) FontWeight.Bold else FontWeight.Medium
                            )
                        )
                        drawText(
                            textLayoutResult = medicionTexto,
                            topLeft = Offset(
                                x = (centro.x - medicionTexto.size.width / 2f)
                                    .coerceIn(8f, size.width - medicionTexto.size.width - 8f),
                                y = centro.y + 12.dp.toPx()
                            )
                        )
                    }
                }
            }

            Spacer(Modifier.height(10.dp))

            Text(
                text = "Punto seleccionado: ${puntoSeleccionado.nombre} • ${puntoSeleccionado.coordenadas}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}