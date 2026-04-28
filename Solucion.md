# Solucion Punto 1
## Videoclub de Don Mario — Análisis de Diseño

## 1. Patrones de diseño aplicados

###  Strategy Pattern (calculo de descuentos por membresia)
- **Ubicación**: paquete `com.videoclub.membership`.
- **Por qué**: cada membresía calcula su descuento de forma distinta. En lugar de un `if/else` gigante (`if (tipo.equals("Premium"))...`), se define la interfaz `Membresia` y cada estrategia concreta (`MembresiaBasica`, `MembresiaPremium`) implementa su propio `getPorcentajeDescuento()`. Agregar una membresía VIP (30%) es solo crear una clase nueva — no se modifica nada existente.

###  Factory Method (creacion de membresias)
- **Ubicación**: `MembresiaFactory`.
- **Por qué**: el `Main` no sabe ni le importa qué clase concreta corresponde a `"Premium"`. Le pide al factory que la cree. La lógica de mapeo `String → Membresia` queda en un solo lugar.

###  (Implícito) Repository / Catalog
- **Ubicación**: `CatalogoPeliculas`.
- Encapsula el almacenamiento y la búsqueda de películas, ocultando que internamente usa una `List`.

---

## 2. Principios SOLID aplicados

| Principio | Aplicación en el código |
|-----------|------------------------|
| **S — Single Responsibility** | Cada clase tiene una sola razón para cambiar: `CatalogoPeliculas` gestiona películas, `ServicioAlquiler` orquesta el alquiler, `ImpresorRecibo` solo presenta, `Recibo` solo guarda datos. |
| **O — Open/Closed** | El sistema está **abierto a extensión, cerrado a modificación**. Agregar `MembresiaVIP` o `PeliculaStreaming4K` no requiere tocar `ServicioAlquiler` ni `Main` — solo crear una clase nueva. |
| **L — Liskov Substitution** | Cualquier `Pelicula` (Física o Digital) puede usarse donde se espere `Pelicula` sin romper el comportamiento. Lo mismo para `Membresia`. |
| **I — Interface Segregation** | La interfaz `Membresia` es pequeña y enfocada (nombre + porcentaje). No se obliga a las membresías a implementar métodos que no usan. |
| **D — Dependency Inversion** | `ServicioAlquiler` depende de la **abstracción** `Membresia`, no de `MembresiaPremium` o `MembresiaBasica`. Las clases de alto nivel no dependen de detalles de bajo nivel. |

---

## 3. Polimorfismo y encapsulamiento

### Polimorfismo
- `Pelicula` es **abstracta** y obliga a subclases a implementar `getTipo()`. `PeliculaFisica` retorna `"Fisica"`, `PeliculaDigital` retorna `"Digital"`. El `ImpresorRecibo` llama `pelicula.getTipo()` sin preguntar de qué clase es — Java decide en runtime.
- `Membresia.calcularDescuento()` se comporta distinto según la implementación concreta, sin que el `ServicioAlquiler` lo sepa.

### Encapsulamiento
- Atributos de `Pelicula` son `private final` con getters. El estado `disponible` solo se cambia mediante `marcarComoAlquilada()` (no hay setter público que permita inconsistencias).
- `CatalogoPeliculas.listar()` retorna una lista **inmodificable** (`Collections.unmodifiableList`) para que nadie de afuera pueda corromper el catálogo.
- `Recibo` es inmutable: una vez creado, sus valores no cambian.

---

## 4. Estructura del proyecto

```
src/main/java/com/videoclub/
├── model/           → Pelicula (abstracta), PeliculaFisica, PeliculaDigital
├── membership/      → Membresia, MembresiaBasica, MembresiaPremium, MembresiaFactory
├── catalog/         → CatalogoPeliculas
├── rental/          → ItemAlquiler, Recibo, ServicioAlquiler
└── ui/              → Main, ImpresorRecibo
```

---

## 5. Cómo ejecutar

```bash
javac -d out $(find src -name "*.java")
java -cp out com.videoclub.ui.Main
```

---

## 6. Evidencia de ejecución

### Caso 1 — Cliente Premium, películas 1 y 3 (caso del enunciado)

```
=== VIDEOCLUB DE DON MARIO ===

Peliculas Disponibles:
 1. [Fisica] Interestellar - $8,000 - Disponible
 2. [Fisica] El Padrino - $7,000 - No disponible
 3. [Digital] Inception - $5,000 - Disponible
 4. [Digital] Matrix - $6,000 - Disponible

Membresia del cliente (Basica/Premium): Premium
Seleccione peliculas (numeros separados por coma): 1,3

--- RECIBO DE ALQUILER ---
Cliente: Premium
Peliculas:
 - Interestellar (Fisica) - $8.000
 - Inception (Digital) - $5.000
Subtotal: $13.000
Descuento (20%): $2.600
Total a pagar: $10.400
--------------------------
¡Disfrute su pelicula!
```
 Coincide exactamente con el caso de ejemplo del enunciado.

### Caso 2 — Cliente Básico, películas 1 y 4

```
--- RECIBO DE ALQUILER ---
Cliente: Basica
Peliculas:
 - Interestellar (Fisica) - $8.000
 - Matrix (Digital) - $6.000
Subtotal: $14.000
Descuento (0%): $0
Total a pagar: $14.000
--------------------------
```

### Caso 3 — Validación de película no disponible

Al intentar alquilar "El Padrino" (no disponible):
```
Error: La pelicula 'El Padrino' no esta disponible.
```
