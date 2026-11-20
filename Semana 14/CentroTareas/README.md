# CentroTareas

App del Laboratorio 14 (Hilos en Android).

## Observación del Paso 1

Al tocar **Tarea larga (MAL)** la pantalla deja de responder durante 4 segundos: el
botón **Tócame** no suma y nada se mueve. Pasados los 4 segundos, el texto cambia
directo a **Terminó**.

El texto **Trabajando...** no llega a verse porque `Thread.sleep` duerme el hilo
principal, que es el mismo que dibuja la pantalla. El valor sí cambia, pero el hilo
no tiene tiempo de dibujarlo antes de dormirse, y cuando despierta ya vale
**Terminó**.

Si la espera fuera de 10 segundos, Android mostraría el aviso **La aplicación no
responde** (ANR), que deja al usuario esperar o cerrar la app. Ese aviso sale
cuando el hilo principal no atiende un toque durante unos 5 segundos.

## Avance

| Paso | Estado |
|------|--------|
| 0. Proyecto base | Hecho |
| 1. Provocar el congelamiento | Hecho |
| 2. Arreglarlo con una corrutina | Hecho |
| 3. Mostrar el progreso | Hecho |
| 4 a 9 | Pendiente |
