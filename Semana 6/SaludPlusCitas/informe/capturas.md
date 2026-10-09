# capturas.md — Tomar las capturas y generar el informe en Word

Instrucciones para **Claude Code en la Mac**. Se usan después de `inicio.md` (con el
emulador encendido y las dos ramas compiladas).

El resultado es `Informe_Lab06_SaludPlus.docx` en esta carpeta, con las 27 capturas en
su lugar.

## Reglas

- No modifiques el código de la app. Solo se toman capturas y se genera el Word.
- **Tú no tocas el emulador:** el usuario navega. Tú le dices qué pantalla abrir,
  esperas a que diga "listo" y tomas la captura con `adb`.
- Todas las capturas se guardan en **la carpeta `informe/capturas` de la rama
  `mejora-ia`**: `PROYECTO_IA/informe/capturas/` (ver `inicio.md`), incluso las de `main`.

## 1. Preparar el emulador para las capturas

Barra de estado limpia (hora fija, batería llena, sin notificaciones):

```bash
adb shell settings put global sysui_demo_allowed 1
adb shell am broadcast -a com.android.systemui.demo -e command enter
adb shell am broadcast -a com.android.systemui.demo -e command clock -e hhmm 0900
adb shell am broadcast -a com.android.systemui.demo -e command battery -e level 100 -e plugged false
adb shell am broadcast -a com.android.systemui.demo -e command notifications -e visible false
```

Para tomar cada captura (cambia `<nombre>`):

```bash
adb exec-out screencap -p > "PROYECTO_IA/informe/capturas/<nombre>.png"
```

Después de cada captura, confirma que el archivo pesa más de 0 bytes.

## 2. Capturas de la rama `main` (Fase 1)

Instala y abre la app de `main` (paso 8 de `inicio.md`). Usuario: teléfono `987654321`,
contraseña `123456`.

| Nombre | Qué debe verse en pantalla |
|---|---|
| `f1-01-splash` | Splash al abrir la app |
| `f1-02-registro` | Registro con los campos llenos (por ejemplo, un error de validación visible) |
| `f1-03-login` | Iniciar sesión con teléfono y contraseña escritos |
| `f1-04-inicio` | Inicio: saludo "¡Hola, Juan!", mosaicos, especialidades destacadas y barra inferior |
| `f1-05-especialidades` | Especialidades con algo escrito en el buscador (por ejemplo "card") |
| `f1-06-medicos` | Médicos de una especialidad |
| `f1-07-fecha-hora` | Fecha y hora con un día y una hora elegidos |
| `f1-08-confirmar` | Confirmar cita |
| `f1-09-cita-agendada` | Cita agendada |
| `f1-10-mis-citas` | Mis citas con la cita recién creada |
| `f1-11-perfil` | Perfil |

## 3. Capturas de la rama `mejora-ia`

Instala y abre la app de `mejora-ia` (paso 8 de `inicio.md`).

### Calendario dinámico (Fase 2)

Primero inicia sesión con `juan@correo.com` / `123456` y entra a **Agendar cita →
sede Miraflores → Medicina General → Dr. Fernando Chávez** (o cualquier médico con
cupos).

| Nombre | Qué debe verse en pantalla |
|---|---|
| `f2-01-calendario-semana-actual` | Fecha y hora en la semana actual (flecha `<` sin efecto), sin día elegido |
| `f2-02-calendario-semana-siguiente` | Después de tocar `>`: otra semana (si se puede, con cambio de mes) |
| `f2-03-dia-y-hora-elegidos` | Un día y una hora elegidos, con "Continuar" habilitado |
| `f2-04-confirmar-fecha-espanol` | Confirmar cita con la fecha en texto en español |

Vuelve atrás sin agendar (o agenda; no afecta las capturas siguientes). Cierra sesión
desde Perfil.

### Mejoras del profesor

| Nombre | Qué debe verse en pantalla |
|---|---|
| `p-01-registro-correo` | Registro lleno con María López, `912345678`, `maria@correo.com`, `654321` |
| `p-02-login-cuenta-creada` | Login después de registrarse: correo ya escrito y aviso "Cuenta creada, inicia sesión" |
| `p-03-inicio-mis-doctores` | Inicio con el mosaico "Mis doctores" |
| `p-04-sedes` | Elige tu sede (lista de sedes) |
| `p-05-medicos-por-sede` | Médicos de Medicina General en Miraflores, con los chips de disponibilidad de colores |
| `p-06-calendario-semaforo` | Fecha y hora de la Dra. Lucía Fernández: días con colores y días bloqueados. Si esa semana no se ven varios colores, avanzar con `>` hasta que sí |
| `p-07-horas-bloqueadas` | El mismo calendario con un día elegido: horas libres en verde, ocupadas tachadas y una hora elegida en azul |
| `p-08-confirmar-con-sede` | Confirmar cita con la sede y su dirección |
| `p-09-cita-agendada-sede` | Cita agendada con la sede |
| `p-10-mis-citas-por-sede` | Pestaña Citas: sede arriba con "Cambiar sede" y la cita en la lista |
| `p-11-mis-doctores` | Mis doctores agrupados por especialidad |
| `p-12-mis-doctores-busqueda` | Mis doctores con algo escrito en el buscador (por ejemplo "rojas") |

## 4. Generar el Word

```bash
cd "PROYECTO_IA/informe"
npm install        # solo la primera vez (necesita Node.js; si falta: brew install node)
node generar_informe.js
open Informe_Lab06_SaludPlus.docx
```

El script dice cuántos commits leyó de cada rama y qué capturas faltan, si falta
alguna. Las que falten salen en el Word como un recuadro "Captura pendiente".

Al abrir el Word por primera vez, actualiza el índice: clic derecho sobre el índice →
**Actualizar campos** (o en Word: Referencias → Actualizar tabla).

## 5. Revisar con el usuario

Antes de dar por terminado:

- Que no falte ninguna captura (el script debe decir "Todas las capturas están
  incluidas").
- Que las secciones 7 (preguntas de reflexión) y 8 (observaciones y conclusiones) las lea
  el usuario: son un borrador y la guía pide que sean de elaboración individual.
- No hagas commit del Word ni de las capturas sin que el usuario lo pida.

Para salir del modo de capturas del emulador:

```bash
adb shell am broadcast -a com.android.systemui.demo -e command exit
```
