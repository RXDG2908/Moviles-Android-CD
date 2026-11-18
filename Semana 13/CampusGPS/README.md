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
├── util/
│   ├── Distancia.kt                  distanciaMetros con Location.distanceBetween
│   └── Formato.kt                    formatearDistancia: 350 m o 1.5 km
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
| 2 | Permisos y clave de Maps | Hecho |
| 3 | Permiso en tiempo de ejecución | Hecho |
| 4 | Mapa con la ubicación actual | Hecho |
| 5 | Marcadores de los lugares | Hecho |
| 6 | Distancias y orden | Hecho |
| 7 | Lista de lugares y selección | Pendiente |
| 8 | Lugar más cercano | Pendiente |
| 9 | Estados de error | Pendiente |
| 10 | README con capturas y respuestas | Pendiente |

## Clave de Google Maps

La clave no está en el código ni en el Manifest. Se guarda en `local.properties`
(que no se sube a GitHub):

```
MAPS_API_KEY=tu_clave
```

`app/build.gradle.kts` lee ese archivo y la pasa al Manifest con
`manifestPlaceholders["MAPS_API_KEY"]`, que la pone en el `meta-data`
`com.google.android.geo.API_KEY`. Si no hay clave, la app compila igual, pero el
mapa sale gris.

Restricción de la clave en Google Cloud (Maps SDK for Android):

- Paquete: `com.tecsup.campusgps`
- SHA-1 de depuración (`./gradlew signingReport`):
  `1E:D0:35:9B:C2:34:82:EF:E5:22:60:B5:E8:4F:99:9B:E4:6E:75:A3`

## Pendiente

- Commits 7 a 10 de la Fase 1.
- Fase 2 en la rama `mejora-ia` (3 mejoras con Gemini y `PROMPTS.md`).

## Notas

- Se prueba en el emulador `CampusGPS_Play` (Pixel 7, API 35 con Google Play),
  con la ubicación en el punto base `-12.0464, -77.0428`. El emulador `Lab03` no
  sirve: no trae Google Play Services.
- `maps-compose` va en la versión 6.4.1: la 8.4.0 de la guía exige Android Gradle
  Plugin 9.1 y compileSdk 37, y no compila con este proyecto. Por eso los
  marcadores usan `rememberMarkerState(position = ...)`: `rememberUpdatedMarkerState`,
  que sugiere la guía, no existe en la 6.4.1.
