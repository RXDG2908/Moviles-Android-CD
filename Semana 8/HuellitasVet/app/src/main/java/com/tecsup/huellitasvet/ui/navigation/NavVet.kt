package com.tecsup.huellitasvet.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.remember
import androidx.navigation.NavBackStackEntry
import com.tecsup.huellitasvet.model.Rol
import com.tecsup.huellitasvet.model.TipoServicio
import com.tecsup.huellitasvet.ui.admin.AdminFormUsuarioScreen
import com.tecsup.huellitasvet.ui.admin.AdminMascotasScreen
import com.tecsup.huellitasvet.ui.admin.AdminPanelScreen
import com.tecsup.huellitasvet.ui.admin.AdminUsuariosScreen
import com.tecsup.huellitasvet.viewmodel.AdminViewModel
import com.tecsup.huellitasvet.ui.dueno.FichaMascotaScreen
import com.tecsup.huellitasvet.ui.dueno.FormularioMascotaScreen
import com.tecsup.huellitasvet.ui.dueno.InicioScreen
import com.tecsup.huellitasvet.ui.dueno.LoginScreen
import com.tecsup.huellitasvet.ui.dueno.MascotasScreen
import com.tecsup.huellitasvet.ui.dueno.MisCitasScreen
import com.tecsup.huellitasvet.ui.dueno.PerfilScreen
import com.tecsup.huellitasvet.ui.dueno.PedirCitaScreen
import com.tecsup.huellitasvet.ui.dueno.RegistroScreen
import com.tecsup.huellitasvet.ui.dueno.ReprogramarCitaScreen
import com.tecsup.huellitasvet.ui.dueno.SplashScreen
import com.tecsup.huellitasvet.ui.veterinario.AgendaScreen
import com.tecsup.huellitasvet.ui.veterinario.AjustesScreen
import com.tecsup.huellitasvet.ui.veterinario.FormularioServicioScreen
import com.tecsup.huellitasvet.ui.veterinario.AtenderCitaScreen
import com.tecsup.huellitasvet.ui.veterinario.PacientesScreen
import com.tecsup.huellitasvet.ui.veterinario.PanelScreen
import com.tecsup.huellitasvet.ui.veterinario.ServiciosScreen
import com.tecsup.huellitasvet.viewmodel.AgendaViewModel
import com.tecsup.huellitasvet.viewmodel.AtenderCitaViewModel
import com.tecsup.huellitasvet.viewmodel.PacientesViewModel
import com.tecsup.huellitasvet.viewmodel.InicioViewModel
import com.tecsup.huellitasvet.viewmodel.MascotaViewModel
import com.tecsup.huellitasvet.viewmodel.MisCitasViewModel
import com.tecsup.huellitasvet.viewmodel.PanelViewModel
import com.tecsup.huellitasvet.viewmodel.PedirCitaViewModel
import com.tecsup.huellitasvet.viewmodel.ReprogramarCitaViewModel
import com.tecsup.huellitasvet.viewmodel.ServiciosViewModel
import com.tecsup.huellitasvet.viewmodel.SesionViewModel
import com.tecsup.huellitasvet.viewmodel.VetViewModelFactory

// Grafos de cada perfil
private const val GRAFO_DUENO = "grafo_dueno"
private const val GRAFO_VET = "grafo_vet"
private const val GRAFO_ADMIN = "grafo_admin" // mejora: administrador

// Navega al grafo del rol y limpia la pila (no se puede volver al login)
// Mejora (administrador): Rol.ADMINISTRADOR entra a su propio grafo con barra de 5 pestañas
fun NavHostController.irAlInicio(rol: Rol) {
    val destino = when (rol) {
        Rol.ADMIN -> GRAFO_VET
        Rol.ADMINISTRADOR -> GRAFO_ADMIN
        Rol.CLIENTE -> GRAFO_DUENO
    }
    navigate(destino) { popUpTo(0) { inclusive = true } }
}

// Pestaña de la barra que se marca en las pantallas internas del administrador
private fun tabAdminDe(ruta: String?): String? = when (ruta) {
    Rutas.ADMIN_FICHA_MASCOTA -> Rutas.ADMIN_MASCOTAS
    else -> ruta
}

@Composable
fun NavVet(factory: VetViewModelFactory, sesion: SesionViewModel) {
    val nav = rememberNavController()
    val oscuro by sesion.modoOscuro.collectAsState()
    val aceptaCitas by sesion.aceptarCitas.collectAsState()
    val usuario by sesion.usuarioActual.collectAsState()
    val duenoId = usuario?.id ?: 0

    // La barra inferior solo aparece en las pestañas principales
    val entrada by nav.currentBackStackEntryAsState()
    val rutaActual = entrada?.destination?.route

    // La ficha también muestra la barra (con Mascotas o Pacientes marcado), como en el mockup
    val tabDueno = if (rutaActual == Rutas.FICHA_MASCOTA) Rutas.MASCOTAS else rutaActual
    val tabVet = if (rutaActual == Rutas.FICHA_PACIENTE) Rutas.PACIENTES else rutaActual
    val recordatorios by sesion.notificaciones.collectAsState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            when {
                itemsDueno.any { it.ruta == tabDueno } -> BarraNavegacion(itemsDueno, tabDueno, nav)
                itemsVet.any { it.ruta == tabVet } -> BarraNavegacion(itemsVet, tabVet, nav)
                itemsAdmin.any { it.ruta == tabAdminDe(rutaActual) } -> BarraNavegacion(itemsAdmin, tabAdminDe(rutaActual), nav)
            }
        }
    ) { padding ->
        NavHost(navController = nav, startDestination = Rutas.SPLASH, modifier = Modifier.padding(padding)) {

            composable(Rutas.SPLASH) {
                SplashScreen(onTerminar = {
                    sesion.restaurarSesion { rol ->
                        if (rol == null) {
                            nav.navigate(Rutas.LOGIN) { popUpTo(0) { inclusive = true } }
                        } else {
                            nav.irAlInicio(rol)
                        }
                    }
                })
            }

            composable(Rutas.LOGIN) {
                LoginScreen(
                    vm = sesion,
                    oscuro = oscuro,
                    onEntrar = { rol -> nav.irAlInicio(rol) },
                    onRegistrarse = { nav.navigate(Rutas.REGISTRO) { launchSingleTop = true } }
                )
            }

            // Mejora: al crear la cuenta vuelve al login (que ya muestra el correo escrito)
            composable(Rutas.REGISTRO) {
                RegistroScreen(
                    vm = sesion,
                    oscuro = oscuro,
                    onRegistrado = { nav.popBackStack(Rutas.LOGIN, inclusive = false) },
                    onAtras = { nav.popBackStack() }
                )
            }

            // ==================== Grafo del dueño ====================
            navigation(startDestination = Rutas.INICIO, route = GRAFO_DUENO) {

                composable(Rutas.INICIO) {
                    val vm: InicioViewModel = viewModel(factory = factory)
                    InicioScreen(
                        vm = vm,
                        duenoId = duenoId,
                        nombre = usuario?.nombre ?: "",
                        recordatorios = recordatorios,
                        onMascota = { nav.navigate(Rutas.fichaMascota(it)) },
                        onVerTodas = { nav.navigate(Rutas.MASCOTAS) { popUpTo(Rutas.INICIO); launchSingleTop = true } },
                        onAgregar = { nav.navigate(Rutas.formularioMascota()) },
                        onPedirCita = { nav.navigate(Rutas.pedirCita()) },
                        onPedirCitaTipo = { nav.navigate(Rutas.pedirCita(tipo = it.name)) }
                    )
                }

                composable(Rutas.MASCOTAS) {
                    val vm: InicioViewModel = viewModel(factory = factory)
                    MascotasScreen(
                        vm = vm,
                        duenoId = duenoId,
                        onMascota = { nav.navigate(Rutas.fichaMascota(it)) },
                        onAgregar = { nav.navigate(Rutas.formularioMascota()) }
                    )
                }

                composable(
                    Rutas.FORMULARIO_MASCOTA,
                    arguments = listOf(navArgument("id") { type = NavType.IntType; defaultValue = -1 })
                ) { entrada ->
                    val id = entrada.arguments?.getInt("id")?.takeIf { it > 0 }
                    val vm: MascotaViewModel = viewModel(factory = factory)
                    FormularioMascotaScreen(
                        vm = vm,
                        duenoId = duenoId,
                        mascotaId = id,
                        onListo = { nav.popBackStack() },
                        // Al eliminar, vuelve a la lista (la ficha ya no existe)
                        onEliminada = { nav.popBackStack(Rutas.FICHA_MASCOTA, inclusive = true) },
                        onAtras = { nav.popBackStack() }
                    )
                }

                composable(
                    Rutas.FICHA_MASCOTA,
                    arguments = listOf(navArgument("id") { type = NavType.IntType })
                ) { entrada ->
                    val id = entrada.arguments?.getInt("id") ?: 0
                    val vm: MascotaViewModel = viewModel(factory = factory)
                    FichaMascotaScreen(
                        vm = vm,
                        mascotaId = id,
                        esDueno = true,
                        onPedirCita = { nav.navigate(Rutas.pedirCita(it)) },
                        onEditar = { nav.navigate(Rutas.formularioMascota(it)) },
                        onAtras = { nav.popBackStack() }
                    )
                }

                composable(
                    Rutas.PEDIR_CITA,
                    arguments = listOf(
                        navArgument("mascotaId") { type = NavType.IntType; defaultValue = -1 },
                        // Mejora (tipo de servicio): tipo opcional que llega desde las tarjetas de Inicio
                        navArgument("tipo") { type = NavType.StringType; nullable = true; defaultValue = null }
                    )
                ) { entrada ->
                    val mascotaId = entrada.arguments?.getInt("mascotaId")?.takeIf { it > 0 }
                    val tipo = entrada.arguments?.getString("tipo")?.let { TipoServicio.de(it) }
                    val vm: PedirCitaViewModel = viewModel(factory = factory)
                    PedirCitaScreen(
                        tipoInicial = tipo,
                        vm = vm,
                        duenoId = duenoId,
                        mascotaInicial = mascotaId,
                        aceptaCitas = aceptaCitas,
                        // Vuelve a "Mis citas" y saca Pedir cita de la pila
                        onCitaCreada = {
                            nav.navigate(Rutas.MIS_CITAS) {
                                popUpTo(Rutas.INICIO)
                                launchSingleTop = true
                            }
                        },
                        onAtras = { nav.popBackStack() }
                    )
                }

                composable(Rutas.MIS_CITAS) {
                    val vm: MisCitasViewModel = viewModel(factory = factory)
                    MisCitasScreen(
                        vm = vm,
                        duenoId = duenoId,
                        onPedirCita = { nav.navigate(Rutas.pedirCita()) },
                        onReprogramar = { nav.navigate(Rutas.reprogramarCita(it)) }
                    )
                }

                composable(
                    Rutas.REPROGRAMAR_CITA,
                    arguments = listOf(navArgument("id") { type = NavType.IntType })
                ) { entrada ->
                    val vm: ReprogramarCitaViewModel = viewModel(factory = factory)
                    ReprogramarCitaScreen(
                        vm = vm,
                        citaId = entrada.arguments?.getInt("id") ?: 0,
                        onReprogramada = { nav.popBackStack() },
                        onAtras = { nav.popBackStack() }
                    )
                }

                composable(Rutas.PERFIL) {
                    val inicioVm: InicioViewModel = viewModel(factory = factory)
                    val citasVm: MisCitasViewModel = viewModel(factory = factory)
                    PerfilScreen(
                        sesion = sesion,
                        inicioVm = inicioVm,
                        citasVm = citasVm,
                        duenoId = duenoId,
                        onCambiarPerfil = { rol -> nav.irAlInicio(rol) },
                        onCerrarSesion = { nav.navigate(Rutas.LOGIN) { popUpTo(0) { inclusive = true } } }
                    )
                }
            }

            // ==================== Grafo del veterinario ====================
            navigation(startDestination = Rutas.PANEL, route = GRAFO_VET) {

                composable(Rutas.PANEL) {
                    val vm: PanelViewModel = viewModel(factory = factory)
                    PanelScreen(
                        vm = vm,
                        onVerPendientes = {
                            nav.navigate(Rutas.AGENDA) { popUpTo(Rutas.PANEL); launchSingleTop = true }
                        }
                    )
                }

                composable(Rutas.AGENDA) {
                    val vm: AgendaViewModel = viewModel(factory = factory)
                    AgendaScreen(vm = vm, onAtender = { nav.navigate(Rutas.atenderCita(it)) })
                }

                composable(
                    Rutas.ATENDER_CITA,
                    arguments = listOf(navArgument("id") { type = NavType.IntType })
                ) { entrada ->
                    val vm: AtenderCitaViewModel = viewModel(factory = factory)
                    AtenderCitaScreen(
                        vm = vm,
                        citaId = entrada.arguments?.getInt("id") ?: 0,
                        onTerminar = { nav.popBackStack() },
                        onAtras = { nav.popBackStack() }
                    )
                }

                composable(Rutas.PACIENTES) {
                    val vm: PacientesViewModel = viewModel(factory = factory)
                    PacientesScreen(vm = vm, onPaciente = { nav.navigate(Rutas.fichaPaciente(it)) })
                }

                // Misma ficha del dueño, sin los botones Pedir cita / Editar
                composable(
                    Rutas.FICHA_PACIENTE,
                    arguments = listOf(navArgument("id") { type = NavType.IntType })
                ) { entrada ->
                    val vm: MascotaViewModel = viewModel(factory = factory)
                    FichaMascotaScreen(
                        vm = vm,
                        mascotaId = entrada.arguments?.getInt("id") ?: 0,
                        esDueno = false,
                        onPedirCita = { },
                        onEditar = { },
                        onAtras = { nav.popBackStack() }
                    )
                }

                composable(Rutas.SERVICIOS) {
                    val vm: ServiciosViewModel = viewModel(factory = factory)
                    ServiciosScreen(
                        vm = vm,
                        onEditar = { nav.navigate(Rutas.formularioServicio(it)) },
                        onNuevo = { nav.navigate(Rutas.formularioServicio()) }
                    )
                }

                composable(
                    Rutas.FORMULARIO_SERVICIO,
                    arguments = listOf(navArgument("id") { type = NavType.IntType; defaultValue = -1 })
                ) { entrada ->
                    val vm: ServiciosViewModel = viewModel(factory = factory)
                    FormularioServicioScreen(
                        vm = vm,
                        servicioId = entrada.arguments?.getInt("id")?.takeIf { it > 0 },
                        onListo = { nav.popBackStack() },
                        onAtras = { nav.popBackStack() }
                    )
                }

                composable(Rutas.AJUSTES) {
                    AjustesScreen(
                        sesion = sesion,
                        onCambiarPerfil = { rol -> nav.irAlInicio(rol) },
                        onCerrarSesion = { nav.navigate(Rutas.LOGIN) { popUpTo(0) { inclusive = true } } }
                    )
                }
            }

            // ==================== Mejora: grafo del administrador ====================
            // Un solo AdminViewModel para todo el grafo: la pestaña elegida y los avisos se comparten.
            navigation(startDestination = Rutas.ADMIN_PANEL, route = GRAFO_ADMIN) {

                composable(Rutas.ADMIN_PANEL) { entrada ->
                    val vm = adminVm(nav, entrada, factory)
                    AdminPanelScreen(
                        vm = vm,
                        nombre = usuario?.nombre ?: "",
                        onUsuarios = { rol -> vm.elegirRol(rol); nav.navigate(Rutas.ADMIN_USUARIOS) { popUpTo(Rutas.ADMIN_PANEL); launchSingleTop = true } },
                        onNuevoUsuario = { rol -> nav.navigate(Rutas.adminFormUsuario(rol = rol.name)) },
                        onMascotas = { nav.navigate(Rutas.ADMIN_MASCOTAS) { popUpTo(Rutas.ADMIN_PANEL); launchSingleTop = true } },
                        onNuevaMascota = { nav.navigate(Rutas.adminFormMascota()) },
                        onServicios = { nav.navigate(Rutas.ADMIN_SERVICIOS) { popUpTo(Rutas.ADMIN_PANEL); launchSingleTop = true } },
                        onAgenda = { nav.navigate(Rutas.ADMIN_AGENDA) { popUpTo(Rutas.ADMIN_PANEL); launchSingleTop = true } },
                        onAjustes = { nav.navigate(Rutas.ADMIN_AJUSTES) }
                    )
                }

                composable(Rutas.ADMIN_USUARIOS) { entrada ->
                    AdminUsuariosScreen(
                        vm = adminVm(nav, entrada, factory),
                        idActual = usuario?.id ?: 0,
                        onNuevo = { rol -> nav.navigate(Rutas.adminFormUsuario(rol = rol.name)) },
                        onEditar = { nav.navigate(Rutas.adminFormUsuario(id = it)) }
                    )
                }

                composable(
                    Rutas.ADMIN_FORM_USUARIO,
                    arguments = listOf(
                        navArgument("id") { type = NavType.IntType; defaultValue = -1 },
                        navArgument("rol") { type = NavType.StringType; nullable = true; defaultValue = null }
                    )
                ) { entrada ->
                    AdminFormUsuarioScreen(
                        vm = adminVm(nav, entrada, factory),
                        usuarioId = entrada.arguments?.getInt("id")?.takeIf { it > 0 },
                        rolInicial = entrada.arguments?.getString("rol")?.let { Rol.valueOf(it) } ?: Rol.CLIENTE,
                        onListo = { nav.popBackStack() },
                        onAtras = { nav.popBackStack() }
                    )
                }

                composable(Rutas.ADMIN_MASCOTAS) { entrada ->
                    AdminMascotasScreen(
                        vm = adminVm(nav, entrada, factory),
                        onNueva = { nav.navigate(Rutas.adminFormMascota()) },
                        onEditar = { nav.navigate(Rutas.adminFormMascota(it)) },
                        onFicha = { nav.navigate(Rutas.adminFichaMascota(it)) }
                    )
                }

                composable(
                    Rutas.ADMIN_FORM_MASCOTA,
                    arguments = listOf(navArgument("id") { type = NavType.IntType; defaultValue = -1 })
                ) { entrada ->
                    val admin = adminVm(nav, entrada, factory)
                    val duenos by admin.duenos.collectAsState()
                    val vm: MascotaViewModel = viewModel(factory = factory)
                    FormularioMascotaScreen(
                        vm = vm,
                        duenoId = 0,
                        mascotaId = entrada.arguments?.getInt("id")?.takeIf { it > 0 },
                        onListo = { nav.popBackStack() },
                        onEliminada = { nav.popBackStack(Rutas.ADMIN_MASCOTAS, inclusive = false) },
                        onAtras = { nav.popBackStack() },
                        duenos = duenos
                    )
                }

                composable(
                    Rutas.ADMIN_FICHA_MASCOTA,
                    arguments = listOf(navArgument("id") { type = NavType.IntType })
                ) { entrada ->
                    val vm: MascotaViewModel = viewModel(factory = factory)
                    FichaMascotaScreen(
                        vm = vm,
                        mascotaId = entrada.arguments?.getInt("id") ?: 0,
                        esDueno = false,
                        onPedirCita = { },
                        onEditar = { },
                        onAtras = { nav.popBackStack() }
                    )
                }

                // Reutiliza las pantallas del veterinario con rutas propias
                composable(Rutas.ADMIN_SERVICIOS) {
                    val vm: ServiciosViewModel = viewModel(factory = factory)
                    ServiciosScreen(
                        vm = vm,
                        onEditar = { nav.navigate(Rutas.adminFormServicio(it)) },
                        onNuevo = { nav.navigate(Rutas.adminFormServicio()) }
                    )
                }

                composable(
                    Rutas.ADMIN_FORM_SERVICIO,
                    arguments = listOf(navArgument("id") { type = NavType.IntType; defaultValue = -1 })
                ) { entrada ->
                    val vm: ServiciosViewModel = viewModel(factory = factory)
                    FormularioServicioScreen(
                        vm = vm,
                        servicioId = entrada.arguments?.getInt("id")?.takeIf { it > 0 },
                        onListo = { nav.popBackStack() },
                        onAtras = { nav.popBackStack() }
                    )
                }

                composable(Rutas.ADMIN_AGENDA) {
                    val vm: AgendaViewModel = viewModel(factory = factory)
                    AgendaScreen(vm = vm, onAtender = { nav.navigate(Rutas.adminAtender(it)) })
                }

                composable(
                    Rutas.ADMIN_ATENDER,
                    arguments = listOf(navArgument("id") { type = NavType.IntType })
                ) { entrada ->
                    val vm: AtenderCitaViewModel = viewModel(factory = factory)
                    AtenderCitaScreen(
                        vm = vm,
                        citaId = entrada.arguments?.getInt("id") ?: 0,
                        onTerminar = { nav.popBackStack() },
                        onAtras = { nav.popBackStack() }
                    )
                }

                composable(Rutas.ADMIN_AJUSTES) {
                    AjustesScreen(
                        sesion = sesion,
                        onCambiarPerfil = { },
                        onCerrarSesion = { nav.navigate(Rutas.LOGIN) { popUpTo(0) { inclusive = true } } },
                        esAdministrador = true
                    )
                }
            }
        }
    }
}

// Mejora (administrador): AdminViewModel guardado en la entrada del grafo del administrador.
// Así Panel, Usuarios, Formulario y Mascotas usan la MISMA instancia: la pestaña de rol elegida
// y el mensaje del Snackbar (por ejemplo "Veterinario creado") pasan de una pantalla a otra.
@Composable
private fun adminVm(nav: NavHostController, entrada: NavBackStackEntry, factory: VetViewModelFactory): AdminViewModel {
    val padre = remember(entrada) { nav.getBackStackEntry(GRAFO_ADMIN) }
    return viewModel(padre, factory = factory)
}
