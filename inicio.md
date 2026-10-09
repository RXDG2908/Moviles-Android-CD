# inicio.md — Preparar la presentación de SaludPlus Citas (Mac)

Instrucciones para **Claude Code en la Mac del alumno**. El objetivo es dejar todo listo
para presentar al profesor la tarea **Clínica SaludPlus — App Paciente** (Semana 6):

- las **dos ramas** actualizadas y abiertas en Android Studio, cada una en su propia
  ventana;
- las dos compiladas;
- un emulador encendido con la app instalada;
- un resumen final con los datos para la demo.

Sigue los pasos en orden. Al terminar cada paso, di en una línea qué hiciste y si salió
bien.

---

## 0. Reglas (obligatorias)

1. **No modifiques código, no hagas commit, no hagas push y no hagas merge.** Esta tarea
   solo prepara y ejecuta.
2. **Nunca** mezcles `mejora-ia` en `main` ni `main` en `mejora-ia`. Son ramas separadas:
   - `main` → **Fase 1, sin IA**.
   - `mejora-ia` → **Fase 2, con IA** (calendario dinámico) **+ mejoras del profesor**
     (re-login, sedes, Mis citas por sede, Mis doctores, días disponibles del médico).
3. Antes de cualquier comando que borre cambios locales (`reset --hard`, `checkout --`,
   `clean`, `stash drop`), **pregunta al usuario** y explica qué se perdería.
4. Si algo falla (compilación, emulador, Gradle), **no lo arregles cambiando el código**.
   Muestra el error exacto y propón la solución al usuario.
5. Ignora la carpeta `Semana 6/TecsupStore`: no es parte de esta presentación.

---

## 1. Ubicar el repositorio y revisar su estado

El repositorio es `RXDG2908/Moviles-Android-CD`. Búscalo en la Mac, por ejemplo con
`find ~ -maxdepth 4 -type d -name "Moviles-Android-CD" 2>/dev/null`. Si no existe, pregunta
al usuario si quiere clonarlo y dónde.

En adelante, `REPO` es esa carpeta. Dentro de ella:

```bash
git status
git fetch origin --prune
```

Si `git status` muestra cambios sin commit, **detente y pregunta** qué hacer con ellos.

## 2. Actualizar las dos ramas

> El historial de `mejora-ia` se reescribió (para quitar la firma de IA de los commits) y
> `main` se regresó a un commit anterior para deshacer un merge equivocado. Por eso un
> `git pull` normal puede fallar en esta Mac.

Para cada rama (`main` y `mejora-ia`):

```bash
git checkout <rama>
git pull --ff-only origin <rama>
```

Si `pull --ff-only` falla porque las ramas divergieron:

1. Muestra los commits que solo existen en esta Mac: `git log --oneline origin/<rama>..<rama>`.
2. Si esos commits ya están en GitHub con otro hash (mismos mensajes), o son el merge
   equivocado "Merge pull request #1", **pregunta al usuario** si se puede usar
   `git reset --hard origin/<rama>`. Ejecútalo solo si el usuario dice que sí.
3. Si hay commits locales que no están en GitHub, **no los borres**: avisa al usuario.

Cuando termines, comprueba:

```bash
git log --oneline -1 origin/main
git log --oneline -1 origin/mejora-ia
git merge-base --is-ancestor origin/mejora-ia origin/main && echo "ERROR: main contiene mejora-ia" || echo "OK: ramas separadas"
```

Debe salir `OK: ramas separadas`.

## 3. Preparar una carpeta para cada rama

Para tener las dos ramas abiertas a la vez en Android Studio sin cambiar de rama durante
la presentación, usa un **worktree** (una segunda carpeta del mismo repositorio):

```bash
cd REPO
git checkout main
git worktree add ../Moviles-Android-CD-mejora-ia mejora-ia
```

- `REPO` queda en **main**.
- `../Moviles-Android-CD-mejora-ia` queda en **mejora-ia**.

Si el worktree ya existía, solo actualízalo (`git -C ../Moviles-Android-CD-mejora-ia status`).

El proyecto en cada carpeta está en `Semana 6/SaludPlusCitas`:

- `PROYECTO_MAIN = REPO/Semana 6/SaludPlusCitas`
- `PROYECTO_IA = REPO/../Moviles-Android-CD-mejora-ia/Semana 6/SaludPlusCitas`

(La ruta tiene un espacio en "Semana 6": usa siempre comillas).

## 4. Revisar las herramientas

```bash
ls "/Applications/Android Studio.app"
export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
export ANDROID_HOME="$HOME/Library/Android/sdk"
export PATH="$ANDROID_HOME/platform-tools:$ANDROID_HOME/emulator:$PATH"
java -version
adb version
```

- Si Android Studio no está instalado o falta el SDK, detente y avísale al usuario.
- Cada proyecto necesita `local.properties` con `sdk.dir=/Users/<usuario>/Library/Android/sdk`.
  Android Studio lo crea solo al abrir el proyecto; si no existe, créalo con esa línea
  (está en `.gitignore`, no se sube).

## 5. Abrir los dos proyectos en Android Studio

```bash
open -a "Android Studio" "PROYECTO_MAIN"
open -a "Android Studio" "PROYECTO_IA"
```

Se abren dos ventanas. Pídele al usuario que espere a que termine el **Gradle Sync** en
las dos. Es normal que en la rama `mejora-ia` Android Studio subraye las llamadas a
`LocalDate` con el aviso "Call requires API level 26": la app compila igual.

## 6. Compilar las dos ramas

En cada proyecto (primero `PROYECTO_MAIN` y luego `PROYECTO_IA`):

```bash
cd "PROYECTO_..."
./gradlew assembleDebug
```

La primera vez puede tardar varios minutos (descarga Gradle y dependencias).

Si falla, muestra el error exacto y **detente**. La rama `mejora-ia` nunca se compiló
antes (se escribió en un entorno sin Android SDK): si hay errores ahí, anótalos para
corregirlos en otra sesión y avísale al usuario.

Guarda la ruta de cada APK:

- `APK_MAIN = PROYECTO_MAIN/app/build/outputs/apk/debug/app-debug.apk`
- `APK_IA = PROYECTO_IA/app/build/outputs/apk/debug/app-debug.apk`

## 7. Preparar el emulador

**Debe ser API 26 o mayor** (se recomienda API 34 o 35): la rama `mejora-ia` usa
`java.time.LocalDate`, que en API 24 y 25 cierra la Pantalla 6.

```bash
emulator -list-avds
```

- Si hay un AVD con API 26 o mayor, úsalo.
- Si no hay ninguno, pídele al usuario que cree uno en Android Studio:
  **Device Manager → Create Virtual Device → Pixel 8 → API 35**.

Enciéndelo en segundo plano y espera a que arranque:

```bash
emulator -avd <NOMBRE_AVD> -no-snapshot-load > /dev/null 2>&1 &
adb wait-for-device
until [ "$(adb shell getprop sys.boot_completed | tr -d '\r')" = "1" ]; do sleep 2; done
echo "Emulador listo"
```

## 8. Instalar y abrir la app

Las dos ramas usan el mismo `applicationId` (`com.saludplus.citas`), así que **en el
emulador solo cabe una a la vez**: instalar una reemplaza a la otra. Para la presentación
se empieza con `main` y luego se cambia a `mejora-ia`.

```bash
# Rama main (Fase 1)
adb install -r "APK_MAIN"
adb shell am start -n com.saludplus.citas/.MainActivity
```

Para cambiar a la rama con IA durante la presentación:

```bash
adb install -r "APK_IA"
adb shell am force-stop com.saludplus.citas
adb shell am start -n com.saludplus.citas/.MainActivity
```

Para volver a `main`, se repite lo mismo con `APK_MAIN`. Los datos viven en memoria, así
que al reinstalar la app empieza de cero.

Deja instalada la rama **main** al terminar, lista para empezar.

## 9. Resumen final para el usuario

Muéstrale al usuario este resumen, con los datos reales:

1. **Ramas:** el hash y el mensaje del último commit de `main` y de `mejora-ia`, y que
   están separadas.
2. **Carpetas:** `PROYECTO_MAIN` y `PROYECTO_IA`, abiertas en Android Studio.
3. **Compilación:** OK o error, de cada rama.
4. **Emulador:** nombre y API, encendido, con la rama `main` instalada.
5. **Comandos para cambiar de rama en el emulador** (los del paso 8, con las rutas
   reales).
6. **Guion de la demo** (sección siguiente).

---

## Guion de la demo (para el usuario)

**Usuario de prueba:**

- Rama `main`: teléfono `987654321`, contraseña `123456` (Juan Pérez).
- Rama `mejora-ia`: correo `juan@correo.com` **o** teléfono `987654321`, contraseña
  `123456` (Juan Pérez). En esta rama el Login tiene un solo campo, "Correo o teléfono".

También se puede registrar uno nuevo.

### Rama `main` — Fase 1 (sin IA)

1. Splash → **Registro** con validaciones → **Iniciar sesión**.
2. **Inicio:** saludo con el nombre, mosaicos, LazyRow de especialidades destacadas y
   **NavigationBar** (Inicio, Citas, Resultados, Perfil).
3. **Agendar cita:** Especialidades (búsqueda en tiempo real) → Médicos (por
   `especialidadId`, de mejor a menor calificación) → **Fecha y hora** (días fijos,
   LazyVerticalGrid de horarios) → **Confirmar** → **Cita agendada** (`popUpTo`).
4. Volver a agendar con el mismo médico y fecha: **la hora reservada ya no aparece**.
5. **Mis citas** y **Perfil** → Cerrar sesión.

### Rama `mejora-ia` — Fase 2 (con IA) + mejoras del profesor

1. **Inicio de sesión con correo o teléfono, y re-login:** al registrarse (el correo es
   obligatorio) **no entra directo**. Va a Login con el correo ya escrito y el aviso
   "Cuenta creada, inicia sesión".
2. **Inicio:** "Mis citas" ya no está en los mosaicos; en su lugar está **Mis doctores**.
3. **Agendar cita → Elige tu sede** (12 sedes en Lima y Callao) → Especialidades →
   Médicos **solo de esa sede**.
   Cada sede tiene las 7 especialidades, con 2 médicos de cada una.
4. **Calendario dinámico (Fase 2 de la guía):**
   - días hábiles reales desde hoy, con `LocalDate`;
   - flechas `<` `>` por semana (no se puede retroceder antes de la semana actual);
   - mes y año que cambian con la semana;
   - la hora se reinicia al cambiar de día.
5. **Solo días disponibles del médico** (pedido del profesor): debajo del médico dice
   "Atiende: …". Ejemplo para mostrar: **Medicina General → sede Miraflores →
   Dra. Lucía Fernández**, que atiende solo **martes y jueves**.
6. **Confirmar cita:** fecha en español ("Martes 13 de octubre 2026") y la sede con su
   dirección. **Cita agendada** también muestra la sede.
7. **Pestaña Citas:** pide la sede si no hay una elegida. Muestra solo las citas de esa
   sede y tiene **Cambiar sede**.
8. **Mis doctores:** todos los médicos agrupados por especialidad, con buscador. Al
   tocar un médico se va a Fecha y hora (pasa antes por Sedes si hace falta).
9. Mostrar `Semana 6/SaludPlusCitas/PROMPTS.md`: prompts, respuestas y correcciones.

---

## Después de la presentación (solo si el usuario lo pide)

```bash
cd REPO
git worktree remove ../Moviles-Android-CD-mejora-ia
```
