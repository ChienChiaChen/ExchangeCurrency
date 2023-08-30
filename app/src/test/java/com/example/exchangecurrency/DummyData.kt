package com.example.exchangecurrency

import com.example.exchangecurrency.data.entity.CurrencyInfo


internal object DummyData {
    val fakeCurrencyBaseUSD = mutableMapOf("TWD" to 30f, "JPY" to 150f, "USD" to 1f)

    val fakeCurrencyMapBaseABC = fakeCurrencyBaseUSD

    val fakeCurrencyDataBaseUSD = CurrencyInfo(
        base = "USD",
        timestamp = 123,
        license = "license",
        disclaimer = "disclaimer",
        rates = fakeCurrencyBaseUSD
    )
    val fakeCurrencyDataBaseABC = CurrencyInfo(
        base = "ABC",
        timestamp = 456,
        license = "license",
        disclaimer = "disclaimer",
        rates = fakeCurrencyMapBaseABC
    )
}
