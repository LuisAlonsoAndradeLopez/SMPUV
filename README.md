# SMPUV
Proyecto Final de la Materia Procesos para la Ingeniería del Software.
SMPUV es un sistema sencillo para llevar a cabo una administración sencilla (CRUD) de refacciones de computadoras, computadoras, y creación de mantenimientos a las computadoras registradas en el sistema (solo creación, no hay opción de consulta).

## Tecnologías requeridas para la instalación
* Java versión 11 o 17.
* JavaFX versión 19.0.2.1.
* MySQL versión 8.0 o posterior.

## Instrucciones para la ejecución del sistema
1. Ejecutar el script "CrearBaseDeDatos.sql" en MySQL.
2. Ejecutar el script "Datos.sql" en MySQL.
3. En el archivo "src/dependencies/resources/DatabaseAccess.properties", poner la dirección de la base de datos, y el nombre de usuario y contraseña del usuario que usted haya creado para acceder a la base de datos.
4. En el entorno de desarrollo que usted use para ejecutar proyectos desarrollados en Java (ej: NetBeans, IntelliJ, Eclipse, Visual Studio Code), modificar los archivos de configuración necesarios del entorno de desarrollo para referenciar en el proyecto las librerías ubicadas en la carpeta "lib".
5. En ese mismo entorno de desarrollo, referenciar en el proyecto las librerías de JavaFX (deberá descomprimir el .zip de la instalación de JavaFX, y la carpeta generada ponerla en una ruta ajena a alguna ruta del proyecto. NO INTENTE REFERENCIAR LAS LIBRERIAS DE JAVAFX EN LA CARPETA LIB PORQUE EL ENTORNO DE DESARROLLO REQUIERE LEER LOS ARCHIVOS .dll QUE VIENEN INCLUIDOS EN LA CARPETA DESCOMPRIMIDA DE LA INSTALACIÓN DE JAVAFX).
6. En ese mismo entorno de desarrollo, ejecutar el archivo "src/mx/uv/fei/SMPUV.java"
7. ¡Listo!, SMPUV está listo para ser usado.

## Ingresar al Sistema
Para poder ingresar al sistema, en la ventana de login en el campo de texto "Identificador" introduzca el identificador "123456789" y en el campo de texto "Contraseña" introduzca la contraseña "basquetbol".
