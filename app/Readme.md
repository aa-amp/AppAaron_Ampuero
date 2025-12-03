# AppAaron_Ampuero

Aplicación Android diseñada para la gestión de un bazar. Permite administrar 
productos, usuarios y un carrito de compras, integrándose con un backend local que facilita
la persistencia y sincronización de datos durante el desarrollo.

Caracteristicas principales.
- Gestión de productos (listado, creación, edición, eliminación).
- Manejo de carrito de compras local
- Administración de usuarios.
- Administración de productos.
- Sincronización con backend local mediante json server.
- Compatible con emulador Pixel 9a (Es con el que se probó esta app)
- Manejo de api local y externa con la implementación del clima (con Open-Meteo).

Requisitos técnicos. 
- Android Studio
- Node.js Instalado
- Dependencia global o local de json-server

Iniciar el proyecto.
- Para iniciar el proyecto y conectar con la base de datos se debe abrir una terminal en la carpeta raiz del proyecto
- Ejecutar el backend ingresando este comando " npx json-server --watch db.json --port 3001 " y luego play en la clase "MainActivity"
- La base de datos es un archivo llamado db.json y está en la carpeta raíz del proyecto
- En la consola saldrá la url a la que deberá entrar. debería ser http://10.0.2.2:3001/
- Usar el emulador Pixel 9a para ejecutar la app y probar las funciones de comunicacion api.

Test
- Para hacer funcionar los test puede hacerlo desde la carpeta com/example/appaaron_ampuero/test
- Para ver el resultado debe ingresar a app/build/reports/tests/testDebugUnitTest/index.html
- y llevar el archivo .html a su navegador.
- Tambien puede abrir un terminal y ejecutar " ./gradlew :app:testDebugUnitTest " o ./gradlew :app:testDebugUnitTest --tests "CLASE CON SU PATH" 
- por ejemplo "./gradlew :app:testDebugUnitTest --tests "com.example.appaaron_ampuero.test.DuenoViewModelTest" 