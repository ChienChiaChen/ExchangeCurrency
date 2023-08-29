package com.example.exchangecurrency.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.exchangecurrency.main.ViewState

@Composable
fun CurrenciesGrid(
    viewState: ViewState,
    modifier: Modifier,
    onRetry: () -> Unit,
) {
    AnimatedVisibility(
        modifier = Modifier.fillMaxSize(),
        visible = viewState.isLoading,
        enter = fadeIn(),
        exit = fadeOut(),
    ) { LoadingIndicator(modifier = Modifier) }

    AnimatedVisibility(
        modifier = Modifier.fillMaxSize(),
        visible = viewState.showRetry,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        RetryButton(
            modifier = Modifier.fillMaxSize(),
            onRetry = onRetry,
        )
    }

    Box(
        modifier = modifier
    ) {
        LazyVerticalGrid(
            modifier = Modifier.padding(top = 1.dp),
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(horizontal = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            content = {
                val currencies = viewState.currenciesItems
                if (!currencies.isEmpty()) {
                    items(currencies.size) { index: Int ->
                        CurrencyItemCell(currencies[index])
                    }
                }
            }
        )
    }
}