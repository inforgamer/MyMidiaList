package com.example.mymidialist.ui.components

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.mymidialist.model.Midia


@Composable
fun ItemdeMidia(item: Midia) {
    val alturaDoCard = when (item.tipo) {
        "Livros", "Mangás" -> 200.dp
        else -> 140.dp
    }

    val modificadorDaImagem = when (item.tipo) {
        "Livros", "Mangás" -> Modifier.width(100.dp).height(150.dp)
        else -> Modifier.width(160.dp).height(90.dp)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(alturaDoCard),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            AsyncImage(
                model = item.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .blur(20.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f))
            )

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Card(
                    modifier = modificadorDaImagem,
                    shape = MaterialTheme.shapes.small,
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    AsyncImage(
                        model = item.imageUrl,
                        contentDescription = item.titulo,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = item.titulo,
                        fontSize = 20.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = item.status,
                        color = Color(0xFFDDDDDD),
                        fontSize = 14.sp,
                        modifier = Modifier
                            .background(Color.DarkGray.copy(alpha = 0.8f), MaterialTheme.shapes.small)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )

                    if (item.nota > 0) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "★ ${item.nota}/10",
                            color = Color(0xFFFFD700),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }
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
