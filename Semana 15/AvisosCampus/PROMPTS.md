# Prompts utilizados - Fase 2 (rama mejora-ia)

**Laboratorio 15:** Notificaciones en Android (AvisosCampus)
**Alumno:** Renzo Raúl León Fernández
**Asistente de IA usado:** Claude Code (modelo Claude Sonnet 5.5). La guía menciona Gemini; no se usó.
La Fase 1 (rama main) también se desarrolló con este asistente.

## Prompts

| Prompt que escribí | Qué respondió la IA | Qué se cambió o descartó | Por qué |
|---|---|---|---|
| «bajar todo el material y actividad de moviles lab15, analizar y ver como proceder» | Descargó la guía y la presentación y resumió los entregables | No se encontró el proyecto base en la actividad | Había que reconstruirlo desde la guía |
| «hacer desde 0 con la estructura que nos dio» | Creó el proyecto base con los archivos entregados y esqueletos con TODO | Configuración Gradle tomada del laboratorio 14 | Ya compilaba en este equipo |
| «es con ia todo, normal trabaja la fase 1.» | Implementó los pasos 1 a 9 con un commit por paso y el README | Se agregaron los flags SINGLE_TOP y CLEAR_TOP al Intent de la notificación | Evitar abrir otra actividad y perder el historial |
| «Sí, haz Fase 2» | Implementó tres mejoras: dos canales, texto largo y receptor del cargador | Se dejaron fuera Posponer, historial persistente y push; se agregó el gradle-wrapper.jar que faltaba | Priorizar el tiempo; el .gitignore raíz había excluido el jar |

## Mejoras

1. **Dos canales:** el recordatorio usa el canal Urgentes (`IMPORTANCE_HIGH`); el resto usa Avisos. Cambia `Notificador`.
2. **Texto largo:** `BigTextStyle` muestra el texto completo al expandir. Cambia `Notificador` y el texto del recordatorio en el ViewModel.
3. **Receptor del cargador:** `CargadorReceiver` con su propio switch. Cambia receptor, ViewModel, pantalla y el modelo `Origen`.

**Condición que se mantuvo:** el ViewModel no usa `Context` ni `NotificationManager`, y cada receptor se quita en `onDispose`.
