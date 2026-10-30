package com.tecsup.huellitasvet.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import com.tecsup.huellitasvet.model.Rol
import com.tecsup.huellitasvet.model.TipoServicio

// VetTheme(rol) entrega un ColorScheme según el perfil.
// En las pantallas usa MaterialTheme.colorScheme.primary, nunca un color fijo.
@Composable
fun VetTheme(
    rol: Rol = Rol.CLIENTE,
    oscuro: Boolean = false,
    content: @Composable () -> Unit
) {
    // Mejora: el administrador tiene su propio esquema cálido (sin grises)
    if (rol == Rol.ADMINISTRADOR) {
        MaterialTheme(colorScheme = if (oscuro) esquemaAdminOscuro() else esquemaAdminClaro(), typography = VetTypography, content = content)
        return
    }

    val primario = if (rol == Rol.ADMIN) VetPrimario else DuenoPrimario
    val contenedor = if (rol == Rol.ADMIN) VetContenedor else DuenoContenedor
    val fondo = if (rol == Rol.ADMIN) VetFondo else DuenoFondo

    val esquema = if (oscuro) {
        darkColorScheme(
            primary = primario,
            onPrimary = Color.White,
            primaryContainer = primario.copy(alpha = 0.35f),
            onPrimaryContainer = Color.White,
            secondary = primario,
            secondaryContainer = primario.copy(alpha = 0.35f),
            onSecondaryContainer = Color.White,
            background = FondoOscuro,
            surface = SuperficieOscura,
            surfaceContainer = SuperficieOscura,
            surfaceContainerHigh = SuperficieOscura,
            surfaceContainerHighest = SuperficieOscura,
            onBackground = Color(0xFFE2E8F0),
            onSurface = Color(0xFFE2E8F0),
            onSurfaceVariant = Color(0xFF94A3B8),
            surfaceVariant = Color(0xFF273449),
            outline = Color(0xFF475569),
            outlineVariant = Color(0xFF334155)
        )
    } else {
        lightColorScheme(
            primary = primario,
            onPrimary = Color.White,
            primaryContainer = contenedor,
            onPrimaryContainer = primario,
            secondary = primario,
            secondaryContainer = contenedor,
            onSecondaryContainer = primario,
            background = fondo,
            surface = Color.White,
            surfaceContainer = Color.White,
            surfaceContainerHigh = Color.White,
            surfaceContainerHighest = Color.White,
            onBackground = Color(0xFF0F172A),
            onSurface = Color(0xFF0F172A),
            onSurfaceVariant = Color(0xFF6B7280), // texto gris secundario
            surfaceVariant = Color(0xFFF1F5F9),   // fondo de selectores y horarios ocupados
            outline = Color(0xFFD1D5DB),
            outlineVariant = Color(0xFFE5E7EB)    // borde fino de tarjetas
        )
    }

    MaterialTheme(colorScheme = esquema, typography = VetTypography, content = content)
}

// ---------- Mejora: esquema del administrador ----------
private fun esquemaAdminClaro(): ColorScheme = lightColorScheme(
    primary = AdminPrimario,
    onPrimary = Color.White,
    primaryContainer = AdminContenedor,
    onPrimaryContainer = AdminPrimario,
    secondary = AdminPrimario,
    secondaryContainer = AdminContenedor,
    onSecondaryContainer = AdminPrimario,
    background = AdminFondo,
    surface = Color.White,
    surfaceContainer = Color.White,
    surfaceContainerHigh = Color.White,
    surfaceContainerHighest = Color.White,
    surfaceContainerLow = Color.White,
    onBackground = AdminTexto,
    onSurface = AdminTexto,
    onSurfaceVariant = AdminTextoSuave,     // texto secundario marrón cálido (no plomo)
    surfaceVariant = AdminSuperficieSuave,
    outline = AdminBorde,
    outlineVariant = AdminBordeFino,
    inverseSurface = AdminTexto,        // Snackbar marrón oscuro en vez del gris por defecto
    inverseOnSurface = AdminFondo,
    inversePrimary = AdminPrimarioOscuro
)

private fun esquemaAdminOscuro(): ColorScheme = darkColorScheme(
    primary = AdminPrimarioOscuro,
    onPrimary = AdminSobrePrimarioOscuro,
    primaryContainer = AdminSuperficieSuaveOscura,
    onPrimaryContainer = AdminPrimarioOscuro,
    secondary = AdminPrimarioOscuro,
    secondaryContainer = AdminSuperficieSuaveOscura,
    onSecondaryContainer = AdminPrimarioOscuro,
    background = AdminFondoOscuro,
    surface = AdminSuperficieOscura,
    surfaceContainer = AdminSuperficieOscura,
    surfaceContainerHigh = AdminSuperficieOscura,
    surfaceContainerHighest = AdminSuperficieOscura,
    surfaceContainerLow = AdminSuperficieOscura,
    onBackground = AdminTextoOscuro,
    onSurface = AdminTextoOscuro,
    onSurfaceVariant = AdminTextoSuaveOscuro,
    surfaceVariant = AdminSuperficieSuaveOscura,
    outline = AdminBordeOscuro,
    outlineVariant = AdminBordeOscuro,
    inverseSurface = AdminTextoOscuro,
    inverseOnSurface = AdminFondoOscuro,
    inversePrimary = AdminPrimario
)

// Color principal de cada rol (el administrador lo usa para distinguir a sus usuarios)
fun primarioDe(rol: Rol, oscuro: Boolean): Color = when (rol) {
    Rol.CLIENTE -> DuenoPrimario
    Rol.ADMIN -> VetPrimario
    Rol.ADMINISTRADOR -> if (oscuro) AdminPrimarioOscuro else AdminPrimario
}

// El tema activo es oscuro si el fondo es oscuro
@Composable
fun esTemaOscuro(): Boolean = MaterialTheme.colorScheme.background.luminance() < 0.3f

// Mejora: color de identidad de cada tipo de servicio (íconos, bordes y barras; el texto va en onSurface)
@Composable
fun colorDeTipo(tipo: TipoServicio): Color {
    val oscuro = esTemaOscuro()
    return when (tipo) {
        TipoServicio.SALUD -> if (oscuro) TipoSaludOscuro else TipoSalud
        TipoServicio.ESTETICA -> if (oscuro) TipoEsteticaOscuro else TipoEstetica
        TipoServicio.PREVENCION -> if (oscuro) TipoPrevencionOscuro else TipoPrevencion
    }
}
