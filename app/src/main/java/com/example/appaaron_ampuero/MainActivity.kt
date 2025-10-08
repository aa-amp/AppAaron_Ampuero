package com.example.appaaron_ampuero

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.appaaron_ampuero.ui.theme.AppAaron_AmpueroTheme
import ui.navigation.AppNavigation



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppAaron_AmpueroTheme {
                Scaffold { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding)) {
                    AppNavigation()
                }
                }}
            }
        }
    }

