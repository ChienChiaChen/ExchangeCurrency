package com.example.exchangecurrency.main

import androidx.lifecycle.viewModelScope
import com.example.exchangecurrency.data.entity.CurrencyInfo
import com.example.exchangecurrency.mvi.base.AbstractMviViewModel
import com.example.exchangecurrency.utils.Constants.BASE_NUMBER_INPUT
import com.hoc081098.flowext.flatMapFirst
import com.hoc081098.flowext.flowFromSuspend
import com.hoc081098.flowext.startWith
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getCurrenciesUseCase: GetCurrenciesUseCase,
) : AbstractMviViewModel<ViewIntent, ViewState, SingleEvent>() {

    override val viewState: StateFlow<ViewState>

    init {
        val initialVS = ViewState.initial()
        viewState =
            intentSharedFlow
                .toPartialStateChangeFlow(initialVS)
                .scan(initialVS) { vs, change ->
                    change.reduce(vs)
                }
                .stateIn(
                    viewModelScope,
                    SharingStarted.Eagerly,
                    initialVS
                )
    }

    private fun SharedFlow<ViewIntent>.toPartialStateChangeFlow(initialVS: ViewState): Flow<PartialStateChange> {
        val numInputFlow = filterIsInstance<ViewIntent.NumberChanged>()
            .map {
                try {
                    it.numString.toFloat()
                    it.numString
                } catch (e: NumberFormatException) {
                    BASE_NUMBER_INPUT
                }
            }
            .startWith(initialVS.numString)
            .distinctUntilChanged()
            .shareWhileSubscribed()

        val baseCurrencyFlow = filterIsInstance<ViewIntent.BaseCurrencyChanged>()
            .map { it.currency }
            .startWith(initialVS.baseCurrency)
            .distinctUntilChanged()
            .shareWhileSubscribed()


        return merge(
            dataChangedFlow(numInputFlow, baseCurrencyFlow),
            filterIsInstance<ViewIntent.Retry>().toRetryFlow(initialVS),
            filterIsInstance<ViewIntent.ExpansionChanged>().toExpansionChangedFlow(),
        )
    }

    private fun Flow<ViewIntent.Retry>.toRetryFlow(initialVS: ViewState): Flow<PartialStateChange.Currencies> {
        return flatMapFirst {
            flowFromSuspend {
                getCurrenciesUseCase.invoke().first()
            }.map { currenciesTable ->
                return@map calculateCurrencies(
                    currenciesTable,
                    initialVS.baseCurrency,
                    initialVS.numString
                )
            }.catch {
                flowOf(PartialStateChange.Currencies.Failed)
            }
        }
    }

    private fun dataChangedFlow(
        numInputFlow: SharedFlow<String>,
        baseCurrencyFlow: SharedFlow<String>,
    ): Flow<PartialStateChange.Currencies> {
        return combine(
            numInputFlow, baseCurrencyFlow
        ) { numInput, baseCurrency ->
            try {
                val currenciesTable = getCurrenciesUseCase.invoke().first()
                calculateCurrencies(currenciesTable, baseCurrency, numInput)
            } catch (e: Exception) {
                PartialStateChange.Currencies.Retry
            }
        }
    }

    private fun calculateCurrencies(
        currenciesTable: CurrencyInfo,
        baseCurrency: String,
        numInput: String
    ): PartialStateChange.Currencies.Data {
        // Example: Usd->TWD = 1:30, Usd->JPY = 1:150
        // 60TWD -> JPY?
        //
        // Step 1. 60/30 = 2
        // Step 2. 2*150 = 300
        // Ans. 60TWD ≈ 300 JPY
        val rate = currenciesTable.rates[baseCurrency] ?: 1f
        val multiplier = numInput.toFloat().div(rate) // Step1
        val newCurrencies =
            currenciesTable.rates.mapValues { it.value.times(multiplier) }.toList()// Step2
        return PartialStateChange.Currencies.Data(numInput, baseCurrency, newCurrencies)
    }

    private fun Flow<ViewIntent.ExpansionChanged>.toExpansionChangedFlow(): Flow<PartialStateChange.Currencies.ExpansionChanged> =
        map { PartialStateChange.Currencies.ExpansionChanged(it.expanded) }
}