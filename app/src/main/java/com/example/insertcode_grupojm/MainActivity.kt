package com.example.insertcode_grupojm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
// 1. Importas la pantalla que creaste para la Guía 12
import com.example.insertcode_grupojm.ui.screens.TurnoScreen
import com.example.insertcode_grupojm.ui.theme.InsertCode_GrupoJMTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InsertCode_GrupoJMTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // 2. Invocas directamente TurnoScreen para probar animaciones y persistencia
                    TurnoScreen()
                }
            }
        }
    }
}