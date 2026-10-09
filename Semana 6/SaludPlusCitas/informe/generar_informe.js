// Genera el informe en Word del Laboratorio 06 (Tarea: Clínica SaludPlus - App Paciente).
//
// Uso (desde esta carpeta):
//   npm install          (solo la primera vez)
//   node generar_informe.js
//
// Las capturas se leen de ./capturas/<nombre>.png (ver capturas.md).
// Si una captura no existe, en su lugar sale un recuadro "Captura pendiente".
// Las tablas de commits se arman con "git log" de las ramas main y mejora-ia.

const fs = require("fs");
const path = require("path");
const { execSync } = require("child_process");
const {
  Document, Packer, Paragraph, TextRun, ImageRun, Table, TableRow, TableCell,
  AlignmentType, HeadingLevel, WidthType, BorderStyle, ShadingType, PageBreak,
  Header, Footer, PageNumber, TableOfContents, LevelFormat, VerticalAlign,
} = require("docx");

const CARPETA_CAPTURAS = path.join(__dirname, "capturas");
const SALIDA = path.join(__dirname, "Informe_Lab06_SaludPlus.docx");

// ---------- Colores y medidas ----------
const AZUL = "2C68EC";
const AZUL_MARINO = "253771";
const GRIS_TEXTO = "6B6F78";
const GRIS_FONDO = "F4F4F8";
const AZUL_CLARO = "E0F0F8";
const ANCHO_CONTENIDO = 9638; // A4 con márgenes de 1 pulgada (DXA)
const ANCHO_CAPTURA_PX = 200; // ancho de cada captura en el documento

// ---------- Ayudas de texto ----------
const texto = (t, opciones = {}) => new TextRun({ text: t, ...opciones });

function parrafo(contenido, opciones = {}) {
  const hijos = typeof contenido === "string" ? [texto(contenido)] : contenido;
  return new Paragraph({ children: hijos, spacing: { after: 120 }, ...opciones });
}

function titulo1(t) {
  return new Paragraph({ heading: HeadingLevel.HEADING_1, children: [texto(t)] });
}

function titulo2(t) {
  return new Paragraph({ heading: HeadingLevel.HEADING_2, children: [texto(t)] });
}

function vineta(contenido, nivel = 0) {
  const hijos = typeof contenido === "string" ? [texto(contenido)] : contenido;
  return new Paragraph({ numbering: { reference: "vinetas", level: nivel }, children: hijos, spacing: { after: 60 } });
}

function numerado(contenido, referencia = "numeros") {
  const hijos = typeof contenido === "string" ? [texto(contenido)] : contenido;
  return new Paragraph({ numbering: { reference: referencia, level: 0 }, children: hijos, spacing: { after: 60 } });
}

// Texto con partes en negrita: ["normal ", {b: "negrita"}, " normal"]
function mixto(partes) {
  return partes.map((p) => (typeof p === "string" ? texto(p) : texto(p.b, { bold: true })));
}

function saltoDePagina() {
  return new Paragraph({ children: [new PageBreak()] });
}

// ---------- Tablas ----------
const bordeFino = { style: BorderStyle.SINGLE, size: 4, color: "D0D3DA" };
const bordesTabla = { top: bordeFino, bottom: bordeFino, left: bordeFino, right: bordeFino };
const sinBorde = { style: BorderStyle.NONE, size: 0, color: "FFFFFF" };
const sinBordes = { top: sinBorde, bottom: sinBorde, left: sinBorde, right: sinBorde };

function celda(contenido, ancho, opciones = {}) {
  const parrafos = (Array.isArray(contenido) ? contenido : [contenido]).map((c) =>
    c instanceof Paragraph ? c : new Paragraph({ children: [texto(String(c), opciones.run || {})] })
  );
  return new TableCell({
    width: { size: ancho, type: WidthType.DXA },
    borders: opciones.bordes || bordesTabla,
    shading: opciones.fondo ? { fill: opciones.fondo, type: ShadingType.CLEAR, color: "auto" } : undefined,
    margins: { top: 60, bottom: 60, left: 100, right: 100 },
    verticalAlign: opciones.vertical || VerticalAlign.TOP,
    children: parrafos,
  });
}

// Tabla con encabezado azul. filas = [[col1, col2, ...], ...]
function tabla(encabezados, anchos, filas) {
  const total = anchos.reduce((a, b) => a + b, 0);
  return new Table({
    width: { size: total, type: WidthType.DXA },
    columnWidths: anchos,
    rows: [
      new TableRow({
        tableHeader: true,
        children: encabezados.map((e, i) =>
          celda(e, anchos[i], { fondo: AZUL, run: { bold: true, color: "FFFFFF", size: 20 } })
        ),
      }),
      ...filas.map((fila, f) =>
        new TableRow({
          children: fila.map((valor, i) =>
            celda(valor, anchos[i], { fondo: f % 2 === 1 ? GRIS_FONDO : undefined, run: { size: 19 } })
          ),
        })
      ),
    ],
  });
}

// ---------- Capturas ----------
// Lee ancho y alto de un PNG (bytes 16 a 23 del encabezado)
function tamanoPng(buffer) {
  return { ancho: buffer.readUInt32BE(16), alto: buffer.readUInt32BE(20) };
}

const capturasFaltantes = [];

// Contenido de una captura: la imagen (o un recuadro "pendiente") y su pie de figura
let numeroFigura = 0;
function contenidoCaptura(nombre, pie) {
  numeroFigura++;
  const archivo = path.join(CARPETA_CAPTURAS, nombre + ".png");
  let imagen;
  if (fs.existsSync(archivo)) {
    const datos = fs.readFileSync(archivo);
    const { ancho, alto } = tamanoPng(datos);
    const altoPx = Math.round((ANCHO_CAPTURA_PX * alto) / ancho);
    imagen = new Paragraph({
      alignment: AlignmentType.CENTER,
      children: [new ImageRun({ type: "png", data: datos, transformation: { width: ANCHO_CAPTURA_PX, height: altoPx } })],
    });
  } else {
    capturasFaltantes.push(nombre);
    imagen = new Paragraph({
      alignment: AlignmentType.CENTER,
      spacing: { before: 1400, after: 1400 },
      shading: { fill: GRIS_FONDO, type: ShadingType.CLEAR, color: "auto" },
      children: [
        texto("Captura pendiente", { bold: true, color: GRIS_TEXTO }),
        new TextRun({ break: 1 }),
        texto(nombre + ".png", { color: GRIS_TEXTO, size: 18 }),
      ],
    });
  }
  const leyenda = new Paragraph({
    alignment: AlignmentType.CENTER,
    spacing: { before: 80, after: 200 },
    children: [texto(`Figura ${numeroFigura}. `, { bold: true, size: 18 }), texto(pie, { size: 18, color: GRIS_TEXTO })],
  });
  return [imagen, leyenda];
}

// Capturas de dos en dos, una al lado de la otra (tabla sin bordes)
function galeria(capturas) {
  const mitad = Math.floor(ANCHO_CONTENIDO / 2);
  const filas = [];
  for (let i = 0; i < capturas.length; i += 2) {
    const par = capturas.slice(i, i + 2);
    filas.push(
      new TableRow({
        cantSplit: true,
        children: [0, 1].map((j) =>
          new TableCell({
            width: { size: mitad, type: WidthType.DXA },
            borders: sinBordes,
            children: par[j] ? contenidoCaptura(par[j][0], par[j][1]) : [new Paragraph("")],
          })
        ),
      })
    );
  }
  return new Table({ width: { size: mitad * 2, type: WidthType.DXA }, columnWidths: [mitad, mitad], rows: filas });
}

// ---------- Commits desde git ----------
function commits(rango) {
  try {
    const salida = execSync(
      `git log --no-merges --reverse --format="%h|%ad|%s" --date=format:"%d/%m/%Y %H:%M" ${rango} -- ":(top)Semana 6/SaludPlusCitas"`,
      { cwd: __dirname, encoding: "utf8" }
    ).trim();
    return salida ? salida.split("\n").map((l) => l.split("|")) : [];
  } catch (e) {
    return [];
  }
}

// Usa origin/<rama> si existe; si no, la rama local
function ref(rama) {
  try {
    execSync(`git rev-parse --verify --quiet origin/${rama}`, { cwd: __dirname, stdio: "ignore" });
    return `origin/${rama}`;
  } catch (e) {
    return rama;
  }
}

function tablaCommits(lista) {
  if (lista.length === 0) {
    return parrafo([texto("(No se pudo leer el historial de git en esta carpeta.)", { italics: true, color: GRIS_TEXTO })]);
  }
  return tabla(
    ["#", "Commit", "Fecha y hora", "Mensaje"],
    [500, 1100, 1800, 6238],
    lista.map((c, i) => [String(i + 1), c[0], c[1], c[2]])
  );
}

// =====================================================================
// CONTENIDO
// =====================================================================
const MAIN = ref("main");
const IA = ref("mejora-ia");
const commitsMain = commits(MAIN);
const commitsIA = commits(`${MAIN}..${IA}`);

const caratula = [
  new Paragraph({ spacing: { before: 1800 }, alignment: AlignmentType.CENTER, children: [texto("TECSUP", { bold: true, size: 40, color: AZUL_MARINO })] }),
  new Paragraph({ alignment: AlignmentType.CENTER, children: [texto("Diseño y Desarrollo de Software", { size: 26, color: GRIS_TEXTO })] }),
  new Paragraph({ alignment: AlignmentType.CENTER, spacing: { after: 600 }, children: [texto("Programación en Móviles — 4to ciclo", { size: 26, color: GRIS_TEXTO })] }),
  new Paragraph({ alignment: AlignmentType.CENTER, children: [texto("LABORATORIO 06", { bold: true, size: 32, color: AZUL })] }),
  new Paragraph({ alignment: AlignmentType.CENTER, children: [texto("Menú - Navegación", { size: 28 })] }),
  new Paragraph({ alignment: AlignmentType.CENTER, spacing: { before: 400 }, children: [texto("Tarea: Clínica SaludPlus — App Paciente", { bold: true, size: 36, color: AZUL_MARINO })] }),
  new Paragraph({ alignment: AlignmentType.CENTER, spacing: { after: 1200 }, children: [texto("Informe de evidencias — Fase 1 (sin IA), Fase 2 (con IA) y mejoras del profesor", { size: 22, color: GRIS_TEXTO })] }),
  new Table({
    width: { size: 6000, type: WidthType.DXA },
    columnWidths: [2000, 4000],
    alignment: AlignmentType.CENTER,
    rows: [
      ["Alumno", "Renzo Raúl León Fernández"],
      ["Docente", "Juan José León Suiyon"],
      ["Sección", "C24 4to D"],
      ["Repositorio", "github.com/RXDG2908/Moviles-Android-CD"],
      ["Fecha", "Octubre 2026"],
    ].map(([a, b]) =>
      new TableRow({
        children: [
          celda(a, 2000, { fondo: AZUL_CLARO, run: { bold: true, size: 20 } }),
          celda(b, 4000, { run: { size: 20 } }),
        ],
      })
    ),
  }),
  saltoDePagina(),
];

const indice = [
  new Paragraph({ children: [texto("Índice", { bold: true, size: 32, color: AZUL_MARINO })], spacing: { after: 200 } }),
  new TableOfContents("Índice", { hyperlink: true, headingStyleRange: "1-2" }),
  parrafo([texto("(Si el índice sale vacío: clic derecho sobre él → Actualizar campos.)", { italics: true, size: 18, color: GRIS_TEXTO })]),
  saltoDePagina(),
];

const introduccion = [
  titulo1("1. Introducción y objetivos"),
  parrafo(
    "La tarea consiste en completar una app real de agendamiento de citas médicas (App Paciente de la Clínica SaludPlus) a partir de un código esqueleto, llenando cada archivo .kt según sus comentarios TODO. Todos los datos viven en colecciones dentro del objeto Repositorio, sin base de datos."
  ),
  parrafo("Objetivos de la guía:"),
  vineta("Completar la app a partir del código esqueleto, resolviendo cada TODO."),
  vineta("Implementar NavigationBar como menú principal en Inicio (Inicio, Citas, Resultados, Perfil)."),
  vineta("Aplicar LazyRow, LazyColumn y LazyVerticalGrid con colecciones en memoria."),
  vineta("Navegar con parámetros (especialidadId, medicoId, fecha, hora) y usar popUpTo."),
  vineta("Diseñar las vistas faltantes respetando el estilo del diseño de referencia."),
  vineta("Trabajar con GitHub en dos fases: desarrollo propio (main) y mejora asistida por IA (mejora-ia)."),
];

const repositorio = [
  titulo1("2. Repositorio y metodología"),
  parrafo(mixto(["El proyecto está en la carpeta ", { b: "Semana 6/SaludPlusCitas" }, " del repositorio. Se trabajó con dos ramas separadas, que nunca se mezclan:"])),
  tabla(
    ["Rama", "Fase", "Contenido"],
    [1600, 2200, 5838],
    [
      ["main", "Fase 1 — sin IA", "Repositorio, componentes y las 11 pantallas obligatorias completadas a mano, a partir del esqueleto."],
      ["mejora-ia", "Fase 2 — con IA", "Calendario dinámico (mejora obligatoria de la guía) y las mejoras pedidas por el profesor. Cada prompt está documentado en PROMPTS.md."],
    ]
  ),
  titulo2("2.1 Commits de la Fase 1 (rama main)"),
  parrafo(`Commits del proyecto SaludPlusCitas en main (${commitsMain.length}; la guía pide mínimo 8):`),
  tablaCommits(commitsMain),
  titulo2("2.2 Commits de la Fase 2 y mejoras (rama mejora-ia)"),
  parrafo(`Commits que solo están en mejora-ia (${commitsIA.length}; la guía pide mínimo 3):`),
  tablaCommits(commitsIA),
  titulo2("2.3 Estructura del proyecto"),
  parrafo("Organización por capas (datos, navegación e interfaz); cada pantalla en su propio archivo:"),
  tabla(
    ["Paquete", "Contenido"],
    [3600, 6038],
    [
      ["data.model", "Usuario, Especialidad, Medico, Cita y Sede (mejora-ia)"],
      ["data.repository", "Repositorio (object): colecciones en memoria y todas las funciones de negocio"],
      ["navigation", "Rutas.kt (rutas y funciones con parámetros) y AppNavigation.kt (NavHost)"],
      ["ui.components", "Componentes reutilizables y Calendario.kt (mejora-ia)"],
      ["ui.screens.auth", "Splash, Registro, Login, Términos"],
      ["ui.screens.home", "Inicio con NavigationBar"],
      ["ui.screens.agendamiento", "Especialidades, Médicos, Fecha y hora, Confirmar cita, Cita agendada"],
      ["ui.screens.citas / perfil", "Mis citas, Detalle de cita, Perfil"],
      ["ui.screens.sedes / doctores", "Elige tu sede y Mis doctores (mejora-ia)"],
    ]
  ),
  saltoDePagina(),
];

const fase1 = [
  titulo1("3. Fase 1 — Desarrollo sin IA (rama main)"),
  parrafo("Flujo completo de la app tal como quedó en main, sin asistentes de IA. Usuario de prueba: teléfono 987654321, contraseña 123456."),
  titulo2("3.1 Autenticación"),
  galeria([
    ["f1-01-splash", "Splash: logo, ilustración y botones."],
    ["f1-02-registro", "Registro con validaciones."],
    ["f1-03-login", "Iniciar sesión (vista faltante)."],
    ["f1-04-inicio", "Inicio: saludo, mosaicos, LazyRow y NavigationBar."],
  ]),
  titulo2("3.2 Flujo de agendamiento"),
  galeria([
    ["f1-05-especialidades", "Especialidades con búsqueda en tiempo real."],
    ["f1-06-medicos", "Médicos de la especialidad (parámetro especialidadId)."],
    ["f1-07-fecha-hora", "Fecha y hora: LazyVerticalGrid de horarios."],
    ["f1-08-confirmar", "Confirmar cita: medicoId, fecha y hora."],
    ["f1-09-cita-agendada", "Cita agendada (vista faltante) con popUpTo."],
    ["f1-10-mis-citas", "Mis citas (vista faltante)."],
  ]),
  titulo2("3.3 Perfil"),
  galeria([["f1-11-perfil", "Perfil / Mis datos con cerrar sesión."]]),
  saltoDePagina(),
];

const fase2 = [
  titulo1("4. Fase 2 — Mejora con IA: calendario dinámico (rama mejora-ia)"),
  parrafo("Mejora obligatoria de la guía en la Pantalla 6. Los días se generan con java.time.LocalDate:"),
  vineta("Próximos 5 días hábiles desde hoy, sin sábados, domingos ni días pasados."),
  vineta("Flechas < y > para cambiar de semana; no se puede retroceder antes de la semana actual."),
  vineta("El mes y el año cambian según la semana mostrada."),
  vineta("Al cambiar de día, los horarios se recalculan y la hora elegida se reinicia."),
  vineta("La Pantalla 7 muestra la fecha en texto en español."),
  vineta("Los horarios ya reservados siguen bloqueados."),
  galeria([
    ["f2-01-calendario-semana-actual", "Semana actual: días hábiles reales; la flecha < no retrocede."],
    ["f2-02-calendario-semana-siguiente", "Semana siguiente: el mes y los días cambian."],
    ["f2-03-dia-y-hora-elegidos", "Día y hora elegidos; Continuar habilitado."],
    ["f2-04-confirmar-fecha-espanol", "Confirmar cita con la fecha en español."],
  ]),
  saltoDePagina(),
];

const mejorasProfe = [
  titulo1("5. Mejoras pedidas por el profesor (rama mejora-ia, con IA)"),
  titulo2("5.1 Inicio de sesión con correo o teléfono y re-login"),
  parrafo("Después de registrarse la app no entra directo: va a Login con el correo ya escrito y el aviso \"Cuenta creada, inicia sesión\". El Login acepta el correo o el teléfono."),
  galeria([
    ["p-01-registro-correo", "Registro con correo obligatorio."],
    ["p-02-login-cuenta-creada", "Login después del registro (re-login)."],
  ]),
  titulo2("5.2 Sedes antes de agendar"),
  parrafo("Se agregaron 12 sedes en Lima y Callao. Antes de agendar siempre se elige la sede, y cada sede tiene las 7 especialidades con 2 médicos de cada una. Inicio cambió el mosaico \"Mis citas\" por \"Mis doctores\"."),
  galeria([
    ["p-03-inicio-mis-doctores", "Inicio con el mosaico Mis doctores."],
    ["p-04-sedes", "Elige tu sede (12 sedes)."],
    ["p-05-medicos-por-sede", "Médicos de la sede con disponibilidad real (semáforo)."],
  ]),
  titulo2("5.3 Calendario tipo cine: días y horarios bloqueados con semáforo"),
  parrafo("Cada médico atiende ciertos días. Cada día muestra sus cupos con un semáforo (verde = muchos, amarillo = pocos, rojo = últimos); los días que no atiende o llenos salen bloqueados. Las horas ocupadas o ya pasadas salen tachadas."),
  galeria([
    ["p-06-calendario-semaforo", "Días con semáforo de cupos y días bloqueados."],
    ["p-07-horas-bloqueadas", "Horarios libres, ocupados (tachados) y elegido."],
  ]),
  titulo2("5.4 Confirmación con sede y Mis citas por sede"),
  parrafo("Confirmar cita y Cita agendada muestran la sede y su dirección. \"Mis citas\" salió del Inicio: la pestaña Citas pide primero la sede, y la lista muestra solo las citas de esa sede, con la opción Cambiar sede."),
  galeria([
    ["p-08-confirmar-con-sede", "Confirmar cita con la sede y su dirección."],
    ["p-09-cita-agendada-sede", "Cita agendada con la sede."],
    ["p-10-mis-citas-por-sede", "Mis citas de la sede elegida."],
  ]),
  titulo2("5.5 Mis doctores agrupados por especialidad"),
  parrafo("Todos los médicos agrupados con groupBy, ordenados por calificación y con buscador por nombre. Al tocar uno se va a su Fecha y hora (pasando por Sedes si hace falta)."),
  galeria([
    ["p-11-mis-doctores", "Mis doctores agrupados por especialidad."],
    ["p-12-mis-doctores-busqueda", "Búsqueda de médico por nombre."],
  ]),
  saltoDePagina(),
];

const prompts = [
  titulo1("6. Uso de IA: resumen de PROMPTS.md"),
  parrafo(mixto(["Asistente usado: ", { b: "Claude Code" }, ". El detalle completo (prompt, respuesta resumida y correcciones) está en Semana 6/SaludPlusCitas/PROMPTS.md."])),
  tabla(
    ["#", "Pedido", "Resultado", "Qué se corrigió"],
    [500, 2600, 3600, 2938],
    [
      ["1", "Hacer la Fase 2 de la guía", "Calendario con LocalDate, flechas por semana, mes dinámico y fecha en español", "Nombres de meses propios para decir \"setiembre\""],
      ["2", "Seguir la guía al pie de la letra", "Se quitó lo que la guía no pedía", "Primera versión con agregados de más"],
      ["3", "Mejoras del profesor", "Re-login, sedes, Mis citas por sede, Mis doctores", "Destino tras elegir sede sin \"/\" en la ruta; médico sin atención en la sede"],
      ["4", "Seguir el md al pie de la letra", "medicosAgrupadosPorEspecialidad() sin parámetros", "Firma igual a la pedida"],
      ["5", "Solo días disponibles del médico", "diasAtencion por médico", "Mes calculado aunque no haya días disponibles"],
      ["6", "Auditoría", "Día oculto que seguía elegido; sede tras cerrar sesión", "Se borran al cambiar de semana y al cerrar sesión"],
      ["7–8", "Login con correo o teléfono", "Un solo campo \"Correo o teléfono\"", "Primero se entendió solo correo"],
      ["9", "Más sedes y médicos", "12 sedes, 42 médicos, 2 por especialidad en cada sede", "Cobertura comprobada con un script"],
      ["10", "Calendario tipo cine", "Días y horas bloqueados con semáforo de cupos", "Antes se ocultaban; ahora se muestran bloqueados"],
    ]
  ),
  saltoDePagina(),
];

const reflexion = [
  titulo1("7. Preguntas de reflexión"),
  titulo2("7.1 ¿Por qué los modelos, Rutas.kt y AppNavigation.kt se entregaron completos y las pantallas no?"),
  parrafo("Porque son la base que comparten todas las pantallas: si cada alumno los escribiera distinto, las pantallas no encajarían entre sí. Lo que se dejó como esqueleto (Repositorio y pantallas) tiene en común que es justo lo que se quería practicar: colecciones, estados, listas Lazy y navegación con parámetros."),
  titulo2("7.2 ¿Por qué el Repositorio es un object y no una clase normal?"),
  parrafo("Un object tiene una sola instancia para toda la app. Si cada pantalla creara su propia lista, una cita guardada en Confirmar no aparecería en Mis citas ni bloquearía el horario en Fecha y hora, porque cada pantalla tendría una copia distinta."),
  titulo2("7.3 ¿Cómo se actualizan solas la búsqueda y los horarios disponibles?"),
  parrafo("La búsqueda se recalcula en cada recomposición a partir del estado del texto (remember + mutableStateOf). Las citas son un mutableStateListOf: al agregar una, Compose vuelve a dibujar todo lo que la lee, así que el horario reservado desaparece y el semáforo de cupos cambia sin actualizar nada a mano."),
  titulo2("7.4 ¿Qué diferencia hay entre navigate() normal y navigate() con popUpTo?"),
  parrafo("Con navigate() normal (Especialidades → Médicos) la pantalla anterior queda en el historial y Atrás vuelve a ella. Con popUpTo(HOME) al confirmar, se borran del historial Sedes, Especialidades, Médicos, Fecha y hora y Confirmar: desde Cita agendada, Atrás vuelve al Inicio y no se puede repetir la reserva."),
  titulo2("7.5 ¿Qué se tuvo que corregir del código que generó la IA para el calendario dinámico?"),
  parrafo("Los nombres de los meses se escribieron en una lista propia, porque con el idioma del celular sale \"septiembre\" y la guía usa \"setiembre\". También se quitaron agregados que la guía no pedía. Después, ya con las mejoras del profesor, se corrigió que un día de otra semana quedara elegido aunque no se viera."),
  titulo2("7.6 NavigationDrawer (Laboratorio 6) frente a NavigationBar (esta tarea)"),
  parrafo("El NavigationBar sirve para 3 a 5 destinos principales que se usan todo el tiempo, siempre visibles abajo (Inicio, Citas, Resultados, Perfil). El NavigationDrawer conviene cuando hay más secciones o secciones secundarias (configuración, ayuda, cerrar sesión) que no necesitan estar siempre a la vista."),
  saltoDePagina(),
];

const observaciones = [
  titulo1("8. Observaciones y conclusiones"),
  titulo2("8.1 Observaciones"),
  vineta("Las vistas faltantes (Login, Cita agendada, Mis citas y Perfil) se diseñaron con el mismo estilo del diseño de referencia: barra superior blanca con flecha, tarjetas grises y botones azules."),
  vineta("java.time.LocalDate funciona desde Android 8 (API 26), pero el proyecto empieza en API 24, así que la rama mejora-ia se prueba en un emulador con API 26 o mayor."),
  vineta("Los datos se pierden al cerrar la app porque viven en memoria; es intencional según la guía (sin base de datos)."),
  vineta("Para que el semáforo de cupos se vea en la demo, el Repositorio crea citas de ejemplo de otros pacientes."),
  titulo2("8.2 Conclusiones"),
  vineta("Trabajar sobre un esqueleto obliga a leer y respetar código ajeno (nombres, parámetros y rutas), algo más parecido a un proyecto real que empezar desde cero."),
  vineta("En la Fase 1 cada pantalla se entendió paso a paso; en la Fase 2 la IA aceleró mucho los cambios, pero hubo que revisar cada resultado y corregirlo para que cumpliera exactamente lo pedido."),
  vineta("Guardar los datos en un único object con listas observables (mutableStateListOf) hace que todas las pantallas se mantengan sincronizadas sin código extra."),
  saltoDePagina(),
];

const anexo = [
  titulo1("Anexo: datos de prueba"),
  tabla(
    ["Rama", "Usuario", "Contraseña"],
    [2400, 4838, 2400],
    [
      ["main", "Teléfono 987654321", "123456"],
      ["mejora-ia", "juan@correo.com o 987654321", "123456"],
    ]
  ),
  parrafo(""),
  parrafo("Ejemplo para el calendario: Medicina General → sede Miraflores → Dra. Lucía Fernández (atiende martes y jueves)."),
];

// =====================================================================
// DOCUMENTO
// =====================================================================
const documento = new Document({
  creator: "Renzo Raúl León Fernández",
  title: "Laboratorio 06 - Clínica SaludPlus",
  styles: {
    default: { document: { run: { font: "Calibri", size: 22 } } },
    paragraphStyles: [
      {
        id: "Heading1", name: "Heading 1", basedOn: "Normal", next: "Normal", quickFormat: true,
        run: { size: 30, bold: true, font: "Calibri", color: AZUL_MARINO },
        paragraph: { spacing: { before: 240, after: 160 }, outlineLevel: 0, keepNext: true, keepLines: true },
      },
      {
        id: "Heading2", name: "Heading 2", basedOn: "Normal", next: "Normal", quickFormat: true,
        run: { size: 25, bold: true, font: "Calibri", color: AZUL },
        paragraph: { spacing: { before: 200, after: 120 }, outlineLevel: 1, keepNext: true, keepLines: true },
      },
    ],
  },
  numbering: {
    config: [
      {
        reference: "vinetas",
        levels: [
          { level: 0, format: LevelFormat.BULLET, text: "•", alignment: AlignmentType.LEFT, style: { paragraph: { indent: { left: 720, hanging: 360 } } } },
          { level: 1, format: LevelFormat.BULLET, text: "◦", alignment: AlignmentType.LEFT, style: { paragraph: { indent: { left: 1440, hanging: 360 } } } },
        ],
      },
      {
        reference: "numeros",
        levels: [{ level: 0, format: LevelFormat.DECIMAL, text: "%1.", alignment: AlignmentType.LEFT, style: { paragraph: { indent: { left: 720, hanging: 360 } } } }],
      },
    ],
  },
  sections: [
    {
      properties: {
        page: { size: { width: 11906, height: 16838 }, margin: { top: 1134, right: 1134, bottom: 1134, left: 1134 } },
        titlePage: true,
      },
      headers: {
        default: new Header({
          children: [new Paragraph({ alignment: AlignmentType.RIGHT, children: [texto("Laboratorio 06 — Clínica SaludPlus", { size: 16, color: GRIS_TEXTO })] })],
        }),
      },
      footers: {
        default: new Footer({
          children: [new Paragraph({ alignment: AlignmentType.CENTER, children: [new TextRun({ children: ["Página ", PageNumber.CURRENT], size: 16, color: GRIS_TEXTO })] })],
        }),
      },
      children: [
        ...caratula, ...indice, ...introduccion, ...repositorio, ...fase1, ...fase2,
        ...mejorasProfe, ...prompts, ...reflexion, ...observaciones, ...anexo,
      ],
    },
  ],
});

Packer.toBuffer(documento).then((buffer) => {
  fs.writeFileSync(SALIDA, buffer);
  console.log("Informe generado: " + SALIDA);
  console.log(`Commits leídos: main ${commitsMain.length}, mejora-ia ${commitsIA.length}`);
  if (capturasFaltantes.length > 0) {
    console.log(`Capturas pendientes (${capturasFaltantes.length}): ` + capturasFaltantes.join(", "));
  } else {
    console.log("Todas las capturas están incluidas.");
  }
});
