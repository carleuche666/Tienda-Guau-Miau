# Aplicación Movil para tienda Guau&Miau usando Kotlin junto con AndroidStudio
Desarrollado por Carlos Acevedo y Jean Bizama

# Descripcion
Aplicacion creada para tienda online de mascotas, ofreciendo la posibilidad de acceder los precios de los productos, ubicacion de la tienda, y contacto con esta misma.

# Funcionalidades 
 - Registro
 - Login
 - Navegacion
 - Gestion de perfil
 - Catalogo de productos
 - Funcionalidad nativa (Uso de cámara, Vibracion)
 - Conexion API MASCOTAS (https://github.com/JeanBizama/TiendaGuauMiauService)


### Pasos para ejecutar

### Opción A: Instalación Directa (APK)
Si solo deseas probar la funcionalidad final en un dispositivo Android:
1.  Descarga el archivo `.apk` ubicado en: `Tienda-Guau-Miau/app/release/app-release.apk`.
2.  Transfiere el archivo a tu celular.
3.  Acepta los permisos de "Instalar aplicaciones desconocidas".
4.  Ejecuta la app.

### Opción B: Ejecutar Código Fuente (Para desarrollo)
1.  Clonar el repositorio:
    ```bash
    git clone https://github.com/JeanBizama/Tienda-Guau-Miau.git
    ```
2.  Abrir **Android Studio**.
3.  Seleccionar **Open** y buscar la carpeta clonada.
4.  Esperar a que Gradle sincronice las dependencias.
5.  Conectar un dispositivo físico o iniciar un Emulador.
6.  Dar clic al botón **Run (▶)**.

## 📡 Consumo de Datos (Endpoints)
La aplicación se conecta al microservicio `TiendaGuauMiauService` (https://github.com/JeanBizama/TiendaGuauMiauService) para las siguientes operaciones y gestiona la información de las mascotas a través del controlador `PetController`:

**Base URL:** `/api/pets`

| Método | Endpoint | Descripción | Body / Parámetros |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/pets` | Obtiene la lista de todas las mascotas registradas. | N/A |
| `POST` | `/api/pets` | Registra una nueva mascota. | **JSON Body** (ver ejemplo abajo) |
| `GET` | `/api/pets/email/{email}` | Obtiene las mascotas asociadas a un usuario específico. | Param: `userEmail` (String) |
| `GET` | `/api/pets/id/{id}` | Busca una mascota específica por su ID único. | Param: `id` (Integer) |
| `PUT` | `/api/pets/{id}` | Actualiza los datos de una mascota existente. | Param: `id` + **JSON Body** |
| `DELETE` | `/api/pets/{id}` | Elimina una mascota del sistema. | Param: `id` (Integer) |

### 📝 Ejemplo de JSON
Para los métodos `POST` y `PUT`, se requiere enviar un objeto JSON con la siguiente estructura:

```json
{
  "name": "Michi",
  "type": "Gato",
  "userEmail": "usuario@duocuc.cl"
}