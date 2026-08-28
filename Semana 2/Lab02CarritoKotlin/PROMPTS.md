# Prompts utilizados — Rama R2 (con IA)

Este archivo documenta los prompts que se usaron para construir la rama con IA del
Laboratorio 02. Hay **un prompt por bloque**, y cada bloque corresponde a un commit.

Todos siguen la misma estructura de cuatro partes:

| Parte | Para qué sirve |
|---|---|
| **Contexto** | Qué es el programa y en qué estado está antes del cambio |
| **Tarea** | Qué se pide, en una sola frase |
| **Restricciones** | Los límites que no se pueden cruzar |
| **Criterio de aceptación** | Cómo se comprueba que el resultado es correcto |

La restricción que se repite en todos los bloques es que **la salida en consola no
puede cambiar**: los montos y el formato tienen que seguir siendo idénticos a los de
la rama sin IA. Eso convierte cada paso en una refactorización verificable en vez de
una reescritura a ciegas.

---

## Bloque 1 — POO base

**Contexto**
> Tengo un programa de consola en Kotlin que simula un carrito de compras de una
> tienda. Ahora mismo es código procedural: una `data class Producto` y cinco
> funciones sueltas (`calcularSubtotal`, `calcularIGV`, `calcularTotal`,
> `mostrarDetalle`, `calcularDescuento`) que reciben la lista de productos por
> parámetro, más un `main` que las orquesta.

**Tarea**
> Reestructúralo a programación orientada a objetos: que el carrito sea un objeto con
> su propio estado y sus propias operaciones, en lugar de una lista que se pasa de
> función en función.

**Restricciones**
> - La salida en consola debe quedar exactamente igual, carácter por carácter.
> - No agregues librerías externas; solo Kotlin estándar.
> - Mantén el código en un solo archivo y en un nivel que un alumno de cuarto ciclo
>   pueda explicar en una defensa oral.
> - Nada de patrones avanzados: sin inyección de dependencias, sin genéricos, sin
>   corrutinas.

**Criterio de aceptación**
> Al ejecutar, el subtotal sigue siendo 2786.00, el IGV 501.48, el total 3287.48 y el
> total con descuento 3123.11, con las columnas alineadas igual que antes.

---

## Bloque 2 — Abstracción

*(pendiente)*

## Bloque 3 — Herencia

*(pendiente)*

## Bloque 4 — Polimorfismo

*(pendiente)*

## Bloque 5 — Encapsulamiento

*(pendiente)*

## Bloque 6 — Integración

*(pendiente)*
