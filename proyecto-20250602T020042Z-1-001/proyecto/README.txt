===========================================
 SISTEMA DE PRUEBAS EDUCATIVAS (JAVA SWING)
===========================================

Descripción:
------------
Este es un sistema de evaluación por pruebas educativas construido en Java utilizando Swing. 
Permite cargar un archivo de preguntas en formato JSON y realizar evaluaciones basadas en 
la taxonomía de Bloom, mostrando un resumen al finalizar.

Repositorio:
------------
https://github.com/usuario/sistema-pruebas-educativas

Requisitos:
-----------
- Java JDK 8 o superior
- Biblioteca externa: gson-2.10.1.jar
- Sistema operativo: Windows (recomendado)
- Git (opcional, para clonar)

Estructura del proyecto:
------------------------
/src
  /backend         --> Clases de lógica (Pregunta.java, Prueba.java)
  /frontend        --> Interfaces de usuario (VentanaPrincipal.java, VentanaPrueba.java)
/recursos          --> Archivo prueba.json (archivo de preguntas)
/lib               --> gson-2.10.1.jar (librería JSON)
/compilar_y_ejecutar.bat --> Script para compilar y ejecutar
/README.txt        --> Este archivo

Instrucciones para compilar y ejecutar:
---------------------------------------

1. Clonar el repositorio (opcional):
   git clone https://github.com/usuario/sistema-pruebas-educativas.git

2. Entrar a la carpeta del proyecto descargado.

3. Hacer doble clic sobre el archivo:
   compilar_y_ejecutar.bat

   Este script compilará automáticamente todos los archivos necesarios
   y lanzará el programa principal (`VentanaPrincipal`).

Uso:
----
- Al abrir el programa, haz clic en "Cargar archivo" y selecciona un archivo JSON válido con las preguntas.
  **O también puedes utilizar nuestra prueba de ejemplo incluida en el proyecto como "prueba.json",**
  que consta de 10 preguntas generales de verdadero/falso y selección múltiple.
- Luego presiona "Realizar prueba".
- Responde todas las preguntas y finaliza para ver el resumen de evaluación.

Autor:
------
Desarrollado por Eduardo Vargas y Martin Sierra - 2025

