# 📘 Explicación: Productor-Consumidor con BlockingQueue

> **Autor:** Miguel Ángel Ramírez Pérez (@marp0604)
> 
> **Módulo:** Programación de Servicios y Procesos (PSP)

---

## 📑 Índice

1. [¿Qué hace este programa?](#-qué-hace-este-programa)
2. [Estructura del Programa](#-estructura-del-programa)
3. [Análisis Detallado del Código](#-análisis-detallado-del-código)
4. [Ejecución Típica](#-ejecución-típica)
5. [Conceptos Clave](#-conceptos-clave)
6. [Ventajas de BlockingQueue](#-ventajas-de-blockingqueue)
7. [Comparación con wait/notify](#-comparación-con-waitnotify)
8. [Casos de Uso Reales](#-casos-de-uso-reales)

---

## 🎯 ¿Qué hace este programa?

Este programa implementa el **patrón Productor-Consumidor**, uno de los problemas clásicos de programación concurrente. Simula una situación donde:

- Un **Productor** genera datos (números del 0 al 9)
- Un **Consumidor** procesa esos datos
- Ambos trabajan **al mismo tiempo** compartiendo una cola
- La **sincronización es automática** gracias a `BlockingQueue`

### 🎭 Analogía del mundo real

Imagina una **fábrica de pizzas**:
- **Productor** = Chef que hace pizzas
- **Consumidor** = Repartidor que las entrega
- **Cola** = Mostrador donde se colocan las pizzas (capacidad limitada de 5)
- Si el mostrador está lleno → el chef espera
- Si el mostrador está vacío → el repartidor espera

---

## 🏗️ Estructura del Programa

### Diagrama de componentes

```
┌─────────────────────────────────────────────────┐
│                    main()                        │
│  ┌──────────────────────────────────────────┐   │
│  │ BlockingQueue<Integer> (capacidad: 5)   │   │
│  └────────────┬─────────────────┬───────────┘   │
│               │                 │                │
│               ↓                 ↓                │
│         [Productor]       [Consumidor]          │
│          (Hilo 1)          (Hilo 2)             │
└─────────────────────────────────────────────────┘
```

### Componentes principales

1. **`BlockingQueue<Integer>`**: Cola compartida thread-safe
2. **`Productor`**: Clase interna que implementa `Runnable`
3. **`Consumidor`**: Clase interna que implementa `Runnable`
4. **`main()`**: Método que orquesta todo

---

## 🔍 Análisis Detallado del Código

### 1️⃣ La Cola Compartida (`BlockingQueue`)

```java
BlockingQueue<Integer> colaCompartida = new ArrayBlockingQueue<>(5);
```

#### ¿Qué es BlockingQueue?

- Es una interfaz del paquete `java.util.concurrent`
- Representa una cola que bloquea automáticamente a los hilos cuando:
    - Está llena (al intentar añadir)
    - Está vacía (al intentar extraer)

#### ¿Qué es ArrayBlockingQueue?

- Es una implementación de `BlockingQueue`
- Basada en un array de tamaño fijo (no puede crecer)
- Capacidad definida en el constructor: `new ArrayBlockingQueue<>(5)`

#### Características

| Característica | Descripción |
|----------------|-------------|
| **Capacidad** | 5 elementos máximo |
| **Tipo de elementos** | `Integer` (números enteros) |
| **Thread-safe** | ✅ Sí (varios hilos pueden usarla sin problemas) |
| **Bloqueo automático** | ✅ Sí (no necesita `synchronized`, `wait()`, `notify()`) |
| **Orden** | FIFO (First In, First Out) - El primero en entrar es el primero en salir |

#### ¿Por qué capacidad 5?

```java
// Capacidad 5 = Puede almacenar hasta 5 elementos
[  ]  [  ]  [  ]  [  ]  [  ]
  0     1     2     3     4

// Si intentas añadir un 6º elemento → el productor se BLOQUEA
// Si intentas sacar de una cola vacía → el consumidor se BLOQUEA
```

---

### 2️⃣ Clase Productor

```java
public static class Productor implements Runnable {
    private final BlockingQueue<Integer> cola;
    private final int numElementos;

    public Productor(BlockingQueue<Integer> cola, int numElementos) {
        this.cola = cola;
        this.numElementos = numElementos;
    }

    @Override
    public void run() {
        for (int i = 0; i < numElementos; i++) {
            try {
                cola.put(i);
                System.out.println("Productor: Puso " + i + 
                                 ". Tamaño cola: " + cola.size());
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Productor interrumpido.");
            }
        }
        System.out.println("Productor: Finalizó producción.");
    }
}
```

#### 📖 Explicación línea por línea

##### Declaración de la clase

```java
public static class Productor implements Runnable {
```

- `public static class`: Clase interna estática (puede instanciarse sin instancia externa)
- `implements Runnable`: Implementa la interfaz para poder ejecutarse en un hilo

##### Atributos

```java
private final BlockingQueue<Integer> cola;
private final int numElementos;
```

- `cola`: Referencia a la cola compartida (inyectada por constructor)
- `numElementos`: Cuántos elementos producir (10 en nuestro caso)
- `final`: No pueden modificarse después de la construcción

##### Constructor

```java
public Productor(BlockingQueue<Integer> cola, int numElementos) {
    this.cola = cola;
    this.numElementos = numElementos;
}
```

- Recibe la cola compartida y el número de elementos
- **Inyección de dependencias**: El productor no crea la cola, se la pasan

##### Método run() - El corazón del productor

```java
@Override
public void run() {
    for (int i = 0; i < numElementos; i++) {
```

- Se ejecuta cuando el hilo se inicia con `start()`
- Bucle que produce 10 elementos (0 a 9)

##### Añadir a la cola: `cola.put(i)`

```java
try {
    cola.put(i);
```

**¿Qué hace `put()`?**

1. Intenta añadir el elemento `i` a la cola
2. Si la cola está llena (5 elementos):
    - El hilo productor se **BLOQUEA** automáticamente
    - Espera hasta que el consumidor extraiga un elemento
    - Cuando hay espacio, se despierta y añade el elemento

**Flujo visual:**

```
Cola antes de put(3):  [0][1][2][4][5]  ← LLENA (5 elementos)
                           ↓
Productor intenta put(3) → BLOQUEADO (esperando...)
                           ↓
Consumidor hace take() → [1][2][4][5]  ← Ahora hay espacio
                           ↓
Productor se desbloquea → [1][2][4][5][3]  ← put(3) exitoso
```

##### Imprimir información

```java
System.out.println("Productor: Puso " + i + 
                 ". Tamaño cola: " + cola.size());
```

- `.size()`: Devuelve el número actual de elementos en la cola (0-5)
- Ayuda a visualizar el estado de la cola

##### Simular tiempo de producción

```java
Thread.sleep(200);
```

- Pausa el hilo productor durante **200 milisegundos** (0.2 segundos)
- Simula el tiempo que tarda en producir un elemento
- En una aplicación real: procesamiento, cálculos, acceso a BD, etc.

##### Manejo de interrupciones

```java
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
    System.err.println("Productor interrumpido.");
}
```

**¿Cuándo ocurre `InterruptedException`?**

- Si otro hilo llama a `producerThread.interrupt()`
- Durante `put()` o `sleep()`

**¿Qué hacer?**

1. `Thread.currentThread().interrupt()`: Restaura el flag de interrupción
2. Imprimir mensaje de error
3. Salir del método (termina el hilo)

##### Mensaje final

```java
System.out.println("Productor: Finalizó producción.");
```

- Se ejecuta después del bucle
- Indica que el productor terminó de producir todos los elementos

---

### 3️⃣ Clase Consumidor

```java
public static class Consumidor implements Runnable {
    private final BlockingQueue<Integer> cola;
    private final int numElementos;

    public Consumidor(BlockingQueue<Integer> cola, int numElementos) {
        this.cola = cola;
        this.numElementos = numElementos;
    }

    @Override
    public void run() {
        for (int i = 0; i < numElementos; i++) {
            try {
                Integer elemento = cola.take();
                System.out.println("Consumidor: Tomó " + elemento + 
                                 ". Tamaño cola: " + cola.size());
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Consumidor interrumpido.");
            }
        }
        System.out.println("Consumidor: Finalizó consumo.");
    }
}
```

#### 📖 Explicación línea por línea

La estructura es muy similar al Productor, pero con diferencias clave:

##### Extraer de la cola: `cola.take()`

```java
Integer elemento = cola.take();
```

**¿Qué hace `take()`?**

1. Extrae y elimina el primer elemento de la cola (FIFO)
2. Si la cola está vacía:
    - El hilo consumidor se **BLOQUEA** automáticamente
    - Espera hasta que el productor añada un elemento
    - Cuando hay un elemento, se despierta y lo extrae

**Flujo visual:**

```
Cola antes de take():  [  ]  ← VACÍA
                        ↓
Consumidor intenta take() → BLOQUEADO (esperando...)
                        ↓
Productor hace put(7) → [7]  ← Ahora hay elemento
                        ↓
Consumidor se desbloquea → [  ]  ← take() extrae el 7
```

##### Tiempo de procesamiento más lento

```java
Thread.sleep(500);
```

- El consumidor tarda **500ms** (0.5 segundos) en procesar cada elemento
- El productor solo tarda **200ms** (0.2 segundos) en producir

**Consecuencia:**

- El productor es **más rápido** que el consumidor (2.5 veces)
- La cola tiende a **llenarse**
- El productor se bloqueará frecuentemente

---

### 4️⃣ Método main() - Orquestación

```java
public static void main(String[] args) {
    // 1. Crear la cola compartida
    BlockingQueue<Integer> colaCompartida = new ArrayBlockingQueue<>(5);
    
    int totalElementos = 10;
    
    // 2. Crear los hilos
    Thread productorThread = new Thread(
            new Productor(colaCompartida, totalElementos), "Hilo-Productor");
    Thread consumidorThread = new Thread(
            new Consumidor(colaCompartida, totalElementos), "Hilo-Consumidor");
    
    // 3. Iniciar los hilos
    productorThread.start();
    consumidorThread.start();
    
    // 4. Esperar a que terminen
    try {
        productorThread.join();
        consumidorThread.join();
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        System.err.println("Hilo principal interrumpido.");
    }
    
    // 5. Mostrar resultado final
    System.out.println("\n--- Ejecución Finalizada ---");
    System.out.println("Tamaño final de la cola: " + colaCompartida.size());
}
```

#### 📖 Explicación paso a paso

##### Paso 1: Crear la cola compartida

```java
BlockingQueue<Integer> colaCompartida = new ArrayBlockingQueue<>(5);
int totalElementos = 10;
```

- Crea una cola con capacidad de **5 elementos**
- Define que se producirán/consumirán **10 elementos** en total

##### Paso 2: Crear los hilos

```java
Thread productorThread = new Thread(
        new Productor(colaCompartida, totalElementos), "Hilo-Productor");
Thread consumidorThread = new Thread(
        new Consumidor(colaCompartida, totalElementos), "Hilo-Consumidor");
```

Desglose del constructor de `Thread`:

```java
new Thread(Runnable target, String name)
           ↑              ↑               ↑
           │              │               └─ Nombre del hilo
           │              └─────────────────── Instancia de Productor/Consumidor
           └────────────────────────────────── Constructor de Thread
```

Ambos hilos reciben:
- La **misma cola** (referencia compartida)
- El **mismo número de elementos** a procesar

##### Paso 3: Iniciar los hilos

```java
productorThread.start();
consumidorThread.start();
```

**¿Qué ocurre?**

1. Se crean dos hilos nuevos del sistema operativo
2. Cada uno ejecuta su método `run()` concurrentemente
3. El método `main()` continúa inmediatamente (no espera)

**Estado de hilos:**

```
Antes de start():
main → RUNNABLE
productorThread → NEW
consumidorThread → NEW

Después de start():
main → RUNNABLE
productorThread → RUNNABLE (ejecutando run())
consumidorThread → RUNNABLE (ejecutando run())
```

##### Paso 4: Esperar a que terminen con `join()`

```java
try {
    productorThread.join();
    consumidorThread.join();
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
    System.err.println("Hilo principal interrumpido.");
}
```

**¿Qué hace `join()`?**

- Bloquea el hilo actual (`main`) hasta que el hilo especificado termine
- Garantiza que el programa no termine antes que los hilos trabajadores

**Flujo:**

```
main ejecuta productorThread.join()
    ↓
main se BLOQUEA esperando a productorThread
    ↓
productorThread termina su run()
    ↓
main se DESBLOQUEA
    ↓
main ejecuta consumidorThread.join()
    ↓
main se BLOQUEA esperando a consumidorThread
    ↓
consumidorThread termina su run()
    ↓
main se DESBLOQUEA y continúa
```

##### Paso 5: Mostrar resultado final

```java
System.out.println("\n--- Ejecución Finalizada ---");
System.out.println("Tamaño final de la cola: " + colaCompartida.size());
```

- `\n`: Salto de línea para separar visualmente
- `.size()`: Debería ser **0** si todo funcionó correctamente
    - Productor produjo 10 elementos
    - Consumidor consumió 10 elementos
    - Cola vacía = 0 elementos

---

## 🎬 Ejecución Típica

### Salida de consola esperada

```
Productor: Puso 0. Tamaño cola: 1
Productor: Puso 1. Tamaño cola: 2
Consumidor: Tomó 0. Tamaño cola: 1
Productor: Puso 2. Tamaño cola: 2
Productor: Puso 3. Tamaño cola: 3
Productor: Puso 4. Tamaño cola: 4
Consumidor: Tomó 1. Tamaño cola: 3
Productor: Puso 5. Tamaño cola: 4
Productor: Puso 6. Tamaño cola: 5
[Productor BLOQUEADO - cola llena]
Consumidor: Tomó 2. Tamaño cola: 4
[Productor se DESBLOQUEA]
Productor: Puso 7. Tamaño cola: 5
[Productor BLOQUEADO - cola llena]
Consumidor: Tomó 3. Tamaño cola: 4
[Productor se DESBLOQUEA]
Productor: Puso 8. Tamaño cola: 5
[Productor BLOQUEADO - cola llena]
Consumidor: Tomó 4. Tamaño cola: 4
[Productor se DESBLOQUEA]
Productor: Puso 9. Tamaño cola: 5
Productor: Finalizó producción.
Consumidor: Tomó 5. Tamaño cola: 4
Consumidor: Tomó 6. Tamaño cola: 3
Consumidor: Tomó 7. Tamaño cola: 2
Consumidor: Tomó 8. Tamaño cola: 1
Consumidor: Tomó 9. Tamaño cola: 0
Consumidor: Finalizó consumo.

--- Ejecución Finalizada ---
Tamaño final de la cola: 0
```

### Análisis de la ejecución

#### Fase 1: Llenado de la cola (0-6)

| Tiempo | Evento | Estado Cola | Comentario |
|--------|--------|-------------|------------|
| 0ms | Productor pone 0 | `[0]` | |
| 200ms | Productor pone 1 | `[0][1]` | |
| 500ms | Consumidor toma 0 | `[1]` | |
| 400ms | Productor pone 2 | `[1][2]` | |
| 600ms | Productor pone 3 | `[1][2][3]` | |
| 800ms | Productor pone 4 | `[1][2][3][4]` | |
| 1000ms | Consumidor toma 1 | `[2][3][4]` | |
| 1000ms | Productor pone 5 | `[2][3][4][5]` | |
| 1200ms | Productor pone 6 | `[2][3][4][5][6]` | Cola LLENA |

#### Fase 2: Productor bloqueado (7-9)

```
Productor intenta poner 7 → BLOQUEADO (cola llena)
                           ↓
Consumidor toma 2 → hay espacio
                           ↓
Productor se desbloquea → pone 7
                           ↓
Cola llena de nuevo → BLOQUEADO
                           ↓
(Se repite el patrón)
```

#### Fase 3: Productor termina, consumidor vacía

```
Productor pone 9 (último elemento) → Termina
                           ↓
Consumidor continúa extrayendo: 5, 6, 7, 8, 9
                           ↓
Cola vacía [  ] → Consumidor termina
```

---

## 🧠 Conceptos Clave

### ⚡ 1. Bloqueo Automático

#### Productor bloqueado

```
Cola: [0][1][2][3][4]  ← LLENA (5/5)
         ↓
Productor: put(5) → BLOCKED
         ↓
[Espera automática, no consume CPU]
         ↓
Consumidor: take() → [1][2][3][4]  (4/5)
         ↓
Productor: RUNNABLE → put(5) exitoso
```

**Características:**
- ✅ No consume CPU mientras espera
- ✅ Se despierta automáticamente cuando hay espacio
- ✅ No necesitas `synchronized`, `wait()`, ni `notify()`

#### Consumidor bloqueado

```
Cola: [  ]  ← VACÍA (0/5)
  ↓
Consumidor: take() → BLOCKED
  ↓
[Espera automática, no consume CPU]
  ↓
Productor: put(7) → [7]  (1/5)
  ↓
Consumidor: RUNNABLE → take() devuelve 7
```

---

### 🔒 2. Thread-Safety Automático

#### Sin BlockingQueue (código manual):

```java
// ❌ NECESITAS hacer esto manualmente
private List<Integer> cola = new ArrayList<>();

public synchronized void producir(int elemento) {
    while (cola.size() >= 5) {
        try {
            wait(); // Esperar si está llena
        } catch (InterruptedException e) { }
    }
    cola.add(elemento);
    notifyAll(); // Notificar a consumidores
}

public synchronized int consumir() {
    while (cola.isEmpty()) {
        try {
            wait(); // Esperar si está vacía
        } catch (InterruptedException e) { }
    }
    int elemento = cola.remove(0);
    notifyAll(); // Notificar a productores
    return elemento;
}
```

#### Con BlockingQueue:

```java
// ✅ TODO esto se hace automáticamente
cola.put(elemento);   // Bloqueo automático si llena
int elemento = cola.take(); // Bloqueo automático si vacía
```

---

### ⏱️ 3. Timing y Ritmo de Trabajo

#### Análisis de velocidades

```
Productor: 200ms por elemento → 5 elementos/segundo
Consumidor: 500ms por elemento → 2 elementos/segundo

Productor es 2.5x más rápido
↓
La cola tiende a LLENARSE
↓
El productor se bloquea frecuentemente
```

#### ¿Qué pasaría si el consumidor fuera más rápido?

```java
// Productor: 500ms
Thread.sleep(500);

// Consumidor: 200ms
Thread.sleep(200);
```

**Resultado:**
- La cola tiende a **VACIARSE**
- El **CONSUMIDOR** se bloquea frecuentemente
- El productor trabaja sin interrupciones

---

### 📊 4. Estados de la Cola

| Tamaño | Estado | Efecto en Productor | Efecto en Consumidor |
|--------|--------|---------------------|---------------------|
| 0 | Vacía | `put()` exitoso inmediatamente | `take()` se BLOQUEA |
| 1-4 | Parcial | `put()` exitoso inmediatamente | `take()` exitoso inmediatamente |
| 5 | Llena | `put()` se BLOQUEA | `take()` exitoso inmediatamente |

---

## 🆚 Comparación con wait/notify

### Tabla Comparativa

| Aspecto | wait/notify Manual | BlockingQueue |
|---------|-------------------|---------------|
| **Complejidad** | Alta | Baja |
| **Líneas de código** | 30-40 líneas | 2 líneas (`put`/`take`) |
| **Riesgo de errores** | Alto | Bajo |
| **Necesita synchronized** | ✅ Sí | ❌ No |
| **Necesita wait() / notify()** | ✅ Sí | ❌ No |
| **Control de capacidad** | Manual | Automático |
| **Legibilidad** | Baja | Alta |
| **Recomendación** | Solo para aprender | ✅ Usar en producción |

### Ejemplo de código comparado

#### Sin BlockingQueue (Manual)

```java
public class BufferManual {
    private List<Integer> cola = new ArrayList<>();
    private final int CAPACIDAD = 5;
    
    public synchronized void producir(int elemento) {
        while (cola.size() >= CAPACIDAD) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
        cola.add(elemento);
        System.out.println("Producido: " + elemento);
        notifyAll();
    }
    
    public synchronized int consumir() {
        while (cola.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return -1;
            }
        }
        int elemento = cola.remove(0);
        System.out.println("Consumido: " + elemento);
        notifyAll();
        return elemento;
    }
}
```

**Problemas:**
- ❌ Necesitas `synchronized` en todo
- ❌ Fácil olvidar `notifyAll()`
- ❌ Riesgo de `notify()` en lugar de `notifyAll()`
- ❌ Código difícil de mantener
- ❌ Propenso a deadlocks

#### Con BlockingQueue (Automático)

```java
BlockingQueue<Integer> cola = new ArrayBlockingQueue<>(5);

// Productor
cola.put(elemento); // ✅ ¡Ya está!

// Consumidor
int elemento = cola.take(); // ✅ ¡Ya está!
```

**Ventajas:**
- ✅ Thread-safe automáticamente
- ✅ Bloqueo automático
- ✅ Desbloqueo automático
- ✅ Código limpio y claro
- ✅ Sin riesgo de deadlocks

---

## 💡 Ventajas de BlockingQueue

### 1. Simplicidad

```java
// Solo necesitas 2 métodos
put(elemento)  // Añadir
take()         // Extraer
```

### 2. Thread-Safety Garantizado

- ✅ No necesitas `synchronized`
- ✅ No necesitas `wait()` / `notify()`
- ✅ Múltiples productores y consumidores sin problemas

### 3. Múltiples Implementaciones

| Implementación | Característica |
|----------------|----------------|
| `ArrayBlockingQueue` | Tamaño fijo, basado en array |
| `LinkedBlockingQueue` | Tamaño fijo u opcional, basado en nodos |
| `PriorityBlockingQueue` | Ordenada por prioridad |
| `DelayQueue` | Elementos disponibles tras un delay |
| `SynchronousQueue` | Sin capacidad (transferencia directa) |

### 4. Métodos Adicionales

```java
// Operaciones bloqueantes
put(e)           // Añade, bloquea si llena
take()           // Extrae, bloquea si vacía

// Operaciones no bloqueantes (devuelven inmediatamente)
offer(e)         // Añade, false si llena
poll()           // Extrae, null si vacía

// Operaciones con timeout
offer(e, timeout, unit)  // Intenta durante X tiempo
poll(timeout, unit)      // Intenta durante X tiempo

// Inspección
peek()           // Ver el primero sin extraer
size()           // Tamaño actual
remainingCapacity()  // Espacio disponible
```

---

## 🌍 Casos de Uso Reales

### 1. Servidores Web

```java
// Cola de peticiones HTTP
BlockingQueue<HttpRequest> colaPeticiones = new ArrayBlockingQueue<>(100);

// Thread Pool de workers
for (int i = 0; i < 10; i++) {
    new Thread(() -> {
        while (true) {
            HttpRequest request = colaPeticiones.take();
            procesarPeticion(request);
        }
    }).start();
}
```

### 2. Procesamiento de Imágenes

```java
// Cola de imágenes a procesar
BlockingQueue<Image> colaImagenes = new LinkedBlockingQueue<>();

// Productor: Escanea carpeta
new Thread(() -> {
    for (File file : carpeta.listFiles()) {
        Image img = cargarImagen(file);
        colaImagenes.put(img);
    }
}).start();

// Consumidor: Procesa imágenes
new Thread(() -> {
    while (true) {
        Image img = colaImagenes.take();
        redimensionar(img);
        aplicarFiltros(img);
        guardar(img);
    }
}).start();
```

### 3. Sistema de Mensajería

```java
// Cola de mensajes entre módulos
BlockingQueue<Message> colaMensajes = new ArrayBlockingQueue<>(1000);

// Módulo A envía mensajes
colaMensajes.put(new Message("Hola"));

// Módulo B recibe mensajes
Message msg = colaMensajes.take();
procesarMensaje(msg);
```

### 4. Descarga de Archivos

```java
// Cola de URLs a descargar
BlockingQueue<String> colaURLs = new LinkedBlockingQueue<>();

// Productor: Lee archivo con URLs
colaURLs.put("https://ejemplo.com/archivo1.zip");
colaURLs.put("https://ejemplo.com/archivo2.zip");

// Consumidor: Descarga archivos
String url = colaURLs.take();
descargarArchivo(url);
```

---

## 📚 Resumen Final

### Lo que aprendimos

- ✅ **BlockingQueue** = Cola thread-safe con bloqueo automático
- ✅ **`put()`** = Añade elemento (bloquea si llena)
- ✅ **`take()`** = Extrae elemento (bloquea si vacía)
- ✅ No necesita `synchronized`, `wait()`, `notify()`
- ✅ Thread-safe por diseño
- ✅ Fácil de usar y mantener
- ✅ Usado en producción en aplicaciones reales

### Buenas prácticas

```java
// ✅ Usar BlockingQueue para productor-consumidor
BlockingQueue<Integer> cola = new ArrayBlockingQueue<>(10);

// ✅ Manejar interrupciones correctamente
try {
    cola.put(elemento);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
    return;
}

// ✅ Cerrar los hilos correctamente
productorThread.join();
consumidorThread.join();

// ❌ NUNCA hacer esto
while (!cola.isEmpty()) {
    // Consume CPU innecesariamente
}
```

---

## 📚 Documentación

- [Documentación oficial de BlockingQueue](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/BlockingQueue.html)
- [Documentación oficial de ArrayBlockingQueue](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ArrayBlockingQueue.html)
- [Tutorial Oracle: Concurrency](https://docs.oracle.com/javase/tutorial/essential/concurrency/)

---

**Autor:** Miguel Ángel Ramírez Pérez (@marp0604)  
**Repositorio:** PSP_2DAM