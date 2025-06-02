@echo off
echo Compilando el proyecto...
javac -cp "lib/gson-2.10.1.jar" src\backend\*.java src\frontend\*.java

if %errorlevel% neq 0 (
    echo ❌ Error al compilar. Revisa los mensajes arriba.
    pause
    exit /b
)

echo Ejecutando la aplicación...
java -cp "lib/gson-2.10.1.jar;src" frontend.VentanaPrincipal
pause
