package com.example.exchangecurrency.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.exchangecurrency.main.ViewIntent
import com.example.exchangecurrency.main.ViewState

@Composable
fun CurrencyTop(
    viewState: ViewState,
    modifier: Modifier,
    dispatch: (ViewIntent) -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        InputTextField(
            initValue = viewState.numString,
            onChanged = { newInput ->
                val modifiedValue = if (newInput.trim().isEmpty()) {
                    newInput.trim()
                } else {
                    when (newInput.toFloatOrNull()) {
                        null -> viewState.numString
                        else -> newInput.trim()
                    }
                }

                dispatch(ViewIntent.NumberChanged(modifiedValue))
            }
        )

        DropDownMenu(
            currencies = viewState.currenciesItems,
            isExpanded = viewState.isExpanded,
            selectedCurrency = viewState.baseCurrency,
            onChanged = { newCurrency ->
                dispatch(ViewIntent.BaseCurrencyChanged(newCurrency))
            },
            onExpanded = { isExpanded ->
                dispatch(ViewIntent.ExpansionChanged(isExpanded))
            }
        )
    }
}