package com.example.appaaron_ampuero.test

import android.app.Application
import android.net.Uri
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import viewmodel.DuenoViewModel

@OptIn(ExperimentalCoroutinesApi::class)
class DuenoViewModelTest : StringSpec({

    val dispatcher = StandardTestDispatcher()

    beforeTest { Dispatchers.setMain(dispatcher) }
    afterTest { Dispatchers.resetMain() }

    "nombre del dueño es Aarón Ampuero" {
        val fakeApp = mockk<Application>(relaxed = true)
        val vm = DuenoViewModel(application = fakeApp)

        vm.nombre.value shouldBe "Aarón Ampuero"
    }

    "setImagen actualiza el StateFlow con la nueva Uri (mockeada)" {
        val fakeApp = mockk<Application>(relaxed = true)
        val vm = DuenoViewModel(application = fakeApp)

        val nuevaUri = mockk<Uri>()
        vm.setImagen(nuevaUri)

        vm.imagenUri.value shouldBe nuevaUri
    }
})