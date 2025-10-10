# 📘 Ejercicio 04: Lanzar un Programa Java desde Java

> **Autor:** Miguel Ángel Ramírez Pérez (marp0604)  
> 
> **Módulo:** Programación de Servicios y Procesos (PSP)

---

## 📑 Índice

1. [Enunciado](#enunciado)
2. [Código Completo](#código-completo)
3. [Explicación Paso a Paso](#explicación-paso-a-paso)
4. [Preparación del Entorno](#preparación-del-entorno)
5. [Casos de Prueba](#casos-de-prueba)
6. [Diagrama de Flujo](#diagrama-de-flujo)
7. [Errores Comunes](#errores-comunes)

---

## 🎯 Enunciado

Crear un programa Java que ejecute el programa Java del ejercicio anterior (Ejercicio03).

### **Requisitos:**

1. ✅ Aceptar 2 argumentos: directorio del script y nombre
2. ✅ Ejecutar el `Ejercicio03.java` pasándole esos argumentos
3. ✅ Capturar y mostrar la salida del `Ejercicio03`
4. ✅ Mostrar el código de salida

**Cadena de ejecución:**
```
Ejercicio04 → Ejercicio03 → saluda.sh
```

---

## 💻 Código Completo

```java
package unidad01;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Ejecuta otro programa Java (Ejercicio03) desde Java.
 *
 * Este programa lanza el Ejercicio03 pasándole los argumentos necesarios
 * para ejecutar el script de Bash.
 *
 * @author Miguel Angel Ramirez (marp0604)
 */
public class Ejercicio04 {

    /**
     * Nombre de la clase Java a ejecutar (con paquete completo).
     */
    private static final String CLASE_A_EJECUTAR = "unidad01.Ejercicio03";

    /**
     * Método principal. Requiere 2 argumentos: directorio del script y nombre.
     *
     * @param args [0] = directorio del script, [1] = nombre para el script
     */
    public static void main(String[] args) {
        // Valida el número de argumentos
        if (args.length != 2) {
            System.err.println("Error: Número incorrecto de argumentos.");
            System.exit(1);
        }

        String directorioScript = args[0];
        String nombre = args[1];

        try {
            ejecutarProgramaJava(directorioScript, nombre);
        } catch (IOException e) {
            System.err.println("Error de E/S: " + e.getMessage());
            System.exit(1);
        } catch (InterruptedException e) {
            System.err.println("Proceso interrumpido: " + e.getMessage());
            Thread.currentThread().interrupt();
            System.exit(1);
        }
    }

    /**
     * Ejecuta el programa Java Ejercicio03 con los parámetros dados.
     *
     * Configura el directorio de trabajo, ejecuta el comando java,
     * captura la salida y muestra el código de salida.
     *
     * @param directorioScript Directorio donde está el script de Bash
     * @param nombre Nombre a pasar al script como parámetro
     * @throws IOException Si hay error de E/S al ejecutar el programa
     * @throws InterruptedException Si el proceso es interrumpido
     */
    private static void ejecutarProgramaJava(String directorioScript, String nombre)
            throws IOException, InterruptedException {

        // Muestra información inicial
        System.out.println("Ejecutar Programa Java");
        System.out.println("-------------------------------------");
        System.out.println();

        // Construye el comando para ejecutar el programa Java
        ProcessBuilder processBuilder = new ProcessBuilder(
                "java",                    // Comando Java
                CLASE_A_EJECUTAR,          // Clase a ejecutar (con paquete)
                directorioScript,          // Primer argumento para Ejercicio03
                nombre                     // Segundo argumento para Ejercicio03
        );

        // Determina el directorio de trabajo correcto
        File directorioActual = new File(System.getProperty("user.dir"));
        File directorioSrc = new File(directorioActual, "src");

        // Lógica de selección del directorio de trabajo
        if (directorioActual.getName().equals("src")) {
            // Ya estamos en src/
            processBuilder.directory(directorioActual);
            System.out.println("Directorio de trabajo: " + directorioActual.getAbsolutePath());
        } else if (directorioSrc.exists() && directorioSrc.isDirectory()) {
            // Existe src/ como subdirectorio
            processBuilder.directory(directorioSrc);
            System.out.println("Directorio de trabajo: " + directorioSrc.getAbsolutePath());
        } else {
            // Usa el directorio actual
            processBuilder.directory(directorioActual);
            System.out.println("Directorio de trabajo: " + directorioActual.getAbsolutePath());
        }

        // Combina stdout y stderr en un solo stream
        processBuilder.redirectErrorStream(true);

        // Ejecuta el proceso
        Process proceso = processBuilder.start();

        System.out.println();
        System.out.println("Salida del programa Java:");
        System.out.println("-------------------------------------");

        // Captura y muestra la salida del proceso
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(proceso.getInputStream()))) {

            String linea;
            while ((linea = reader.readLine()) != null) {
                System.out.println(linea);
            }
        }

        System.out.println("-------------------------------------");

        // Espera a que el proceso termine y obtener código de salida
        int codigoSalida = proceso.waitFor();

        // Muestra el resultado final
        System.out.println();
        System.out.println("Resultado:");
        System.out.println("-------------------------------------");

        if (codigoSalida == 0) {
            System.out.println("Programa ejecutado correctamente");
            System.out.println("\tCódigo de salida: " + codigoSalida);
        } else {
            System.out.println("Programa terminó con error");
            System.out.println("\tCódigo de salida: " + codigoSalida);
        }

        System.out.println("-------------------------------------");
    }
}
```

---

## 📖 Explicación Paso a Paso

### **Paso 1: Constante de la clase a ejecutar**

```java
private static final String CLASE_A_EJECUTAR = "unidad01.Ejercicio03";
```

**¿Qué es?**
- Nombre completo de la clase Java a ejecutar (con paquete)
- **NO** incluye la extensión `.java`
- Equivalente a: `java unidad01.Ejercicio03`

**Ejemplos de errores:**
```java
"Ejercicio03.java"           // ❌ Con extensión
"Ejercicio03"                // ❌ Sin paquete
"unidad01.Ejercicio03.java"  // ❌ Con extensión
```

---

### **Paso 2: Validación de argumentos**

```java
if (args.length != 2) {
    System.err.println("Error: Número incorrecto de argumentos.");
    System.exit(1);
}

String directorioScript = args[0];
String nombre = args[1];
```

**¿Qué hace?**
- Verifica que se pasaron exactamente 2 argumentos
- Si no, muestra error y termina con código `1` (error)
- Guarda los argumentos en variables descriptivas

**Flujo de argumentos:**
```
java Ejercicio04 unidad01 Miguel
                 ^^^^^^^^ ^^^^^^
                 args[0]  args[1]
                    ↓        ↓
java unidad01.Ejercicio03 unidad01 Miguel
```

---

### **Paso 3: Manejo de excepciones**

```java
try {
    ejecutarProgramaJava(directorioScript, nombre);
} catch (IOException e) {
    System.err.println("Error de E/S: " + e.getMessage());
    System.exit(1);
} catch (InterruptedException e) {
    System.err.println("Proceso interrumpido: " + e.getMessage());
    Thread.currentThread().interrupt();
    System.exit(1);
}
```

**Tipos de excepciones:**

| Excepción | Cuándo ocurre |
|-----------|---------------|
| `IOException` | Error al iniciar el proceso o leer su salida |
| `InterruptedException` | El proceso es interrumpido (Ctrl+C) |

**¿Por qué `Thread.currentThread().interrupt()`?**
- Restaura el flag de interrupción del hilo
- Buena práctica cuando se captura `InterruptedException`

---

### **Paso 4: Construcción del comando**

```java
ProcessBuilder processBuilder = new ProcessBuilder(
    "java",                    // Comando Java
    CLASE_A_EJECUTAR,          // Clase a ejecutar
    directorioScript,          // Argumento 1 para Ejercicio03
    nombre                     // Argumento 2 para Ejercicio03
);
```

**¿Qué construye?**

Equivale a ejecutar en terminal:
```bash
java unidad01.Ejercicio03 unidad01 Miguel
```

**Desglose:**
- `"java"`: El ejecutable de Java
- `CLASE_A_EJECUTAR`: La clase a ejecutar (con paquete)
- `directorioScript`: Se pasa como `args[0]` al Ejercicio03
- `nombre`: Se pasa como `args[1]` al Ejercicio03

---

### **Paso 5: Configuración del directorio de trabajo**

```java
File directorioActual = new File(System.getProperty("user.dir"));
File directorioSrc = new File(directorioActual, "src");

if (directorioActual.getName().equals("src")) {
    processBuilder.directory(directorioActual);
} else if (directorioSrc.exists() && directorioSrc.isDirectory()) {
    processBuilder.directory(directorioSrc);
} else {
    processBuilder.directory(directorioActual);
}
```

**¿Por qué es importante?**

Java busca las clases desde el directorio actual. La lógica es:

1. **Si ya estamos en `src/`**: Usar ese directorio
2. **Si existe `src/` como subdirectorio**: Usarlo
3. **Si no**: Usar el directorio actual

**Ejemplo:**
```
Sin directory():
  Busca en: /home/marp0604/IdeaProjects/PSP/
  ❌ Error: Could not find or load main class

Con directory(src):
  Busca en: /home/marp0604/IdeaProjects/PSP/src/
  ✅ Encuentra: unidad01/Ejercicio03.class
```

---

### **Paso 6: Combinar streams de salida**

```java
processBuilder.redirectErrorStream(true);
```

**¿Qué hace?**
- Combina stdout (salida normal) y stderr (errores)
- Simplifica la lectura: solo necesitas leer un stream

**Sin esto:**
```java
// Tendrías que leer dos streams
InputStream stdout = proceso.getInputStream();
InputStream stderr = proceso.getErrorStream();
```

---

### **Paso 7: Ejecutar el proceso**

```java
Process proceso = processBuilder.start();
```

**¿Qué ocurre?**
1. El sistema operativo crea un nuevo proceso
2. Ejecuta: `java unidad01.Ejercicio03 unidad01 Miguel`
3. El método devuelve **inmediatamente** (no espera)

---

### **Paso 8: Capturar la salida**

```java
try (BufferedReader reader = new BufferedReader(
        new InputStreamReader(proceso.getInputStream()))) {

    String linea;
    while ((linea = reader.readLine()) != null) {
        System.out.println(linea);
    }
}
```

**¿Qué hace?**
1. `try-with-resources`: Cierra automáticamente el `BufferedReader`
2. Lee línea por línea la salida del `Ejercicio03`
3. Muestra cada línea por consola

**¿Qué captura?**
- Toda la salida del `Ejercicio03`
- Que incluye la salida del script `saluda.sh`

---

### **Paso 9: Esperar y verificar código de salida**

```java
int codigoSalida = proceso.waitFor();

if (codigoSalida == 0) {
    System.out.println("Programa ejecutado correctamente");
    System.out.println("\tCódigo de salida: " + codigoSalida);
} else {
    System.out.println("Programa terminó con error");
    System.out.println("\tCódigo de salida: " + codigoSalida);
}
```

**`waitFor()`:**
- Bloquea el hilo actual
- Espera hasta que el proceso termine
- Devuelve el código de salida

**Códigos de salida:**
- `0` = Éxito
- `1` u otro = Error

---

## 🛠️ Preparación del Entorno

### **1. Estructura de archivos necesaria**

```
PSP/
└── src/
    └── unidad01/
        ├── Ejercicio03.java
        ├── Ejercicio03.class  ← Debe estar compilado
        ├── Ejercicio04.java
        ├── Ejercicio04.class  ← Debe estar compilado
        └── saluda.sh          ← Con permisos de ejecución
```

### **2. Compilar los ejercicios**

```bash
cd ~/IdeaProjects/PSP/src

# Compilar Ejercicio03
javac unidad01/Ejercicio03.java

# Compilar Ejercicio04
javac unidad01/Ejercicio04.java

# Verificar que se crearon los .class
ls -la unidad01/Ejercicio03.class
ls -la unidad01/Ejercicio04.class
```

### **3. Verificar el script**

```bash
# Verificar que existe
ls -la unidad01/saluda.sh

# Verificar permisos de ejecución
# Debe mostrar: -rwxr-xr-x
ls -l unidad01/saluda.sh

# Si no tiene permisos, añadirlos
chmod +x unidad01/saluda.sh

# Probar el script
./unidad01/saluda.sh Miguel
```

---

## 🧪 Casos de Prueba

### ✅ **Caso 1: Ejecución exitosa**

**Ejecutar:**
```bash
cd ~/IdeaProjects/PSP/src
java unidad01.Ejercicio04 unidad01 Miguel
```

**Salida esperada:**
```
Ejecutar Programa Java
-------------------------------------

Directorio de trabajo: /home/marp0604/IdeaProjects/PSP/src

Salida del programa Java:
-------------------------------------
Salida del script: 
¡Hola, Miguel! Este mensaje viene del script de Bash.
La fecha de hoy es: 2025-10-09
---------------------------------------

Resultado de la ejecución:
Script ejecutado correctamente
	Código de salida: 0
---------------------------------------
-------------------------------------

Resultado:
-------------------------------------
Programa ejecutado correctamente
	Código de salida: 0
-------------------------------------
```

---

### ❌ **Caso 2: Sin argumentos**

**Ejecutar:**
```bash
java unidad01.Ejercicio04
```

**Salida esperada:**
```
Error: Número incorrecto de argumentos.
```

**Código de salida:** `1`

---

### ❌ **Caso 3: Solo un argumento**

**Ejecutar:**
```bash
java unidad01.Ejercicio04 unidad01
```

**Salida esperada:**
```
Error: Número incorrecto de argumentos.
```

---

### ❌ **Caso 4: Directorio no existe**

**Ejecutar:**
```bash
java unidad01.Ejercicio04 directorio_inexistente Miguel
```

**Salida esperada:**
```
Ejecutar Programa Java
-------------------------------------

Directorio de trabajo: /home/marp0604/IdeaProjects/PSP/src

Salida del programa Java:
-------------------------------------
Error. El directorio: directorio_inexistente no existe.
-------------------------------------

Resultado:
-------------------------------------
Programa terminó con error
	Código de salida: 1
-------------------------------------
```

---

### ❌ **Caso 5: Ejercicio03 no compilado**

**Preparación:**
```bash
rm src/unidad01/Ejercicio03.class
```

**Ejecutar:**
```bash
java unidad01.Ejercicio04 unidad01 Miguel
```

**Salida esperada:**
```
Ejecutar Programa Java
-------------------------------------

Directorio de trabajo: /home/marp0604/IdeaProjects/PSP/src

Salida del programa Java:
-------------------------------------
Error: Could not find or load main class unidad01.Ejercicio03
Caused by: java.lang.ClassNotFoundException: unidad01.Ejercicio03
-------------------------------------

Resultado:
-------------------------------------
Programa terminó con error
	Código de salida: 1
-------------------------------------
```

**Solución:**
```bash
javac unidad01/Ejercicio03.java
```

---

## 🎯 Diagrama de Flujo

```
┌─────────────────────────────────────────┐
│ Usuario ejecuta:                        │
│ java Ejercicio04 unidad01 Miguel        │
└─────────────┬───────────────────────────┘
              │
              ▼
┌─────────────────────────────────────────┐
│ Ejercicio04 valida argumentos           │
│ ✓ Verifica que sean 2 argumentos        │
└─────────────┬───────────────────────────┘
              │
              ▼
┌─────────────────────────────────────────┐
│ Ejercicio04 construye comando:          │
│ "java unidad01.Ejercicio03 unidad01     │
│  Miguel"                                │
└─────────────┬───────────────────────────┘
              │
              ▼
┌─────────────────────────────────────────┐
│ Configura directorio de trabajo         │
│ → Busca directorio src/                 │
└─────────────┬───────────────────────────┘
              │
              ▼
┌─────────────────────────────────────────┐
│ Ejecuta Ejercicio03 con:                │
│ args[0] = "unidad01"                    │
│ args[1] = "Miguel"                      │
└─────────────┬───────────────────────────┘
              │
              ▼
┌─────────────────────────────────────────┐
│ Ejercicio03 valida y ejecuta script     │
│ → Directorio: unidad01/                 │
│ → Script: saluda.sh                     │
└─────────────┬───────────────────────────┘
              │
              ▼
┌─────────────────────────────────────────┐
│ Script saluda.sh ejecuta:               │
│ "¡Hola, Miguel! ..."                    │
│ Devuelve: exit 0                        │
└─────────────┬───────────────────────────┘
              │
              ▼
┌─────────────────────────────────────────┐
│ Ejercicio03 muestra salida:             │
│ "Script ejecutado correctamente"        │
│ Devuelve: exit 0                        │
└─────────────┬───────────────────────────┘
              │
              ▼
┌─────────────────────────────────────────┐
│ Ejercicio04 captura y muestra todo:     │
│ "Programa ejecutado correctamente"      │
│ Código de salida: 0                     │
└─────────────────────────────────────────┘
```

---

## ⚠️ Errores Comunes

### **Error 1: Usar `.java` en el nombre de clase**

```java
// ❌ INCORRECTO
private static final String CLASE_A_EJECUTAR = "unidad01.Ejercicio03.java";

// ✅ CORRECTO
private static final String CLASE_A_EJECUTAR = "unidad01.Ejercicio03";
```

---

### **Error 2: Olvidar el paquete**

```java
// ❌ INCORRECTO
private static final String CLASE_A_EJECUTAR = "Ejercicio03";

// ✅ CORRECTO
private static final String CLASE_A_EJECUTAR = "unidad01.Ejercicio03";
```

---

### **Error 3: No establecer directorio de trabajo**

```java
// ❌ INCORRECTO
ProcessBuilder pb = new ProcessBuilder("java", "unidad01.Ejercicio03", ...);
// Sin directory()

// ✅ CORRECTO
ProcessBuilder pb = new ProcessBuilder("java", "unidad01.Ejercicio03", ...);
pb.directory(directorioSrc);
```

---

### **Error 4: Pasar argumentos incorrectos**

```bash
# ❌ INCORRECTO (desde src/)
java unidad01.Ejercicio04 src/unidad01 Miguel
# Buscaría: .../src/src/unidad01 (no existe)

# ✅ CORRECTO
java unidad01.Ejercicio04 unidad01 Miguel
# Busca: .../src/unidad01 (existe)
```

---

### **Error 5: No compilar antes de ejecutar**

```bash
# ❌ Orden incorrecto
javac unidad01/Ejercicio04.java
java unidad01.Ejercicio04 unidad01 Miguel
# Error: Ejercicio03 no encontrado

# ✅ Orden correcto
javac unidad01/Ejercicio03.java  # Primero el que se ejecutará
javac unidad01/Ejercicio04.java  # Luego el lanzador
java unidad01.Ejercicio04 unidad01 Miguel
```

---

## 🔑 Conceptos Clave

### **1. Ejecutar Java desde Java**

```java
ProcessBuilder pb = new ProcessBuilder(
    "java",              // Comando
    "paquete.Clase",     // Clase con paquete
    "arg1", "arg2"      // Argumentos
);
```

---

### **2. Importancia del directorio de trabajo**

El directorio de trabajo determina **desde dónde** Java busca las clases:

```
Directorio actual: /home/marp0604/IdeaProjects/PSP/src/
Comando:           java unidad01.Ejercicio03
Busca en:          /home/marp0604/IdeaProjects/PSP/src/unidad01/Ejercicio03.class
```

---

### **3. Propagación de códigos de salida**

Los códigos de salida se propagan desde el nivel más bajo:

```
saluda.sh       → exit 0
    ↓
Ejercicio03     → System.exit(0) implícito
    ↓
Ejercicio04     → captura 0
```

---

### **4. Nombre de clase vs archivo**

| Elemento | Para compilar | Para ejecutar |
|----------|---------------|---------------|
| **Archivo** | `Ejercicio03.java` | - |
| **Clase compilada** | - | `unidad01.Ejercicio03` |
| **Extensión** | `.java` | Sin extensión |

---

## 📊 Comparación con Ejercicio03

| Aspecto | Ejercicio03 | Ejercicio04 |
|---------|-------------|-------------|
| **Ejecuta** | Script Bash | Programa Java |
| **Comando** | Ruta del script | `java` + clase |
| **Argumentos** | Directorio + nombre | Los mismos |
| **Salida** | Del script | Del Ejercicio03 |
| **Directory** | Dir. del script | Dir. `src/` |

---

**Autor:** Miguel Ángel Ramírez Pérez (marp0604)  
**Repositorio:** [PSP_2DAM](https://github.com/marp0604/PSP_2DAM)