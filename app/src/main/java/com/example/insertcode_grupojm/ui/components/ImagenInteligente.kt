package com.example.insertcode_grupojm.ui.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun ImagenInteligente(
    imagenUri: Uri?,
    modifier: Modifier = Modifier,
    tamano: Dp = 140.dp
) {
    Box(
        modifier = modifier
            .size(tamano)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (imagenUri != null) {
            // Carga reactiva de la imagen mediante Coil
            AsyncImage(
                model = imagenUri,
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(tamano)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            // Ícono por defecto cuando aún no se ha elegido foto
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Sin foto de perfil",
                modifier = Modifier.size(tamano * 0.6f),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}