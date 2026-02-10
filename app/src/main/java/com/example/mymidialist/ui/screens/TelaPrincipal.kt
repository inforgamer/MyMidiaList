package com.example.mymidialist.ui.screens

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mymidialist.model.Midia
import com.example.mymidialist.ui.components.ItemdeMidia
import com.example.mymidialist.ui.components.StarRatingBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun TelaPrincipal(dao: com.example.mymidialist.data.MidiaDao) {
    var midiaSelecionada by remember { mutableStateOf<Midia?>(null) }

    var mostrandoTelaBusca by remember { mutableStateOf(false) }

    var itemParaAdicionar by remember { mutableStateOf<Midia?>(null) }
    var statusParaAdicionar by remember { mutableStateOf("") }
    var notaParaAdicionar by remember { mutableStateOf(0) }

    val context = LocalContext.current
    val listaGeral by dao.getTodasMidias().collectAsState(initial = emptyList())

    // --- LÓGICA DE NAVEGAÇÃO ---

    if (midiaSelecionada != null) {
        TelaDetalhe(midia = midiaSelecionada!!, onVoltar = { midiaSelecionada = null })
    }
    else if (mostrandoTelaBusca) {
        Surface(modifier = Modifier.fillMaxSize()) {
            TelaBusca(
                onVoltar = { mostrandoTelaBusca = false },
                onItemSelecionado = { itemEscolhido ->
                    val jaExiste = listaGeral.any { it.titulo == itemEscolhido.titulo }

                    if (jaExiste) {
                        Toast.makeText(context, "Já está na lista!", Toast.LENGTH_SHORT).show()
                    } else {

                        itemParaAdicionar = itemEscolhido
                        statusParaAdicionar = ""
                        mostrandoTelaBusca = false
                    }
                }
            )
        }
    }
    else {
        val titulosAbas = listOf("Jogos", "Livros", "Séries/Filmes", "Perfil")
        val pagerState = rememberPagerState(pageCount = { titulosAbas.size })
        val coroutineScope = rememberCoroutineScope()

        Scaffold(
            floatingActionButton = {
                if (titulosAbas[pagerState.currentPage] != "Perfil") {
                    FloatingActionButton(onClick = { mostrandoTelaBusca = true }) {
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

            // --- JANELA 2: CONFIGURAR O ITEM ---
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

                            if (statusParaAdicionar in listOf("Zerado", "Lido", "Concluído")) {
                                Spacer(modifier = Modifier.height(16.dp))
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(text = "Sua Nota:", fontWeight = FontWeight.Bold, color = Color.Black)
                                        StarRatingBar(
                                            notaAtual = notaParaAdicionar,
                                            onNotaEditada = { novaNota -> notaParaAdicionar = novaNota }
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
                                    nota = notaParaAdicionar,
                                    comentario = ""
                                )

                                coroutineScope.launch {
                                    dao.inserir(novoItem)
                                }

                                itemParaAdicionar = null
                                statusParaAdicionar = ""
                                notaParaAdicionar = 0
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