package com.example.mymidialist.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mymidialist.data.MidiaDao
import com.example.mymidialist.model.Midia
import com.example.mymidialist.ui.components.ItemEstatistica
import kotlinx.coroutines.launch

@Composable
fun TelaDePerfil(listaCompleta: List<Midia>, dao: MidiaDao) {
    var mostrarNotaDev by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val urlFormulario = "https://docs.google.com/forms/d/e/1FAIpQLSd0t36IvEQ3s-6-GPqVDPv0VlddGsU9M-Rrie2czVEPr3JieA/viewform?usp=publish-editor"
    val coroutineScope = rememberCoroutineScope()

    var cliquesNaFoto by remember { mutableIntStateOf(0) }
    var modoDeveloperAtivado by remember { mutableStateOf(false) }
    var mostrarAlertaDeletar by remember { mutableStateOf(false) }

    val totalJogos = listaCompleta.count { it.tipo == "Jogos" }
    val jogosZerados = listaCompleta.count { it.tipo == "Jogos" && it.status == "Zerado" }
    val totalLivros = listaCompleta.count { it.tipo == "Livros" }
    val livrosLidos = listaCompleta.count { it.tipo == "Livros" && it.status == "Lido" }
    val totalSeries = listaCompleta.count { it.tipo == "Séries" }
    val seriesConcluidas = listaCompleta.count { it.tipo == "Séries" && it.status == "Concluído" }
    val totalFilmes = listaCompleta.count { it.tipo == "Filmes" }
    val filmesVistos = listaCompleta.count { it.tipo == "Filmes" && it.status == "Lido" || it.status == "Concluido" }
    if (mostrarNotaDev) {
        AlertDialog(
            onDismissRequest = { mostrarNotaDev = false },
            title = { Text("Nota do Desenvolvedor 📝") },
            text = {
                Text(
                    "Fala, pessoal! Este é o KIWI, um projeto que estou desenvolvendo para organizar " +
                            "meus jogos, livros e séries em um só lugar.\n\n" +
                            "O app ainda está em fase Beta, então se algo não carregar ou bugar, " +
                            "por favor, use o botão de reporte! Valeu por testar e me ajudar a evoluir o projeto. \n\n" +
                            "INFOR " +
                            "Código disponível em: https://github.com/inforgamer/MyMidiaList "
                )
            },
            confirmButton = {
                TextButton(onClick = { mostrarNotaDev = false }) {
                    Text("Bora testar!")
                }
            }
        )
    }
    if (mostrarAlertaDeletar) {
        AlertDialog(
            onDismissRequest = { mostrarAlertaDeletar = false },
            title = { Text("Apagar tudo?") },
            text = { Text("Isso vai limpar toda a sua lista local. Tem certeza?") },
            confirmButton = {
                TextButton(onClick = {
                    coroutineScope.launch {
                        dao.deletarTudo()
                        mostrarAlertaDeletar = false
                        Toast.makeText(context, "Banco de dados limpo!", Toast.LENGTH_SHORT).show()
                    }
                }) { Text("Confirmar", color = Color.Red) }
            },
            dismissButton = {
                TextButton(onClick = { mostrarAlertaDeletar = false }) { Text("Cancelar") }
            }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .size(100.dp)
                .clickable {
                    cliquesNaFoto++
                    if (cliquesNaFoto in 4..6) {
                        Toast.makeText(context, "Faltam ${7 - cliquesNaFoto} cliques para o modo dev!", Toast.LENGTH_SHORT).show()
                    } else if (cliquesNaFoto == 7) {
                        modoDeveloperAtivado = true
                        Toast.makeText(context, "Modo Desenvolvedor Ativado!", Toast.LENGTH_SHORT).show()
                    }
                },
            shape = CircleShape,
            colors = CardDefaults.cardColors(containerColor = Color.Black)
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Icon(imageVector = Icons.Default.Person, contentDescription = null, modifier = Modifier.size(60.dp), tint = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Usuário Teste", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Text(text = "Versão Beta 1.0", fontSize = 14.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(32.dp))
        Text(text = "Estatísticas Gerais", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            ItemEstatistica("Jogos", "$jogosZerados / $totalJogos", Color(0xFFE91E63))
            ItemEstatistica("Livros", "$livrosLidos / $totalLivros", Color(0xFF2196F3))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            ItemEstatistica("Séries", "$seriesConcluidas / $totalSeries", Color(0xFF4CAF50))
            ItemEstatistica("Filmes", "$filmesVistos / $totalFilmes", Color(0xFF9C27B0))
        }

        Spacer(modifier = Modifier.weight(1f)) // Empurra os botões para baixo

        Button(
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlFormulario))
                context.startActivity(intent)
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Warning, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Reportar Bug ou Sugestão")
        }
        TextButton(
            onClick = { mostrarNotaDev = true },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Nota do Desenvolvedor", color = Color.Gray, fontSize = 12.sp)
        }

        if (modoDeveloperAtivado) {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = { mostrarAlertaDeletar = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
            ) {
                Icon(Icons.Default.Delete, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("LIMPAR BANCO DE DADOS")
            }
        }
    }
}