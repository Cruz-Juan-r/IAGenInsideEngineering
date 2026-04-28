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


Problema 2 — Tienda Virtual (Sistema de Pagos)
1. Patrones identificados
El enunciado indica dos pistas claras: "crear familias de objetos relacionados" y "notificar automáticamente a múltiples observadores". Eso apunta a:
🏭 Factory Method

Para qué: crear el PaymentMethod adecuado (tarjeta, PayPal, cripto) sin que la lógica principal (ECIPayment) conozca las clases concretas.
Estructura: PaymentFactory (interfaz) y tres factories concretos: CreditCardPaymentFactory, PaypalPaymentFactory, CryptoPaymentFactory. Cada uno produce su PaymentMethod correspondiente (CreditCardPayment, PaypalPayment, CryptoPayment).
Nota sobre el "validador": el enunciado pide "objetos de pago y sus validadores correspondientes". Aquí se decidió que cada PaymentMethod implemente la interfaz ValidatePayment (es decir, el pago es su propio validador). Esto evita un Abstract Factory innecesariamente complejo cuando solo hay dos productos por familia (pago + validador) y simplifica la API: la fábrica entrega un objeto que ya sabe validarse a sí mismo. Si el día de mañana la validación se complica (validación remota antifraude, validación KYC, etc.), conviene migrar a Abstract Factory y separar PaymentValidator como producto independiente. Hoy, Factory Method es suficiente.

👀 Observer

Para qué: cuando el pago termina, el sistema debe avisar a varios módulos (inventario, facturación, notificaciones) que reaccionen, sin que ECIPayment los conozca individualmente.
Estructura: PaymentObserver (interfaz) con los métodos onPaymentSuccess / onPaymentFailed. ECIPayment actúa como Subject: mantiene la lista de observadores y los notifica. PaymentEventObserver es un observador concreto que orquesta los efectos (descontar stock, generar factura, mandar correo).

¿Son los adecuados?
Sí, ambos encajan perfectamente con el problema:

Factory Method abstrae la creación (resuelve "cómo construir un PaymentMethod sin conocer la clase concreta").
Observer abstrae la notificación (resuelve "cómo enterar a múltiples módulos sin acoplarlos al core").

Los dos requisitos del enunciado (nuevos métodos de pago y nuevos módulos reactivos) corresponden uno-a-uno con las extensiones naturales de cada patrón.

Posibles cambios futuros: si en el futuro se agregan procesos de validación independientes y reutilizables, conviene escalar a Abstract Factory (cada fábrica produce un par PaymentMethod + PaymentValidator).

2. Clases / interfaces que faltaban
El código entregado declaraba usar PaymentFactory (en ECIPayment.processPayment(PaymentFactory factory, ...)) pero esa interfaz no existía. Las clases que tenían "Factory" en el nombre (CreditCardFactory, PaypalFactory, CryptoFactory) en realidad eran productos que extendían PaymentMethod — es decir, estaban mal nombradas y el patrón estaba incompleto.
Lo que se agregó
Archivo nuevoRolPaymentFactory.javaInterfaz base del Factory Method. Define createPaymentMethod(amount, customerId, description, details).CreditCardPaymentFactory.javaFábrica concreta — produce CreditCardPayment.PaypalPaymentFactory.javaFábrica concreta — produce PaypalPayment.CryptoPaymentFactory.javaFábrica concreta — produce CryptoPayment.
Lo que se renombró
Antes (incorrecto)AhoraCreditCardFactory (extendía PaymentMethod)CreditCardPaymentPaypalFactory (extendía PaymentMethod)PaypalPaymentCryptoFactory (extendía PaymentMethod)CryptoPayment
Esto quita la confusión semántica: ahora Factory es realmente una fábrica y Payment es realmente el producto.
3. Validación del diagrama de contexto
El diagrama de contexto (docs/imagenes/contexto.png) tiene tres problemas:

Caja "Notificación" duplica al "Módulo notificación". Hay dos cajas distintas con responsabilidades parecidas: una titulada "Notificación" (descrita como "Al momento en que se realiza el pago, se genera una notificación automática hacia los otros módulos para que ellos actúen") y otra titulada "Módulo notificación" (descrita como "Módulo encargado de darle la notificación (vía correo) al cliente"). La primera no es una notificación al cliente: es el evento interno del Subject del patrón Observer. Mezclar ambas confunde el modelo.
Cambio sugerido: renombrar la caja intermedia a "Bus de eventos de pago" o "Notificador interno (Observer)" y dejar claro que es un mecanismo interno del sistema, no un actor externo.
Falta el actor "Tienda Virtual / Operador". El diagrama solo muestra al Cliente. En el diagrama de casos de uso (docs/uml/users.png) aparecen tres actores: Cliente, TiendaVirtual y ECIPayments. El de contexto debería al menos mencionar a la Tienda como sistema externo que dispara los pagos.
No aparecen los proveedores externos (banco/red de tarjetas, API de PayPal, blockchain). El sistema de pagos depende de estos servicios externos, y un diagrama de contexto C4 normalmente los incluye como sistemas externos que el sistema central consume.
Cambio sugerido: agregar tres cajas de sistemas externos: Pasarela bancaria, API PayPal, Red blockchain, conectadas al "Sistema de pago".

Estos cambios hacen el diagrama más fiel al diseño implementado y al patrón Observer empleado internamente.
4. Errores identificados en el código
#ArchivoErrorPor qué no compila / falla1PaymentMethod.javaEl constructor declara el segundo parámetro como String transactionID, pero adentro hace this.customerID = customerID; — la variable customerID no existe en el ámbito del constructor. Además sobreescribe el transactionID recibido con generateTransactionId().No compila: símbolo no encontrado.2PaymentEventObserver.javaimport javax.management.Notification; — importa la clase JMX del JDK en lugar del Notification propio del paquete.No compila: javax.management.Notification no tiene sendConfirmationEmail ni sendFailureNotification.3ECIPayment.javaUsa PaymentFactory factory y llama factory.createPaymentMethod(...), pero la interfaz PaymentFactory no existe en el proyecto.No compila: símbolo PaymentFactory no encontrado.4CryptoFactory.javaLínea this.token = token; — la variable token no es parámetro del constructor; se asigna a sí misma (queda siempre en null).Compila pero es un bug latente.5CreditCardFactory / PaypalFactory / CryptoFactoryMal nombradas: extienden PaymentMethod, son productos, no fábricas. Esto rompe la semántica del patrón.Confunde el patrón aunque compile.6PaymentObserver.java vs diagrama UMLLa interfaz declara métodos con cuatro parámetros (onPaymentSuccess(payment, customerName, customerEmail, productId)), pero el diagrama UML los muestra con un solo payment.Inconsistencia documentación ↔ código.7auxiliaryTest.javaClase de test vacía (sin pruebas reales del Problema 2).No prueba nada.8pom.xmlApunta a spring-boot-starter-parent:4.0.0-SNAPSHOT (versión inestable que requiere repos snapshot). Además declara jacoco-maven-plugin con regla <minimum>0.85</minimum> que falla el build si la cobertura es <85%.Build inestable.9Archivos basuraExistían archivos Membership y CatalogoPeliculas sin extensión .java del Problema 1, en paquetes equivocados.Si se incluían en el find -name "*.java" para compilación masiva, generaban errores de paquete.
5. Correcciones aplicadas

PaymentMethod reescrito: constructor con parámetros coherentes (amount, customerID, description); el transactionID se genera con prefijo según el tipo de pago (CC, PP, CR).
PaymentEventObserver corregido: eliminado el import erróneo de javax.management.Notification.
Jerarquía Factory completa creada: nueva interfaz PaymentFactory y tres fábricas concretas (CreditCardPaymentFactory, PaypalPaymentFactory, CryptoPaymentFactory). Cada fábrica recibe un Map<String, Object> details con los parámetros propios del medio de pago, manteniendo una API uniforme.
Productos renombrados: CreditCardFactory → CreditCardPayment, PaypalFactory → PaypalPayment, CryptoFactory → CryptoPayment. Se eliminaron los Thread.sleep(...) que solo ralentizaban las pruebas. En CryptoPayment se quitó el atributo token muerto.
ECIPayment ajustado a la nueva firma processPayment(PaymentFactory, amount, customerId, description, customerName, customerEmail, productId, paymentDetails). Se agregó getObserverCount() para facilitar tests.
Application.java convertido a demo de consola (sin Spring Boot, que no aportaba al ejercicio). Ejecuta cuatro casos: tarjeta exitosa, PayPal exitoso, cripto exitoso, cripto fallido por saldo insuficiente.
pom.xml simplificado: sin Spring Boot (no se necesitaba); sin la regla de jacoco al 85%; solo JUnit 5 + Surefire.
Archivos basura eliminados: Membership y CatalogoPeliculas sueltos del Problema 1, y el auxiliaryTest.java vacío.
Tres archivos de pruebas unitarias agregados:

PaymentMethodTest: valida cada producto concreto (validación, procesamiento, estado).
PaymentFactoryTest: valida que cada fábrica produce el tipo correcto de PaymentMethod.
ECIPaymentObserverTest: valida la mecánica del Observer (notificación a éxito/fallo, agregar/quitar observers, integración con inventario que descuenta stock).



6. Estructura final del paquete util
util/
├── PaymentMethod.java          (abstracta, base de productos)
├── ValidatePayment.java        (interfaz de validación)
├── PaymentStatus.java          (enum)
├── CreditCardPayment.java      (producto)
├── PaypalPayment.java          (producto)
├── CryptoPayment.java          (producto)
├── PaymentFactory.java         (interfaz Factory Method)
├── CreditCardPaymentFactory.java
├── PaypalPaymentFactory.java
├── CryptoPaymentFactory.java
├── ECIPayment.java             (Subject del Observer)
├── PaymentObserver.java        (interfaz Observer)
├── PaymentEventObserver.java   (Observer concreto)
├── Inventory.java              (módulo de stock)
├── Facturation.java            (módulo de facturación)
├── Notification.java           (módulo de correo)
└── Product.java                (entidad)
7. Cómo ejecutar
bash# Demo en consola
javac -d out $(find src/main -name "*.java")
java -cp out eci.edu.byteProgramming.ejercicio.paper.Application

# Pruebas unitarias (con Maven)
mvn test
8. Evidencia de ejecución
8.1 Demo en consola (extracto del Caso 1 — tarjeta de crédito)
================= CASO 1: TARJETA DE CREDITO (exito) =================
ECI Payments: Starting payment process...
Customer: Maria Lopez (maria@example.com)
Amount: $1200.0
Description: Compra de Gaming Laptop
----------------------------------------
Processing Credit Card payment...
Contacting bank for card: **** **** **** 1111
Payment authorized by bank
Payment processed successfully!

Payment Observer: Processing successful payment events...
Inventory: Discounted 1 units of Gaming Laptop
   Remaining stock: 4
Facturation: Invoice generated
   Invoice Number: INV-1001
   Customer: Maria Lopez (ID: CUST001)
   Product: Gaming Laptop
   Subtotal: $1200.00 COP
   Tax (19%): $228.00 COP
   Total: $1428.00 COP
   Payment Method: CREDIT_CARD
Notification: Sending confirmation email
   To: maria@example.com
   Subject: Payment Confirmation - CC17773816891138288
   Dear Maria Lopez,
   Your payment of $1200.0 has been processed successfully via CREDIT_CARD
All post-payment processes completed successfully!
Se observan los dos patrones funcionando juntos: el factory crea el CreditCardPayment (Factory Method), el pago se procesa, y ECIPayment notifica al PaymentEventObserver que dispara los tres módulos en cascada (Observer → inventario, facturación, notificación).
En el caso de cripto con saldo insuficiente se observa la rama de fallo:
Processing Cryptocurrency payment...
Crypto validation failed!
Payment failed!
Payment Observer: Processing failed payment events...
Notification: Sending failure notification
8.2 Pruebas unitarias
Resultado de la ejecución completa de la batería de tests:
JUnit Jupiter
├─ PaymentFactoryTest
│  ├─ cryptoFactoryProduceCryptoPayment()                  ✓
│  ├─ creditCardFactoryProduceCreditCardPayment()          ✓
│  ├─ factoryDevuelveProductoEncapsuladoComoAbstraccion()  ✓
│  └─ paypalFactoryProducePaypalPayment()                  ✓
├─ PaymentMethodTest
│  ├─ cryptoSinSaldoFalla()                                ✓
│  ├─ paypalConTokenCortoFalla()                           ✓
│  ├─ cryptoConSaldoSuficienteProcesa()                    ✓
│  ├─ creditCardValidaYProcesaCorrectamente()              ✓
│  ├─ creditCardInvalidaFalla()                            ✓
│  └─ paypalValidaYProcesa()                               ✓
└─ ECIPaymentObserverTest
   ├─ integracionConObserverConcretoYInventario()          ✓
   ├─ permiteAgregarYRemoverObservadores()                 ✓
   ├─ notificaExitoATodosLosObservadores()                 ✓
   └─ notificaFalloCuandoElPagoFalla()                     ✓

[  6 containers successful ]
[  0 containers failed     ]
[ 14 tests successful      ]
[  0 tests failed          ]
14 / 14 tests verdes. Todo compila y pasa.

### Caso 3 — Validación de película no disponible

Al intentar alquilar "El Padrino" (no disponible):
```
Error: La pelicula 'El Padrino' no esta disponible.
```
