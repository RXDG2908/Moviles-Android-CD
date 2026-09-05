# Lab03 - Registro de Producto

**Alumno:** Renzo Raúl León Fernández
**Curso:** Programación en Móviles - 4to Ciclo
**Docente:** Juan José León Suiyon

## Descripción

App hecha con Jetpack Compose que registra un producto. La pantalla tiene tres
campos de ingreso (nombre, precio y cantidad), un botón AGREGAR PRODUCTO y una
Card que muestra el resumen con el importe calculado (precio × cantidad) con 2
decimales.

El estado de la pantalla se maneja con `remember` y `mutableStateOf`. El precio y
la cantidad se leen con `toDoubleOrNull` y `toIntOrNull` junto al operador Elvis
`?:`, para que la app no se caiga si el usuario escribe letras.

## Capturas

Pantalla inicial (formulario vacío):

![Pantalla inicial](capturas/pantalla-inicial.png)

Después de presionar AGREGAR PRODUCTO:

![Pantalla con producto registrado](capturas/pantalla-con-producto.png)

## Evidencias del desarrollo

Estructura del proyecto y código en Android Studio (rama `main`, proyecto dentro
de `Semana 3`):

![Estructura del proyecto](capturas/estructura-proyecto.png)

Historial de los 6 commits de la sesión:

![Historial de commits](capturas/historial-commits.png)

## ¿Qué pasaría si declaras las variables de los campos SIN remember?

Sin `remember` la variable se vuelve a crear en cada recomposición, así que
regresa a su valor inicial (`""`). Al escribir en el campo, Compose se redibuja y
el texto se pierde: el `OutlinedTextField` se queda vacío y parece que no se
puede escribir nada. Con `remember` el valor se conserva entre recomposiciones y
por eso sí se ve lo que uno escribe.

## Mejora con IA

Esta sección corresponde a la Parte B del laboratorio, hecha en la rama `S03-R2`.
El asistente que usé fue **Claude**. La mejora pedida era validación de campos
vacíos y un botón para limpiar el formulario.

| Prompt que usé | Qué generó la IA | Qué acepté o corregí (y por qué) |
| --- | --- | --- |
| "En el archivo MainActivity.kt del proyecto Lab03RegistroProducto, dentro del composable PantallaRegistro, agrega dos cosas. Primero, validación de campos vacíos: al presionar AGREGAR PRODUCTO, si el nombre, el precio o la cantidad están vacíos, no muestres la Card del resumen sino un mensaje de error en rojo que diga qué falta. Segundo, un botón LIMPIAR que vacíe los tres campos, oculte la Card y borre el mensaje de error. No toques el encabezado, ni el cálculo del importe, ni el mensaje verde de confirmación, ni los estilos existentes." | Un estado `mensajeError` con `remember`; un `if` en el `onClick` del botón AGREGAR que compara los tres campos contra `""` y muestra "Faltan datos por completar"; un botón LIMPIAR que vacía los tres campos, apaga `mostrarResumen` y borra el error; y un `Text` con `Color.Red` para el mensaje. Respetó lo que le pedí no tocar. | **Acepté** la estructura: el estado `mensajeError`, la validación dentro del `onClick` y el botón LIMPIAR con sus cinco asignaciones. Está bien resuelto y sigue el mismo patrón de estado del resto de la pantalla. **Corregí cuatro cosas:** (1) el mensaje era genérico, "Faltan datos por completar", cuando yo había pedido que dijera qué falta — lo cambié por mensajes específicos por campo; (2) comparaba con `== ""`, así que un campo con solo espacios pasaba como válido — le agregué `.trim()`; (3) no validaba que el precio y la cantidad fueran números, y la guía pide probar el caso "precio con letras" — agregué las comprobaciones con `toDoubleOrNull` y `toIntOrNull`, que ya usaba el proyecto; (4) usaba `Color.Red`, un color suelto que rompe la regla de un solo color primario — lo cambié por `MaterialTheme.colorScheme.error`, que es el rojo del tema. |

### Casos probados

Probados en el emulador, uno por uno:

| Caso | Resultado |
| --- | --- |
| Los tres campos vacíos | "Falta el nombre del producto" en rojo, sin Card |
| Nombre con tres espacios en blanco | "Falta el nombre del producto" — antes de la corrección este caso pasaba como válido |
| Teclado / precio "abc" / cantidad 2 | "El precio debe ser un numero", sin Card |
| Teclado / precio 25.50 / cantidad "2.5" | "La cantidad debe ser un numero entero", sin Card |
| Teclado / precio 25.50 / cantidad 23 | Muestra la Card: Importe S/ 586.50, sin mensaje de error |
| Botón LIMPIAR | Vacía los tres campos, oculta la Card y borra el mensaje de error |
