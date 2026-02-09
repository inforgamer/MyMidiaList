package com.example.mymidialist.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mymidialist.model.Midia
import com.example.mymidialist.ui.components.ItemEstatistica

@Composable
fun TelaDePerfil(listaCompleta: List<Midia>) {
    val totalJogos = listaCompleta.count { it.tipo == "Jogos" }
    val jogosZerados = listaCompleta.count { it.tipo == "Jogos" && it.status == "Zerado" }
    val totalLivros = listaCompleta.count { it.tipo == "Livros" }
    val livrosLidos = listaCompleta.count { it.tipo == "Livros" && it.status == "Lido" }
    val totalSeries = listaCompleta.count { it.tipo == "Séries/Filmes" }
    val seriesVistas = listaCompleta.count { it.tipo == "Séries/Filmes" && it.status == "Concluído" }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Card(modifier = Modifier.size(100.dp), shape = CircleShape, colors = CardDefaults.cardColors(containerColor = Color.Black)) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Icon(imageVector = Icons.Default.Person, contentDescription = null, modifier = Modifier.size(60.dp), tint = Color.White)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Usuario", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Text(text = "Teste", fontSize = 16.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(32.dp))
        Text(text = "Estatísticas Gerais", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            ItemEstatistica("Jogos", "$jogosZerados / $totalJogos", Color(0xFFE91E63))
            ItemEstatistica("Livros", "$livrosLidos / $totalLivros", Color(0xFF2196F3))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            ItemEstatistica("Séries", "$seriesVistas / $totalSeries", Color(0xFF4CAF50))
        }
    }
}