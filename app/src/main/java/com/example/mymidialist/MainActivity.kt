package com.example.mymidialist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
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

@Composable
fun TelaPrincipal() {
    val listaGeral = remember {
        mutableStateListOf(
            Midia("Persona 5", "Jogos", "Jogando", 10),
            Midia("Banana Fish", "Livros", "Lido", 9),
            Midia("Harry Potter", "Livros", "Lido", 7),
            // Mudei aqui pra "Séries/Filmes" pra aparecer na aba certa!
            Midia("One Piece", "Séries/Filmes", "Assistindo", 10)
        )
    }

    val titulosAbas = listOf("Jogos", "Livros", "Séries/Filmes", "Perfil")

    val pagerState = rememberPagerState(pageCount = { titulosAbas.size })
    val coroutineScope = rememberCoroutineScope()

    var mostrarDialog by remember { mutableStateOf(false) }
    var textoNome by remember { mutableStateOf("") }
    var textoStatus by remember { mutableStateOf("") }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { mostrarDialog = true }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Adicionar")
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

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val abaDoMomento = titulosAbas[paginaAtual]
                    val listaFiltrada = listaGeral.filter { it.tipo == abaDoMomento }

                    items(listaFiltrada) { itemAtual ->
                        ItemdeMidia(item = itemAtual)
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

