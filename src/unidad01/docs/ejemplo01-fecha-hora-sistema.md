# 📘 Ejemplo 01: Obtener Fecha y Hora del Sistema

> **Autor:** Miguel Ángel Ramírez Pérez (marp0604)  
> 
> **Módulo:** Programación de Servicios y Procesos (PSP)

---

## 🎯 Objetivo

Aprender a ejecutar un comando simple del sistema operativo desde Java y capturar su salida utilizando `ProcessBuilder`.

Este ejemplo demuestra:
- ✅ Detección del sistema operativo
- ✅ Ejecución de comandos del SO
- ✅ Uso de `INHERIT` para mostrar salida directamente
- ✅ Manejo de códigos de salida

---

## 📋 ¿Qué hace el programa?

El programa detecta automáticamente el sistema operativo y ejecuta el comando apropiado para mostrar la fecha y hora:

- **Windows:** `date /t` (muestra solo la fecha)
- **Linux/Mac:** `date +%Y-%m-%d %H:%M:%S` (muestra fecha y hora con formato)

---

## 💻 Código Fuente

```java
package unidad01.ejemplos;

import java.io.IOException;

/**
 * Ejemplo 1: Obtener la fecha y hora actual del sistema.
 *
 * @author Miguel Angel Ramirez (marp0604)
 */
public class Ejemplo01_FechaHoraSistema {

    public static void main(String[] args) {
        try {
            obtenerFechaHora();
        } catch (IOException e) {
            System.err.println("Error de E/S: " + e.getMessage());
        } catch (InterruptedException e) {
            System.err.println("Proceso interrumpido: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Obtiene y muestra la fecha y hora del sistema usando comandos del SO.
     * @throws IOException Si hay un error al ejecutar el comando
     * @throws InterruptedException Si el proceso es interrumpido
     */
    private static void obtenerFechaHora() throws IOException, InterruptedException {
        // Detecta el sistema operativo
        String sistemaOperativo = System.getProperty("os.name").toLowerCase();

        // Selecciona el comando según el SO
        ProcessBuilder processBuilder;

        if (sistemaOperativo.contains("win")) {
            // Windows: Comando para mostrar fecha
            processBuilder = new ProcessBuilder("cmd.exe", "/c", "date", "/t");
        } else {
            // Linux/Mac: Comando para mostrar fecha y hora con formato
            processBuilder = new ProcessBuilder("date", "+%Y-%m-%d %H:%M:%S");
        }

        // Configura redirección de salida
        // INHERIT hace que la salida del proceso aparezca en la consola
        processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);

        // Mostrar información
        System.out.println("Obteniendo fecha y hora del sistema...");
        System.out.println("\tSistema operativo detectado: " + sistemaOperativo);
        System.out.println("-------------------------------------------");

        // Ejecuta el proceso
        Process proceso = processBuilder.start();

        // Espera a que termine y obtiene el código de salida
        int codigoSalida = proceso.waitFor();

        // Verifica el resultado
        System.out.println("-------------------------------------------");

        if (codigoSalida == 0) {
            System.out.println("Comando ejecutado correctamente (código: " + codigoSalida + ")");
        } else {
            System.out.println("Error en la ejecución (código: " + codigoSalida + ")");
        }
    }
}
```

---

## 📖 Explicación Paso a Paso

### **PASO 1: Detectar el Sistema Operativo**

```java
String sistemaOperativo = System.getProperty("os.name").toLowerCase();
```

**¿Qué hace?**
- Obtiene el nombre del sistema operativo
- Lo convierte a minúsculas para facilitar la comparación

**Valores típicos:**
- Windows: `"windows 10"`, `"windows 11"`
- Linux: `"linux"`
- macOS: `"mac os x"`

---

### **PASO 2: Preparar el Comando**

```java
ProcessBuilder processBuilder;

if (sistemaOperativo.contains("win")) {
    processBuilder = new ProcessBuilder("cmd.exe", "/c", "date", "/t");
} else {
    processBuilder = new ProcessBuilder("date", "+%Y-%m-%d %H:%M:%S");
}
```

**Windows:**
- `cmd.exe`: Intérprete de comandos de Windows
- `/c`: Ejecuta el comando y termina
- `date /t`: Muestra la fecha actual

**Linux/Mac:**
- `date`: Comando para mostrar fecha/hora
- `+%Y-%m-%d %H:%M:%S`: Formato personalizado (YYYY-MM-DD HH:MM:SS)

---

### **PASO 3: Configurar Redirección**

```java
processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
```

**¿Qué hace `INHERIT`?**
- La salida del proceso aparece **directamente** en tu consola
- No necesita leer manualmente con `BufferedReader`
- Ideal para comandos simples donde solo quieres ver el resultado

**Ventaja:**
- Código más simple
- No hay riesgo de deadlock
- Perfecto para este ejemplo

---

### **PASO 4: Mostrar Información**

```java
System.out.println("Obteniendo fecha y hora del sistema...");
System.out.println("\tSistema operativo detectado: " + sistemaOperativo);
System.out.println("-------------------------------------------");
```

**¿Por qué?**
- Proporciona contexto al usuario
- Ayuda en el debugging
- Hace la salida más legible

---

### **PASO 5: Ejecutar el Proceso**

```java
Process proceso = processBuilder.start();
```

**¿Qué ocurre?**
1. El SO crea un nuevo proceso
2. Se ejecuta el comando configurado
3. El método devuelve **inmediatamente** (no espera)

---

### **PASO 6: Esperar a que Termine**

```java
int codigoSalida = proceso.waitFor();
```

**¿Qué hace `waitFor()`?**
- **Bloquea** el hilo actual
- Espera hasta que el proceso termine
- Devuelve el código de salida

---

### **PASO 7: Verificar el Resultado**

```java
if (codigoSalida == 0) {
    System.out.println("Comando ejecutado correctamente");
} else {
    System.out.println("Error en la ejecución (código: " + codigoSalida + ")");
}
```

**Códigos de salida:**
- `0`: Éxito
- `1` u otro: Error

---

## 🧪 Pruebas

### **Compilar:**

```bash
cd ~/IdeaProjects/PSP_2DAM/src
javac unidad01/ejemplos/Ejemplo01_FechaHoraSistema.java
```

### **Ejecutar:**

```bash
java unidad01.ejemplos.Ejemplo01_FechaHoraSistema
```

---

## 📊 Salida Esperada

### **En Linux/Mac:**

```
📅 Obteniendo fecha y hora del sistema...
🖥️  Sistema operativo detectado: linux
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
2025-10-07 17:48:29
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
✅ Comando ejecutado correctamente (código: 0)
```

### **En Windows:**

```
📅 Obteniendo fecha y hora del sistema...
🖥️  Sistema operativo detectado: windows 10
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
07/10/2025
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
✅ Comando ejecutado correctamente (código: 0)
```

---

## 💡 Conceptos Clave Aprendidos

1. **Detección del SO:**
   ```java
   System.getProperty("os.name")
   ```

2. **ProcessBuilder con argumentos separados:**
   ```java
   new ProcessBuilder("comando", "arg1", "arg2")
   ```

3. **INHERIT para salida directa:**
   ```java
   processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT)
   ```

4. **Esperar y verificar:**
   ```java
   int codigo = proceso.waitFor();
   if (codigo == 0) { /* éxito */ }
   ```

---

## 🔗 Relación con la Teoría

Este ejemplo pone en práctica los conceptos de la sección **1.3 Programación con Procesos en Java**:

- ✅ Uso de `ProcessBuilder` (forma moderna recomendada)
- ✅ Evita `Runtime.exec()` (obsoleto y propenso a errores)
- ✅ Configuración de redirección de streams
- ✅ Manejo correcto de excepciones

---

## 📚 Para Profundizar

- [Documentación oficial de ProcessBuilder](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/ProcessBuilder.html)
- [Explicación completa de ProcessBuilder](processbuilder-explicacion.md)

---

**Autor:** Miguel Ángel Ramírez Pérez (marp0604)
**Repositorio:** [PSP_2DAM](https://github.com/marp0604/PSP_2DAM)