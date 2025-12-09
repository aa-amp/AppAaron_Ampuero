# AppAaron_Ampuero

AppAaron_Ampuero es una aplicación Android diseñada para la gestión de un bazar. Permite administrar productos, usuarios y un carrito de compras, integrándose con un backend local que facilita la persistencia y sincronización
de datos durante el desarrollo. Además, incorpora el consumo de una API externa de clima (Open-Meteo) para mostrar información en la pantalla de inicio.

## Características
- Gestión de productos (listado, creación, edición, eliminación).
- Manejo de carrito de compras local.
- Administración de usuarios.
- Sincronización con backend local mediante json-server.
- Consumo de API externa de clima con Open-Meteo.
- Interfaz desarrollada con Jetpack Compose.
- Compatible con emulador Pixel 9a (probado en desarrollo).

## ESTRUCTURA DEL REPOSITORIO
- app/src/main/java/ui/screens/ → Pantallas principales (Home, Login, Registro, Productos).
- app/src/main/java/viewmodel/ → Lógica de negocio y gestión de estado.
- app/src/main/java/network/ → Interfaces Retrofit para backend y API externa.
- db.json → Base de datos simulada para el backend local.
- app/src/test/ → Pruebas unitarias con JUnit.

## EJEMPLOS DESTACADOS
- Pantalla de inicio con clima: HomeScreen.kt muestra datos de Open-Meteo junto a botones de navegación.
- Gestión de productos: CRUD completo conectado al backend local (json-server).
- Carrito de compras: lógica local para agregar y quitar productos.
- Pruebas unitarias: DuenoViewModelTest como ejemplo de test de ViewModel.

## Tecnologías Utilizadas
- Kotlin como lenguaje principal.
- Jetpack Compose para la interfaz de usuario.
- Coroutines para programación asíncrona.
- Retrofit para consumo de APIs (backend y clima).
- ViewModel y StateFlow para gestión del estado.
- json-server para simular el backend local.
- JUnit para pruebas unitarias.
  
## Instalación
- Clona el repositorio:
git clone https://github.com/aa-amp/AppAaron_Ampuero.git
- Abre el proyecto en Android Studio.
- Inicia el backend local con:
npx json-server --watch db.json --port 3001
- El archivo db.json está en la carpeta raíz del proyecto.
- Ejecuta la app desde Android Studio en la clase MainActivity.
- Usa el emulador Pixel 9a para probar la comunicación con el backend en la URL:
http://10.0.2.2:3001/

## Tests
- Los tests se encuentran en la carpeta:
com/example/appaaron_ampuero/test
- Para ver resultados:
app/build/reports/tests/testDebugUnitTest/index.html
Abre el archivo .html en tu navegador.
- También puedes ejecutar desde terminal:
./gradlew :app:testDebugUnitTest
- O ejecutar una clase específica:
./gradlew :app:testDebugUnitTest --tests "com.example.appaaron_ampuero.test.DuenoViewModelTest"
