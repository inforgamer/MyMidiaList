package com.example.mymidialist

import android.icu.number.Scale
import android.os.Bundle
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material.icons.filled.Add
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.mymidialist.ui.theme.MyMidiaListTheme


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
    // --- 1. MEMÓRIA (STATE) ---
    val listaGeral = remember {
        mutableStateListOf(
            Midia("Persona 5", "Jogo", "Jogando", 10),
            Midia("Banana Fish", "Mangá", "Lido", 9),
            Midia("Harry Potter", "Livro", "Lido", 7),
            Midia("One Piece", "Anime", "Assistindo", 10)
        )
    }

    var abaSelecionada by remember {mutableStateOf(0)}
    val titulosAbas = listOf ("Jogos", "Livros", "Séries", "Perfil")

    var mostrarDialog by remember { mutableStateOf(false) }
    var textoNome by remember { mutableStateOf("") }
    var textoStatus by remember { mutableStateOf("") }
    // --- 2. ESQUELETO ---
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { mostrarDialog = true }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Adicionar")
            }
        }
    ) { paddingValues ->

        Column (modifier = Modifier.padding(paddingValues)){
            TabRow(selectedTabIndex = abaSelecionada) {
                titulosAbas.forEachIndexed { index, titulo ->
                    Tab(
                        selected = abaSelecionada == index,
                        onClick = {abaSelecionada = index},
                        text = {Text(text = titulo, fontWeight = FontWeight.Bold)}
                    )
                }
            }
            LazyColumn (
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ){
                val listaFiltrada = listaGeral.filter {it.tipo == titulosAbas[abaSelecionada]}

                items(listaFiltrada) {itemAtual ->
                    ItemdeMidia(item = itemAtual)
                }
            }
        }

        // --- 4. JANELINHA ---
        if (mostrarDialog) {
            AlertDialog(
                onDismissRequest = { mostrarDialog = false },
                title = { Text(text = "Adicionar em ${titulosAbas[abaSelecionada]}") },
                text = {
                    Column {
                        OutlinedTextField(
                            value = textoNome,
                            onValueChange = { textoNome = it },
                            label = { Text("Nome do Titulo") }
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = textoStatus,
                            onValueChange = { textoStatus = it },
                            label = { Text("Status") }
                        )
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        if (textoNome.isNotEmpty()) {
                            listaGeral.add(
                                Midia(textoNome, titulosAbas[abaSelecionada], textoStatus, 0)
                            )
                            textoNome = ""
                            textoStatus = ""
                            mostrarDialog = false
                        }
                    }) {
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
// --- 5. O CARD INDIVIDUAL (A Fábrica) ---
@Composable
fun ItemdeMidia(item: Midia) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0)), // Cinza clarinho
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

            Text(
                text = "Tipo: ${item.tipo}",
                color = Color.Black
            )
            Text(
                text = "Status: ${item.status}",
                color = Color.Black
            )

            // Só mostra a nota se for maior que 0
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

// --- 6. O MOLDE DE DADOS ---
data class Midia(
    val titulo: String,
    val tipo: String,
    val status: String,
    val nota: Int
)

