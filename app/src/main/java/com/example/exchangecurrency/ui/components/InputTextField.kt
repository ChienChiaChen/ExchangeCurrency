package com.example.exchangecurrency.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputTextField(
    initValue: String,
    onChanged: (String) -> Unit,
) {

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(5.dp)),
        value = initValue,
        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End),
        placeholder = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Please Input",
                textAlign = TextAlign.End
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        onValueChange = onChanged,
        maxLines = 1,
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )

    )
}