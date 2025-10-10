# 📘 Ejercicio 01: Listar Directorios con Comandos del SO

> **Autor:** Miguel Ángel Ramírez (marp0604)  
> **Fecha:** 2025-10-06  
> **Asignatura:** Programación de Servicios y Procesos (PSP)  
> **Unidad:** 01 - Procesos

---

## 📑 Índice

1. [Enunciado del Ejercicio](#enunciado-del-ejercicio)
2. [Objetivos de Aprendizaje](#objetivos-de-aprendizaje)
3. [Análisis del Problema](#análisis-del-problema)
4. [Código Completo](#código-completo)
5. [Explicación Detallada](#explicación-detallada)
6. [Casos de Prueba](#casos-de-prueba)
7. [Errores Comunes](#errores-comunes)
8. [Conclusiones](#conclusiones)

---

## 🎯 Enunciado del Ejercicio

Crear un programa en Java que ejecute el comando del sistema operativo para mostrar la lista de ficheros y directorios de cada directorio pasado como argumento.

### Requisitos:

1. **Detectar el sistema operativo** para usar el comando correcto:
    - Linux/Mac: `ls -l`
    - Windows: `dir`

2. **Validar los argumentos** (directorios):
    - Comprobar que existen
    - Comprobar que son directorios válidos
    - Usar excepciones específicas (NO `Exception` genérica)
    - Si un directorio es inválido, mostrar error y continuar con el siguiente

3. **Ejecutar un proceso por cada directorio** válido

4. **Mostrar la salida** con el siguiente formato:
   ```
   /ruta/absoluta/del/directorio/
   <salida del comando>
   
   /siguiente/directorio/
   <salida del comando>
   ```

---

## 🎓 Objetivos de Aprendizaje

Al completar este ejercicio, aprenderás:

- ✅ Usar `ProcessBuilder` para ejecutar comandos del sistema
- ✅ Detectar el sistema operativo en tiempo de ejecución
- ✅ Validar argumentos de entrada
- ✅ Manejar excepciones específicas (buenas prácticas)
- ✅ Capturar y procesar la salida de procesos externos
- ✅ Trabajar con la clase `File` para validar rutas

---

## 🔍 Análisis del Problema

### Diagrama de Flujo

```
INICIO
  │
  ├─→ ¿Hay argumentos? ──No──→ Mostrar error y salir
  │        │
  │       Sí
  │        │
  ├─→ Por cada argumento (ruta):
  │     │
  │     ├─→ ¿El directorio existe? ──No──→ Lanzar IllegalArgumentException
  │     │         │
  │     │        Sí
  │     │         │
  │     ├─→ ¿Es un directorio? ──No──→ Lanzar IllegalArgumentException
  │     │         │
  │     │        Sí
  │     │         │
  │     ├─→ Detectar SO
  │     │         │
  │     ├─→ Crear ProcessBuilder con comando apropiado
  │     │         │
  │     ├─→ Ejecutar proceso
  │     │         │
  │     ├─→ Capturar y mostrar salida
  │     │         │
  │     └─→ Esperar a que termine
  │
  └─→ FIN
```

---

## 💻 Código Completo

```java
package unidad01.ejercicios;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Programa que lista el contenido de directorios usando comandos del sistema operativo.
 * Detecta automáticamente el SO y ejecuta el comando apropiado (ls -l o dir) dependiendo del SO.
 * 
 * @author Miguel Angel Ramirez (marp0604)
 */
public class Ejercicio02 {
    
    /**
     * Método principal que procesa los argumentos y ejecuta el listado de directorios.
     * 
     * @param args Array de rutas de directorios a listar
     */
    public static void main(String[] args) {
        // Valída qué argumentos se están pasando
        if (args.length == 0) {
            System.err.println("Error: No se proporcionaron directorios como argumentos.");
            System.exit(1);
        }

        // Confirma que sea un directorio existente
        for (String ruta : args) {
            try {
                lanzarComando(ruta);
            } catch (IllegalArgumentException e) {
                System.err.println("Error: " + e.getMessage());
                System.err.println("\tContinuando con el siguiente directorio...\n");
            } catch (IOException e) {
                System.err.println("Error de E/S: " + e.getMessage());
            } catch (InterruptedException e) {
                System.err.println("Proceso interrumpido: " + e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Encuentra el tipo de SO y ejecuta el comando correspondiente
     * 
     * @param ruta Ruta del directorio
     * @throws IllegalArgumentException Si el directorio no es válido
     * @throws IOException Si hay error de E/S
     * @throws InterruptedException Si el proceso es interrumpido
     */
    public static void lanzarComando(String ruta) throws IllegalArgumentException, IOException, InterruptedException {
        
        // Crea el objeto File al cual se le pasa la ruta
        File directorio = new File(ruta);

        // Valída que el directorio existe
        if (!directorio.exists()) {
            throw new IllegalArgumentException("El directorio '" + ruta + "' no existe");
        }

        // Valída que es un directorio
        if (!directorio.isDirectory()) {
            throw new IllegalArgumentException("'" + ruta + "' no es un directorio");
        }

        // Detecta que tipo de SO es el que está ejecutando el programa
        String sistemaOperativo = System.getProperty("os.name").toLowerCase();

        // Verifica el SO y lanza el comando correspondiente
        ProcessBuilder processBuilder;

        if (sistemaOperativo.contains("win")) {
            // Windows: cmd /c dir
            processBuilder = new ProcessBuilder("cmd.exe", "/c", "dir", ruta);
        } else {
            // Linux/Mac: ls -l
            processBuilder = new ProcessBuilder("ls", "-l", ruta);
        }

        // Combina stdout y stderr en un solo stream
        processBuilder.redirectErrorStream(true);

        // Ejecuta el proceso
        Process proceso = processBuilder.start();

        // Muestra el directorio
        System.out.println(directorio.getAbsolutePath() + "/");

        // Lee y muestra la salida línea por línea
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(proceso.getInputStream()))) {
            
            String linea;
            while ((linea = reader.readLine()) != null) {
                System.out.println(linea);
            }
        }

        // Espera a que el proceso termine
        int codigoSalida = proceso.waitFor();

        // Verifica el código de salida
        if (codigoSalida != 0) {
            System.err.println("Advertencia: El comando terminó con código " + codigoSalida);
        }
        System.out.println();
    }
}
```

---

## 📖 Explicación Detallada

### 🔹 Sección 1: Método `main()`

#### **Validación de argumentos**

```java
if (args.length == 0) {
    System.err.println("Error: No se proporcionaron directorios como argumentos.");
    System.exit(1);
}
```

**¿Qué hace?**
- Verifica que el usuario pasó al menos un argumento
- Si no hay argumentos, muestra un mensaje de error y termina el programa
- `System.exit(1)` indica que el programa terminó con error

**¿Por qué es importante?**
- Evita un `ArrayIndexOutOfBoundsException` más adelante
- Proporciona feedback inmediato al usuario
- Sigue las convenciones Unix (código 0 = éxito, != 0 = error)

---

#### **Bucle de procesamiento**

```java
for (String ruta : args) {
    try {
        lanzarComando(ruta);
    } catch (IllegalArgumentException e) {
        System.err.println("Error: " + e.getMessage());
        System.err.println("\tContinuando con el siguiente directorio...\n");
    } catch (IOException e) {
        System.err.println("Error de E/S: " + e.getMessage());
    } catch (InterruptedException e) {
        System.err.println("Proceso interrumpido: " + e.getMessage());
        Thread.currentThread().interrupt();
    }
}
```

**¿Qué hace?**
- Itera sobre cada directorio pasado como argumento
- Intenta procesar cada uno con `lanzarComando()`
- Captura diferentes tipos de excepciones de forma específica

**Excepciones capturadas:**

| Excepción | Cuándo ocurre | Acción |
|-----------|---------------|--------|
| `IllegalArgumentException` | Directorio inválido | Mostrar error y continuar |
| `IOException` | Error de entrada/salida | Mostrar error |
| `InterruptedException` | Proceso interrumpido | Mostrar error y restaurar flag |

**¿Por qué NO usamos `Exception`?**

```java
// ❌ MAL - Demasiado genérico
catch (Exception e) {
    // Captura TODO, incluso errores que no esperamos
}

// ✅ BIEN - Específico y controlado
catch (IllegalArgumentException e) {
    // Solo captura argumentos inválidos
}
catch (IOException e) {
    // Solo captura errores de E/S
}
```

**Ventajas de excepciones específicas:**
- Manejo diferenciado según el error
- Código más legible y mantenible
- Evita ocultar bugs inesperados

---

### 🔹 Sección 2: Método `lanzarComando()`

#### **PASO 1: Crear objeto File**

```java
File directorio = new File(ruta);
```

**¿Qué es `File`?**
- Representa una ruta del sistema de archivos
- NO abre ni lee el archivo/directorio
- Solo es una representación abstracta de la ruta

**Métodos útiles de `File`:**

| Método | Descripción |
|--------|-------------|
| `exists()` | ¿La ruta existe en el disco? |
| `isDirectory()` | ¿Es un directorio? |
| `isFile()` | ¿Es un archivo regular? |
| `getAbsolutePath()` | Obtiene la ruta absoluta |
| `getName()` | Obtiene el nombre del archivo/directorio |
| `canRead()` | ¿Tenemos permisos de lectura? |

---

#### **PASO 2: Validar existencia**

```java
if (!directorio.exists()) {
    throw new IllegalArgumentException("El directorio '" + ruta + "' no existe");
}
```

**¿Qué hace?**
- Comprueba si la ruta existe en el sistema de archivos
- Si no existe, lanza `IllegalArgumentException`

**¿Por qué `IllegalArgumentException`?**
- Es la excepción estándar para argumentos inválidos en Java
- Indica claramente que el problema está en el input del usuario
- Es una **unchecked exception** (no necesita declararse en el método)

---

#### **PASO 3: Validar que es un directorio**

```java
if (!directorio.isDirectory()) {
    throw new IllegalArgumentException("'" + ruta + "' no es un directorio");
}
```

**¿Qué hace?**
- Comprueba que la ruta es un directorio (no un archivo)
- Si es un archivo, lanza `IllegalArgumentException`

**Ejemplo de error:**

```bash
# Si el usuario pasa un archivo en lugar de directorio
java Ejercicio02 C:\Windows\System32\cmd.exe

# Salida:
❌ Error: 'C:\Windows\System32\cmd.exe' no es un directorio
```

---

#### **PASO 4: Detectar el Sistema Operativo**

```java
String sistemaOperativo = System.getProperty("os.name").toLowerCase();
```

**¿Qué hace?**
- Obtiene el nombre del SO del sistema
- Convierte a minúsculas para facilitar la comparación

**Valores típicos:**

| Sistema | Valor de `os.name` |
|---------|-------------------|
| Windows 10 | `"windows 10"` |
| Windows 11 | `"windows 11"` |
| Linux Ubuntu | `"linux"` |
| macOS | `"mac os x"` |

**⚠️ IMPORTANTE - Error común:**

```java
// ❌ INCORRECTO - Con espacio
System.getProperty("os name")

// ✅ CORRECTO - Con punto
System.getProperty("os.name")
```

**Otras propiedades útiles:**

```java
System.getProperty("os.version");    // Versión del SO
System.getProperty("os.arch");       // Arquitectura (x86, amd64)
System.getProperty("user.name");     // Nombre del usuario
System.getProperty("user.home");     // Directorio home del usuario
System.getProperty("user.dir");      // Directorio actual de trabajo
System.getProperty("file.separator"); // Separador de rutas (\ en Windows, / en Unix)
```

---

#### **PASO 5: Preparar el comando**

```java
ProcessBuilder processBuilder;

if (sistemaOperativo.contains("win")) {
    // Windows: cmd /c dir
    processBuilder = new ProcessBuilder("cmd.exe", "/c", "dir", ruta);
} else {
    // Linux/Mac: ls -l
    processBuilder = new ProcessBuilder("ls", "-l", ruta);
}
```

**¿Por qué usar `contains("win")`?**
- Funciona con todas las versiones de Windows
- `"windows 10".contains("win")` → `true`
- `"windows 11".contains("win")` → `true`
- `"windows server 2019".contains("win")` → `true`

**Comandos utilizados:**

##### **Windows: `cmd.exe /c dir`**

| Componente | Descripción |
|------------|-------------|
| `cmd.exe` | Intérprete de comandos de Windows |
| `/c` | Ejecuta el comando y termina |
| `dir` | Comando para listar directorios |
| `ruta` | El directorio a listar |

**Ejemplo:**
```cmd
cmd.exe /c dir C:\Windows
```

##### **Linux/Mac: `ls -l`**

| Componente | Descripción |
|------------|-------------|
| `ls` | Comando para listar archivos |
| `-l` | Formato largo (detallado) |
| `ruta` | El directorio a listar |

**Ejemplo:**
```bash
ls -l /home/marp0604
```

---

#### **PASO 6: Configurar redirección de streams**

```java
processBuilder.redirectErrorStream(true);
```

**¿Qué hace?**
- Combina **stdout** (salida estándar) y **stderr** (salida de error) en un solo stream
- Simplifica la lectura: solo necesitas leer `getInputStream()`

**Sin `redirectErrorStream(true)`:**

```java
// ❌ Más complejo - Necesitas dos readers
BufferedReader stdoutReader = new BufferedReader(
    new InputStreamReader(proceso.getInputStream()));

BufferedReader stderrReader = new BufferedReader(
    new InputStreamReader(proceso.getErrorStream()));
```

**Con `redirectErrorStream(true)`:**

```java
// ✅ Más simple - Solo un reader
BufferedReader reader = new BufferedReader(
    new InputStreamReader(proceso.getInputStream()));
// stderr también viene por getInputStream()
```

---

#### **PASO 7: Ejecutar el proceso**

```java
Process proceso = processBuilder.start();
```

**¿Qué ocurre internamente?**

1. El SO crea un **nuevo proceso**
2. Se ejecuta el comando configurado
3. Java devuelve un objeto `Process` para controlarlo
4. El método `start()` **NO bloquea** - continúa inmediatamente

**Métodos importantes de `Process`:**

| Método | Descripción |
|--------|-------------|
| `getInputStream()` | Lee la salida del proceso (stdout) |
| `getErrorStream()` | Lee los errores del proceso (stderr) |
| `getOutputStream()` | Envía datos al proceso (stdin) |
| `waitFor()` | Espera a que termine y devuelve código de salida |
| `isAlive()` | ¿El proceso sigue ejecutándose? |
| `destroy()` | Termina el proceso forzosamente |

---

#### **PASO 8: Mostrar encabezado**

```java
System.out.println(directorio.getAbsolutePath() + "/");
```

**¿Qué hace?**
- Muestra la ruta absoluta del directorio
- Añade `/` al final para claridad visual

**Diferencia entre rutas:**

```java
String rutaRelativa = directorio.getPath();       // "src\unidad01"
String rutaAbsoluta = directorio.getAbsolutePath(); // "C:\Users\...\PSP_2DAM\src\unidad01"
String rutaCanonica = directorio.getCanonicalPath(); // Resuelve enlaces simbólicos
```

**Ejemplo de salida:**

```
C:\Users\Miguel Angel\IdeaProjects\PSP_2DAM\src/
```

---

#### **PASO 9: Leer y mostrar la salida**

```java
try (BufferedReader reader = new BufferedReader(
        new InputStreamReader(proceso.getInputStream()))) {
    
    String linea;
    while ((linea = reader.readLine()) != null) {
        System.out.println(linea);
    }
}
```

**Desglose de la estructura:**

##### **1. `try-with-resources`**

```java
try (BufferedReader reader = ...) {
    // Usar el reader
}
// reader.close() se llama automáticamente aquí
```

**Ventajas:**
- Cierra automáticamente los recursos
- Evita fugas de memoria
- Código más limpio

**Sin try-with-resources (❌ no recomendado):**

```java
BufferedReader reader = null;
try {
    reader = new BufferedReader(...);
    // Usar el reader
} finally {
    if (reader != null) {
        reader.close();
    }
}
```

##### **2. Cadena de streams**

```java
proceso.getInputStream()                  // InputStream (bytes)
    ↓
new InputStreamReader(...)               // InputStreamReader (bytes → caracteres)
    ↓
new BufferedReader(...)                  // BufferedReader (lee líneas completas)
```

**¿Por qué esta cadena?**

| Clase | Función |
|-------|---------|
| `InputStream` | Lee bytes crudos (0-255) |
| `InputStreamReader` | Convierte bytes a caracteres (UTF-8, ASCII, etc.) |
| `BufferedReader` | Lee líneas completas de forma eficiente |

##### **3. Bucle de lectura**

```java
String linea;
while ((linea = reader.readLine()) != null) {
    System.out.println(linea);
}
```

**¿Cómo funciona?**

1. `reader.readLine()` lee una línea completa
2. Asigna el resultado a `linea`
3. Comprueba si es `null` (fin del stream)
4. Si NO es `null`, imprime la línea
5. Repite hasta que `readLine()` devuelva `null`

**¿Por qué `readLine()` devuelve `null`?**
- Cuando el proceso termina de escribir, cierra su stream de salida
- `readLine()` detecta el fin del stream y devuelve `null`

---

#### **PASO 10: Esperar a que termine**

```java
int codigoSalida = proceso.waitFor();
```

**¿Qué hace `waitFor()`?**
- **Bloquea** el hilo actual
- Espera a que el proceso externo termine completamente
- Devuelve el **código de salida** del proceso

**Códigos de salida estándar:**

| Código | Significado | Ejemplo |
|--------|------------|---------|
| `0` | ✅ Éxito | Comando ejecutado correctamente |
| `1` | ❌ Error genérico | Comando falló |
| `2` | ❌ Uso incorrecto | Argumentos inválidos del comando |
| `126` | ❌ No ejecutable | Archivo sin permisos de ejecución |
| `127` | ❌ Comando no encontrado | `ls` no existe en el sistema |
| `130` | ⚠️ Interrumpido | Usuario presionó Ctrl+C |

---

#### **PASO 11: Verificar código de salida**

```java
if (codigoSalida != 0) {
    System.err.println("Advertencia: El comando terminó con código " + codigoSalida);
}
```

**¿Qué hace?**
- Comprueba si el proceso terminó con error
- Si el código NO es 0, muestra una advertencia

**Ejemplo de error:**

```bash
# Directorio sin permisos de lectura
java Ejercicio02 /root

# Salida:
/root/
ls: cannot open directory '/root': Permission denied
⚠️  Advertencia: El comando terminó con código 2
```

---

## 🧪 Casos de Prueba

### ✅ **Caso 1: Directorio válido**

**Comando:**
```bash
java Ejercicio02 C:\Windows\System32
```

**Salida esperada:**
```
C:\Windows\System32/
 El volumen de la unidad C es OS
 El número de serie del volumen es: XXXX-XXXX

 Directorio de C:\Windows\System32

06/10/2025  14:09    <DIR>          .
06/10/2025  14:09    <DIR>          ..
01/06/2025  08:00           123,456 cmd.exe
02/06/2025  09:15            45,678 notepad.exe
...
```

---

### ✅ **Caso 2: Múltiples directorios**

**Comando:**
```bash
java Ejercicio02 C:\Windows C:\Temp C:\Users
```

**Salida esperada:**
```
C:\Windows/
<listado de C:\Windows>

C:\Temp/
<listado de C:\Temp>

C:\Users/
<listado de C:\Users>
```

---

### ❌ **Caso 3: Directorio inexistente**

**Comando:**
```bash
java Ejercicio02 C:\DirectorioQueNoExiste C:\Windows
```

**Salida esperada:**
```
❌ Error: El directorio 'C:\DirectorioQueNoExiste' no existe
   Continuando con el siguiente directorio...

C:\Windows/
<listado de C:\Windows>
```

---

### ❌ **Caso 4: Archivo en lugar de directorio**

**Comando:**
```bash
java Ejercicio02 C:\Windows\System32\cmd.exe C:\Windows
```

**Salida esperada:**
```
❌ Error: 'C:\Windows\System32\cmd.exe' no es un directorio
   Continuando con el siguiente directorio...

C:\Windows/
<listado de C:\Windows>
```

---

### ❌ **Caso 5: Sin argumentos**

**Comando:**
```bash
java Ejercicio02
```

**Salida esperada:**
```
❌ Error: No se proporcionaron directorios como argumentos.
Uso: java Ejercicio02 <directorio1> [directorio2] ...
```

---

### ⚠️ **Caso 6: Directorio sin permisos (Linux)**

**Comando:**
```bash
java Ejercicio02 /root
```

**Salida esperada:**
```
/root/
ls: cannot open directory '/root': Permission denied
⚠️  Advertencia: El comando terminó con código 2
```

---

## ⚠️ Errores Comunes y Soluciones

### **Error 1: `NullPointerException` en `System.getProperty()`**

❌ **Código incorrecto:**
```java
String so = System.getProperty("os name").toLowerCase();
                                      ^^^ ESPACIO en lugar de PUNTO
```

✅ **Solución:**
```java
String so = System.getProperty("os.name").toLowerCase();
                                      ^^^ PUNTO
```

**¿Por qué ocurre?**
- `System.getProperty("os name")` devuelve `null` (no existe esa propiedad)
- Llamar a `.toLowerCase()` sobre `null` causa `NullPointerException`

---

### **Error 2: No capturar la salida del proceso**

❌ **Código incorrecto:**
```java
Process proceso = processBuilder.start();
int codigo = proceso.waitFor(); // ⚠️ Puede bloquearse
```

**Problema:**
- Si el proceso genera mucha salida, el buffer se llena
- El proceso se bloquea esperando que leas la salida
- Tu programa se queda esperando en `waitFor()`

✅ **Solución:**
```java
Process proceso = processBuilder.start();

// SIEMPRE leer la salida antes de waitFor()
try (BufferedReader reader = new BufferedReader(
        new InputStreamReader(proceso.getInputStream()))) {
    String linea;
    while ((linea = reader.readLine()) != null) {
        System.out.println(linea);
    }
}

int codigo = proceso.waitFor();
```

---

### **Error 3: No cerrar el `BufferedReader`**

❌ **Código incorrecto:**
```java
BufferedReader reader = new BufferedReader(
    new InputStreamReader(proceso.getInputStream()));
String linea;
while ((linea = reader.readLine()) != null) {
    System.out.println(linea);
}
// ⚠️ reader nunca se cierra → fuga de recursos
```

✅ **Solución:**
```java
try (BufferedReader reader = new BufferedReader(
        new InputStreamReader(proceso.getInputStream()))) {
    String linea;
    while ((linea = reader.readLine()) != null) {
        System.out.println(linea);
    }
}
// ✅ reader se cierra automáticamente
```

---

### **Error 4: Usar `Exception` genérica**

❌ **Código incorrecto:**
```java
try {
    lanzarComando(ruta);
} catch (Exception e) {
    // Captura TODO, incluso errores inesperados
}
```

✅ **Solución:**
```java
try {
    lanzarComando(ruta);
} catch (IllegalArgumentException e) {
    // Manejo específico para argumentos inválidos
} catch (IOException e) {
    // Manejo específico para errores de E/S
} catch (InterruptedException e) {
    // Manejo específico para interrupciones
}
```

---

### **Error 5: No restaurar el flag de interrupción**

❌ **Código incorrecto:**
```java
catch (InterruptedException e) {
    System.err.println("Proceso interrumpido");
    // ⚠️ No restauramos el flag de interrupción
}
```

✅ **Solución:**
```java
catch (InterruptedException e) {
    System.err.println("Proceso interrumpido");
    Thread.currentThread().interrupt(); // ✅ Restaurar el flag
}
```

**¿Por qué es importante?**
- Cuando capturamos `InterruptedException`, se limpia el flag de interrupción
- Otros componentes del programa podrían necesitar saber que hubo una interrupción
- `Thread.currentThread().interrupt()` restaura el flag

---

## 🎯 Conclusiones

### **¿Qué hemos aprendido?**

1. ✅ **Ejecutar comandos del SO** con `ProcessBuilder`
2. ✅ **Detectar el sistema operativo** en tiempo de ejecución
3. ✅ **Validar argumentos** correctamente
4. ✅ **Manejar excepciones específicas** (buenas prácticas)
5. ✅ **Capturar y procesar** la salida de procesos externos
6. ✅ **Gestionar recursos** con try-with-resources

### **Conceptos clave**

| Concepto | Descripción |
|----------|-------------|
| `ProcessBuilder` | Clase moderna para ejecutar procesos externos |
| `Process` | Representa un proceso en ejecución |
| `File` | Representa rutas del sistema de archivos |
| `BufferedReader` | Lee texto línea por línea eficientemente |
| `try-with-resources` | Gestiona el cierre automático de recursos |
| Excepciones específicas | Manejo granular de errores |

### **Buenas prácticas aplicadas**

- ✅ Validar entrada antes de procesarla
- ✅ Usar excepciones específicas en lugar de genéricas
- ✅ Cerrar recursos automáticamente (try-with-resources)
- ✅ Proporcionar mensajes de error claros
- ✅ Documentar el código con JavaDoc
- ✅ Manejar códigos de salida de procesos
- ✅ Restaurar flags de interrupción

---

## 📚 Referencias

- [Documentación oficial de ProcessBuilder](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/ProcessBuilder.html)
- [Documentación oficial de Process](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Process.html)
- [Documentación oficial de File](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/io/File.html)
- [Try-with-resources Tutorial](https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html)

---

## 📄 Licencia

Este documento es material educativo para la asignatura de **Programación de Servicios y Procesos**.

---

**Autor:** Miguel Ángel Ramírez (marp0604)
**Repositorio:** [PSP_2DAM](https://github.com/marp0604/PSP_2DAM)