# CampusGPS

Laboratorio 13 de Programación en Móviles: localización geográfica.

App que obtiene la ubicación del celular, la muestra en un mapa y dice qué lugar
del campus está más cerca. Usa Kotlin, Jetpack Compose, Material 3, Google Play
Services Location y Maps Compose, con MVVM + Repository.

- **Alumno:** Renzo Raúl León Fernández
- **Modalidad:** individual
- **Paquete:** `com.tecsup.campusgps`

## Estructura de paquetes

```
com.tecsup.campusgps/
├── MainActivity.kt
├── model/
│   └── Modelos.kt                    Posicion, Lugar, LugarConDistancia, UbicacionUi
├── data/
│   ├── UbicacionRepository.kt        interface
│   ├── UbicacionRepositoryGoogle.kt  implementación con Fused
│   ├── LugaresRepository.kt          interface
│   └── LugaresRepositoryLocal.kt     los 5 lugares
├── viewmodel/
│   ├── MapaViewModel.kt
│   └── CampusViewModelFactory.kt
└── ui/
    ├── PantallaMapa.kt
    ├── MapaUbicacion.kt
    └── theme/
```

## Avance (Fase 1, rama main)

| Commit | Paso | Estado |
|---|---|---|
| 1 | Proyecto base | Hecho |
| 2 | Permisos y clave de Maps | Pendiente |
| 3 | Permiso en tiempo de ejecución | Pendiente |
| 4 | Mapa con la ubicación actual | Pendiente |
| 5 | Marcadores de los lugares | Pendiente |
| 6 | Distancias y orden | Pendiente |
| 7 | Lista de lugares y selección | Pendiente |
| 8 | Lugar más cercano | Pendiente |
| 9 | Estados de error | Pendiente |
| 10 | README con capturas y respuestas | Pendiente |

## Pendiente

- Crear la clave de Maps SDK for Android en Google Cloud y guardarla en
  `local.properties` como `MAPS_API_KEY=...` (ese archivo no se sube).
- Probar en un emulador con Google Play. El emulador `Lab03` no trae Google Play
  Services, así que ahí la ubicación falla con un mensaje.
- Fase 2 en la rama `mejora-ia` (3 mejoras con Gemini y `PROMPTS.md`).

## Notas

- `maps-compose` va en la versión 6.4.1: la 8.4.0 de la guía exige Android Gradle
  Plugin 9.1 y compileSdk 37, y no compila con este proyecto.
