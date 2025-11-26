package com.example.appaaron_ampuero.test

import android.app.Application
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.booleans.shouldBeFalse
import io.mockk.mockk
import viewmodel.UsuarioViewModel

class UsuarioViewModelTest : StringSpec({

    "validarFormulario devuelve true con datos válidos" {
        val fakeApp = mockk<Application>(relaxed = true)
        val vm = UsuarioViewModel(application = fakeApp)

        vm.onNombreChange("Aarón Ampuero")
        vm.onCorreoChange("aaron@duocuc.cl")
        vm.onClaveChange("123456")
        vm.onDireccionChange("Calle Falsa 123")
        vm.onAceptarTerminosChange(true)

        val ok = vm.validarFormulario()
        ok.shouldBeTrue()
    }

    "validarFormulario detecta errores y devuelve false" {
        val fakeApp = mockk<Application>(relaxed = true)
        val vm = UsuarioViewModel(application = fakeApp)

        vm.onNombreChange("")                     // nombre vacío
        vm.onCorreoChange("usuario@gmail.com")    // correo incorrecto, sin el @duoc.cl
        vm.onClaveChange("123")                   // clave menos 6 caracteres
        vm.onDireccionChange("")                  // dirección vacia

        val ok = vm.validarFormulario()
        ok.shouldBeFalse()
    }

    "validarLogin devuelve true con credenciales válidas" {
        val fakeApp = mockk<Application>(relaxed = true)
        val vm = UsuarioViewModel(application = fakeApp)

        vm.onCorreoChange("usuario@duocuc.cl")
        vm.onClaveChange("abcdef")

        val ok = vm.validarLogin()
        ok.shouldBeTrue()
    }

    "validarLogin devuelve false con correo o clave inválidos" {
        val fakeApp = mockk<Application>(relaxed = true)
        val vm = UsuarioViewModel(application = fakeApp)

        vm.onCorreoChange("usuario@gmail.com")
        vm.onClaveChange("123")

        val ok = vm.validarLogin()
        ok.shouldBeFalse()
    }
})