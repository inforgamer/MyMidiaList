package com.example.mymidialist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.mymidialist.ui.theme.MyMidiaListTheme
import com.example.mymidialist.ui.screens.TelaPrincipal

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyMidiaListTheme {
                TelaPrincipal()
            }
        }
    }
}