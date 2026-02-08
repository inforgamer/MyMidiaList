package com.example.mymidialist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mymidialist.ui.theme.MyMidiaListTheme
import kotlinx.coroutines.launch

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaPrincipal() {
    val listaGeral = remember {
        mutableStateListOf(
            Midia("Persona 5", "Jogos", "Jogando", 10),
            Midia("Banana Fish", "Livros", "Lido", 9),
            Midia("Harry Potter", "Livros", "Lido", 7),
            Midia("One Piece", "Séries/Filmes", "Assistindo", 10)
        )
    }

    var midiaSelecionada by remember { mutableStateOf<Midia?>(null) }

    if (midiaSelecionada != null) {
        TelaDetalhe(
            midia = midiaSelecionada!!,
            onVoltar = { midiaSelecionada = null }
        )
    } else {
        val titulosAbas = listOf("Jogos", "Livros", "Séries/Filmes", "Perfil")
        val pagerState = rememberPagerState(pageCount = { titulosAbas.size })
        val coroutineScope = rememberCoroutineScope()

        var mostrarDialog by remember { mutableStateOf(false) }
        var textoNome by remember { mutableStateOf("") }
        var textoStatus by remember { mutableStateOf("") }

        Scaffold(
            floatingActionButton = {
                if (titulosAbas[pagerState.currentPage] != "Perfil") {
                    FloatingActionButton(onClick = { mostrarDialog = true }) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Adicionar")
                    }
                }
            }
        ) { paddingValues ->

            Column(modifier = Modifier.padding(paddingValues)) {
                TabRow(selectedTabIndex = pagerState.currentPage) {
                    titulosAbas.forEachIndexed { index, titulo ->
                        Tab(
                            selected = pagerState.currentPage == index,
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            },
                            text = { Text(text = titulo, fontWeight = FontWeight.Bold) }
                        )
                    }
                }
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { paginaAtual ->

                    val abaDoMomento = titulosAbas[paginaAtual]

                    if (abaDoMomento == "Perfil") {
                        TelaDePerfil(listaCompleta = listaGeral)
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {

                            val listaFiltrada = listaGeral.filter { it.tipo == abaDoMomento }

                            items(listaFiltrada) { itemAtual ->
                                Box(modifier = Modifier.clickable {
                                    midiaSelecionada = itemAtual
                                }) {
                                    ItemdeMidia(item = itemAtual)
                                }
                            }

                            if (listaFiltrada.isEmpty()) {
                                item {
                                    Text(
                                        text = "Nada aqui",
                                        modifier = Modifier.padding(16.dp),
                                        color = Color.Gray
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (mostrarDialog) {
                val categoriaAtual = titulosAbas[pagerState.currentPage]

                val opcoesStatus = when (categoriaAtual) {
                    "Jogos" -> listOf("Jogando", "Zerado", "Na fila", "Dropado")
                    "Livros" -> listOf("Lendo", "Lido", "Na fila", "Abandonado")
                    "Séries/Filmes" -> listOf("Vendo", "Concluído", "Planejando", "Esquecido")
                    else -> listOf("Em andamento", "Concluido", "Planejamento")
                }

                AlertDialog(
                    onDismissRequest = { mostrarDialog = false },
                    title = { Text(text = "Adicionar em $categoriaAtual") },
                    text = {
                        Column {
                            OutlinedTextField(
                                value = textoNome,
                                onValueChange = { textoNome = it },
                                label = { Text("Nome do Titulo") },
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            Text(text = "Selecione o Status:", fontWeight = FontWeight.Bold)

                            @OptIn(ExperimentalLayoutApi::class)
                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                opcoesStatus.forEach { opcao ->
                                    val selecionado = (textoStatus == opcao)
                                    FilterChip(
                                        selected = selecionado,
                                        onClick = { textoStatus = opcao },
                                        label = { Text(opcao) },
                                        leadingIcon = if (selecionado) {
                                            { Icon(imageVector = Icons.Default.Check, contentDescription = null) }
                                        } else null
                                    )
                                }
                            }
                        }
                    },
                    confirmButton = {
                        Button(
                            enabled = textoNome.isNotEmpty() && textoStatus.isNotEmpty(),
                            onClick = {
                                listaGeral.add(
                                    Midia(textoNome, categoriaAtual, textoStatus, 0)
                                )
                                textoNome = ""
                                textoStatus = ""
                                mostrarDialog = false
                            }
                        ) {
                            Text("Salvar")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { mostrarDialog = false }) {
                            Text("Cancelar")
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ItemdeMidia(item: Midia) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = item.titulo,
                fontSize = 22.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))

            Text(text = "Tipo: ${item.tipo}", color = Color.Black)
            Text(text = "Status: ${item.status}", color = Color.Black)

            if (item.nota > 0) {
                Text(
                    text = "Nota: ${item.nota}/10",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

data class Midia(
    val titulo: String,
    val tipo: String,
    val status: String,
    val nota: Int
)

@Composable
fun TelaDePerfil(listaCompleta: List<Midia>) {
    val totalJogos = listaCompleta.count { it.tipo == "Jogos" }
    val jogosZerados = listaCompleta.count { it.tipo == "Jogos" && it.status == "Zerado" }

    val totalLivros = listaCompleta.count { it.tipo == "Livros" }
    val livrosLidos = listaCompleta.count { it.tipo == "Livros" && it.status == "Lido" }

    val totalSeries = listaCompleta.count { it.tipo == "Séries/Filmes" }
    val seriesVistas = listaCompleta.count { it.tipo == "Séries/Filmes" && it.status == "Concluído" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.size(100.dp),
            shape = CircleShape,
            colors = CardDefaults.cardColors(containerColor = Color.Black)
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(60.dp),
                    tint = Color.White
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Usuario", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Text(text = "Teste", fontSize = 16.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(32.dp))

        Text(text = "Estatísticas Gerais", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ItemEstatistica("Jogos", "$jogosZerados / $totalJogos", Color(0xFFE91E63))
            ItemEstatistica("Livros", "$livrosLidos / $totalLivros", Color(0xFF2196F3))
        }
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ItemEstatistica("Séries", "$seriesVistas / $totalSeries", Color(0xFF4CAF50))
        }
    }
}

@Composable
fun ItemEstatistica(titulo: String, valor: String, cor: Color) {
    Card(
        modifier = Modifier.size(110.dp),
        colors = CardDefaults.cardColors(containerColor = cor.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = valor, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = cor)
            Text(text = titulo, fontSize = 14.sp, color = Color.Black)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaDetalhe(midia: Midia, onVoltar: () -> Unit) {
    androidx.activity.compose.BackHandler {
        onVoltar()
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(midia.titulo, maxLines = 1) },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.size(200.dp),
                colors = CardDefaults.cardColors(containerColor = Color.LightGray),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        modifier = Modifier.size(100.dp),
                        tint = Color.White
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = midia.titulo,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = midia.tipo.uppercase(),
                color = Color.Gray,
                fontSize = 14.sp,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                InfoCard(titulo = "Status", valor = midia.status)
                if (midia.nota > 0) {
                    InfoCard(titulo = "Nota", valor = "${midia.nota}/10")
                } else {
                    InfoCard(titulo = "Nota", valor = "-")
                }
            }
            Spacer(modifier = Modifier.height(32.dp)) // Aqui tava Space e virou Spacer

            Text(
                text = "Sinopse",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )
            Text(
                text = "Aqui vai aparecer a descrição completa, o autor, editora e tudo mais que a gente puxar da internet no futuro!",
                color = Color.DarkGray
            )
        }
    }
}

@Composable
fun InfoCard(titulo: String, valor: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier.padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = titulo, fontSize = 12.sp)
            Text(text = valor, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}