# 🔧 Guía para Usar el Sistema con XAMPP

## ✅ Configuración con XAMPP

Ya que estás usando **XAMPP**, hay pasos específicos que debes seguir.

---

## 🚀 Pasos para la Otra Máquina

### **1. Instala XAMPP**

Si no está instalado en la otra máquina:
```
1. Descarga XAMPP desde: https://www.apachefriends.org/
2. Instala en C:\xampp (ruta por defecto)
3. Durante instalación, asegúrate que MySQL esté seleccionado
```

### **2. Inicia XAMPP Control Panel**

En la otra máquina:
```
C:\xampp\xampp-control.exe
```

O búscalo en el menú de inicio: **"XAMPP Control Panel"**

### **3. Inicia MySQL**

En el panel de control:
```
┌─────────────────────────────────────────┐
│ XAMPP Control Panel                     │
├─────────────────────────────────────────┤
│ Module    | PID    | Port(s) | Actions │
├───────────┼────────┼─────────┼─────────┤
│ Apache    | [    ] | 80, 443 | [Start] │
│ MySQL     | [    ] | 3306    | [Start] │ ← Click aquí
│ FileZilla | [    ] | 21, 14  | [Start] │
└─────────────────────────────────────────┘
```

1. Click en **"Start"** junto a MySQL
2. Espera que aparezca el PID (número de proceso)
3. El fondo se pone **VERDE** = MySQL corriendo ✅

### **4. Verifica que MySQL esté corriendo**

Debe verse así:
```
┌─────────────────────────────────────────┐
│ Module    | PID    | Port(s) | Actions │
├───────────┼────────┼─────────┼─────────┤
│ MySQL     | [5248] | 3306    | [Stop]  │ ← VERDE, muestra PID
└─────────────────────────────────────────┘
```

### **5. Crea la Base de Datos**

Opción A - Usando phpMyAdmin:
```
1. Abre navegador: http://localhost/phpmyadmin
2. Click en "Nueva" (en el panel izquierdo)
3. Nombre: bd_comida
4. Cotejamiento: utf8_general_ci
5. Click en "Crear"
```

Opción B - Usando SQL desde phpMyAdmin:
```
1. Abre: http://localhost/phpmyadmin
2. Click en pestaña "SQL"
3. Pega el contenido de init_database.sql
4. Click en "Continuar"
```

Opción C - Desde línea de comandos:
```cmd
cd C:\xampp\mysql\bin
mysql -u root -p bd_comida < C:\ruta\al\init_database.sql
```

### **6. Verifica la Configuración**

En tu proyecto, `ConexionDB.java` debe tener:
```java
URL: jdbc:mysql://localhost:3306/bd_comida
Usuario: root
Contraseña: (vacía)
```

Esto es correcto para XAMPP por defecto.

### **7. Despliega y Prueba**

```
1. Clean and Build del proyecto
2. Run
3. Accede a: http://localhost:8080/SistemaComida-1.0-SNAPSHOT/TestConexion
4. Debe mostrar: ✓ CONEXIÓN EXITOSA
```

---

## ⚠️ Problemas Comunes con XAMPP

### **Problema 1: Puerto 3306 Ocupado**

**Síntoma:** XAMPP no puede iniciar MySQL, dice "Port 3306 in use"

**Causa:** Otro servicio MySQL está usando el puerto (MySQL instalado fuera de XAMPP)

**Solución:**
```
Opción A - Cambiar puerto de XAMPP:
1. XAMPP Control Panel > Config (junto a MySQL) > my.ini
2. Busca: port=3306
3. Cámbialo a: port=3307
4. Guarda y reinicia MySQL en XAMPP

Opción B - Detener otro MySQL:
1. Abre services.msc
2. Busca "MySQL80" o "MySQL"
3. Click derecho > Detener
4. Inicia MySQL en XAMPP
```

Si cambias el puerto, actualiza `ConexionDB.java`:
```java
private static final String URL = "jdbc:mysql://localhost:3307/bd_comida?useSSL=false&serverTimezone=UTC";
```

### **Problema 2: MySQL Se Detiene Solo**

**Síntoma:** MySQL inicia pero se detiene a los segundos

**Causa:** Archivos de MySQL corruptos o puerto bloqueado

**Solución:**
```
1. XAMPP Control Panel > Logs (junto a MySQL)
2. Revisa el error
3. Común: Busca otro proceso usando puerto 3306:
   netstat -ano | findstr :3306
4. Mata el proceso o cambia el puerto de XAMPP
```

### **Problema 3: Error "Access Denied"**

**Síntoma:** TestConexion muestra error de autenticación

**Causa:** XAMPP configurado con contraseña para root

**Solución:**

Opción A - Sin contraseña (resetear):
```sql
1. Abre phpMyAdmin: http://localhost/phpmyadmin
2. Click en "Cuentas de usuario"
3. Edita usuario "root"
4. Establece contraseña vacía
5. Guarda
```

Opción B - Actualiza el código:
```java
// En ConexionDB.java:
private static final String CONTRASENA = "tu_contraseña_aqui";
```

### **Problema 4: Base de Datos No Existe**

**Síntoma:** Error "Unknown database 'bd_comida'"

**Solución:**
```
1. http://localhost/phpmyadmin
2. Nueva base de datos: bd_comida
3. Ejecuta init_database.sql desde la pestaña SQL
```

---

## 🎯 Checklist para la Otra Máquina

- [ ] XAMPP instalado
- [ ] XAMPP Control Panel abierto
- [ ] MySQL iniciado (fondo verde)
- [ ] Puerto 3306 disponible (o cambiado a 3307)
- [ ] phpMyAdmin accesible (http://localhost/phpmyadmin)
- [ ] Base de datos `bd_comida` creada
- [ ] Tablas creadas (desde init_database.sql)
- [ ] TestConexion muestra ✓ CONEXIÓN EXITOSA

---

## 📊 Configuración Típica de XAMPP

| Componente | Puerto por Defecto |
|------------|-------------------|
| Apache     | 80, 443           |
| MySQL      | 3306              |
| FileZilla  | 21                |
| Mercury    | 25, 110, 143      |
| Tomcat     | 8080              |

**Conflicto común:** Tomcat de XAMPP (8080) vs tu Tomcat de NetBeans (8080)

**Solución:** No inicies Tomcat en XAMPP, solo Apache y MySQL.

---

## 🔄 Flujo de Trabajo Diario

### **Cada vez que trabajes en el proyecto:**

1. **Inicia XAMPP Control Panel**
   ```
   C:\xampp\xampp-control.exe
   ```

2. **Inicia MySQL** (click en Start)

3. **Trabaja en NetBeans** normalmente

4. **Cuando termines:**
   - Detén tu servidor en NetBeans
   - Opcionalmente detén MySQL en XAMPP (o déjalo corriendo)

---

## ✅ Verificación Rápida

### **¿MySQL de XAMPP está corriendo?**

```cmd
# Opción 1 - Ver en XAMPP Control Panel
Fondo verde en MySQL = ✅ Corriendo

# Opción 2 - Desde CMD
netstat -ano | findstr :3306
(Si muestra algo = MySQL corriendo)

# Opción 3 - Desde navegador
http://localhost/phpmyadmin
(Si abre = MySQL corriendo)
```

---

## 🚀 Script Rápido para Otra Máquina

Ejecuta esto en CMD (otra máquina):

```cmd
@echo off
echo Verificando XAMPP...
echo.

echo 1. Verificando si XAMPP esta instalado...
if exist "C:\xampp\xampp-control.exe" (
    echo [OK] XAMPP instalado en C:\xampp
) else (
    echo [X] XAMPP no encontrado
    echo Instala XAMPP desde: https://www.apachefriends.org/
    pause
    exit
)

echo.
echo 2. Verificando puerto 3306...
netstat -ano | findstr :3306 >nul
if %errorlevel% equ 0 (
    echo [OK] Puerto 3306 en uso (MySQL probablemente corriendo)
) else (
    echo [X] Puerto 3306 libre
    echo Inicia MySQL en XAMPP Control Panel
)

echo.
echo 3. Verificando phpMyAdmin...
echo Abriendo http://localhost/phpmyadmin en el navegador...
start http://localhost/phpmyadmin

echo.
echo Si phpMyAdmin abre correctamente, MySQL funciona.
echo Entonces crea la base de datos 'bd_comida'.
echo.
pause
```

---

## 📝 Resumen

| Pregunta | Respuesta |
|----------|-----------|
| ¿Dónde está MySQL? | En XAMPP (C:\xampp\mysql) |
| ¿Cómo iniciarlo? | XAMPP Control Panel > Start MySQL |
| ¿Puerto? | 3306 (o 3307 si hay conflicto) |
| ¿Usuario? | root |
| ¿Contraseña? | (vacía por defecto) |
| ¿Cómo crear BD? | phpMyAdmin o init_database.sql |

---

## 🎯 Solución Rápida

**En la otra máquina:**

1. ✅ Abre **XAMPP Control Panel**
2. ✅ Click en **"Start"** junto a MySQL
3. ✅ Espera que se ponga **VERDE**
4. ✅ Abre **http://localhost/phpmyadmin**
5. ✅ Crea base de datos **bd_comida**
6. ✅ Ejecuta **init_database.sql** en phpMyAdmin
7. ✅ Refresca **TestConexion**

**¡Listo!** Debería conectar correctamente. 🚀

---

**NOTA IMPORTANTE:** XAMPP debe estar corriendo CADA VEZ que uses el sistema. Si apagas la PC o cierras XAMPP, MySQL se detiene.
