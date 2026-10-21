package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.data.local.entity.Mascota
import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.model.EstadoCita
import com.tecsup.huellitasvet.model.Fechas
import com.tecsup.huellitasvet.ui.components.AvisoCard
import com.tecsup.huellitasvet.ui.components.EstadoCitaChip
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.ui.components.Tarjeta
import com.tecsup.huellitasvet.ui.components.TextoGris
import com.tecsup.huellitasvet.ui.components.TituloSeccion
import com.tecsup.huellitasvet.ui.components.VetButton
import com.tecsup.huellitasvet.ui.veterinario.iconoServicio
import com.tecsup.huellitasvet.viewmodel.InicioViewModel
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Composable
fun InicioScreen(
    vm: InicioViewModel,
    duenoId: Int,
    nombre: String,
    recordatorios: Boolean,
    onMascota: (Int) -> Unit,
    onVerTodas: () -> Unit,
    onAgregar: () -> Unit,
    onPedirCita: () -> Unit
) {
    LaunchedEffect(duenoId) { vm.iniciar(duenoId) }
    val mascotas by vm.mascotas.collectAsState()
    val proximas by vm.proximasCitas.collectAsState()
    val servicios by vm.servicios.collectAsState()

    Column(
        Modifier.verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // ---------- Saludo ----------
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("Hola, ${nombre.substringBefore(" ")}", style = MaterialTheme.typography.headlineSmall)
                TextoGris("¿Cómo están tus mascotas hoy?")
            }
            Icon(Icons.Filled.Notifications, contentDescription = null)
        }

        TarjetaProximaCita(proximas.firstOrNull())

        // ---------- Mis mascotas ----------
        Row(verticalAlignment = Alignment.CenterVertically) {
            TituloSeccion("Mis mascotas", Modifier.weight(1f))
            TextButton(onClick = onVerTodas) { Text("Ver todas") }
        }
        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            items(mascotas, key = { it.id }) { m -> MascotaMini(m, onClick = { onMascota(m.id) }) }
            item { AgregarMini(onAgregar) }
        }

        // ---------- Servicios (accesos rápidos) ----------
        TituloSeccion("Servicios")
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            val accesos = servicios.take(3)
            accesos.forEach { s ->
                Tarjeta(Modifier.weight(1f), onClick = onPedirCita, fondo = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)) {
                    Column(
                        Modifier.fillMaxWidth().padding(vertical = 14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(iconoServicio(s.icono), contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.height(6.dp))
                        Text(s.nombre.substringBefore(" "), style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
            // Si hay menos de 3 servicios activos, las tarjetas mantienen su ancho
            repeat(3 - accesos.size) { Spacer(Modifier.weight(1f)) }
        }

        // ---------- Recordatorio: la cita que sigue después de la próxima ----------
        val siguiente = proximas.getOrNull(1)
        if (recordatorios && siguiente != null) {
            val dias = ChronoUnit.DAYS.between(LocalDate.now(), LocalDate.parse(siguiente.cita.fecha))
            val cuando = when (dias) {
                0L -> "hoy"
                1L -> "mañana"
                else -> "en $dias días"
            }
            AvisoCard(
                icono = Icons.Filled.Notifications,
                titulo = "Recordatorio",
                texto = "${siguiente.mascota.mascota.nombre}: ${siguiente.servicio.nombre.lowercase()} $cuando"
            )
        }

        VetButton("Pedir una cita", onClick = onPedirCita)
    }
}

// Tarjeta del color del rol con la próxima cita
@Composable
private fun TarjetaProximaCita(proxima: CitaConDetalle?) {
    val esquema = MaterialTheme.colorScheme
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = esquema.primary, contentColor = esquema.onPrimary),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Próxima cita", style = MaterialTheme.typography.bodySmall)
                if (proxima == null) {
                    Text("Aún no tienes citas", style = MaterialTheme.typography.titleMedium)
                } else {
                    Text(
                        "${proxima.mascota.mascota.nombre} · ${proxima.servicio.nombre}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.Schedule, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(6.dp))
                        Text(
                            "${Fechas.larga(proxima.cita.fecha)} · ${proxima.cita.hora}",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                    EstadoCitaChip(EstadoCita.valueOf(proxima.cita.estado), solido = true)
                }
            }
            if (proxima != null) {
                Box(
                    Modifier.size(58.dp).background(esquema.surface, RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    MascotaAvatar(Especie.valueOf(proxima.mascota.mascota.especie), tamano = 46.dp)
                }
            }
        }
    }
}

@Composable
private fun MascotaMini(m: Mascota, onClick: () -> Unit) {
    Tarjeta(Modifier.width(104.dp), onClick = onClick) {
        Column(
            Modifier.fillMaxWidth().padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MascotaAvatar(Especie.valueOf(m.especie), tamano = 56.dp)
            Spacer(Modifier.height(6.dp))
            Text(m.nombre, style = MaterialTheme.typography.titleSmall)
            TextoGris("Ver ficha")
        }
    }
}

// Tarjeta "+ Agregar" con borde del color del rol
@Composable
private fun AgregarMini(onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary),
        modifier = Modifier.width(104.dp).height(116.dp)
    ) {
        Column(
            Modifier.fillMaxWidth().padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Filled.Add, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(8.dp))
            Text("Agregar", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
        }
    }
}
