# 🔧 Test de Conexión a Base de Datos

## 🎯 Qué es esto

Un servlet de diagnóstico que **prueba la conexión a MySQL** y muestra:

- ✅ Estado de la conexión
- ✅ Información del servidor MySQL
- ✅ Tablas disponibles
- ✅ Usuarios en la base de datos
- ✅ Diagnóstico y recomendaciones

---

## 🚀 Cómo Usar

### **Paso 1: Build y Run**

```
1. Click derecho en proyecto → Clean and Build
2. Click derecho en proyecto → Run
```

### **Paso 2: Acceder al Test**

Abre tu navegador y ve a:

```
http://localhost:8080/SistemaComida-1.0-SNAPSHOT/TestConexion
```

### **Paso 3: Ver Resultados**

La página mostrará:

#### ✅ **Si la conexión funciona:**
```
✓ CONEXIÓN EXITOSA
Tiempo de conexión: 45 ms

Base de datos: MySQL 8.0.33
Driver: mysql-connector-j-8.0.33
Tablas encontradas: Usuarios, Productos, Ventas...
Total de usuarios: 3
```

#### ❌ **Si hay error:**
```
✗ ERROR DE CONEXIÓN
Mensaje: No suitable driver found...

Posibles causas:
1. MySQL no está corriendo
2. Base de datos 'bd_comida' no existe
3. Driver JDBC no cargado
...
```

---

## 📊 Información que Muestra

### **1. Test de Conexión**
- Estado de conexión (exitosa o fallida)
- Tiempo de respuesta
- Mensaje de error (si hay)

### **2. Información del DBMS**
- Nombre y versión de MySQL
- Driver JDBC utilizado
- URL de conexión
- Usuario conectado
- Esquema actual

### **3. Tablas en la Base de Datos**
- Lista de todas las tablas
- Tipo de cada tabla

### **4. Test de Tabla Usuarios**
- Total de usuarios en la tabla
- Lista de usuarios (ID, username, rol, hash preview)
- Alerta si la tabla está vacía

### **5. Diagnóstico y Recomendaciones**
- Estado general del sistema
- Posibles causas de errores
- Soluciones sugeridas

---

## 🎨 Ejemplo Visual

```
┌─────────────────────────────────────────────────────────┐
│ 🔧 Test de Conexión a Base de Datos                    │
├─────────────────────────────────────────────────────────┤
│                                                          │
│ 1. Test de Conexión a MySQL                            │
│    ✓ CONEXIÓN EXITOSA                                  │
│    Tiempo de conexión: 45 ms                           │
│                                                          │
│ 2. Información de la Base de Datos                     │
│    Nombre del DBMS: MySQL                               │
│    Versión: 8.0.33                                      │
│    Driver: mysql-connector-j-8.0.33                     │
│                                                          │
│ 3. Tablas en la Base de Datos                          │
│    • Usuarios                                           │
│    • Productos                                          │
│    • Ventas                                             │
│    • DetalleVenta                                       │
│                                                          │
│ 4. Test de Tabla 'Usuarios'                            │
│    ✓ Tabla existe                                       │
│    Total de usuarios: 3                                 │
│                                                          │
│    ID | Username | Rol   | Hash                         │
│    ───┼──────────┼───────┼──────────                    │
│    1  | admin    | ADMIN | $2a$10$hM1wgKol2...         │
│    2  | empleado | USER  | $2a$10$cRrfJmvY8...         │
│                                                          │
│ 5. Diagnóstico                                          │
│    ✓ Estado: CORRECTO                                   │
│    El sistema está listo para usar                      │
│                                                          │
└─────────────────────────────────────────────────────────┘
```

---

## 🔍 Casos de Uso

### **Caso 1: Verificar que MySQL funciona**
```
Problema: No sé si MySQL está corriendo
Solución: Accede a TestConexion
          Si muestra ✓ CONEXIÓN EXITOSA → MySQL funciona
          Si muestra ✗ ERROR → MySQL no está corriendo
```

### **Caso 2: Verificar que la BD existe**
```
Problema: No sé si la base de datos 'bd_comida' existe
Solución: Accede a TestConexion
          Si muestra tablas → La BD existe
          Si muestra error → Ejecuta init_database.sql
```

### **Caso 3: Verificar usuarios**
```
Problema: No sé si hay usuarios en la tabla
Solución: Accede a TestConexion
          Verás la lista de usuarios y sus roles
          Si está vacía → Usa UtilServlet para crear usuarios
```

### **Caso 4: Diagnosticar problemas de login**
```
Problema: El login no funciona
Solución: Accede a TestConexion
          1. ¿Conexión exitosa? → Problema es de contraseñas
          2. ¿Error de driver? → Ejecuta fix-driver-problem.bat
          3. ¿BD no existe? → Ejecuta init_database.sql
```

---

## ⚙️ Configuración

El servlet usa la configuración de `ConexionDB.java`:

```java
URL: jdbc:mysql://localhost:3306/bd_comida
Usuario: root
Contraseña: (vacía)
```

Si necesitas cambiar la configuración, edita `ConexionDB.java`.

---

## 🔒 Seguridad

⚠️ **IMPORTANTE:**

- Este servlet es **solo para desarrollo**
- Muestra información sensible de la BD
- **NO lo uses en producción**

### **Para deshabilitar en producción:**

Opción 1 - Comentar la anotación:
```java
// @WebServlet(name = "TestConexionServlet", urlPatterns = {"/TestConexion"})
```

Opción 2 - Eliminar el archivo:
```
Borrar: TestConexionServlet.java
```

---

## 🚦 Mensajes Comunes

### ✅ **Éxito:**
```
✓ CONEXIÓN EXITOSA
→ Todo funciona correctamente
→ Puedes usar el sistema normalmente
```

### ❌ **"No suitable driver found":**
```
→ El driver MySQL no está cargado
→ Solución: Ejecuta fix-driver-problem.bat
→ O descarga el driver manualmente
```

### ❌ **"Unknown database 'bd_comida'":**
```
→ La base de datos no existe
→ Solución: Ejecuta init_database.sql
→ Comando: mysql -u root -p < init_database.sql
```

### ❌ **"Access denied for user 'root'":**
```
→ Las credenciales son incorrectas
→ Solución: Verifica usuario/contraseña en ConexionDB.java
→ O cambia las credenciales de MySQL
```

### ⚠️ **"Tabla 'Usuarios' vacía":**
```
→ La tabla existe pero no hay usuarios
→ Solución: Ejecuta init_database.sql
→ O usa UtilServlet para crear usuarios
```

---

## 📋 Checklist de Diagnóstico

Usa este servlet para verificar:

- [ ] ¿MySQL está corriendo?
- [ ] ¿La base de datos 'bd_comida' existe?
- [ ] ¿El driver JDBC se carga correctamente?
- [ ] ¿Las tablas están creadas?
- [ ] ¿Hay usuarios en la tabla Usuarios?
- [ ] ¿Los hashes de contraseñas son válidos?

---

## 🔗 Enlaces Útiles

Después de usar TestConexion:

- **Si todo funciona:**
  - Ve a [login.jsp](http://localhost:8080/SistemaComida-1.0-SNAPSHOT/login.jsp)
  - O desactiva MODO_DESARROLLO en LoginServlet

- **Si hay problemas:**
  - Usa [UtilServlet](http://localhost:8080/SistemaComida-1.0-SNAPSHOT/UtilServlet) para crear usuarios
  - Ejecuta `fix-driver-problem.bat` si falla el driver
  - Ejecuta `init_database.sql` si falta la BD

---

## 📝 Ejemplo de Salida

### **Conexión Exitosa:**
```html
🔧 Test de Conexión a Base de Datos

1. Test de Conexión a MySQL
   ✓ CONEXIÓN EXITOSA
   Tiempo de conexión: 45 ms

2. Información de la Base de Datos
   Nombre del DBMS: MySQL
   Versión del DBMS: 8.0.33
   Driver JDBC: mysql-connector-j-8.0.33
   URL de conexión: jdbc:mysql://localhost:3306/bd_comida

3. Tablas en la Base de Datos 'bd_comida'
   Usuarios          TABLE
   Productos         TABLE
   Ventas            TABLE
   DetalleVenta      TABLE

4. Test de Tabla 'Usuarios'
   ✓ Tabla 'Usuarios' existe
   Total de usuarios: 3

   ID | Username | Rol   | Hash (preview)
   ───┼──────────┼───────┼────────────────────
   1  | admin    | ADMIN | $2a$10$hM1wgKol2...
   2  | empleado | USER  | $2a$10$cRrfJmvY8...
   3  | test     | USER  | $2a$10$kJqJaKJQr...

5. Diagnóstico y Recomendaciones
   ✓ Estado: CORRECTO
   La conexión funciona correctamente.
   
   Siguiente paso:
   - Cambia MODO_DESARROLLO a false en LoginServlet
   - Accede al login
```

---

## ✅ Resumen

| Característica | Descripción |
|---------------|-------------|
| **URL** | `/TestConexion` |
| **Función** | Probar conexión a MySQL |
| **Muestra** | Estado, tablas, usuarios, diagnóstico |
| **Uso** | Solo desarrollo/diagnóstico |
| **Seguridad** | ⚠️ Deshabilitar en producción |

---

**Accede ahora:** http://localhost:8080/SistemaComida-1.0-SNAPSHOT/TestConexion

Verás inmediatamente si tu base de datos funciona correctamente. 🚀
