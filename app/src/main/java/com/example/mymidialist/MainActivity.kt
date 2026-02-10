package com.example.mymidialist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.mymidialist.ui.theme.MyMidiaListTheme
import com.example.mymidialist.ui.screens.TelaPrincipal
import androidx.room.Room
import com.example.mymidialist.data.AppDatabase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "meu-banco-de-midias"
        ).build()

        setContent {
            MyMidiaListTheme {
                TelaPrincipal(dao = db.midiaDao())
            }
        }
    }
}
