package viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.ClimaRespuesta
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import repository.ClimaApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ClimaViewModel : ViewModel() {

    private val _estadoClima = MutableStateFlow<ClimaRespuesta?>(null)
    val estadoClima: StateFlow<ClimaRespuesta?> = _estadoClima

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.open-meteo.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(ClimaApi::class.java)

    fun cargarClima(latitud: Double, longitud: Double) {
        viewModelScope.launch {
            try {
                val respuesta = api.obtenerClimaActual(latitud, longitud)
                _estadoClima.value = respuesta
            } catch (e: Exception) {
                Log.e("CLIMA", "Error al obtener clima", e)
            }
        }
    }
}