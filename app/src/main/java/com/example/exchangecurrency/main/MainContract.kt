package com.example.exchangecurrency.main

import androidx.compose.runtime.Immutable
import com.example.exchangecurrency.mvi.base.MviIntent
import com.example.exchangecurrency.mvi.base.MviSingleEvent
import com.example.exchangecurrency.mvi.base.MviViewState
import com.example.exchangecurrency.utils.Constants.BASE_CURRENCY_USD
import com.example.exchangecurrency.utils.Constants.BASE_NUMBER_INPUT
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

@Immutable
sealed interface ViewIntent : MviIntent {
    data class NumberChanged(val numString: String) : ViewIntent
    data class BaseCurrencyChanged(val currency: String) : ViewIntent
    data class ExpansionChanged(val expanded: Boolean) : ViewIntent
    object Retry : ViewIntent
}

@Immutable
data class ViewState(
    val currenciesItems: PersistentList<Pair<String, Float>>,
    val isLoading: Boolean,
    val showRetry: Boolean,
    val numString: String,
    val baseCurrency: String,
    val isExpanded: Boolean
) : MviViewState {
    companion object {
        fun initial() = ViewState(
            currenciesItems = persistentListOf(),
            isLoading = true,
            showRetry = false,
            numString = BASE_NUMBER_INPUT,
            baseCurrency = BASE_CURRENCY_USD,
            isExpanded = false
        )
    }
}

sealed interface SingleEvent : MviSingleEvent {
}


internal sealed interface PartialStateChange {
    fun reduce(viewState: ViewState): ViewState

    sealed interface Currencies : PartialStateChange {
        override fun reduce(viewState: ViewState): ViewState {
            return when (this) {
                is Loading -> viewState.copy(
                    isLoading = true,
                    isExpanded = false,
                    showRetry = false,
                )

                is Data -> viewState.copy(
                    isLoading = false,
                    isExpanded = false,
                    showRetry = false,
                    currenciesItems = currenciesItems.toPersistentList(),
                    numString = num,
                    baseCurrency = baseCurrency
                )

                is ExpansionChanged -> viewState.copy(
                    isExpanded = isExpanded,
                    isLoading = false,
                )
                is Retry, Failed -> viewState.copy(
                    isExpanded = false,
                    isLoading = false,
                    showRetry = true,
                )
            }
        }

        object Loading : Currencies
        object Retry : Currencies

        object Failed: Currencies

        data class ExpansionChanged(val isExpanded: Boolean) : Currencies
        data class Data(
            val num: String,
            val baseCurrency: String,
            val currenciesItems: List<Pair<String, Float>>
        ) : Currencies
    }

}
