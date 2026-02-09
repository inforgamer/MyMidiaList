package com.example.mymidialist.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun StarRatingBar(
    notaAtual: Int,
    onNotaEditada:(Int) -> Unit
){
    Row {
        for (i in 1..5) {
            Icon(
                imageVector = if (i <= notaAtual) Icons.Filled.Star else Icons.Outlined.Star,
                contentDescription = "Nota $i",
                tint = if (i <= notaAtual) Color(0xFFFFD700) else Color.Gray,
                modifier = Modifier
                    .size(40.dp)
                    .padding(4.dp)
                    .clickable {
                        onNotaEditada(i)
                    }
            )
        }
    }
}