# TecsupNite: instrucciones para Claude

Este archivo explica a otra sesión de Claude cómo funciona el proyecto y cómo dejarlo corriendo en Android Studio, sobre todo en una computadora del instituto (Tecsup). Léelo completo antes de tocar nada.

## Qué es

- Laboratorio 11-12 de Programación en Móviles, **individual**, alumno **Renzo León**
- App Android en Kotlin + Jetpack Compose + Material 3: la tienda de hoy de Fortnite
- API pública `https://fortnite-api.com` (sin clave): lista `v2/shop?language=es`, detalle `v2/cosmetics/br/{id}`
- Firebase (proyecto `tecsupnite`): login con correo y contraseña, y el rol de cada usuario en Firestore
- Paquete `com.tecsup.fortnite`, minSdk 26, compileSdk y targetSdk 35

Archivos que conviene leer después de este:

- **`CREDENCIALES.md`:** usuarios de prueba, contraseña, UIDs y proyecto de Firebase
- **`GUIA-PRESENTACION.md`:** dónde está cada archivo, cada requisito (R01-R13) con su línea y el guion de la demostración
- **`README.md`:** diseño, capturas y tabla de pruebas PA-01 a PA-14
- **`PROMPTS.md`:** solo en `mejora-ia`; consultas a la IA de la Fase 2

## Ramas

- **`main`:** Fase 1, la app que pide la guía. También tiene el informe en `Semana 12/LAB12-LEON-RENZO-2026.docx` y `.pdf`
- **`mejora-ia`:** Fase 2 = todo lo de `main` + 15 pruebas unitarias, búsqueda sin tildes, deslizar para actualizar y login mejorado. El release v1.0 (APK) sale de aquí

`main` y `mejora-ia` tienen historias separadas por labs anteriores. **No hagas merge entre ellas**: salen unos 20 conflictos de otras semanas. Un cambio que deba ir en las dos ramas se commitea dos veces.

## Reglas del usuario

- Commits sin `Co-Authored-By` ni ninguna marca de IA; mensajes en español, en minúsculas, tipo `docs: ...` o `feat: ...`
- Deja el repo en `main` al terminar. Renzo cambia de rama él mismo en Android Studio. Sin worktrees
- Antes de cada commit, actualiza el `README.md` con lo hecho y lo pendiente
- Código simple, con lo visto en el curso; comentarios cortos en español
- Diseño: Burbank solo en títulos, Inter en el resto; azul tipo Fortnite `#0A63D8`; sin morado, sin neón, sin fondo de rayos
- Textos con tildes: escríbelos con Python o con la herramienta Write, nunca con un `.ps1` sin BOM (PowerShell 5 rompe las tildes)
- El `README.md` usa saltos de línea CRLF: al reemplazar varias líneas usa una expresión regular con `\r?\n` y revisa el resultado con `git show`

## Instalar en Android Studio (computadora del instituto)

El repositorio ya trae todo lo necesario para compilar: `app/google-services.json` y `gradle/wrapper/gradle-wrapper.jar`. Solo falta `local.properties`, que Android Studio crea solo con la ruta del SDK de esa computadora.

### Pasos

1. Clona el repositorio:

   ```bash
   git clone https://github.com/RXDG2908/Moviles-Android-CD.git
   ```

2. En Android Studio abre **File → Open** y elige la carpeta `Moviles-Android-CD/Semana 12/TecsupNite`. No abras la raíz del repositorio: ahí hay muchos proyectos y Gradle no encuentra ninguno
3. Si Android Studio pregunta si confías en el proyecto, elige **Trust Project**
4. Espera a que termine **Gradle Sync** (la primera vez descarga Gradle 8.13 y las dependencias; puede tardar varios minutos)
5. Para la Fase 2, cambia a `mejora-ia` con el menú de Git (abajo a la derecha) y sincroniza otra vez
6. Crea o elige un emulador con imagen **Google Play** o **Google APIs**, API 26 o superior. Firebase Authentication necesita los servicios de Google
7. Pulsa **Run ▶** y entra con un usuario de `CREDENCIALES.md`

### Versiones del proyecto

| Pieza | Versión | Dónde se cambia |
|---|---|---|
| Android Gradle Plugin (AGP) | 8.13.2 | `build.gradle.kts` (raíz del proyecto), línea 3 |
| Gradle | 8.13 | `gradle/wrapper/gradle-wrapper.properties` |
| Kotlin | 2.0.21 | `build.gradle.kts`, líneas 4 a 6 |
| KSP (para Room) | 2.0.21-1.0.28 | `build.gradle.kts`, línea 8 (debe empezar igual que Kotlin) |
| JDK | 17 o 21 | Android Studio → Settings → Build Tools → Gradle → Gradle JDK |

### Si el Android Studio del instituto es más antiguo

El error típico es *«The project is using an incompatible version of the Android Gradle plugin»* o *«requires Android Studio ... or newer»*. AGP 8.13 necesita Android Studio Narwhal 3 Feature Drop (2025.1.3) o más nuevo. Si el instituto tiene uno anterior, **no actualices Android Studio**: baja la versión de AGP en `build.gradle.kts` y la de Gradle en `gradle-wrapper.properties`, sin commitear el cambio:

| Android Studio del instituto | AGP | Gradle (`distributionUrl`) |
|---|---|---|
| Narwhal 2025.1.1 / 2025.1.2 | 8.11.1 | `gradle-8.13-bin.zip` |
| Meerkat 2024.3 | 8.9.1 | `gradle-8.11.1-bin.zip` |
| Ladybug 2024.2 | 8.7.3 | `gradle-8.9-bin.zip` |

Con versiones más antiguas que Ladybug, compileSdk 35 da problemas; mejor usa el APK del release.

Después del cambio pulsa **Sync Now**. Revierte esos dos archivos antes de cualquier commit (`git checkout -- build.gradle.kts gradle/wrapper/gradle-wrapper.properties`).

### Otros problemas comunes

- **«SDK location not found»:** crea `local.properties` con `sdk.dir=C\:\\Users\\<usuario>\\AppData\\Local\\Android\\Sdk` (la ruta del SDK de esa PC). No lo subas a Git
- **Falta la plataforma 35:** SDK Manager → SDK Platforms → Android 15 (API 35)
- **Gradle no descarga nada:** la red del instituto puede tener proxy. Settings → Appearance & Behavior → System Settings → HTTP Proxy, o pedir los datos de proxy al laboratorio
- **«Invalid Gradle JDK»:** en Settings → Gradle → Gradle JDK elige el JDK que trae Android Studio (`jbr-17` o `jbr-21`)
- **La tienda sale vacía o con error:** la red bloquea `fortnite-api.com`. Prueba con los datos del celular o instala el APK en un teléfono
- **El login dice «Sin conexión»:** el emulador no tiene internet o no tiene servicios de Google; usa una imagen con Google Play
- **El login entra pero no aparece Admin:** revisa en Firestore que el documento `usuarios/{UID}` exista con `rol = admin` (UIDs en `CREDENCIALES.md`)
- **Plan B:** descarga `TecsupNite-v1.0.apk` del release v1.0 y arrástralo al emulador o instálalo en un celular

## Compilar y probar desde la terminal

Desde la carpeta `TecsupNite`, con `JAVA_HOME` apuntando a un JDK 17 o 21:

```bash
./gradlew assembleDebug
```

```bash
./gradlew testDebugUnitTest
```

En Windows usa `gradlew.bat`. El APK queda en `app/build/outputs/apk/debug/app-debug.apk`. Las 15 pruebas unitarias solo existen en `mejora-ia`.

En la PC de Renzo (no la del instituto): JDK en `~/.jdks/temurin-21.0.12`, SDK en `%LOCALAPPDATA%\Android\Sdk`, emulador `Lab03`.

## Cómo funciona el código

Todo el Kotlin está en `app/src/main/java/com/tecsup/fortnite/`. Arquitectura MVVM con Room como fuente de verdad:

```text
fortnite-api.com → Retrofit (data/remote) → ItemRepositoryImpl → Room (data/local) → ViewModel → pantalla Compose
Firebase Auth + Firestore (data/auth) → LoginViewModel → AppNav decide login o app y si muestra Admin
```

- **`AppContainer.kt`:** crea una sola vez Room, Retrofit, Firebase y el notificador; `ui/ViewModelFactory.kt` los pasa a cada ViewModel
- **`data/repository/ItemRepositoryImpl.kt`:** `refrescar()` descarga la tienda, quita pistas, instrumentos y autos, conserva los favoritos y reemplaza la tabla en una transacción
- **`data/auth/FirebaseAuthRepository.kt`:** inicia sesión y lee `usuarios/{uid}.rol`; sin internet asume `USUARIO`
- **`ui/UiState.kt`:** `Cargando`, `Exito`, `Error` para todas las pantallas
- **`ui/navigation/AppNav.kt`:** barra inferior (Tienda, Favoritos, Admin solo para admin); el detalle es un destino `dialog`
- **`notification/NotificadorAndroid.kt`:** canal «sincronizacion», aviso «Se guardaron N elementos» al sincronizar
- Room versión 2 con `fallbackToDestructiveMigration`: si cambias `ItemEntity`, sube la versión en `AppDatabase.kt`

Para el detalle de cada archivo y requisito, usa `GUIA-PRESENTACION.md`. Si cambias código, revisa que las líneas citadas ahí sigan correctas.

## Firebase desde Claude

El conector de Firebase (MCP) solo muestra las herramientas de Firestore si la sesión se abre con la carpeta `TecsupNite` como directorio del proyecto (porque ahí está `firebase.json`). Con él puedes listar usuarios de Authentication y leer o crear documentos en `usuarios`. No intentes leer los tokens del CLI de Firebase.
