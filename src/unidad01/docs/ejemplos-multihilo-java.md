# 📘 Ejemplos de Programación Multihilo en Java

> **Autor:** Miguel Ángel Ramírez Pérez (marp0604)  
>
> **Módulo:** Programación de Servicios y Procesos (PSP)

---

## 📑 Índice

1. [Introducción](#introducción)
2. [Ejemplo 1: Contador (Clase auxiliar)](#ejemplo-1-contador-clase-auxiliar)
3. [Ejemplo 2: Implementar Runnable](#ejemplo-2-implementar-runnable)
4. [Ejemplo 3: Extender Thread](#ejemplo-3-extender-thread)
5. [Comparación: Runnable vs Thread](#comparación-runnable-vs-thread)
6. [Conceptos Clave](#conceptos-clave)
7. [Ejercicios Propuestos](#ejercicios-propuestos)

---

## 🎯 Introducción

La programación multihilo permite que una aplicación ejecute **varias tareas concurrentemente**. En Java existen dos formas principales de crear hilos:

1. **Implementar la interfaz `Runnable`** ⭐ (Recomendado)
2. **Extender la clase `Thread`**

Estos ejemplos demuestran ambas formas y explican cuándo usar cada una.

---

## 📦 Ejemplo 1: Contador (Clase auxiliar)

### **Archivo:** `Contador.java`

Esta clase será utilizada en ejemplos posteriores para demostrar problemas de sincronización.

```java
package unidad01.ejemplos;

/**
 * Clase simple de contador que incrementa un valor.
 * 
 * ⚠️ ADVERTENCIA: Esta clase NO es thread-safe.
 * Si varios hilos llaman a incrementar() simultáneamente,
 * pueden ocurrir condiciones de carrera.
 * 
 * @author Miguel Angel Ramirez (marp0604)
 * @version 1.0
 * @since 2025-10-08
 */
public class Contador {
    private int valor = 0;

    /**
     * Incrementa el valor del contador en 1.
     * 
     * ⚠️ Esta operación NO es atómica. La expresión valor++
     * se descompone en 3 pasos:
     * 1. Leer el valor actual
     * 2. Sumarle 1
     * 3. Escribir el nuevo valor
     * 
     * Si dos hilos ejecutan estos pasos simultáneamente,
     * pueden perderse incrementos.
     */
    public void incrementar() {
        valor++;
    }
    
    /**
     * Obtiene el valor actual del contador.
     * 
     * @return El valor actual
     */
    public int getValor() {
        return valor;
    }
}
```

### 📖 **Explicación**

#### **¿Para qué sirve?**

Esta clase es un ejemplo simple de una clase que **NO es thread-safe**. Se usa para:
- Demostrar problemas de sincronización
- Entender las condiciones de carrera
- Practicar con soluciones de sincronización

#### **¿Por qué NO es thread-safe?**

La operación `valor++` parece simple, pero **NO es atómica**:

```java
// Lo que escribes:
valor++;

// Lo que realmente ocurre:
int temp = valor;  // PASO 1: Leer
temp = temp + 1;   // PASO 2: Incrementar
valor = temp;      // PASO 3: Escribir
```

**Problema con múltiples hilos:**

| Tiempo | Hilo A | Hilo B | valor |
|--------|--------|--------|-------|
| t0 | Lee valor (0) | - | 0 |
| t1 | - | Lee valor (0) | 0 |
| t2 | Incrementa (0+1) | - | 0 |
| t3 | - | Incrementa (0+1) | 0 |
| t4 | Escribe 1 | - | 1 |
| t5 | - | Escribe 1 | 1 |

**Resultado:** El contador vale 1, pero debería valer 2 (se perdió un incremento).

#### **Usos de esta clase:**

```java
// Ejemplo de uso (veremos en unidades posteriores cómo hacerlo thread-safe)
Contador contador = new Contador();

// Crear múltiples hilos que incrementan el contador
Thread hilo1 = new Thread(() -> {
    for (int i = 0; i < 1000; i++) {
        contador.incrementar();
    }
});

Thread hilo2 = new Thread(() -> {
    for (int i = 0; i < 1000; i++) {
        contador.incrementar();
    }
});

hilo1.start();
hilo2.start();
hilo1.join();
hilo2.join();

// Esperado: 2000
// Real: Probablemente menos de 2000 (por condiciones de carrera)
System.out.println("Valor final: " + contador.getValor());
```

---

## 🔹 Ejemplo 2: Implementar Runnable

### **Archivo:** `Ej02_MiTarea.java`

Esta es la **forma recomendada** de crear hilos en Java.

```java
package unidad01.ejemplos;

/**
 * Ejemplo de creación de hilos implementando la interfaz Runnable.
 * 
 * Esta es la forma RECOMENDADA porque:
 * - Permite que la clase herede de otra clase
 * - Separa la tarea (Runnable) del mecanismo de ejecución (Thread)
 * - Es más flexible y reutilizable
 * - Sigue el principio de composición sobre herencia
 * 
 * @author Miguel Angel Ramirez (marp0604)
 * @version 1.0
 * @since 2025-10-08
 */
public class Ej02_MiTarea implements Runnable {
    
    /**
     * Método que contiene el código que se ejecutará en el hilo.
     * 
     * Este método se ejecutará cuando se inicie el hilo con start().
     * NUNCA llames a run() directamente, siempre usa start().
     */
    @Override
    public void run() {
        // Código que se ejecutará en el hilo
        System.out.println("Hilo ejecutándose: " + Thread.currentThread().getName());
    }

    /**
     * Método principal para probar la clase.
     */
    public static void main(String[] args) {
        // Crear una instancia de la tarea
        Ej02_MiTarea tarea = new Ej02_MiTarea();
        
        // Crear un hilo pasándole la tarea
        Thread hilo = new Thread(tarea);
        
        // Iniciar el hilo (esto llamará al método run() en un nuevo hilo)
        hilo.start();
        
        // El programa principal continúa ejecutándose
        System.out.println("Hilo principal: " + Thread.currentThread().getName());
    }
}
```

### 📖 **Explicación Detallada**

#### **Paso 1: Implementar la interfaz `Runnable`**

```java
public class Ej02_MiTarea implements Runnable {
```

**¿Qué es `Runnable`?**
- Es una **interfaz funcional** con un solo método: `run()`
- Representa una tarea que puede ejecutarse en un hilo
- Es el contrato que dice: "Esta clase tiene código ejecutable"

**Definición de la interfaz:**
```java
@FunctionalInterface
public interface Runnable {
    void run();
}
```

---

#### **Paso 2: Implementar el método `run()`**

```java
@Override
public void run() {
    System.out.println("Hilo ejecutándose: " + Thread.currentThread().getName());
}
```

**¿Qué hace?**
- Contiene el código que se ejecutará en el hilo
- Se llama automáticamente cuando se inicia el hilo con `start()`

**`Thread.currentThread()`:**
- Método estático que devuelve el hilo actual
- `.getName()` obtiene el nombre del hilo (ej: "Thread-0", "Thread-1")

---

#### **Paso 3: Crear el hilo**

```java
Ej02_MiTarea tarea = new Ej02_MiTarea();
Thread hilo = new Thread(tarea);
```

**¿Qué ocurre?**
1. Creas una instancia de tu clase que implementa `Runnable`
2. Pasas esa instancia al constructor de `Thread`
3. El hilo sabe que debe ejecutar el método `run()` de esa tarea

---

#### **Paso 4: Iniciar el hilo**

```java
hilo.start();
```

**¿Qué hace `start()`?**
1. Crea un **nuevo hilo del sistema operativo**
2. Programa ese hilo para ejecutar el método `run()`
3. Devuelve **inmediatamente** (no espera a que termine)

**⚠️ IMPORTANTE: NUNCA llames a `run()` directamente**

```java
// ❌ INCORRECTO - Ejecuta en el hilo actual (no crea hilo nuevo)
hilo.run();

// ✅ CORRECTO - Crea un nuevo hilo y ejecuta ahí
hilo.start();
```

---

### 📊 **Salida Esperada**

```
Hilo principal: main
Hilo ejecutándose: Thread-0
```

O también podría ser:

```
Hilo ejecutándose: Thread-0
Hilo principal: main
```

**¿Por qué el orden puede variar?**
- Los hilos se ejecutan **concurrentemente**
- El planificador del SO decide el orden
- No hay garantías de orden sin sincronización

---

### 💡 **Ventajas de Implementar Runnable**

#### **1. Permite herencia**

```java
// ✅ FUNCIONA - Puedes heredar de otra clase
public class Ej02_MiTarea extends ClaseBase implements Runnable {
    @Override
    public void run() {
        // Código del hilo
    }
}
```

#### **2. Reutilización**

```java
// Puedes usar la misma tarea en múltiples hilos
Ej02_MiTarea tarea = new Ej02_MiTarea();

Thread hilo1 = new Thread(tarea);
Thread hilo2 = new Thread(tarea);
Thread hilo3 = new Thread(tarea);

hilo1.start();
hilo2.start();
hilo3.start();
```

#### **3. Separación de responsabilidades**

- `Runnable`: Define QUÉ hacer (la tarea)
- `Thread`: Define CÓMO ejecutarlo (el mecanismo)

---

### 🎨 **Ejemplo Mejorado con Parámetros**

```java
public class Ej02_MiTareaConNombre implements Runnable {
    private String nombre;
    
    public Ej02_MiTareaConNombre(String nombre) {
        this.nombre = nombre;
    }
    
    @Override
    public void run() {
        for (int i = 1; i <= 5; i++) {
            System.out.println(nombre + " - Iteración " + i);
            try {
                Thread.sleep(500); // Pausa de 500ms
            } catch (InterruptedException e) {
                System.err.println(nombre + " interrumpido");
                return;
            }
        }
        System.out.println(nombre + " ha terminado ✅");
    }
    
    public static void main(String[] args) {
        Thread hilo1 = new Thread(new Ej02_MiTareaConNombre("Tarea-A"));
        Thread hilo2 = new Thread(new Ej02_MiTareaConNombre("Tarea-B"));
        
        hilo1.start();
        hilo2.start();
    }
}
```

**Salida:**
```
Tarea-A - Iteración 1
Tarea-B - Iteración 1
Tarea-A - Iteración 2
Tarea-B - Iteración 2
Tarea-A - Iteración 3
Tarea-B - Iteración 3
Tarea-A - Iteración 4
Tarea-B - Iteración 4
Tarea-A - Iteración 5
Tarea-B - Iteración 5
Tarea-A ha terminado ✅
Tarea-B ha terminado ✅
```

---

## 🔸 Ejemplo 3: Extender Thread

### **Archivo:** `Ej03_MiHilo.java`

Esta es la forma alternativa (menos recomendada) de crear hilos.

```java
package unidad01.ejemplos;

/**
 * Ejemplo de creación de hilos extendiendo la clase Thread.
 * 
 * Esta forma es MENOS RECOMENDADA porque:
 * - No permite heredar de otra clase (Java no tiene herencia múltiple)
 * - Mezcla la tarea con el mecanismo de ejecución
 * - Menos flexible
 * 
 * Sin embargo, tiene ventajas:
 * - Código más directo (no necesitas crear un Thread aparte)
 * - Acceso directo a métodos de Thread (getName(), sleep(), etc.)
 * 
 * @author Miguel Angel Ramirez (marp0604)
 * @version 1.0
 * @since 2025-10-08
 */
public class Ej03_MiHilo extends Thread {
    
    /**
     * Método que contiene el código que se ejecutará en el hilo.
     * 
     * Sobrescribe el método run() de la clase Thread.
     */
    @Override
    public void run() {
        // Código que se ejecutará en el hilo
        System.out.println("Hilo ejecutándose: " + getName());
        // getName() es un método de Thread, accesible directamente
    }

    /**
     * Método principal para probar la clase.
     */
    public static void main(String[] args) {
        // Crear una instancia de nuestro hilo
        Ej03_MiHilo hilo = new Ej03_MiHilo();
        
        // Iniciar el hilo
        hilo.start();
        
        // El programa principal continúa ejecutándose
        System.out.println("Hilo principal: " + Thread.currentThread().getName());
    }
}
```

### 📖 **Explicación Detallada**

#### **Paso 1: Extender la clase `Thread`**

```java
public class Ej03_MiHilo extends Thread {
```

**¿Qué significa?**
- Tu clase **ES un Thread** (herencia directa)
- Heredas todos los métodos de `Thread`
- No puedes heredar de otra clase

---

#### **Paso 2: Sobrescribir `run()`**

```java
@Override
public void run() {
    System.out.println("Hilo ejecutándose: " + getName());
}
```

**Diferencias con Runnable:**
- `getName()` se llama directamente (no necesitas `Thread.currentThread()`)
- Tienes acceso a todos los métodos de `Thread`: `sleep()`, `interrupt()`, etc.

---

#### **Paso 3: Crear e iniciar**

```java
Ej03_MiHilo hilo = new Ej03_MiHilo();
hilo.start();
```

**Más directo:**
- No necesitas crear un objeto `Thread` separado
- El objeto **es** el hilo

---

### 📊 **Salida Esperada**

```
Hilo principal: main
Hilo ejecutándose: Thread-0
```

O:

```
Hilo ejecutándose: Thread-0
Hilo principal: main
```

---

### 💡 **Ventajas de Extender Thread**

#### **1. Código más directo**

```java
// Con Thread
Ej03_MiHilo hilo = new Ej03_MiHilo();
hilo.start();

// Con Runnable (requiere un paso extra)
Ej02_MiTarea tarea = new Ej02_MiTarea();
Thread hilo = new Thread(tarea);
hilo.start();
```

#### **2. Acceso directo a métodos de Thread**

```java
public class Ej03_MiHilo extends Thread {
    @Override
    public void run() {
        // Acceso directo a métodos de Thread
        System.out.println("Mi nombre: " + getName());
        
        try {
            sleep(1000); // No necesitas Thread.sleep()
        } catch (InterruptedException e) {
            System.err.println("Interrumpido");
        }
        
        System.out.println("Mi prioridad: " + getPriority());
    }
}
```

---

### ⚠️ **Desventajas de Extender Thread**

#### **1. No permite herencia múltiple**

```java
// ❌ ERROR DE COMPILACIÓN
public class Ej03_MiHilo extends OtraClase extends Thread {
    // Java no permite extender dos clases
}
```

#### **2. Menos flexible**

```java
// Con Runnable, puedes pasar la misma tarea a diferentes hilos
Runnable tarea = new Ej02_MiTarea();
Thread hilo1 = new Thread(tarea);
Thread hilo2 = new Thread(tarea);

// Con Thread, cada objeto es un hilo independiente
Ej03_MiHilo hilo1 = new Ej03_MiHilo(); // Hilo 1
Ej03_MiHilo hilo2 = new Ej03_MiHilo(); // Hilo 2 (diferente objeto)
```

---

### 🎨 **Ejemplo Mejorado con Constructor**

```java
public class Ej03_MiHiloConNombre extends Thread {
    
    public Ej03_MiHiloConNombre(String nombre) {
        super(nombre); // Llama al constructor de Thread con el nombre
    }
    
    @Override
    public void run() {
        for (int i = 1; i <= 5; i++) {
            System.out.println(getName() + " - Iteración " + i);
            try {
                sleep(500); // Acceso directo (no Thread.sleep())
            } catch (InterruptedException e) {
                System.err.println(getName() + " interrumpido");
                return;
            }
        }
        System.out.println(getName() + " ha terminado ✅");
    }
    
    public static void main(String[] args) {
        Ej03_MiHiloConNombre hilo1 = new Ej03_MiHiloConNombre("Hilo-A");
        Ej03_MiHiloConNombre hilo2 = new Ej03_MiHiloConNombre("Hilo-B");
        
        hilo1.start();
        hilo2.start();
    }
}
```

**Salida:**
```
Hilo-A - Iteración 1
Hilo-B - Iteración 1
Hilo-A - Iteración 2
Hilo-B - Iteración 2
Hilo-A - Iteración 3
Hilo-B - Iteración 3
Hilo-A - Iteración 4
Hilo-B - Iteración 4
Hilo-A - Iteración 5
Hilo-B - Iteración 5
Hilo-A ha terminado ✅
Hilo-B ha terminado ✅
```

---

## ⚖️ Comparación: Runnable vs Thread

### **Tabla Comparativa**

| Aspecto | **Runnable** (Ej02) | **Thread** (Ej03) |
|---------|---------------------|-------------------|
| **Herencia** | ✅ Puedes heredar de otra clase | ❌ No puedes heredar (ya heredas de Thread) |
| **Flexibilidad** | ✅ Separa tarea de ejecución | ⚠️ Mezcla tarea y ejecución |
| **Reutilización** | ✅ Misma tarea en múltiples hilos | ⚠️ Cada objeto es un hilo |
| **Código** | ⚠️ Requiere crear Thread | ✅ Más directo |
| **Acceso a métodos de Thread** | ⚠️ Usa `Thread.currentThread()` | ✅ Acceso directo |
| **Buenas prácticas** | ✅ **Recomendado** | ⚠️ Usar solo si no heredas |

---

### **Ejemplo Comparativo Lado a Lado**

```java
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
// OPCIÓN 1: Implementar Runnable (Recomendado)
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
public class MiTarea implements Runnable {
    @Override
    public void run() {
        System.out.println("Hilo: " + Thread.currentThread().getName());
    }
}

// Uso:
Thread hilo = new Thread(new MiTarea());
hilo.start();

// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
// OPCIÓN 2: Extender Thread
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
public class MiHilo extends Thread {
    @Override
    public void run() {
        System.out.println("Hilo: " + getName());
    }
}

// Uso:
MiHilo hilo = new MiHilo();
hilo.start();
```

---

### **¿Cuándo usar cada una?**

#### **Usa Runnable si:**
- ✅ Tu clase necesita heredar de otra clase
- ✅ Quieres separar la lógica de la tarea del mecanismo de hilos
- ✅ Necesitas reutilizar la misma tarea en múltiples hilos
- ✅ Sigues el principio de "composición sobre herencia"

#### **Usa Thread si:**
- ✅ No necesitas heredar de otra clase
- ✅ Quieres código más directo y simple
- ✅ Necesitas acceso frecuente a métodos de Thread

---

## 🧠 Conceptos Clave

### **1. Método `run()` vs `start()`**

```java
// ❌ INCORRECTO - No crea un hilo nuevo
hilo.run(); // Se ejecuta en el hilo actual (main)

// ✅ CORRECTO - Crea un hilo nuevo
hilo.start(); // Se ejecuta en un hilo nuevo
```

**Diferencia:**
- `run()`: Ejecuta el código en el **hilo actual** (como cualquier método)
- `start()`: Crea un **nuevo hilo** y ejecuta `run()` en él

---

### **2. Ciclo de vida de un hilo**

```
       NEW
        ↓
    [start()]
        ↓
     RUNNABLE ←→ BLOCKED/WAITING
        ↓
   [run() termina]
        ↓
    TERMINATED
```

---

### **3. Operaciones NO atómicas**

```java
// ⚠️ Esta operación NO es atómica
valor++;

// Se descompone en:
int temp = valor;   // 1. Leer
temp = temp + 1;    // 2. Incrementar
valor = temp;       // 3. Escribir

// Si dos hilos ejecutan esto, pueden interferir entre sí
```

---

### **4. Nombres de hilos**

```java
// Nombre por defecto
Thread hilo1 = new Thread(new MiTarea());
// Nombre: "Thread-0", "Thread-1", etc.

// Nombre personalizado
Thread hilo2 = new Thread(new MiTarea(), "Mi-Hilo-Custom");
// Nombre: "Mi-Hilo-Custom"

// Obtener nombre
String nombre = Thread.currentThread().getName();
```

---

## 🧪 Ejercicios Propuestos

### **Ejercicio 1: Contador con Runnable**

Crea una clase que implemente `Runnable` y que cuente del 1 al 10 con pausas de 500ms entre cada número.

**Pista:**
```java
Thread.sleep(500); // Pausa de 500ms
```

---

### **Ejercicio 2: Comparación de tiempos**

Crea dos programas que calculen la suma de los primeros 1,000,000 números:
1. Uno sin hilos (secuencial)
2. Otro con 2 hilos (cada uno suma 500,000 números)

Compara los tiempos de ejecución.

---

### **Ejercicio 3: Problema del Contador**

Usa la clase `Contador` y crea 10 hilos que incrementen el contador 1000 veces cada uno.

**Preguntas:**
1. ¿Cuál es el valor esperado final?
2. ¿Cuál es el valor real obtenido?
3. ¿Por qué no coinciden?

---

## 📚 Para Profundizar

- [Documentación oficial de Thread](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Thread.html)
- [Documentación oficial de Runnable](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Runnable.html)
- [Java Concurrency Tutorial](https://docs.oracle.com/javase/tutorial/essential/concurrency/)

---

## ✅ Checklist de Comprensión

Después de estudiar estos ejemplos, deberías poder:

- [ ] Explicar la diferencia entre `Runnable` y `Thread`
- [ ] Crear hilos usando ambos métodos
- [ ] Entender por qué `Runnable` es preferible
- [ ] Usar correctamente `start()` en lugar de `run()`
- [ ] Explicar qué es una operación no atómica
- [ ] Identificar condiciones de carrera potenciales
- [ ] Obtener el nombre del hilo actual

---

**Autor:** Miguel Ángel Ramírez Pérez (marp0604)  
**Repositorio:** [PSP_2DAM](https://github.com/marp0604/PSP_2DAM)