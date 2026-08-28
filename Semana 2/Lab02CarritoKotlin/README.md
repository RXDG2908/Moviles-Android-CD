# Laboratorio 02 — Carrito de compras en Kotlin

**Alumno:** Renzo Raúl León Fernández
**Curso:** Programación en Móviles — C24, 4to ciclo
**Docente:** Juan José León Suiyon
**Semana:** 2 — Introducción a la POO en Kotlin

---

## Descripción

Programa de consola en Kotlin que simula el carrito de compras de una tienda. Registra
productos con su nombre, precio y cantidad, calcula el subtotal, el IGV del 18% y el
total a pagar, muestra el detalle en columnas alineadas con dos decimales, identifica
el producto más caro y aplica un descuento escalonado según el monto de la compra.

Se ejecuta desde `fun main()` con el botón Run de Android Studio, sin emulador.

## Funciones implementadas

| Función | Recibe | Devuelve | Qué hace |
|---|---|---|---|
| `calcularSubtotal` | La lista de productos | `Double` | Suma el precio por la cantidad de cada producto |
| `calcularIGV` | El subtotal | `Double` | Calcula el 18% del subtotal |
| `calcularTotal` | Subtotal e IGV | `Double` | Suma ambos montos |
| `mostrarDetalle` | La lista de productos | — | Imprime el detalle numerado con columnas alineadas |
| `calcularDescuento` | El total | `Double` | Aplica 10% sobre S/ 5000, 5% sobre S/ 3000, o nada |
| `buscarProducto` | La lista y un nombre | `Producto?` | Devuelve el producto si existe, o `null` si no |

El modelo de datos es la `data class Producto`, con los campos `nombre`, `precio` y
`cantidad`.

## Reto adicional

- **Buscar producto:** `buscarProducto` usa la función `find` de las listas y devuelve
  un tipo nullable, porque el producto puede no estar en el carrito.
- **Eliminar producto:** se elimina con `removeIf` y se vuelven a mostrar el detalle y
  todos los totales recalculados.

## Captura de la consola

> Pendiente de agregar: guardar la imagen como `captura-consola.png` en esta misma
> carpeta y reemplazar esta nota por `![Salida del programa](captura-consola.png)`.

Salida obtenida al ejecutar el programa:

```text
=========================================
   CARRITO DE COMPRAS - TIENDA TECSUP
=========================================
Cliente: Renzo Leon

Producto agregado: Laptop HP
Producto agregado: Mouse Logitech
Producto agregado: Audifonos Sony
Producto agregado: USB Kingston 64GB

--------- DETALLE DEL CARRITO ---------
1. Laptop HP            x1  S/  2500.00
2. Mouse Logitech       x2  S/    91.00
3. Audifonos Sony       x1  S/   120.00
4. USB Kingston 64GB    x3  S/    75.00
---------------------------------------
Cantidad de productos : 4
Subtotal              : S/  2786.00
IGV (18%)             : S/   501.48
TOTAL A PAGAR         : S/  3287.48
---------------------------------------
Producto mas caro: Laptop HP (S/ 2500.00)
Descuento aplicado: 5% por compra mayor a S/ 3000
TOTAL CON DESCUENTO   : S/  3123.11

--------- BUSCAR PRODUCTO ---------
Producto encontrado: Mouse Logitech
Precio: S/ 45.50
Cantidad: 2

--------- ELIMINAR PRODUCTO ---------
Producto eliminado: USB Kingston 64GB

--------- DETALLE DEL CARRITO ---------
1. Laptop HP            x1  S/  2500.00
2. Mouse Logitech       x2  S/    91.00
3. Audifonos Sony       x1  S/   120.00
---------------------------------------
Cantidad de productos : 3
Subtotal              : S/  2711.00
IGV (18%)             : S/   487.98
TOTAL A PAGAR         : S/  3198.98
TOTAL CON DESCUENTO   : S/  3039.03

Gracias por su compra, Renzo Leon!
```

## ¿Por qué `nombre` y `precio` son `val` pero `cantidad` es `var`?

<!--
    ESCRIBE TU RESPUESTA AQUÍ, con tus palabras.

    Es la misma pregunta de la defensa oral, así que piénsala bien.
    Dos cosas que tienes que responder:

      1. Por qué el nombre y el precio de un producto no deberían cambiar una vez
         creado el objeto, y en cambio la cantidad sí. Piénsalo desde el negocio:
         en un carrito real, ¿qué modificas mientras compras?

      2. Qué pasaría si intentas cambiar el precio después de crear el producto.
         (Pista: no es un error al ejecutar. Ocurre antes.)

    Borra este comentario cuando termines.
-->

## Cómo ejecutarlo

1. Abrir el proyecto en Android Studio.
2. Abrir `app/src/main/java/com/leon/lab02carritokotlin/Carrito.kt`.
3. Pulsar el botón ▶ verde del margen izquierdo, junto a `fun main()`, y elegir
   *Run 'CarritoKt'*.
4. El resultado aparece en la pestaña **Run**.
