package com.example.mymidialist.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mymidialist.model.Midia
import com.example.mymidialist.ui.components.InfoCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaDetalhe(midia: Midia, onVoltar: () -> Unit) {
    androidx.activity.compose.BackHandler { onVoltar() }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(midia.titulo, maxLines = 1) },
                navigationIcon = { IconButton(onClick = onVoltar) { Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Voltar") } },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer, titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer)
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Card(modifier = Modifier.size(200.dp), colors = CardDefaults.cardColors(containerColor = Color.LightGray), elevation = CardDefaults.cardElevation(8.dp)) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Icon(imageVector = Icons.Default.Star, contentDescription = null, modifier = Modifier.size(100.dp), tint = Color.White)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = midia.titulo, fontSize = 32.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            Text(text = midia.tipo.uppercase(), color = Color.Gray, fontSize = 14.sp, letterSpacing = 2.sp)
            Spacer(modifier = Modifier.height(24.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                InfoCard(titulo = "Status", valor = midia.status)
                InfoCard(titulo = "Nota", valor = if (midia.nota > 0) "${midia.nota}/10" else "-")
            }
            Spacer(modifier = Modifier.height(32.dp))
            Text(text = "Sinopse", fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start))
            Text(text = "Aqui vai aparecer a descrição completa, o autor, editora e tudo mais que a gente puxar da internet no futuro!", color = Color.DarkGray)
        }
    }
}