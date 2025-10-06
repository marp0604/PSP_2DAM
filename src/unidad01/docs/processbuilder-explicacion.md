# 📘 Explicación Detallada: ProcessBuilder en Java

> **Autor:** Miguel Ángel Ramírez Pérez (marp0604)  
> 
> **Modulo:** Programación de Servicios y Procesos (PSP)

---

## 📑 Índice

1. [Introducción](#introducción)
2. [¿Por qué NO usar Runtime.exec()?](#por-qué-no-usar-runtimeexec)
3. [ProcessBuilder: La forma correcta](#processbuilder-la-forma-correcta)
4. [Configurar el directorio de trabajo](#configurar-el-directorio-de-trabajo)
5. [Redirigir salida y errores](#redirigir-salida-y-errores)
6. [Iniciar el proceso](#iniciar-el-proceso)
7. [Esperar a que termine](#esperar-a-que-termine)
8. [Ejemplo completo](#ejemplo-completo)
9. [Errores comunes](#errores-comunes)

---

## 🎯 Introducción

`ProcessBuilder` es la clase recomendada en Java para ejecutar procesos externos (comandos del sistema operativo, scripts, otros programas, etc.). Esta guía explica paso a paso cómo usarla correctamente.

---

## ❌ ¿Por qué NO usar Runtime.exec()?

### Código obsoleto (NO RECOMENDADO)

```java
// ❌ FORMA INCORRECTA - NO USAR
Runtime.getRuntime().exec("ls -la /home/usuario");
```

### Problemas de Runtime.exec()

#### 1️⃣ **Parsing deficiente de argumentos**

El sistema no divide correctamente los argumentos, especialmente si hay espacios en las rutas.

```java
// ❌ Esto puede fallar si hay espacios en la ruta
Runtime.getRuntime().exec("ls -la /home/mi usuario/documentos");
```

#### 2️⃣ **Riesgo de Deadlock**

Cuando se ejecuta un proceso externo, este genera dos tipos de salida:

- **stdout** (salida estándar): Información normal del programa
- **stderr** (salida de error): Mensajes de error

Si **NO lees** estos streams, los buffers internos se llenan y el proceso **se bloquea** esperando que los vacíes.

#### 3️⃣ **Problemas de seguridad**

Es más fácil introducir vulnerabilidades de inyección de comandos.

---

## ✅ ProcessBuilder: La forma correcta

### Sintaxis básica

```java
// ✅ FORMA CORRECTA con ProcessBuilder
ProcessBuilder pb = new ProcessBuilder("comando", "arg1", "arg2");
```

### ¿Qué hace?

Crea un objeto `ProcessBuilder` con el comando y sus argumentos **separados por comas**.

---

## 📂 Ejemplos de creación de ProcessBuilder

### Ejemplo 1: Listar archivos en Linux/Mac

```java
ProcessBuilder pb = new ProcessBuilder("ls", "-la", "/home/usuario");
```

### Ejemplo 2: Ejecutar un archivo JAR

```java
ProcessBuilder pb = new ProcessBuilder("java", "-jar", "miAplicacion.jar", "--puerto=8080");
```

### Ejemplo 3: Comando de Windows

```java
ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", "dir", "C:\\Users");
```

---

## 🎁 Ventajas de ProcessBuilder

| Ventaja | Descripción |
|---------|-------------|
| **Separación clara** | Cada argumento es un `String` separado |
| **Más seguro** | No se pueden inyectar comandos accidentalmente |
| **Más claro** | Se ve exactamente qué argumentos se pasan |

---

## 📁 Configurar el directorio de trabajo

### Sintaxis

```java
pb.directory(new File("/ruta/del/directorio"));
```

### ¿Para qué sirve?

Selecciona la carpeta desde donde se ejecutará el proceso. Es equivalente a hacer un `cd` antes de ejecutar el comando.

---

## 💡 Ejemplos de uso de directory()

### Ejemplo 1: Ejecutar git status en un proyecto

```java
// Quiero ejecutar "git status" en mi proyecto
ProcessBuilder pb = new ProcessBuilder("git", "status");
pb.directory(new File("/home/marp0604/mis-proyectos/PSP_2DAM"));

Process proceso = pb.start();
```

> **⚠️ Importante:** Sin `directory()`, el comando se ejecutará desde la carpeta actual de tu aplicación Java, que podría no ser la correcta.

### Ejemplo 2: Ejecutar un script en una ubicación específica

```java
// Ejecutar un script que necesita archivos en una carpeta específica
ProcessBuilder pb = new ProcessBuilder("./script.sh");
pb.directory(new File("/opt/scripts"));
```

---

## 🔀 Redirigir salida y errores

### Sintaxis

```java
pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
pb.redirectError(ProcessBuilder.Redirect.INHERIT);
```

### ¿Qué hace?

Controla **dónde van** los mensajes que genera el proceso externo.

---

## 📊 Opciones de redirección

| Opción | ¿Qué hace? | Uso típico |
|--------|-----------|-----------|
| `INHERIT` | Los mensajes aparecen en la consola de tu aplicación Java | **Debugging**, ver qué hace el proceso |
| `PIPE` | Los mensajes van a un stream que **TÚ debes leer** | Capturar la salida para procesarla |
| `DISCARD` | Ignora los mensajes (se pierden) | No te interesa la salida |
| `to(File)` | Guarda los mensajes en un archivo | **Logging**, guardar resultados |

---

## 📝 Ejemplos de redirección

### Ejemplo 1: Ver la salida en consola (INHERIT)

```java
ProcessBuilder pb = new ProcessBuilder("ping", "-c", "4", "google.com");
pb.redirectOutput(ProcessBuilder.Redirect.INHERIT); // ✅ Ves el ping en tu consola
pb.redirectError(ProcessBuilder.Redirect.INHERIT);

Process proceso = pb.start();
proceso.waitFor();
```

**Salida que verías:**

```
PING google.com (142.250.200.46): 56 data bytes
64 bytes from 142.250.200.46: icmp_seq=0 ttl=117 time=12.5 ms
64 bytes from 142.250.200.46: icmp_seq=1 ttl=117 time=11.8 ms
64 bytes from 142.250.200.46: icmp_seq=2 ttl=117 time=13.2 ms
64 bytes from 142.250.200.46: icmp_seq=3 ttl=117 time=12.8 ms
```

---

### Ejemplo 2: Capturar la salida para procesarla (PIPE)

```java
ProcessBuilder pb = new ProcessBuilder("ls", "-1"); // Lista archivos, uno por línea
pb.redirectOutput(ProcessBuilder.Redirect.PIPE); // 📥 Capturamos la salida

Process proceso = pb.start();

// Leer la salida línea por línea
try (BufferedReader reader = new BufferedReader(
        new InputStreamReader(proceso.getInputStream()))) {
    
    String linea;
    while ((linea = reader.readLine()) != null) {
        System.out.println("Archivo encontrado: " + linea);
    }
}

proceso.waitFor();
```

> **⚠️ IMPORTANTE:** Si usas `PIPE` y **NO lees el stream**, ¡el proceso se bloqueará! Por eso `INHERIT` es más seguro para empezar.

---

### Ejemplo 3: Guardar en un archivo (to)

```java
ProcessBuilder pb = new ProcessBuilder("git", "log", "--oneline");
pb.redirectOutput(ProcessBuilder.Redirect.to(new File("historial.txt")));
pb.redirectError(ProcessBuilder.Redirect.to(new File("errores.txt")));

Process proceso = pb.start();
proceso.waitFor();

System.out.println("Historial guardado en historial.txt");
```

---

## 🚀 Iniciar el proceso

### Sintaxis

```java
Process proceso = pb.start();
```

### ¿Qué ocurre?

1. El **sistema operativo** crea un nuevo proceso
2. Se ejecuta el comando con los argumentos configurados
3. Java te devuelve un objeto `Process` para controlarlo

> **⚠️ Importante:** `start()` **NO espera** a que termine el proceso. Tu programa Java continúa ejecutándose inmediatamente.

---

## ⏳ Esperar a que termine

### Sintaxis

```java
int codigoSalida = proceso.waitFor();
System.out.println("Proceso terminado con código: " + codigoSalida);
```

### ¿Qué hace `waitFor()`?

- **Bloquea** tu hilo de Java hasta que el proceso externo termine
- Devuelve el **código de salida** del proceso

---

## 🔢 Códigos de salida típicos

| Código | Significado |
|--------|------------|
| `0` | ✅ **Éxito** (todo funcionó correctamente) |
| `1` | ❌ Error genérico |
| `2` | ❌ Uso incorrecto del comando |
| `127` | ❌ Comando no encontrado |
| Otros | Depende del programa ejecutado |

### Ejemplo con manejo de códigos de salida

```java
ProcessBuilder pb = new ProcessBuilder("git", "push", "origin", "main");
pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
pb.redirectError(ProcessBuilder.Redirect.INHERIT);

Process proceso = pb.start();
int codigoSalida = proceso.waitFor();

if (codigoSalida == 0) {
    System.out.println("✅ Push exitoso");
} else {
    System.err.println("❌ Error en el push. Código: " + codigoSalida);
}
```

---

## 📦 Ejemplo completo

```java
import java.io.*;

public class EjecutarProcesoCompleto {
    
    public static void main(String[] args) {
        try {
            ejecutarComando();
        } catch (IOException | InterruptedException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void ejecutarComando() throws IOException, InterruptedException {
        // 1. Crear el ProcessBuilder
        ProcessBuilder pb = new ProcessBuilder("ls", "-la", "/home");
        
        // 2. Configurar directorio de trabajo (opcional)
        // pb.directory(new File("/home/marp0604"));
        
        // 3. Configurar redirección de salida
        pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        
        System.out.println("🚀 Ejecutando comando...");
        
        // 4. Iniciar el proceso
        Process proceso = pb.start();
        
        // 5. Esperar a que termine
        int codigoSalida = proceso.waitFor();
        
        // 6. Verificar resultado
        if (codigoSalida == 0) {
            System.out.println("✅ Comando ejecutado correctamente");
        } else {
            System.err.println("❌ Error. Código de salida: " + codigoSalida);
        }
    }
}
```

---

## ⚠️ Errores comunes

### Error 1: Deadlock por no leer los streams

```java
// ❌ MAL - Puede causar deadlock
ProcessBuilder pb = new ProcessBuilder("comando-con-mucha-salida");
pb.redirectOutput(ProcessBuilder.Redirect.PIPE); // Capturas pero NO lees
Process proceso = pb.start();
proceso.waitFor(); // ⚠️ Se puede quedar bloqueado aquí
```

**Solución:**

```java
// ✅ BIEN - Siempre lee el stream si usas PIPE
ProcessBuilder pb = new ProcessBuilder("comando-con-mucha-salida");
pb.redirectOutput(ProcessBuilder.Redirect.PIPE);

Process proceso = pb.start();

// Leer en un hilo separado o inmediatamente
try (BufferedReader reader = new BufferedReader(
        new InputStreamReader(proceso.getInputStream()))) {
    reader.lines().forEach(System.out::println);
}

proceso.waitFor();
```

---

### Error 2: Olvidar manejar excepciones

```java
// ❌ MAL - No maneja excepciones
public void ejecutar() {
    ProcessBuilder pb = new ProcessBuilder("comando");
    Process proceso = pb.start(); // ⚠️ Error de compilación
}
```

**Solución:**

```java
// ✅ BIEN - Siempre maneja IOException e InterruptedException
public void ejecutar() throws IOException, InterruptedException {
    ProcessBuilder pb = new ProcessBuilder("comando");
    Process proceso = pb.start();
    proceso.waitFor();
}

// O con try-catch
public void ejecutarSeguro() {
    try {
        ProcessBuilder pb = new ProcessBuilder("comando");
        Process proceso = pb.start();
        proceso.waitFor();
    } catch (IOException e) {
        System.err.println("Error de E/S: " + e.getMessage());
    } catch (InterruptedException e) {
        System.err.println("Proceso interrumpido");
        Thread.currentThread().interrupt(); // Restaurar el estado de interrupción
    }
}
```

---

## 📚 Resumen rápido

```java
// Plantilla básica de ProcessBuilder

ProcessBuilder pb = new ProcessBuilder("comando", "arg1", "arg2");
pb.directory(new File("/ruta/directorio"));              // Opcional
pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);      // Recomendado
pb.redirectError(ProcessBuilder.Redirect.INHERIT);       // Recomendado

Process proceso = pb.start();
int codigo = proceso.waitFor();

if (codigo == 0) {
    System.out.println("✅ Éxito");
} else {
    System.err.println("❌ Error: " + codigo);
}
```

---

## 🔗 Enlaces útiles

- [Documentación oficial de ProcessBuilder](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/ProcessBuilder.html)
- [Documentación oficial de Process](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Process.html)

---

## 📄 Licencia

Este documento es material educativo para la asignatura de **Programación de Servicios y Procesos**.

---

**Última actualización:** 2025-10-06  
**Repositorio:** [PSP_2DAM](https://github.com/marp0604/PSP_2DAM)