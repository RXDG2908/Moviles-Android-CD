# **Diseño y Desarrollo de Software** 

**<mark>06</mark>** 

# **Menú - Navegación** 

## **Juan José León Suiyon** 

## **Programación en Móviles 4to Ciclo** 

### **LABORATORIO 06** 

###### **OBJETIVO** 

- Implementar un DropdownMenu contextual sobre un elemento específico de una lista. 

- Personalizar un DropdownMenu con íconos y separadores. 

- Implementar un NavigationDrawer como navegación principal de la app, envolviendo un Scaffold existente. 

- Personalizar un NavigationDrawer: encabezado de usuario, íconos, e ítem activo resaltado. 

- Aplicar control de versiones con GitHub en dos fases: desarrollo propio y mejora asistida por IA. 

##### **II. Equipos y materiales** 

- Android Studio con soporte Jetpack Compose. 

- Proyecto de la TECSUP Store (Laboratorios 4 y 5) como base — si no lo conservas, puedes recrear una versión simplificada. 

- Cuenta de GitHub y Git configurado. 

##### **III. Punto de partida** 

Este laboratorio NO empieza de cero: parte de la TECSUP Store que ya construiste (LazyRow de categorías + LazyColumn de secciones con productos, del Laboratorio 4; navegación entre pantallas, del Laboratorio 5). Tu trabajo es agregarle DOS piezas nuevas que le faltaban: 

- Un DropdownMenu en cada tarjeta de producto (menú contextual de 3 puntos). 

- Un NavigationDrawer como navegación principal, reemplazando o complementando la forma en que el usuario se mueve entre secciones. 

##### **IV. Metodología de trabajo: GitHub en dos fases** 

###### **Fase 1 — Desarrollo SIN IA (rama main)** 

Agrega el DropdownMenu y el NavigationDrawer a tu TECSUP Store existente, sin usar ningún asistente de IA. 

1. Si ya tienes el repositorio de la TECSUP Store, continúa sobre él; si no, crea uno nuevo y vincula tu proyecto. 

2. Trabaja sobre la rama main. 

3. Mínimo 6 commits descriptivos y distribuidos. Sugerencia de hitos: (1) ícono de 3 puntos + estado expanded en la tarjeta de producto, (2) DropdownMenu con opciones básicas funcionando, (3) personalización del DropdownMenu (íconos, divisores), (4) estructura del NavigationDrawer con ModalDrawerSheet, (5) navegación real desde los ítems del drawer, (6) personalización del drawer (encabezado, ítem activo). 

4. Al terminar: git push origin main. 

###### **Fase 2 — Mejora con IA (rama mejora-ia)** 

Crea la rama mejora-ia A PARTIR de main. Con ayuda de un asistente de IA, agrega la siguiente mejora: 

- Mejora obligatoria: agrega un badge con contador en el ítem "Favoritos" del drawer (mostrando cuántos productos marcó el usuario como favorito desde el DropdownMenu de cada producto). Esto conecta las dos piezas del laboratorio: una acción en el DropdownMenu debe reflejarse visualmente en el Drawer. 

1. git checkout main 

2. git checkout -b mejora-ia 

3. Mínimo 3 commits descriptivos. 

4. Documenta cada prompt usado en un archivo PROMPTS.md. 

5. Al terminar: git push origin mejora-ia. 

##### **V. Caso propuesto** 

Resultado esperado — DropdownMenu abierto sobre un producto (izquierda) y NavigationDrawer con navegación principal (derecha): 



<!-- Start of picture text -->
TECSUP Store MR Maria Rojas<br>Mas vendidos maria@tecsup.edu.pe<br>a Audifonos : © inicio<br>S/ 89.00<br>© Mis pedidos<br>¥ Favoritos ©: Favoritos<br>2” Compartir<br>©) rerfil<br>A Reportar<br>©  cerrarsesion<br>@&  Smartwatch i<br>$/ 199.00<br>@ Funda celular i<br>S/ 25.00<br><!-- End of picture text -->

_Figura 1. DropdownMenu contextual en un producto y NavigationDrawer de navegación principal._ 

###### **Requisitos funcionales** 

- Cada tarjeta de producto (en cualquier sección de la TECSUP Store) tiene un ícono de 3 puntos (⋮) a la derecha. 

- Al tocarlo, se despliega un DropdownMenu con mínimo 3 opciones: "Favoritos", "Compartir", "Reportar" (o equivalentes de tu criterio). 

- Cada opción del DropdownMenu tiene su ícono correspondiente (leadingIcon). 

- Un ícono ☰ en la topBar abre un NavigationDrawer con mínimo 4 destinos: Inicio, Mis pedidos, Favoritos, Perfil (+ Cerrar sesión opcional). 

- El encabezado del drawer muestra el avatar/iniciales y datos básicos del usuario. 

- El destino activo del drawer se resalta visualmente (color de fondo distinto al resto). 

###### **Estructura de archivos sugerida (agregado a lo ya existente)** 

TarjetaProducto.kt → agrega el ícono de 3 puntos + DropdownMenu AppDrawer.kt → contenido del NavigationDrawer (ModalDrawerSheet) AppNavegacion.kt → se envuelve con ModalNavigationDrawer 

##### **VI. Preguntas de reflexión** 

- ¿Por qué el DropdownMenu se declara dentro de un Box junto al ícono que lo activa, y no en cualquier parte de la pantalla? 

- ¿Qué diferencia de alcance hay entre las opciones del DropdownMenu (afectan solo a un producto) y las del NavigationDrawer (afectan a toda la app)? 

- ¿Cómo tuviste que estructurar tu código para que el contador de favoritos del drawer "se entere" de lo que pasa en el DropdownMenu de cada producto? 

- ¿Qué tuviste que corregir del código que te generó la IA para la mejora del badge de favoritos? 

##### **VII. Observaciones y conclusiones** 

Elaboración individual. Mínimo 2 observaciones y 2 conclusiones. 

- Observaciones: notas aclaratorias, dudas u obstáculos durante el desarrollo. 

- Conclusiones: opinión personal sobre el trabajo realizado, comparando el proceso de la Fase 1 y la Fase 2. 

#### Tarea: Clínica SaludPlus — App Paciente 

Oct 2, 2026 · @C24 

Complementaria al Laboratorio 6, a partir de un código esqueleto. 

##### I. Objetivos 

- Completar una app real de agendamiento de citas médicas a partir de un código esqueleto, llenando cada archivo .kt según sus comentarios TODO. 

- Implementar NavigationBar como menú principal de navegación en la pantalla de Inicio (Inicio, Citas, Resultados, Perfil). 

- Aplicar LazyRow (especialidades destacadas), LazyColumn (especialidades, médicos y citas) y LazyVerticalGrid (horarios) trabajando con colecciones en memoria. 

- Aplicar navegación con paso de parámetros (especialidadId, medicoId, fecha, hora) y popUpTo. 

- Diseñar las vistas que faltan en el diseño de referencia respetando su estilo visual. 

- Aplicar control de versiones con GitHub en dos fases: desarrollo propio y mejora asistida por IA. 

##### II. Punto de partida: código esqueleto 

Esta tarea NO empieza en blanco. Se entrega el proyecto SaludPlusCitas.zip, que se abre directamente en Android Studio (File > Open). El proyecto ya compila y navega: cada pantalla muestra un contenido temporal (PantallaEnConstruccion) con botones para pasar a la siguiente. 

- **Entregados COMPLETOS** (no son el objetivo de aprendizaje): MainActivity.kt, los 4 modelos (Usuario, Especialidad, Medico, Cita), el tema (Color.kt, Theme.kt, Type.kt), Rutas.kt y AppNavigation.kt. 

- **ESQUELETO** (debes completar cada TODO): Repositorio.kt y las 15 pantallas. 

- **Por crear** : los componentes reutilizables del paquete ui/components (Componentes.kt sugiere cuáles). 

Al terminar cada pantalla, borra la llamada a PantallaEnConstruccion. Para ver todo lo pendiente usa View > Tool Windows > TODO. 

Reglas: no usar base de datos (ni Room, ni SQLite, ni Firebase), no cambiar nombres ni parámetros de las funciones del Repositorio ni de las pantallas, y no modificar AppNavigation.kt salvo para agregar pantallas nuevas. 

##### III. Estructura del desarrollo 

El proyecto se organiza por capas (datos, navegación, interfaz) y las pantallas por módulo funcional. Cada pantalla vive en su propio archivo. 

###### Paquetes 

- <mark>com.saludplus.citas ├── MainActivity.kt (completo) ├── data │├── model (completo) ││├── Usuario.kt ││├── Especialidad.kt ││├── Medico.kt ││└── Cita.kt │└── repository │ └── Repositorio.kt (esqueleto: colecciones + funciones TODO) ├── navigation (completo) │├── Rutas.kt │└── AppNavigation.kt └── ui ├── theme (completo) ├── components (por crear) └── screens (esqueleto) ├── auth SplashScreen, RegistroScreen, LoginScreen, TerminosScreen ├── home HomeScreen ├── agendamiento EspecialidadesScreen, MedicosScreen, FechaHoraScreen, │ ConfirmarCitaScreen, CitaExitosaScreen ├── citas MisCitasScreen, DetalleCitaScreen ├── perfil PerfilScreen ├── resultados ResultadosScreen</mark> 

   - <mark>└── notificaciones NotificacionesScreen</mark> 

###### Pantallas 

El diseño trae 7 pantallas. Los botones del diseño llevan a 4 vistas más que no están dibujadas y son obligatorias; otras 4 

###### <u>quedan como reto extra.</u> 

|#|Pantalla|Archivo|Tipo|Qué se practica|
|---|---|---|---|---|
|1|Splash|auth/SplashScreen.k<br>t|Diseño|Image, Column,<br>botones|
|2|Registro|auth/RegistroScreen<br>.kt|Diseño|Estados,<br>OutlinedTextField,<br>validaciones, add a<br>la lista|
|3|Inicio|home/HomeScreen.<br>kt|Diseño|Scaffold,<br>NavigationBar,<br>LazyRow|
|4|Especialidades|agendamiento/Espec<br>ialidadesScreen.kt|Diseño|LazyColumn,<br>búsqueda con filter|
|5|Médicos|agendamiento/Medi<br>cosScreen.kt|Diseño|Parámetro<br>especialidadId, filter<br>+<br>sortedByDescendin<br>g|
|6|Fecha y hora|agendamiento/Fecha<br>HoraScreen.kt|Diseño|LazyVerticalGrid,<br>selección, horarios<br>disponibles|
|7|Confirmar cita|agendamiento/Confi<br>rmarCitaScreen.kt|Diseño|3 parámetros, crear<br>y guardar la cita|
|8|Iniciar sesión|auth/LoginScreen.kt|Faltante|Búsqueda con find,|



|#|Pantalla|Archivo|Tipo|Qué se practica|
|---|---|---|---|---|
||||(obligatoria)|sesión|
|9|Cita agendada|agendamiento/CitaE<br>xitosaScreen.kt|Faltante<br>(obligatoria)|popUpTo, resumen|
|10|Mis citas|citas/MisCitasScree<br>n.kt|Faltante<br>(obligatoria)|LazyColumn, lista<br>vacía|
|11|Perfil / Mis datos|perfil/PerfilScreen.k<br>t|Faltante<br>(obligatoria)|Datos de sesión,<br>cerrar sesión|
|12|Detalle de cita|citas/DetalleCitaScr<br>een.kt|Reto extra|AlertDialog, remove<br>de la lista|
|13|Resultados|resultados/Resultad<br>osScreen.kt|Reto extra|Modelo propio, lista<br>fija|
|14|Notificaciones|notificaciones/Notifi<br>cacionesScreen.kt|Reto extra|map sobre las citas|
|15|Términos y<br>condiciones|auth/TerminosScree<br>n.kt|Reto extra|Scroll o AlertDialog|



###### <u>Funciones del Repositorio</u> 

|Función|La usa|Colección y operación sugerida|
|---|---|---|
|registrarUsuario|Registro|usuarios: any+add|
|iniciarSesion / cerrarSesion|Login, Perfil|usuarios: find; usuarioActual|
|buscarEspecialidades|Especialidades|especialidades: filter+contains|
|especialidadesDestacadas|Inicio|especialidades: take|
|obtenerEspecialidad /<br>obtenerMedico / obtenerCita|Varias|find|
|medicosPorEspecialidad /<br>buscarMedicos|Médicos|medicos: filter +<br>sortedByDescending|
|horariosDisponibles|Fecha y hora|citas: filter + map; horariosBase:<br>filter|
|agendarCita|Confirmarcita|citas: any+add|
|citasDelUsuario|Mis citas, Perfil|citas: filter+sortedWith|
|cancelarCita|Detalle de cita (reto)|citas: removeIf|



Flujo de navegación 



<!-- Start of picture text -->
El agendamiento sale de Inicio y se cierra con popUpTo<br>2Registro<br>1Splash 3inicio<br>Biniciar sesion<br>Agendar cita<br>Barra inferior (NavigationBar)<br>4Especialidades<br>13 Resultados<br>especialidadia<br>5 Médicos pon<br>medicold<br>10 Mis citas<br>6 Fecha y hora<br>medicold, fecha, hora a<br>7 Confirmarntirmarcit cha 12 Detallede cita<br>popUpTo(HOME)<br>9 Cita agendada<br>Pantalla del disefo ©Vista fattante (obtigatoria) Retoextra<br><!-- End of picture text -->

###### Flujo de navegación · 13 pantallas con parámetros 

Las flechas llevan el parámetro que recibe la pantalla siguiente. Al confirmar, popUpTo borra el flujo de agendamiento del historial. También están conectados: Cita agendada →Mis citas, Perfil →Splash (cerrar sesión), la campana de Inicio → Notificaciones y el enlace del Registro →Términos. 

###### Orden de trabajo sugerido 

1. Implementar las funciones de Repositorio.kt. 

2. Crear los componentes reutilizables en ui/components. 

3. Completar las pantallas en el orden del flujo: 1 →2 →8 →3 →4 →5 →6 →7 →9 →10 →11. 

4. Retos extra (12 a 15). 

##### IV. Metodología de trabajo: GitHub en dos fases 

###### Fase 1 — Desarrollo SIN IA (rama main) 

Completa TODOS los TODO obligatorios del esqueleto sin usar ningún asistente de IA. 

1. Crea el repositorio en GitHub y sube el código esqueleto como primer commit. 

2. Trabaja sobre la rama main. 

3. Mínimo 8 commits descriptivos y distribuidos. Hitos sugeridos: 

   - a. Repositorio: usuarios (registrarUsuario, iniciarSesion, cerrarSesion). 

   - b. SplashScreen + RegistroScreen + LoginScreen con validaciones. 

   - c. Repositorio: especialidades y médicos (búsquedas y filtros). 

   - d. HomeScreen con tarjetas, saludo y LazyRow de especialidades destacadas. 

   - e. NavigationBar (bottomBar) con 4 destinos funcionando. 

   - f. EspecialidadesScreen con búsqueda + MedicosScreen con parámetro especialidadId. 

   - g. FechaHoraScreen con LazyVerticalGrid y horariosDisponibles. 

   - h. ConfirmarCitaScreen + CitaExitosaScreen + popUpTo. 

   - i. MisCitasScreen + PerfilScreen. 

4. Al terminar: git push origin main. 

###### Fase 2 — Mejora con IA (rama mejora-ia) 

Crea la rama mejora-ia A PARTIR de main. Con ayuda de un asistente de IA, agrega la siguiente mejora. 

**Mejora obligatoria: calendario dinámico en la Pantalla 6 (Fecha y hora).** En la Fase 1 los días se muestran como una lista fija. En esta fase deben generarse con java.time.LocalDate: 

   - Mostrar los próximos 5 días hábiles a partir de hoy (sin sábados, domingos ni días pasados). 

   - Las flechas < y > avanzan o retroceden una semana; no se puede retroceder antes de la semana actual. 

   - El nombre del mes y año ("Octubre 2026") cambia según la semana mostrada. 

   - Al cambiar de día, los horarios disponibles se recalculan solos y la hora seleccionada se reinicia. 

   - La Pantalla 7 muestra la fecha en texto en español ("Martes 16 de setiembre 2026"). 

- Ambas partes deben seguir funcionando juntas: el calendario dinámico no debe romper el bloqueo de horarios ya reservados. 1. git checkout main 

   2. git checkout -b mejora-ia 

   3. Mínimo 3 commits descriptivos. 

   4. Documenta cada prompt usado en un archivo PROMPTS.md (prompt, respuesta resumida y qué tuviste que corregir). 5. Al terminar: git push origin mejora-ia. 

<u>V. Rúbrica de evaluación (20 puntos)</u> 

|Criterio|Descripción|Pts|
|---|---|---|
|Asistencia|Presencial y puntual a la sesión de<br>laboratorio.|1|
|Puntualidad de entrega|Entrega el informe dentro del plazo<br>indicado por el docente.|1|
|Estructura de archivos completada|Todos los TODO obligatorios<br>resueltos; cada pantalla en su<br>archivo y paquete; sin<br>PantallaEnConstruccion en las<br>pantallas terminadas.|2|
|Repositorio con colecciones|Todas las funciones de<br>Repositorio.kt implementadas con<br>operaciones de colecciones, sin<br>base de datos.|2|
|Registro, login y sesión|Registro con validaciones, login<br>contra la lista de usuarios, saludo<br>con el nombre y cerrar sesión en|1|



|Criterio|Descripción|Pts|
|---|---|---|
||Perfil.||
|Flujo de agendamiento completo|Inicio →Especialidades →<br>Médicos →Fecha y hora →<br>Confirmar →Cita agendada,<br>usando los parámetros recibidos<br>(especialidadId, medicoId, fecha,<br>hora) y popUpTo al confirmar.|3|
|NavigationBar (menú, Semana 6)|bottomBar funcional con 4 destinos<br>en Inicio: Inicio, Citas, Resultados,<br>Perfil.|2|
|LazyRow y LazyColumn|LazyRow de especialidades<br>destacadas; LazyColumn de<br>especialidades con búsqueda en<br>tiempo real, de médicos y de mis<br>citas (con mensaje de lista vacía).|2|
|Horarios reactivos|LazyVerticalGrid de horarios; un<br>horario reservado deja de aparecer<br>para ese médico y fecha; Continuar<br>solo se habilita con día y hora<br>elegidos.|2|
|Fase 1 — Commits en main|Mínimo 8 commits descriptivos y<br>distribuidos durante el desarrollo<br>SIN IA.|2|
|Fase 2 — Calendario dinámico<br>(CON IA)|Días hábiles reales con LocalDate,<br>flechas por semana, mes dinámico y<br>horarios recalculados al cambiar de<br>día.|1|
|Fase 2 — Commits y<br>PROMPTS.md<br>**TOTAL**|Mínimo 3 commits en mejora-ia;<br>PROMPTS.md completo y<br>coherente.|1<br>**20**|



Los retos extra (pantallas 12 a 15) no suman puntos de la rúbrica; el docente puede usarlos para compensar puntos perdidos. VI. Diseño de referencia y alcance Las 7 pantallas del diseño con el flujo completo de agendamiento: 



<!-- Start of picture text -->
APP PACIENTE - Agendar citas médicas<br>© oo= mialloseGe -|@=es | es@:—— ||e==<br>Zi, = mimiie_ |e: -p--:|)o=-<br>vsDSwi) |2 : ne a tslle=| ——eae |/9=—|a— llalaee- =| =€ cee°F<br>a<br>=I =o =n aS =o =n a<br><!-- End of picture text -->

Figura 1. App Paciente — Clínica SaludPlus. Las vistas faltantes (Login, Cita agendada, Mis citas, Perfil) se diseñan con el mismo estilo: colores, tarjetas, botones azules y barra superior con flecha. 

**Nota importante sobre el alcance.** Esta tarea construye SOLO la App Paciente. No hay conexión con un sistema de la clínica ni con una base de datos: usuarios, especialidades, médicos y citas viven en colecciones dentro del objeto Repositorio y se pierden al cerrar la app. Esto es intencional: con lo visto hasta la Semana 6 (sin MVVM formal ni Room) no corresponde todavía una persistencia real. 

VII. Preguntas de reflexión 

- ¿Por qué los modelos, Rutas.kt y AppNavigation.kt se entregaron completos y las pantallas no? ¿Qué tienen en común los archivos que sí se dejaron como esqueleto? 

- ¿Por qué el Repositorio es un object y no una clase normal? ¿Qué pasaría con las citas si cada pantalla creara su propia lista? 

- ¿Cómo lograste que la búsqueda de especialidades y los horarios disponibles se actualicen solos, sin que tú "actualices" nada a mano? 

- ¿Qué diferencia notaste entre navigate() normal (Especialidades →Médicos) y el que usa popUpTo (Confirmar cita →Cita agendada)? ¿Qué pasa al presionar Atrás en cada caso? 

- ¿Qué tuviste que corregir del código que te generó la IA para el calendario dinámico? 

- Compara el NavigationDrawer del Laboratorio 6 con el NavigationBar de esta tarea: ¿en qué caso usarías cada uno en un proyecto propio? 

VIII. Observaciones y conclusiones 

Elaboración individual. Mínimo 2 observaciones y 2 conclusiones. 

- **Observaciones:** notas aclaratorias, dudas u obstáculos durante el desarrollo, incluido el diseño de las vistas faltantes. 

- • **Conclusiones:** opinión personal sobre trabajar a partir de un esqueleto en vez de empezar desde cero, comparando el proceso de la Fase 1 y la Fase 2. 

