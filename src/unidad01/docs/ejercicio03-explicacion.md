# 📘 Ejercicio 03: Ejecutar Script de Bash desde Java

> **Autor:** Miguel Ángel Ramírez Pérez (marp0604)
> 
> **Módulo:** Programación de Servicios y Procesos (PSP)

---

## 📑 Índice

1. [Enunciado](#enunciado)
2. [El Script de Bash](#el-script-de-bash)
3. [Código Completo](#código-completo)
4. [Explicación Paso a Paso](#explicación-paso-a-paso)
5. [Preparación del Entorno](#preparación-del-entorno)
6. [Casos de Prueba](#casos-de-prueba)
7. [Errores Comunes](#errores-comunes)

---

## 🎯 Enunciado

Crea un programa en Java que ejecute un script de Bash llamado `saluda.sh`, pasándole un nombre como parámetro.

### **Requisitos:**

1. ✅ Acepta 2 argumentos: directorio del script y nombre
2. ✅ Verifica que el directorio existe
3. ✅ Verifica que el script existe y tiene permisos de ejecución
4. ✅ Ejecuta el script y captura su salida
5. ✅ Muestra el código de salida

---

## 📜 El Script de Bash

### **Archivo:** `saluda.sh`

```bash
#!/bin/bash

# $1 es el primer parámetro pasado al script

if [ -z "$1" ]; then
    echo "¡Hola! No me has pasado tu nombre."
    exit 1
else
    echo "¡Hola, $1! Este mensaje viene del script de Bash."
    echo "La fecha de hoy es: $(date +%F)"
    exit 0
fi
```

### **¿Qué hace el script?**

- Si **NO** recibe parámetro: muestra error y devuelve código `1`
- Si **SÍ** recibe parámetro: muestra saludo con fecha y devuelve código `0`

**Ejemplo de uso manual:**
```bash
./saluda.sh Miguel
# Salida:
# ¡Hola, Miguel! Este mensaje viene del script de Bash.
# La fecha de hoy es: 2025-01-09
```

---

## 💻 Código Completo

```java
package unidad01.ejercicios;
package unidad01.ejercicios;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Ejecuta un script de Bash desde Java.
 * El script debe llamarse "saluda.sh" y estar en el directorio introducido.
 *
 * @author Miguel Angel Ramirez (marp0604)
 */
public class Ejercicio03 {

    /**
     * Nombre del script de Bash a ejecutar.
     */
    private static final String NOMBRE_SCRIPT = "saluda.sh";

    /**
     * Método principal. Requiere 2 argumentos: directorio del script y nombre.
     *
     * @param args [0] = directorio del script, [1] = nombre para el script
     */
    public static void main(String[] args) {
        if (args.length != 2){
            System.out.println("Error. Número incorrecto de argumentos.");
        }

        String directorioScript = args[0];
        String nombre = args[1];

        try{
            lanzarScript(directorioScript, nombre);
        } catch (IllegalArgumentException e){
            System.err.println("Error. " + e.getMessage());
            System.exit(1);
        } catch (IOException e){
            System.err.println("Error de E/S. " + e.getMessage());
            System.exit(1);
        } catch (InterruptedException e){
            System.err.println("Proceso interrumpido. " + e.getMessage());
            Thread.currentThread().interrupt();
            System.exit(1);
        }
    }

    /**
     * Valída y ejecuta el script de Bash.
     *
     * @param directorioScript Ruta del directorio donde está el script
     * @param nombre Nombre a pasar al script como parámetro
     * @throws IllegalArgumentException Si el directorio o script no son válidos
     * @throws IOException Si hay error de E/S al ejecutar el script
     * @throws InterruptedException Si el proceso es interrumpido
     */
    public static void lanzarScript(String directorioScript, String nombre)
            throws IllegalArgumentException, IOException, InterruptedException {

        // Verifica el directorio
        File directorio = new File(directorioScript);

        if (!directorio.exists()){
            throw new IllegalArgumentException("El directorio: " + directorioScript + " no existe.");
        }

        if (!directorio.isDirectory()){
            throw new IllegalArgumentException(directorioScript + " no es un directorio valido");
        }

        // Construye la ruta del script
        Path pathScript = Paths.get(directorioScript, NOMBRE_SCRIPT);
        File archivoScript = pathScript.toFile();

        // Valida el script
        if (!archivoScript.exists()) {
            throw new IllegalArgumentException("El script " + NOMBRE_SCRIPT + " no existe en el directorio " + directorioScript);
        }

        if (!archivoScript.isFile()){
            throw new IllegalArgumentException(NOMBRE_SCRIPT + " no es un archivo valido");
        }

        if (!archivoScript.canExecute()){
            System.err.println("Advertencia: El script no tiene permisos de ejecución");
            System.err.println("\tIntenta ejecutar: chmod +x " + pathScript.toString());
            throw new IllegalArgumentException("El script " + NOMBRE_SCRIPT + " no tiene permisos de ejecución");
        }

        // Configura y ejecuta ProcessBuilder
        ProcessBuilder processBuilder = new ProcessBuilder(pathScript.toAbsolutePath().toString(), nombre);
        processBuilder.directory(directorio);
        processBuilder.redirectErrorStream(true);

        Process proceso = processBuilder.start();

        // Captura la salida
        System.out.println("Salida del script: ");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(proceso.getInputStream()))){
            String linea;
            while ((linea = reader.readLine()) != null){
                System.out.println(linea);
            }
        }

        System.out.println("---------------------------------------");

        // Espera y muestra el código de salida
        int codigoSalida = proceso.waitFor();

        System.out.println();
        System.out.println("Resultado de la ejecución:");

        if (codigoSalida == 0) {
            System.out.println("Script ejecutado correctamente");
            System.out.println("\tCódigo de salida: " + codigoSalida);
        } else {
            System.out.println("Script terminó con error");
            System.out.println("\tCódigo de salida: " + codigoSalida);
        }

        System.out.println("---------------------------------------");
    }
}
```

---

## 📖 Explicación Paso a Paso

### **Paso 1: Definir la constante**

```java
private static final String NOMBRE_SCRIPT = "saluda.sh";
```

**¿Por qué usar una constante?**
- Si necesitas cambiar el nombre del script, solo modificas un lugar
- Evita errores de tipeo en el código
- Hace el código más mantenible

---

### **Paso 2: Validar argumentos**

```java
if (args.length != 2) {
    System.out.println("Error. Número incorrecto de argumentos.");
    System.exit(1);
}
```

**¿Qué hace?**
- Verifica que se pasaron exactamente 2 argumentos
- Si no, muestra error y termina con código `1` (error)

**⚠️ IMPORTANTE:** `System.exit(1)` es crucial. Sin esto, el programa continuaría y causaría `ArrayIndexOutOfBoundsException` al intentar acceder a `args[0]` y `args[1]`.

---

### **Paso 3: Obtener los argumentos**

```java
String directorioScript = args[0];
String nombre = args[1];
```

**¿Qué hace?**
- `args[0]`: Guarda la ruta del directorio
- `args[1]`: Guarda el nombre a pasar al script

**Ejemplo:**
```bash
java Ejercicio03 /home/marp0604/scripts Miguel
#                ^^^^^^^^^^^^^^^^^^^^^^^ ^^^^^^
#                args[0]                 args[1]
```

---

### **Paso 4: Capturar excepciones**

```java
try {
    lanzarScript(directorioScript, nombre);
} catch (IllegalArgumentException e) {
    System.err.println("Error. " + e.getMessage());
    System.exit(1);
} catch (IOException e) {
    System.err.println("Error de E/S. " + e.getMessage());
    System.exit(1);
} catch (InterruptedException e) {
    System.err.println("Proceso interrumpido. " + e.getMessage());
    Thread.currentThread().interrupt();
    System.exit(1);
}
```

**Tipos de excepciones capturadas:**

| Excepción | Cuándo ocurre | Ejemplo |
|-----------|---------------|---------|
| `IllegalArgumentException` | Directorio o script inválido | Directorio no existe |
| `IOException` | Error de entrada/salida | No se puede leer el script |
| `InterruptedException` | Proceso interrumpido | Usuario presiona Ctrl+C |

**¿Por qué `Thread.currentThread().interrupt()`?**
- Restaura el flag de interrupción del hilo
- Permite que otros componentes sepan que hubo una interrupción

---

### **Paso 5: Validar el directorio**

```java
File directorio = new File(directorioScript);

if (!directorio.exists()) {
    throw new IllegalArgumentException("El directorio: " + directorioScript + " no existe.");
}

if (!directorio.isDirectory()) {
    throw new IllegalArgumentException(directorioScript + " no es un directorio valido");
}
```

**¿Qué valida?**
1. `exists()`: Comprueba que la ruta existe
2. `isDirectory()`: Verifica que es un directorio (no un archivo)

**Ejemplo de error:**
```bash
java Ejercicio03 /directorio/inexistente Miguel
# Error. El directorio: /directorio/inexistente no existe.
```

---

### **Paso 6: Construir ruta del script**

```java
Path pathScript = Paths.get(directorioScript, NOMBRE_SCRIPT);
File archivoScript = pathScript.toFile();
```

**¿Qué hace `Paths.get()`?**
- Combina el directorio y el nombre del script
- Maneja automáticamente los separadores (`/` en Linux, `\` en Windows)
- Más seguro que concatenar strings

**Ejemplo:**
```java
// directorioScript = "/home/marp0604/scripts"
// NOMBRE_SCRIPT = "saluda.sh"
// pathScript = "/home/marp0604/scripts/saluda.sh"
```

---

### **Paso 7: Validar el script**

```java
if (!archivoScript.exists()) {
    throw new IllegalArgumentException("El script " + NOMBRE_SCRIPT + " no existe en el directorio " + directorioScript);
}

if (!archivoScript.isFile()) {
    throw new IllegalArgumentException(NOMBRE_SCRIPT + " no es un archivo valido");
}
```

**¿Qué valida?**
1. `exists()`: El script existe en el directorio
2. `isFile()`: Es un archivo regular (no un directorio)

---

### **Paso 8: Verificar permisos de ejecución**

```java
if (!archivoScript.canExecute()) {
    System.err.println("Advertencia: El script no tiene permisos de ejecución");
    System.err.println("\tIntenta ejecutar: chmod +x " + pathScript.toString());
    throw new IllegalArgumentException("El script " + NOMBRE_SCRIPT + " no tiene permisos de ejecución");
}
```

**¿Por qué es importante?**
- En Linux/macOS, los scripts necesitan el bit de ejecución (`x`)
- `canExecute()` comprueba si el archivo se puede ejecutar
- Si no tiene permisos, muestra el comando para otorgarlos

**Solución:**
```bash
chmod +x /home/marp0604/scripts/saluda.sh
```

---

### **Paso 9: Crear y configurar ProcessBuilder**

```java
ProcessBuilder processBuilder = new ProcessBuilder(pathScript.toAbsolutePath().toString(), nombre);
processBuilder.directory(directorio);
processBuilder.redirectErrorStream(true);
```

**Desglose:**

#### **Línea 1: Constructor**
```java
new ProcessBuilder(pathScript.toAbsolutePath().toString(), nombre);
```
- Primer argumento: Ruta **absoluta** del script
- Segundo argumento: El nombre a pasar al script

**Equivalente en terminal:**
```bash
/home/marp0604/scripts/saluda.sh Miguel
```

#### **Línea 2: Directorio de trabajo**
```java
processBuilder.directory(directorio);
```
- Establece el directorio desde donde se ejecuta
- Es como hacer `cd /home/marp0604/scripts` antes de ejecutar

#### **Línea 3: Combinar streams**
```java
processBuilder.redirectErrorStream(true);
```
- Combina stdout (salida normal) y stderr (errores) en un solo stream
- Simplifica la lectura: solo necesitas leer `getInputStream()`

---

### **Paso 10: Ejecutar el proceso**

```java
Process proceso = processBuilder.start();
```

**¿Qué ocurre?**
1. El sistema operativo crea un nuevo proceso
2. Se ejecuta el script con el nombre como parámetro
3. El método devuelve **inmediatamente** (no espera)

---

### **Paso 11: Capturar y mostrar la salida**

```java
System.out.println("Salida del script: ");

try (BufferedReader reader = new BufferedReader(new InputStreamReader(proceso.getInputStream()))) {
    String linea;
    while ((linea = reader.readLine()) != null) {
        System.out.println(linea);
    }
}

System.out.println("---------------------------------------");
```

**¿Qué hace?**
1. `try-with-resources`: Cierra automáticamente el `BufferedReader`
2. `getInputStream()`: Obtiene la salida del proceso
3. `readLine()`: Lee línea por línea hasta que no hay más (`null`)
4. Muestra cada línea por consola

**⚠️ Orden crítico:**
- SIEMPRE leer la salida **ANTES** de `waitFor()`
- Si el proceso genera mucha salida y no la lees, se puede bloquear

---

### **Paso 12: Esperar y verificar código de salida**

```java
int codigoSalida = proceso.waitFor();

System.out.println();
System.out.println("Resultado de la ejecución:");

if (codigoSalida == 0) {
    System.out.println("Script ejecutado correctamente");
    System.out.println("\tCódigo de salida: " + codigoSalida);
} else {
    System.out.println("Script terminó con error");
    System.out.println("\tCódigo de salida: " + codigoSalida);
}

System.out.println("---------------------------------------");
```

**¿Qué hace `waitFor()`?**
- Bloquea el hilo actual
- Espera hasta que el proceso termine
- Devuelve el código de salida

**Códigos de salida:**
- `0` = Éxito
- `1` u otro = Error

---

## 🛠️ Preparación del Entorno

### **1. Crear el directorio**

```bash
mkdir -p ~/scripts
```

### **2. Crear el script**

```bash
cat > ~/scripts/saluda.sh << 'EOF'
#!/bin/bash

if [ -z "$1" ]; then
    echo "¡Hola! No me has pasado tu nombre."
    exit 1
else
    echo "¡Hola, $1! Este mensaje viene del script de Bash."
    echo "La fecha de hoy es: $(date +%F)"
    exit 0
fi
EOF
```

### **3. Dar permisos**

```bash
chmod +x ~/scripts/saluda.sh
```

### **4. Probar el script**

```bash
~/scripts/saluda.sh Miguel
```

**Salida esperada:**
```
¡Hola, Miguel! Este mensaje viene del script de Bash.
La fecha de hoy es: 2025-01-09
```

---

## 🧪 Casos de Prueba

### ✅ **Caso 1: Ejecución exitosa**

**Compilar:**
```bash
cd ~/IdeaProjects/PSP_2DAM/src
javac unidad01/ejercicios/Ejercicio03.java
```

**Ejecutar:**
```bash
java unidad01.ejercicios.Ejercicio03 /home/marp0604/scripts Miguel
```

**Salida esperada:**
```
Salida del script: 
¡Hola, Miguel! Este mensaje viene del script de Bash.
La fecha de hoy es: 2025-01-09
---------------------------------------

Resultado de la ejecución:
Script ejecutado correctamente
	Código de salida: 0
---------------------------------------
```

---

### ❌ **Caso 2: Sin argumentos**

**Ejecutar:**
```bash
java unidad01.ejercicios.Ejercicio03
```

**Salida esperada:**
```
Error. Número incorrecto de argumentos.
```

---

### ❌ **Caso 3: Solo un argumento**

**Ejecutar:**
```bash
java unidad01.ejercicios.Ejercicio03 /home/marp0604/scripts
```

**Salida esperada:**
```
Error. Número incorrecto de argumentos.
```

---

### ❌ **Caso 4: Directorio no existe**

**Ejecutar:**
```bash
java unidad01.ejercicios.Ejercicio03 /directorio/inexistente Miguel
```

**Salida esperada:**
```
Error. El directorio: /directorio/inexistente no existe.
```

---

### ❌ **Caso 5: Script no existe**

**Ejecutar:**
```bash
java unidad01.ejercicios.Ejercicio03 /home/marp0604 Miguel
```

**Salida esperada:**
```
Error. El script saluda.sh no existe en el directorio /home/marp0604
```

---

### ❌ **Caso 6: Sin permisos de ejecución**

**Preparación:**
```bash
chmod -x ~/scripts/saluda.sh
```

**Ejecutar:**
```bash
java unidad01.ejercicios.Ejercicio03 /home/marp0604/scripts Miguel
```

**Salida esperada:**
```
Advertencia: El script no tiene permisos de ejecución
	Intenta ejecutar: chmod +x /home/marp0604/scripts/saluda.sh
Error. El script saluda.sh no tiene permisos de ejecución
```

**Solución:**
```bash
chmod +x ~/scripts/saluda.sh
```

---

## ⚠️ Errores Comunes

### **Error 1: Olvidar `System.exit(1)` tras validar argumentos**

```java
// ❌ INCORRECTO
if (args.length != 2) {
    System.out.println("Error...");
    // No termina
}
String directorioScript = args[0]; // ArrayIndexOutOfBoundsException

// ✅ CORRECTO
if (args.length != 2) {
    System.out.println("Error...");
    System.exit(1); // Termina aquí
}
String directorioScript = args[0]; // Seguro
```

---

### **Error 2: No restaurar flag de interrupción**

```java
// ❌ INCORRECTO
catch (InterruptedException e) {
    System.err.println("Interrumpido");
}

// ✅ CORRECTO
catch (InterruptedException e) {
    System.err.println("Interrumpido");
    Thread.currentThread().interrupt(); // Restaura
}
```

---

### **Error 3: Usar ruta relativa en lugar de absoluta**

```java
// ⚠️ PUEDE FALLAR
new ProcessBuilder("./saluda.sh", nombre);

// ✅ CORRECTO
new ProcessBuilder(pathScript.toAbsolutePath().toString(), nombre);
```

---

### **Error 4: No leer la salida antes de `waitFor()`**

```java
// ❌ RIESGO DE DEADLOCK
Process proceso = pb.start();
int codigo = proceso.waitFor(); // Puede bloquearse
// Leer salida

// ✅ CORRECTO
Process proceso = pb.start();
// Leer salida PRIMERO
int codigo = proceso.waitFor(); // Seguro
```

---

## 📚 Conceptos Clave

### **1. ProcessBuilder vs Runtime.exec()**

| Aspecto | ProcessBuilder | Runtime.exec() |
|---------|---------------|----------------|
| **Recomendación** | ✅ Usar siempre | ❌ Obsoleto |
| **Seguridad** | ✅ Argumentos separados | ⚠️ String único |
| **Control** | ✅ Muchas opciones | ⚠️ Limitado |
| **Riesgo deadlock** | ✅ Bajo (con buen uso) | ❌ Alto |

---

### **2. Códigos de salida**

```java
0   → Éxito
1   → Error genérico
2   → Uso incorrecto
126 → Sin permisos
127 → Comando no encontrado
```

---

### **3. Paths.get() para rutas**

```java
// ✅ PORTABLE
Path path = Paths.get(dir, archivo);

// ❌ PUEDE FALLAR
String path = dir + "/" + archivo; // Falla en Windows
```

---

**Autor:** Miguel Ángel Ramírez Pérez (marp0604) 
**Repositorio:** [PSP_2DAM](https://github.com/marp0604/PSP_2DAM)