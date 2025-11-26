package com.example.appaaron_ampuero.test

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import navigation.NavigationEvent
import navigation.Screen
import viewmodel.MainViewModel

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest : StringSpec({

    val dispatcher = StandardTestDispatcher()

    beforeTest { Dispatchers.setMain(dispatcher) }
    afterTest { Dispatchers.resetMain() }

    "navigateTo emite un evento NavigateTo con la pantalla indicada" {
        runTest(dispatcher) {
            val vm = MainViewModel()
            val destino = Screen.Product

            vm.navigateTo(destino)

            val evento = vm.navigationEvents.first()
            evento shouldBe NavigationEvent.NavigateTo(destino)
        }
    }
})