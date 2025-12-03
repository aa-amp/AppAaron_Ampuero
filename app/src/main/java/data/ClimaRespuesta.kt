package data

data class ClimaRespuesta(
    val current_weather: ClimaActual
)

data class ClimaActual(
    val temperature: Double,
    val windspeed: Double,
    val winddirection: Double,
    val weathercode: Int,
    val time: String
)