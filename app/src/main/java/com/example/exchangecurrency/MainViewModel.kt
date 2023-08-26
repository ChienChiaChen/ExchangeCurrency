package com.example.exchangecurrency

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exchangecurrency.data.repository.CurrencyInfoRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val currencyInfoRepo: CurrencyInfoRepo,
) : ViewModel() {
    init {
        fetchCurrencies()
    }

    private fun fetchCurrencies() {
        viewModelScope.launch(Dispatchers.IO) {
            currencyInfoRepo.getLatestCurrency()
                .onStart {
                }
                .catch {
                }
                .onCompletion {
                }
                .collectLatest {
                }
        }

    }
}