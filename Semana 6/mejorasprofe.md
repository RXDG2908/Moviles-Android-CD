# Mejoras pedidas por el profesor — SaludPlus Citas

## Lo que pidió el profesor (texto original)

> UNA VEZ REGISTRADO, NO PUEDE MANDAR A MANDAR CITA DIRECTAMENTE, SINO MANDAR A RE-LOGIN. IA potenciada.
>
> Una vez ingresado, agregar nuevo "Mis doctores", ver lista de doctores, organizados por categorías/especialidad.
>
> ESCALAR: agregar SEDES, ejemplo: SEDES en independencia o Molina, cuando se elija SEDE, recién ahí entrara a mis citas, y mis citas salen de la pestaña principal
> /antes de llegar a agendar citar, primero salir locales de ahí recién agendar cita/

---

## Prompt para Claude Code (nube)

Copia todo lo que está dentro del bloque y pégalo como primer mensaje de la sesión:

```text
Trabaja en el repositorio RXDG2908/Moviles-Android-CD, proyecto Android ubicado en
"Semana 6/SaludPlusCitas" (Kotlin + Jetpack Compose Material3 + Navigation Compose 2.7.7,
paquete com.saludplus.citas). La guía oficial del curso está en
"Semana 6/GLAB-S06-JLEONS-2026-02_CD.md": léela primero para entender el estilo y las reglas.

## Contexto del proyecto (ya existe, Fase 1 terminada en main)
- data/model: Usuario(nombre, telefono, correo, contrasena), Especialidad(id, nombre, descripcion),
  Medico(id, nombre, especialidadId, cargo, cmp, calificacion, resenas, disponibilidad),
  Cita(id, telefonoUsuario, medicoId, fecha "yyyy-MM-dd", hora "HH:mm", motivo, estado).
- data/repository/Repositorio.kt: object con colecciones en memoria (citas es mutableStateListOf,
  usuarioActual es mutableStateOf). NO hay base de datos.
- navigation/Rutas.kt (constantes + funciones helper con parámetros) y navigation/AppNavigation.kt (NavHost).
- ui/components/Componentes.kt: BarraSuperior, BotonPrincipal, CampoFormulario, BarraInferior,
  TarjetaEspecialidad, TarjetaMedico, TarjetaCita, FilaDato, ResumenMedico, AvatarMedico, etc.
- ui/theme/Color.kt: paleta exacta del diseño (AzulSalud, AzulMarino, TextoOscuro, TextoGris,
  GrisSuave, AzulClaro, VerdeClaro, MoradoClaro, NaranjaClaro, Verde, Morado, Naranja, Rojo...).
- Flujo actual: Splash → Registro → Login → Home (tiles: Agendar cita, Mis citas, Mis datos,
  Resultados + Especialidades destacadas) → Especialidades → Médicos → Fecha/Hora →
  Confirmar cita → Cita agendada. BarraInferior: Inicio, Citas, Resultados, Perfil.

## Reglas obligatorias
1. Crea la rama `mejoras-profe` desde `main` y trabaja solo ahí. No toques `main` ni `mejora-ia`.
2. Código básico y simple, nivel de curso, con comentarios en español explicando cada parte
   (mismo estilo de comentarios que el código existente). No agregues librerías nuevas.
3. Sin base de datos: todo en colecciones dentro de Repositorio (listOf, mutableStateListOf),
   usando operaciones de colecciones de Kotlin (filter, find, any, map, groupBy, sortedBy...).
4. No cambies los nombres ni los parámetros de las funciones que ya existen en Repositorio;
   solo agrega funciones nuevas o parámetros nuevos con valor por defecto.
5. Reutiliza los componentes y colores existentes; las pantallas nuevas deben verse con el
   mismo estilo (fondo Blanco, BarraSuperior blanca con flecha, botones AzulSalud de 48dp).
6. No cambies la versión de AGP (9.1.0) ni los archivos de Gradle salvo que sea imprescindible.
7. Commits pequeños, en español, uno por mejora (mínimo 4). NO agregues líneas
   "Co-Authored-By" ni ninguna firma de IA en los mensajes de commit.
8. Antes de cada commit verifica que compila con `./gradlew assembleDebug` desde
   "Semana 6/SaludPlusCitas" (si el entorno no tiene Android SDK, dilo claramente y revisa
   el código con mucho cuidado: imports, tipos de argumentos de navegación, etc.).
9. Al final, agrega/actualiza "Semana 6/SaludPlusCitas/PROMPTS.md" con: el prompt usado,
   la respuesta resumida de la IA y lo que hubo que corregir. Haz push de la rama.

## Mejoras a implementar (en este orden)

### Mejora 1 — Después del registro, obligar a iniciar sesión (re-login)
- Al registrarse con éxito NO se debe iniciar sesión automáticamente ni llegar a agendar cita.
- RegistroScreen debe navegar a Login quitando Registro del historial
  (`navigate(Rutas.LOGIN) { popUpTo(Rutas.REGISTRO) { inclusive = true } }`), mostrando un
  mensaje "Cuenta creada, inicia sesión" y con el teléfono ya escrito en Login
  (puedes pasar el teléfono como argumento opcional de la ruta de Login).
- Verifica que registrarUsuario NO asigne usuarioActual.
- Protección extra: si alguna pantalla del flujo de agendamiento se abre con
  Repositorio.usuarioActual == null, debe redirigir a Login.
- Commit: "Obliga a iniciar sesion despues de registrarse"

### Mejora 2 — Sedes (locales) antes de agendar
- Nuevo modelo data/model/Sede.kt: Sede(id: Int, nombre: String, direccion: String, distrito: String).
- En Repositorio agrega `val sedes` con al menos 2 sedes:
  1 "SaludPlus Independencia" — "Av. Túpac Amaru 456" — "Independencia"
  2 "SaludPlus La Molina" — "Av. La Molina 789" — "La Molina"
  y `var sedeActual by mutableStateOf<Sede?>(null)`, más `seleccionarSede(id)` y `obtenerSede(id)` (find).
- Cada médico atiende en una o más sedes: agrega a Medico el campo
  `sedes: List<Int> = listOf(1, 2)` (con valor por defecto para no romper nada) y reparte los
  médicos entre las sedes. Agrega `medicosPorSede(sedeId, especialidadId)` (filter) y haz que
  la pantalla de Médicos muestre solo los de la sede elegida.
- Agrega `sedeId: Int = 0` al final de Cita; agendarCita guarda la sede actual.
- Nueva pantalla ui/screens/sedes/SedesScreen.kt (ruta "sedes"): título "Elige tu sede",
  LazyColumn de tarjetas con icono de ubicación, nombre, dirección y distrito.
  Al tocar una sede: seleccionarSede y continuar a Especialidades.
- Flujo nuevo: Home → "Agendar cita" → SedesScreen → Especialidades → Médicos → Fecha/Hora →
  Confirmar → Cita agendada. Antes de llegar a agendar SIEMPRE se elige primero la sede.
- ConfirmarCitaScreen y CitaExitosaScreen deben mostrar la sede y su dirección
  (en vez de la dirección fija "Av. Los Olivos 123, Lima").
- Commit: "Agrega sedes y seleccion de sede antes de agendar cita"

### Mejora 3 — "Mis citas" sale de la pantalla principal y depende de la sede
- Quita el tile "Mis citas" de la grilla de Home (pantalla principal). En su lugar pon el
  tile "Mis doctores" (Mejora 4). Mantén la grilla 2x2 con los mismos colores.
- Mis citas solo se ve después de elegir una sede: si sedeActual es null, la pestaña
  "Citas" de la BarraInferior lleva primero a SedesScreen y después a Mis citas
  (usa un argumento o un flag para saber a qué pantalla volver después de elegir sede).
- MisCitasScreen muestra arriba la sede actual (con opción "Cambiar sede") y lista solo
  las citas de esa sede: agrega `citasDelUsuarioPorSede(sedeId)` (filter + sortedWith
  compareBy fecha thenBy hora, igual que citasDelUsuario).
- Commit: "Mueve mis citas fuera del inicio y las filtra por sede"

### Mejora 4 — "Mis doctores" agrupados por especialidad
- Nuevo tile "Mis doctores" en Home y nueva pantalla ui/screens/doctores/MisDoctoresScreen.kt
  (ruta "mis_doctores").
- Muestra TODOS los médicos agrupados por especialidad usando
  `medicos.groupBy { it.especialidadId }`: un encabezado por especialidad (nombre de la
  especialidad + IconoEspecialidad) y debajo sus médicos con TarjetaMedico, ordenados por
  calificación (sortedByDescending). Usa LazyColumn con `stickyHeader` o `item` para los
  encabezados.
- Agrega un buscador por nombre de médico arriba (filter + contains, ignoreCase).
- Al tocar un médico, va directo a Fecha/Hora de ese médico (si no hay sede elegida,
  primero pasa por SedesScreen).
- Agrega en Repositorio `medicosAgrupadosPorEspecialidad(): Map<Especialidad, List<Medico>>`.
- Commit: "Agrega mis doctores agrupados por especialidad"

## Al terminar
- Revisa todo el flujo de punta a punta mentalmente (registro → login → sede → especialidad →
  médico → fecha/hora → confirmar → mis citas de esa sede → mis doctores) y que el bloqueo de
  horarios ya reservados siga funcionando.
- Actualiza PROMPTS.md, haz push de la rama `mejoras-profe` y dame un resumen con la lista
  de commits y cómo probar cada mejora en el emulador.
```

---

## Notas

- La rama es `mejoras-profe`, separada de `mejora-ia` (que es la Fase 2 de la guía: calendario dinámico).
- Lo de "SEDES" lo interpreté así: primero se elige el local (Independencia o La Molina) y recién después se agenda o se ven las citas; "Mis citas" deja de estar en la grilla del inicio. Si el profe lo quería distinto, ajusta la Mejora 3 antes de pegar el prompt.
- En la nube no hay emulador: prueba el resultado aquí en la Mac después de hacer `git pull`.
