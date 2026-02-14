package com.example.mymidialist.ui.screens

import android.view.RoundedCorner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.mymidialist.model.Midia
import androidx.compose.runtime.*
import androidx.compose.ui.draw.drawBehind
import com.example.mymidialist.ui.components.InfoCard
import com.example.mymidialist.utils.Tradutor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaDetalhe(midia: Midia, onVoltar: () -> Unit) {

    var textoSinopse by remember { mutableStateOf("Carregando Tradução...") }



    LaunchedEffect(midia.description) {
        if(midia.description.isNotEmpty()){
            textoSinopse = Tradutor.traduzir(midia.description)
        }else{
            textoSinopse = "Nenhuma descrição disponível."
        }
    }
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
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally) {

            val modificadorDaImagem = when(midia.tipo){
                "Livros", "Mangás" ->
                    Modifier.width(170.dp).height(250.dp)

                else ->
                    Modifier.width(320.dp).height(180.dp)
            }

            Card(modifier = modificadorDaImagem, colors = CardDefaults.cardColors(containerColor = Color.LightGray), elevation = CardDefaults.cardElevation(8.dp)) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    AsyncImage(
                        model = midia.imageUrl,
                        contentDescription = midia.titulo,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
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
            Box (
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 100.dp)
                    .verticalScroll(rememberScrollState())
                    .background(Color(0xFF2A2A2A), RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ){
                Text(
                    text =textoSinopse,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFCCCCCC),
                    lineHeight = 20.sp
                )
            }
        }
    }
}