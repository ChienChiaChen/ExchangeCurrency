package com.example.exchangecurrency.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

@Composable
fun CurrencyItemCell(
    value: Pair<String, Float>,
) {
    Column(
        modifier = Modifier
            .height(80.dp)
            .background(Color.Gray)
    ) {
        Text(
            text = value.first,
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )
        )
        Text(
            text = String.format(
                Locale.getDefault(),
                "%.3f",
                value.second
            ),
            style = TextStyle(
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp,
            ),
            maxLines = 1,
        )

    }
}