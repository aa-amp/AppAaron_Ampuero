package com.example.appaaron_ampuero.test

import android.app.Application
import data.AppPreferencias
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import viewmodel.UsuarioViewModel
import navigation.Screen
import java.nio.file.Files

@OptIn(ExperimentalCoroutinesApi::class)
class UsuarioLoginTest : StringSpec({

    val dispatcher = StandardTestDispatcher()
    lateinit var tempDir: java.io.File
    lateinit var fakeApp: Application

    beforeSpec {
        tempDir = Files.createTempDirectory("test-files").toFile()
        tempDir.deleteOnExit()
        fakeApp = mockk(relaxed = true)
        every { fakeApp.filesDir } returns tempDir
        mockkConstructor(AppPreferencias::class)
        every { anyConstructed<AppPreferencias>().obtenerCarrito() } returns flowOf(emptySet())
        coEvery { anyConstructed<AppPreferencias>().guardarCarrito(any()) } just Runs
        coEvery { anyConstructed<AppPreferencias>().guardarSesion(any()) } just Runs
    }

    afterSpec {
        unmockkConstructor(AppPreferencias::class)
        try { tempDir.deleteRecursively() } catch (_: Exception) {}
    }

    beforeTest { Dispatchers.setMain(dispatcher) }
    afterTest { Dispatchers.resetMain(); clearAllMocks() }

    "loginTipoUsuario retorna Screen.Dueno con credenciales del dueño" {
        runTest(dispatcher) {
            val vm = UsuarioViewModel(application = fakeApp)
            dispatcher.scheduler.advanceUntilIdle()
            vm.onCorreoChange("dueno@duocuc.cl")
            vm.onClaveChange("123456")
            val destino = vm.loginTipoUsuario()
            dispatcher.scheduler.advanceUntilIdle()
            destino shouldBe Screen.Dueno
        }
    }

    "loginTipoUsuario retorna Screen.Product con credenciales normales" {
        runTest(dispatcher) {
            val vm = UsuarioViewModel(application = fakeApp)
            dispatcher.scheduler.advanceUntilIdle()
            vm.onCorreoChange("usuario@duocuc.cl")
            vm.onClaveChange("123456")
            val destino = vm.loginTipoUsuario()
            dispatcher.scheduler.advanceUntilIdle()
            destino shouldBe Screen.Product
        }
    }
})