package com.example.mymidialist.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.mymidialist.model.Midia
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaBusca(
    onVoltar: () -> Unit,
    onItemSelecionado: (Midia) -> Unit
) {
    val repository = remember { com.example.mymidialist.data.Repository() }

    var termoBusca by remember { mutableStateOf("") }
    var filtroSelecionado by remember { mutableStateOf("Séries") }
    var resultados by remember { mutableStateOf<List<Midia>>(emptyList()) }
    var carregando by remember { mutableStateOf(false) }

    LaunchedEffect(termoBusca, filtroSelecionado) {
        if (termoBusca.length >= 3) {
            delay(800)
            carregando = true
            resultados = repository.buscarAnimesNaInternet(termoBusca, filtroSelecionado)
            carregando = false
        } else {
            resultados = emptyList()
            carregando = false
        }
    }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface)) {
                TopAppBar(
                    title = {
                        OutlinedTextField(
                            value = termoBusca,
                            onValueChange = { termoBusca = it },
                            placeholder = { Text("Digite o nome") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().padding(end = 16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = Color.Transparent,
                                focusedBorderColor = Color.Transparent
                            ),
                            leadingIcon = { Icon(Icons.Default.Search, null) }
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onVoltar) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Volta")
                        }
                    }
                )

                Row(modifier = Modifier
                    .padding(start = 16.dp, bottom = 8.dp)
                    .horizontalScroll(rememberScrollState())
                ) {
                    FilterChip(
                        selected = filtroSelecionado == "Séries",
                        onClick = { filtroSelecionado = "Séries"; resultados = emptyList() },
                        label = { Text("Séries") },
                        leadingIcon = if (filtroSelecionado == "Séries") { { Icon(Icons.Default.Check, null) } } else null
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    FilterChip(
                        selected = filtroSelecionado == "Filmes",
                        onClick = { filtroSelecionado = "Filmes"; resultados = emptyList() },
                        label = { Text("Filmes") },
                        leadingIcon = if (filtroSelecionado == "Filmes") { { Icon(Icons.Default.Check, null) } } else null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    FilterChip(
                        selected = filtroSelecionado == "Jogos",
                        onClick = { filtroSelecionado = "Jogos"; resultados = emptyList() },
                        label = { Text("Jogos") },
                        leadingIcon = if (filtroSelecionado == "Jogos") { { Icon(Icons.Default.Check, null) } } else null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    FilterChip(
                        selected = filtroSelecionado == "Animes",
                        onClick = { filtroSelecionado = "Animes"; resultados = emptyList() },
                        label = { Text("Animes") },
                        leadingIcon = if (filtroSelecionado == "Animes") { { Icon(Icons.Default.Check, null) } } else null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    FilterChip(
                        selected = filtroSelecionado == "Mangas",
                        onClick = { filtroSelecionado = "Mangas"; resultados = emptyList() },
                        label = { Text("Mangas") },
                        leadingIcon = if (filtroSelecionado == "Mangas") { { Icon(Icons.Default.Check, null) } } else null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    FilterChip(
                        selected = filtroSelecionado == "Livros",
                        onClick = { filtroSelecionado = "Livros"; resultados = emptyList() },
                        label = { Text("Livros") },
                        leadingIcon = if (filtroSelecionado == "Livros") { { Icon(Icons.Default.Check, null) } } else null
                    )
                }
                Divider()
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).padding(16.dp)) {
            if (carregando) LinearProgressIndicator(modifier = Modifier.fillMaxWidth())

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(resultados) { midia ->
                    Card(
                        modifier = Modifier.height(180.dp).clickable { onItemSelecionado(midia) },
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            AsyncImage(
                                model = midia.imageUrl,
                                contentDescription = midia.titulo,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.4f)))

                            Text(
                                text = midia.titulo,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.align(Alignment.BottomStart).padding(8.dp),
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}