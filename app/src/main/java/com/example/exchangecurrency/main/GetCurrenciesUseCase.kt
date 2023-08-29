package com.example.exchangecurrency.main

import com.example.exchangecurrency.data.entity.CurrencyInfo
import com.example.exchangecurrency.data.repository.CurrencyInfoRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrenciesUseCase @Inject constructor(private val currencyRepository: CurrencyInfoRepo) {
    operator fun invoke(): Flow<CurrencyInfo> = currencyRepository.getLatestCurrency()
}
