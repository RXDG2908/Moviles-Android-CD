# Laboratorio 02 — Carrito de compras en Kotlin · Rama R2 (con IA)

**Alumno:** Renzo Raúl León Fernández
**Curso:** Programación en Móviles — C24, 4to ciclo
**Docente:** Juan José León Suiyon
**Semana:** 2 — Introducción a la POO en Kotlin

---

## Qué es esta rama

La rama `S02-R1` resuelve el laboratorio siguiendo el manual: código procedural, una
`data class` y funciones sueltas. Esta rama parte de ahí y la reconstruye aplicando los
cuatro pilares de la programación orientada a objetos, usando IA como herramienta.

La regla que gobernó todo el trabajo: **los montos y el formato de la salida no podían
cambiar**. Subtotal 2786.00, IGV 501.48, total 3287.48 y total con descuento 3123.11
siguen siendo idénticos a la rama sin IA. Eso convirtió cada paso en una
refactorización verificable en vez de una reescritura a ciegas.

## Los cuatro pilares, y dónde está cada uno

| Pilar | Dónde se ve en el código |
|---|---|
| **Abstracción** | `ProductoBase` es una clase abstracta: define **qué** debe saber hacer todo producto —calcular su importe, decir su tipo— sin decir **cómo**. No se puede instanciar. |
| **Herencia** | `ProductoElectronico` y `ProductoAlmacenamiento` heredan de `ProductoBase` sus atributos y su contrato, y cada una agrega lo suyo: meses de garantía y capacidad en GB. |
| **Polimorfismo** | `imprimirClasificacion` recorre la lista tratando a todos como `ProductoBase` y llama a `etiqueta()` y `descripcionGarantia()`. No pregunta de qué tipo es cada uno: cada objeto responde a su manera y Kotlin resuelve cuál versión ejecutar. |
| **Encapsulamiento** | `precio` y `cantidad` tienen `private set`: solo se modifican desde adentro. La cantidad cambia únicamente por `cambiarCantidad()`, que valida. El `init` rechaza precios y cantidades no positivos. La lista del carrito es privada y se entrega como copia de solo lectura. |

## Estructura de clases

```
ProductoBase (abstracta)
├── ProductoElectronico      (mesesGarantia)
└── ProductoAlmacenamiento   (capacidadGB)

Carrito            — guarda los productos y hace los cálculos
ReporteConsola     — solo se ocupa de mostrar
```

Calcular y presentar están separados: si mañana el reporte va a una pantalla Android
en vez de a la consola, `Carrito` no se toca.

---

# Prompts utilizados

Un prompt por bloque, un bloque por commit. Todos siguen la misma estructura de cuatro
partes, porque un prompt sin criterio de aceptación deja que el modelo decida solo si
acertó:

| Parte | Para qué sirve |
|---|---|
| **Contexto** | Qué es el programa y en qué estado está antes del cambio |
| **Tarea** | Qué se pide, en una sola frase |
| **Restricciones** | Los límites que no se pueden cruzar |
| **Criterio de aceptación** | Cómo se comprueba que el resultado es correcto |

---

## Bloque 1 — POO base

**Contexto.** Tengo un programa de consola en Kotlin que simula el carrito de compras
de una tienda. Ahora mismo es código procedural: una `data class Producto` y cinco
funciones sueltas —`calcularSubtotal`, `calcularIGV`, `calcularTotal`, `mostrarDetalle`
y `calcularDescuento`— que reciben la lista de productos por parámetro, más un `main`
que las orquesta.

**Tarea.** Reestructúralo a programación orientada a objetos: que el carrito sea un
objeto con su propio estado y sus propias operaciones, en lugar de una lista que se
pasa de función en función.

**Restricciones.** La salida en consola debe quedar exactamente igual, carácter por
carácter. Solo Kotlin estándar, sin librerías externas. Todo en un solo archivo y en un
nivel que un alumno de cuarto ciclo pueda explicar en una defensa oral: sin inyección de
dependencias, sin genéricos, sin corrutinas.

**Criterio de aceptación.** Al ejecutar, el subtotal sigue siendo 2786.00, el IGV
501.48, el total 3287.48 y el total con descuento 3123.11, con las columnas alineadas
igual que antes.

---

## Bloque 2 — Abstracción

**Contexto.** El programa ya está en clases: `Producto`, `Carrito` y `ReporteConsola`.
`Producto` es una clase concreta que calcula su importe multiplicando precio por
cantidad.

**Tarea.** Introduce una clase abstracta `ProductoBase` que defina el contrato que todo
producto debe cumplir, y haz que `Producto` la implemente.

**Restricciones.** La clase abstracta debe declarar **qué** sabe hacer un producto, no
**cómo** lo hace. El carrito debe trabajar contra el tipo abstracto, no contra la clase
concreta. No cambies todavía los tipos de producto que se crean en `main`. La salida no
puede cambiar.

**Criterio de aceptación.** `ProductoBase` no se puede instanciar directamente, el
carrito almacena `ProductoBase` y la salida sigue siendo idéntica.

---

## Bloque 3 — Herencia

**Contexto.** Existe `ProductoBase` como clase abstracta, con una única implementación
concreta genérica.

**Tarea.** Crea dos subclases que hereden de `ProductoBase` y modelen los productos
reales del carrito: uno para artículos electrónicos y otro para artículos de
almacenamiento.

**Restricciones.** Cada subclase debe aportar al menos un atributo propio que la
distinga, y ese atributo tiene que tener sentido de negocio, no ser relleno. Los
importes no pueden cambiar: los cuatro productos siguen costando lo mismo. La salida no
puede cambiar.

**Criterio de aceptación.** El `main` construye los productos usando las subclases, y
los montos del reporte siguen dando 2786.00, 501.48, 3287.48 y 3123.11.

---

## Bloque 4 — Polimorfismo

**Contexto.** Ya hay dos subclases que heredan de `ProductoBase`, pero el programa
todavía no aprovecha esa diferencia: las trata a todas igual.

**Tarea.** Haz que cada subclase responda a su manera a los métodos de la clase base, y
agrega al reporte una sección que lo demuestre recorriendo la lista sin preguntar de
qué tipo es cada producto.

**Restricciones.** Prohibido usar `is`, `when` sobre el tipo o cualquier comprobación de
clase para decidir qué imprimir: eso sería lo contrario del polimorfismo. La nueva
sección se agrega **después** del bloque de totales, para no alterar ninguna de las
líneas que ya existían. Los montos siguen intactos.

**Criterio de aceptación.** Un mismo bucle imprime descripciones distintas según el tipo
real de cada objeto, y las líneas del reporte original quedan sin tocar.

---

## Bloque 5 — Encapsulamiento

**Contexto.** Las clases ya usan abstracción, herencia y polimorfismo, pero el estado
sigue expuesto: `cantidad` es una propiedad pública mutable y cualquiera puede ponerle
un valor absurdo desde fuera.

**Tarea.** Cierra el estado de las clases: que los atributos solo se modifiquen desde
adentro, a través de métodos que validen.

**Restricciones.** Debe rechazarse un precio menor o igual a cero, una cantidad menor o
igual a cero y un nombre vacío, y el rechazo tiene que ocurrir al construir el objeto,
no después. La lista interna del carrito no puede quedar expuesta de forma que alguien
la modifique saltándose las operaciones del carrito. Agrega una demostración en consola
de que una cantidad inválida se rechaza. Los montos del reporte siguen intactos.

**Criterio de aceptación.** Intentar poner una cantidad en cero lanza una excepción con
mensaje claro, el programa la captura y sigue corriendo, y los totales no cambian.

---

## Bloque 6 — Integración y documentación

**Contexto.** Los cinco bloques anteriores están aplicados y commiteados por separado.

**Tarea.** Revisa el conjunto, verifica que la salida sigue coincidiendo con la de la
rama sin IA, y documenta en el README qué pilar de la POO quedó en qué parte del código
junto con los prompts usados en cada bloque.

**Restricciones.** La documentación tiene que señalar clases y métodos concretos, no
hablar en general de los pilares. Los prompts se transcriben tal como se usaron.

**Criterio de aceptación.** El README permite a un tercero abrir el código y encontrar
cada pilar sin buscarlo a ciegas, y la salida verificada coincide con la de `S02-R1`.

---

## Cómo ejecutarlo

1. Abrir el proyecto en Android Studio.
2. Abrir `app/src/main/java/com/leon/lab02carritokotlin/Carrito.kt`.
3. Pulsar el botón ▶ verde del margen izquierdo, junto a `fun main()`, y elegir
   *Run 'CarritoKt'*.
4. El resultado aparece en la pestaña **Run**.

> Si Android Studio falla con *SourceSet with name 'main' not found*, cambiar en
> **Settings → Build, Execution, Deployment → Build Tools → Gradle** la opción
> *Build and run using* a **IntelliJ IDEA**.
