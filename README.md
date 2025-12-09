AppAaron_Ampuero
AppAaron_Ampuero es una aplicación Android diseñada para la gestión de un bazar. Permite administrar productos, usuarios y un carrito de compras, integrándose con un backend local que facilita la persistencia y sincronizació 
de datos durante el desarrollo. Además, incorpora el consumo de una API externa de clima (Open-Meteo) para mostrar información en la pantalla de inicio.
Características
- Gestión de productos (listado, creación, edición, eliminación).
- Manejo de carrito de compras local.
- Administración de usuarios.
- Sincronización con backend local mediante json-server.
- Consumo de API externa de clima con Open-Meteo.
- Interfaz desarrollada con Jetpack Compose.
- Compatible con emulador Pixel 9a (probado en desarrollo).

Tecnologías Utilizadas
- Kotlin como lenguaje principal.
- Jetpack Compose para la interfaz de usuario.
- Coroutines para programación asíncrona.
- Retrofit para consumo de APIs (backend y clima).
- ViewModel y StateFlow para gestión del estado.
- json-server para simular el backend local.
- JUnit para pruebas unitarias.
Instalación
- Clona el repositorio:
git clone https://github.com/aa-amp/AppAaron_Ampuero.git
- Abre el proyecto en Android Studio.
- Inicia el backend local con:
npx json-server --watch db.json --port 3001
- El archivo db.json está en la carpeta raíz del proyecto.
- Ejecuta la app desde Android Studio (ejemplo: clase MainActivity).
- Usa el emulador Pixel 9a para probar la comunicación con el backend en la URL:
http://10.0.2.2:3001/
Tests
- Los tests se encuentran en la carpeta:
com/example/appaaron_ampuero/test
- Para ver resultados:
app/build/reports/tests/testDebugUnitTest/index.html
Abre el archivo .html en tu navegador.
- También puedes ejecutar desde terminal:
./gradlew :app:testDebugUnitTest
- O ejecutar una clase específica:
./gradlew :app:testDebugUnitTest --tests "com.example.appaaron_ampuero.test.DuenoViewModelTest"
