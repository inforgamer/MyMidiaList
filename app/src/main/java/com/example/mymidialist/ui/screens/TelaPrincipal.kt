package com.example.mymidialist.ui.screens

import android.R
import android.widget.Space
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import com.example.mymidialist.model.Midia
import com.example.mymidialist.ui.components.ItemdeMidia
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import com.example.mymidialist.ui.components.StarRatingBar

val bancoDeDadosFake = listOf(
    Midia("The Last of Us", "Jogos", "Na fila", 0, ""),
    Midia("God of War Ragnarok", "Jogos", "Na fila", 0, ""),
    Midia("Elden Ring", "Jogos", "Na fila", 0, ""),
    Midia("Hollow Knight", "Jogos", "Na fila", 0, ""),
    Midia("Final Fantasy VII", "Jogos", "Na fila", 0, ""),
    Midia("Persona 3 Reload", "Jogos", "Na fila", 0, ""),
    Midia("O Senhor dos Anéis", "Livros", "Na fila", 0, ""),
    Midia("O Hobbit", "Livros", "Na fila", 0, ""),
    Midia("Berserk Vol. 1", "Livros", "Na fila", 0, ""),
    Midia("Mistborn", "Livros", "Na fila", 0, ""),
    Midia("Harry Potter e a Pedra Filosofal", "Livros", "Na fila", 0, ""),
    Midia("Breaking Bad", "Séries/Filmes", "Na fila", 0, ""),
    Midia("Arcane", "Séries/Filmes", "Na fila", 0, ""),
    Midia("Interestelar", "Séries/Filmes", "Na fila", 0, "")
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun TelaPrincipal() {
    val listaGeral = remember {
        mutableStateListOf(
            Midia("Persona 5", "Jogos", "Jogando", 10, ""),
            Midia("Banana Fish", "Livros", "Lido", 9, ""),
            Midia("Harry Potter", "Livros", "Lido", 7, ""),
            Midia("One Piece", "Séries/Filmes", "Assistindo", 10, "")
        )
    }

    var midiaSelecionada by remember { mutableStateOf<Midia?>(null) }

    if (midiaSelecionada != null) {
        TelaDetalhe(midia = midiaSelecionada!!, onVoltar = { midiaSelecionada = null })
    } else {
        val titulosAbas = listOf("Jogos", "Livros", "Séries/Filmes", "Perfil")
        val pagerState = rememberPagerState(pageCount = { titulosAbas.size })
        val coroutineScope = rememberCoroutineScope()

        var mostrarDialog by remember { mutableStateOf(false) }
        var termoBusca by remember { mutableStateOf("") }
        val context = LocalContext.current

        var itemParaAdicionar by remember { mutableStateOf<Midia?>(null) }
        var statusParaAdicionar by remember { mutableStateOf("") }
        var notaParaAdicionar by remember { mutableStateOf(0) }
        var comentarioParaAdicionar by remember{mutableStateOf("")}

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
                            onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
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

                AlertDialog(
                    onDismissRequest = {
                        mostrarDialog = false
                        termoBusca = ""
                    },
                    title = { Text(text = "Pesquisar em $categoriaAtual") },
                    text = {
                        Column {
                            OutlinedTextField(
                                value = termoBusca,
                                onValueChange = { termoBusca = it },
                                label = { Text("Digite o nome") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            if (termoBusca.isNotEmpty()) {
                                val resultados = bancoDeDadosFake.filter {
                                    it.titulo.contains(termoBusca, ignoreCase = true) &&
                                            it.tipo == categoriaAtual
                                }

                                LazyColumn(modifier = Modifier.height(200.dp)) {
                                    items(resultados) { itemEncontrado ->
                                        Card(
                                            colors = CardDefaults.cardColors(containerColor = Color(
                                                0xFF000000
                                            )
                                            ),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp)
                                                .clickable {
                                                    val jaExiste = listaGeral.any { it.titulo == itemEncontrado.titulo }

                                                    if (jaExiste) {
                                                        Toast.makeText(context, "Este item já está em sua lista!", Toast.LENGTH_SHORT).show()
                                                    } else {

                                                        itemParaAdicionar = itemEncontrado
                                                        statusParaAdicionar = ""
                                                        mostrarDialog = false
                                                    }
                                                }
                                        ) {
                                            Text(
                                                text = itemEncontrado.titulo,
                                                modifier = Modifier.padding(16.dp),
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = { mostrarDialog = false }) { Text("Cancelar") }
                    }
                )
            }

            if (itemParaAdicionar != null) {
                AlertDialog(
                    onDismissRequest = { itemParaAdicionar = null },
                    title = { Text(text = "Adicionar ${itemParaAdicionar!!.titulo}") },
                    text = {
                        Column {
                            Text("Qual a situação de ${itemParaAdicionar!!.titulo}?")
                            Spacer(modifier = Modifier.height(16.dp))

                            val opcoesStatus = when (itemParaAdicionar!!.tipo) {
                                "Jogos" -> listOf("Jogando", "Zerado", "Na fila", "Dropado")
                                "Livros" -> listOf("Lendo", "Lido", "Na fila", "Abandonado")
                                "Séries/Filmes" -> listOf("Vendo", "Concluído", "Planejando", "Esquecido")
                                else -> listOf("Em andamento", "Concluido")
                            }

                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                opcoesStatus.forEach { opcao ->
                                    val selecionado = (statusParaAdicionar == opcao)
                                    FilterChip(
                                        selected = selecionado,
                                        onClick = { statusParaAdicionar = opcao },
                                        label = { Text(opcao) },
                                        leadingIcon = if (selecionado) {
                                            { Icon(Icons.Default.Check, null) }
                                        } else null
                                    )
                                }
                            }

                            if (statusParaAdicionar in listOf ("Zerado","Lido","Concluído"))
                            {
                                Spacer(modifier = Modifier.height(16.dp))
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = Color(
                                        0xFF000000
                                    )
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column (modifier = Modifier.padding(16.dp)){
                                        Text(text = "Nota:")
                                        StarRatingBar(
                                            notaAtual = notaParaAdicionar,
                                            onNotaEditada = {novaNota -> notaParaAdicionar=novaNota}
                                        )

                                        Spacer(modifier = Modifier.height(16.dp))
                                        Text(text = "Comentario:")
                                        OutlinedTextField(
                                            value = comentarioParaAdicionar,
                                            onValueChange = { comentarioParaAdicionar = it },
                                            label = { Text("O que achou?") },
                                            modifier = Modifier.fillMaxWidth(),
                                            maxLines = 3
                                        )

                                    }
                                }

                            }
                        }

                    },

                    confirmButton = {
                        Button(
                            enabled = statusParaAdicionar.isNotEmpty(),
                            onClick = {
                                val novoItem = itemParaAdicionar!!.copy(
                                    status = statusParaAdicionar,
                                    nota = notaParaAdicionar
                                )
                                listaGeral.add(novoItem)

                                itemParaAdicionar = null
                                statusParaAdicionar = ""
                                notaParaAdicionar = 0
                                comentarioParaAdicionar = ""
                            }
                        ) {
                            Text("Confirmar e Salvar")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { itemParaAdicionar = null }) { Text("Cancelar") }
                    }
                )
            }
        }
    }
}