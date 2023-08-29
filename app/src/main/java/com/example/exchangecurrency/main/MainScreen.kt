package com.example.exchangecurrency.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.exchangecurrency.ui.components.CurrenciesGrid
import com.example.exchangecurrency.ui.components.CurrencyTop
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
) {

    val intentChannel = remember { Channel<ViewIntent>(Channel.UNLIMITED) }
    LaunchedEffect(Unit) {
        withContext(Dispatchers.Main.immediate) {
            intentChannel
                .consumeAsFlow()
                .onEach(viewModel::processIntent)
                .collect()
        }
    }

    val dispatch = remember {
        { intent: ViewIntent ->
            intentChannel.trySend(intent).getOrThrow()
        }
    }
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .safeDrawingPadding(),
        topBar = {
            CurrencyTop(
                viewState = viewState,
                modifier = Modifier.fillMaxWidth(),
                dispatch = dispatch
            )
        },
        content = {
            CurrenciesGrid(
                viewState = viewState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ){
                dispatch(ViewIntent.Retry)
            }
        })
}