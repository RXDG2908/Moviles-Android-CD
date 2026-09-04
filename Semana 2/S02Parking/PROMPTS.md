# Prompts de desarrollo por función

Este documento registra, para cada función importante de la aplicación
**S02 Parking — Control de Estacionamiento** (versión de consola/terminal
en Kotlin), el prompt profesional que describe exactamente su
especificación, como si esa función hubiera sido generada de forma
independiente a partir de ese mismo prompt.

---

## 1. Modelo de tipos de vehículo — `VehicleType`

📄 `src/main/kotlin/com/leon/s02parking/model/VehicleType.kt`

> Necesito un `enum class` en Kotlin llamado `VehicleType` para una app de
> control de estacionamiento de consola. Debe representar los tres tipos
> de vehículo permitidos: **Moto**, **Auto** y **Camioneta**. Cada valor
> debe exponer:
>
> - `etiqueta: String` — el nombre legible para mostrar por consola.
> - `tarifaPorHora: BigDecimal` — la tarifa básica por hora, usando
>   `BigDecimal` (no `Double`) para evitar errores de redondeo:
>   - Moto → S/ 2.00
>   - Auto → S/ 4.00
>   - Camioneta → S/ 10.00
>
> No agregues lógica de cálculo aquí, solo los datos del tipo de vehículo.

---

## 2. Lectura y validación de datos por consola

📄 `src/main/kotlin/com/leon/s02parking/Main.kt`

> Escribe en Kotlin las funciones de lectura por consola (`readLine()`)
> para el formulario de ingreso de un vehículo, cada una con su propio
> bucle de reintento ante una entrada inválida:
>
> - `leerPlaca(): String` — pide la placa; si viene vacía imprime
>   `"Ingresa la placa."` y vuelve a preguntar. Devuelve el texto en
>   mayúsculas.
> - `leerTipoVehiculo(): VehicleType` — muestra un menú numerado
>   (1. Moto, 2. Auto, 3. Camioneta) y lee la opción; si no es 1, 2 o 3
>   imprime `"Selecciona el tipo de vehículo."` y repite.
> - `leerHoras(): Int` — pide las horas; si está vacío o no es un número
>   entero imprime `"Ingresa la cantidad de horas."`; si es menor a 1
>   imprime `"La cantidad de horas debe ser como mínimo 1."`. No debe
>   aceptar valores negativos.
> - `leerClienteFrecuente(): Boolean` — pregunta "Cliente frecuente
>   (s/n): " y acepta solo `s`/`si`/`sí` o `n`/`no` (sin distinguir
>   mayúsculas); ante cualquier otra respuesta pide que conteste con
>   's' o 'n'.

---

## 3. Cálculo del recargo por hora — `calcularDetallePorHora`

📄 `src/main/kotlin/com/leon/s02parking/model/ParkingCalculator.kt`

> Implementa `calcularDetallePorHora(tarifaBase: BigDecimal, horas: Int):
> List<HourDetail>` que calcule el cobro **hora por hora** de un
> estacionamiento. Reglas de recargo exactas:
>
> | Hora | Recargo |
> |---|---|
> | 1 | 0% |
> | 2 | 0% |
> | 3 | 20% |
> | 4 | 20% |
> | 5 | 20% |
> | 6 en adelante | 50% |
>
> Cada elemento del resultado debe indicar el número de hora, la tarifa
> base, el porcentaje de recargo aplicado y el importe final de esa hora.
> Usa `BigDecimal` con `RoundingMode.HALF_UP` y 2 decimales exactos en
> todos los cálculos para evitar errores de redondeo.

---

## 4. Cálculo del subtotal — `calcularSubtotal`

📄 `src/main/kotlin/com/leon/s02parking/model/ParkingCalculator.kt`

> Crea una función `calcularSubtotal(detalle: List<HourDetail>):
> BigDecimal` que sume el importe de todas las horas calculadas por
> `calcularDetallePorHora` y devuelva el resultado con exactamente
> 2 decimales.

---

## 5. Descuento por cliente frecuente — `calcularDescuento`

📄 `src/main/kotlin/com/leon/s02parking/model/ParkingCalculator.kt`

> Implementa `calcularDescuento(subtotal: BigDecimal, clienteFrecuente:
> Boolean): BigDecimal`. Si el cliente es frecuente, aplica un descuento
> del **10%** sobre el subtotal (`descuento = subtotal × 0.10`); si no lo
> es, el descuento debe ser `0.00`. El resultado siempre debe tener
> 2 decimales, calculado **después** de tener el subtotal completo con
> todos los recargos ya aplicados.

---

## 6. Cálculo del total a pagar — `calcularTotal`

📄 `src/main/kotlin/com/leon/s02parking/model/ParkingCalculator.kt`

> Escribe `calcularTotal(subtotal: BigDecimal, descuento: BigDecimal):
> BigDecimal` que reste el descuento al subtotal
> (`total = subtotal - descuento`) y devuelva el total a pagar con
> 2 decimales exactos.

---

## 7. Registro de vehículo y orquestación del cálculo — `ParkingRecord` / `ParkingCalculator.calcular`

📄 `src/main/kotlin/com/leon/s02parking/model/ParkingRecord.kt`
📄 `src/main/kotlin/com/leon/s02parking/model/ParkingCalculator.kt`

> Necesito una `data class ParkingRecord` que almacene el resultado
> completo de un cobro: `placa`, `tipo`, `horas`, `clienteFrecuente`, el
> `detalle` hora por hora, `subtotal`, `descuento` y `total`.
>
> Además, agrega una función `calcular()` dentro de `ParkingCalculator`
> que reciba `placa`, `tipo`, `horas` y `clienteFrecuente`, combine en
> orden `calcularDetallePorHora` → `calcularSubtotal` →
> `calcularDescuento` → `calcularTotal`, y devuelva el `ParkingRecord`
> completo listo para imprimirse por consola.

---

## 8. Impresión del resumen del cobro — `imprimirResumen`

📄 `src/main/kotlin/com/leon/s02parking/Main.kt`

> Escribe una función `imprimirResumen(registro: ParkingRecord)` que
> imprima por consola, con `println`, el resumen completo del cobro:
> placa, tipo, horas, si es cliente frecuente, el detalle hora por hora
> (hora / tarifa / recargo / importe) alineado en columnas, y al final
> el subtotal, el descuento y el **TOTAL**, todos formateados como
> moneda con 2 decimales exactos (`S/ X.XX`).

---

## 9. Bucle de registro de múltiples vehículos — `main` / `preguntarSiNo`

📄 `src/main/kotlin/com/leon/s02parking/Main.kt`

> Implementa la función principal `main()` como un bucle: por cada
> vuelta, pide los datos de un vehículo, calcula su cobro y lo imprime.
> Al terminar, pregunta **"¿Registrar otro vehículo? (s/n)"** usando una
> función reutilizable `preguntarSiNo(mensaje: String): Boolean` (con
> reintento ante una respuesta que no sea 's' o 'n'). Si la respuesta es
> "sí", vuelve a pedir los datos de un nuevo vehículo desde cero; si es
> "no", termina el programa con un mensaje de despedida.

---

## 10. Formateo de moneda — `aMoneda`

📄 `src/main/kotlin/com/leon/s02parking/util/Money.kt`

> Escribe una función de extensión `BigDecimal.aMoneda(): String` que
> formatee cualquier importe con el prefijo `"S/ "` y exactamente
> 2 decimales. Por ejemplo, `BigDecimal("4")` debe mostrarse como
> `"S/ 4.00"`, y `BigDecimal("10")` como `"S/ 10.00"`.

---

## 11. Nuevo tipo de vehículo — `TRAILER`

📄 `src/main/kotlin/com/leon/s02parking/model/VehicleType.kt`
📄 `src/main/kotlin/com/leon/s02parking/Main.kt`

> Agrega un cuarto valor al `enum class VehicleType`: **Trailer**, con
> etiqueta `"Trailer"` y tarifa básica `BigDecimal("20.00")` por hora.
> Actualiza el menú numerado de `leerTipoVehiculo()` en `Main.kt` para
> incluir la opción `4. Trailer`, devolviendo `VehicleType.TRAILER` cuando
> se seleccione.

---

## 12. Nuevos tramos de recargo y descuento adicional por monto

📄 `src/main/kotlin/com/leon/s02parking/model/ParkingCalculator.kt`
📄 `src/main/kotlin/com/leon/s02parking/model/ParkingRecord.kt`
📄 `src/main/kotlin/com/leon/s02parking/Main.kt`

> Cambia la tabla de recargo por hora en `calcularDetallePorHora` a:
>
> | Hora | Recargo |
> |---|---|
> | 1-2 | 0% |
> | 3-5 | 20% |
> | 6-10 | 40% |
> | 11 en adelante | 50% |
>
> Agrega una segunda función `calcularDescuentoPorMonto(subtotalOriginal:
> BigDecimal, montoTrasFrecuente: BigDecimal): BigDecimal`: si el
> `subtotalOriginal` (el subtotal con recargos, antes de cualquier
> descuento) supera **S/500**, aplica un descuento adicional del **20%**
> sobre `montoTrasFrecuente` (el monto que ya quedó luego de restar, si
> corresponde, el 10% de cliente frecuente); si no supera S/500, el
> descuento es `0.00`.
>
> Este descuento es **adicional** al de cliente frecuente, nunca lo
> reemplaza: primero se resta el 10% de cliente frecuente sobre el
> subtotal, y sobre ese resultado se resta el 20% adicional si aplica.
> Actualiza `ParkingRecord` para exponer `descuentoFrecuente` y
> `descuentoPorMonto` por separado, y el resumen impreso en `Main.kt` para
> mostrar ambas líneas.

---

## 13. IGV del 18%

📄 `src/main/kotlin/com/leon/s02parking/model/ParkingCalculator.kt`
📄 `src/main/kotlin/com/leon/s02parking/model/ParkingRecord.kt`
📄 `src/main/kotlin/com/leon/s02parking/Main.kt`

> Agrega el cálculo del IGV (18%) al flujo de cobro. Crea
> `calcularIGV(montoConDescuento: BigDecimal): BigDecimal` que calcule el
> 18% sobre el monto **ya con los descuentos de cliente frecuente y por
> monto aplicados** (no sobre el subtotal original). El total final a
> pagar (`calcularTotal`) debe ser ese monto con descuento más el IGV.
> Expón el nuevo campo `igv` en `ParkingRecord` y muestra una línea
> `"IGV (18%):"` en el resumen impreso, justo antes del `TOTAL`.
