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

Requisitos técnicos. 
- Android Studio
- Node.js Instalado
- Dependencia global o local de json-server

Iniciar el proyecto.
- Para iniciar el proyecto y conectar con la base de datos se debe abrir una terminal en la carpeta raiz del proyecto
- Ejecutar el backend ingresando este comando " npx json-server --watch db.json --port 3001 " y luego play en la clase "MainActivity"
- La base de datos es un archivo llamado db.json y está en la carpeta raíz del proyecto
- En la consola saldra la url a la que deberá entrar. debería ser http://10.0.2.2:3001/
- En Android Studio entrar a la clase "MainActivity" y ejecutarla.
- Usar el emulador Pixel 9a para ejecutar la app.


