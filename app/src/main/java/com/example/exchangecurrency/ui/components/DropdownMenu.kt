package com.example.exchangecurrency.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList

@Composable
fun DropDownMenu(
    currencies: ImmutableList<Pair<String, Float>>,
    isExpanded: Boolean,
    selectedCurrency: String,
    onChanged: (String) -> Unit,
    onExpanded: (Boolean) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Button(
            onClick = {
                onExpanded(!isExpanded)
            },
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.CenterEnd)
        ) {
            Text(text = selectedCurrency)
            Icon(
                imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp
                else Icons.Default.KeyboardArrowDown,
                contentDescription = null
            )
            DropdownMenu(
                modifier = Modifier.height(350.dp),
                expanded = isExpanded,
                onDismissRequest = {
                    onExpanded(false)
                },
            ) {
                currencies.map {
                    it.first
                }.forEach { currency ->
                    DropdownMenuItem(
                        text = { Text(currency) },
                        onClick = {
                            onChanged.invoke(currency)
                        }
                    )
                }

            }
        }
    }

}