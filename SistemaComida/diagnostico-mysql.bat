@echo off
echo ================================================
echo DIAGNOSTICO DE MYSQL
echo ================================================
echo.

echo [1] Verificando si MySQL esta instalado...
mysql --version 2>nul
if %errorlevel% equ 0 (
    echo [OK] MySQL instalado
) else (
    echo [X] MySQL NO instalado o no esta en PATH
    goto :no_mysql
)
echo.

echo [2] Verificando servicio MySQL...
sc query MySQL80 >nul 2>&1
if %errorlevel% equ 0 (
    echo [OK] Servicio MySQL80 encontrado
    sc query MySQL80 | findstr "STATE"
) else (
    sc query MySQL >nul 2>&1
    if %errorlevel% equ 0 (
        echo [OK] Servicio MySQL encontrado
        sc query MySQL | findstr "STATE"
    ) else (
        echo [X] Servicio MySQL no encontrado
        goto :no_service
    )
)
echo.

echo [3] Verificando puerto 3306...
netstat -ano | findstr :3306 >nul
if %errorlevel% equ 0 (
    echo [OK] Puerto 3306 en uso
    netstat -ano | findstr :3306
) else (
    echo [X] Puerto 3306 no esta en uso
    echo MySQL probablemente no esta corriendo
)
echo.

echo [4] Intentando conectar a MySQL...
mysql -u root -e "SELECT 'Conexion exitosa' as Estado;" 2>nul
if %errorlevel% equ 0 (
    echo [OK] Conexion a MySQL exitosa
) else (
    echo [X] No se pudo conectar a MySQL
    echo Intenta con: mysql -u root -p
)
echo.

echo ================================================
echo RESUMEN
echo ================================================
echo.
echo Si todo muestra [OK], MySQL esta funcionando.
echo.
echo Si hay errores:
echo  1. MySQL no instalado - Instala MySQL 8.0
echo  2. Servicio detenido - Ejecuta: net start MySQL80
echo  3. Puerto no escucha - Inicia el servicio
echo.
echo ALTERNATIVA: Usa MODO_DESARROLLO (no necesita MySQL)
echo.
pause
exit /b

:no_mysql
echo.
echo ================================================
echo SOLUCION
echo ================================================
echo.
echo OPCION 1: Instala MySQL
echo  - Descarga: https://dev.mysql.com/downloads/installer/
echo  - Instala version 8.0
echo  - Usuario: root, Password: (vacio)
echo  - Puerto: 3306
echo.
echo OPCION 2: Usa MODO_DESARROLLO
echo  - No necesita MySQL
echo  - Ya configurado en LoginServlet
echo  - Login funciona sin base de datos
echo.
pause
exit /b

:no_service
echo.
echo ================================================
echo SOLUCION
echo ================================================
echo.
echo MySQL instalado pero servicio no configurado.
echo.
echo 1. Reinstala MySQL
echo 2. O configura el servicio manualmente
echo 3. O usa MODO_DESARROLLO (no necesita MySQL)
echo.
pause
exit /b
