package com.example.exchangecurrency.main

import com.example.exchangecurrency.DummyData.fakeCurrencyDataBaseUSD
import com.example.exchangecurrency.data.usecase.GetCurrenciesUseCase
import com.example.exchangecurrency.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.unmockkObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class MainViewModelTest {

    private lateinit var mainViewModel: MainViewModel
    private val getCurrenciesUseCase: GetCurrenciesUseCase =
        mockk<GetCurrenciesUseCase>()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    var coroutineRule = MainDispatcherRule()

    @Before
    fun setup() {
        unmockkAll()
        unmockkObject()
        mainViewModel = MainViewModel(getCurrenciesUseCase)
    }

    @After
    fun shoutdown() {
        unmockkAll()
        unmockkObject()
    }

    @Test
    fun `Update view state when num input changed`() = runTest {
        coEvery { getCurrenciesUseCase.invoke() } answers { flowOf(fakeCurrencyDataBaseUSD) }
        mainViewModel.processIntent(ViewIntent.NumberChanged("3"))
        assert(mainViewModel.viewState.value.currenciesItems.first().second == 90f)
    }

    @Test
    fun `Update view state when currency changed`() = runTest {
        coEvery { getCurrenciesUseCase.invoke() } answers { flowOf(fakeCurrencyDataBaseUSD) }
        mainViewModel.processIntent(ViewIntent.BaseCurrencyChanged("TWD"))
        assert(mainViewModel.viewState.value.currenciesItems.first().second == 1f)
    }

    @Test
    fun `Update view state when currency and number changed`() = runTest {
        coEvery { getCurrenciesUseCase.invoke() } answers { flowOf(fakeCurrencyDataBaseUSD) }
        mainViewModel.processIntent(ViewIntent.BaseCurrencyChanged("TWD"))
        mainViewModel.processIntent(ViewIntent.NumberChanged("60"))
        assert(mainViewModel.viewState.value.currenciesItems[1].second == 300f)
    }

    @Test
    fun `Turn showRetry into true when throwing exception`() = runTest {
        coEvery { getCurrenciesUseCase.invoke() } throws IOException("")
        mainViewModel.processIntent(ViewIntent.BaseCurrencyChanged("TWD"))
        assert(mainViewModel.viewState.value.showRetry)
        assert(!mainViewModel.viewState.value.isLoading)
        assert(!mainViewModel.viewState.value.isExpanded)
    }

    @Test
    fun `Turn showRetry into true when throwing exception even retry clicked`() = runTest {
        coEvery { getCurrenciesUseCase.invoke() } throws IOException("")
        mainViewModel.processIntent(ViewIntent.Retry)
        assert(mainViewModel.viewState.value.showRetry)
        assert(!mainViewModel.viewState.value.isLoading)
        assert(!mainViewModel.viewState.value.isExpanded)
    }

}