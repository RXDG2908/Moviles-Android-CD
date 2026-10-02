package com.leon.tecsupstore.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.leon.tecsupstore.data.pedidos
import com.leon.tecsupstore.ui.theme.LilaClaro

@Composable
fun PedidosScreen(onAbrirMenu: () -> Unit) {
    Scaffold(
        topBar = {
            BarraSuperior(
                titulo = "Mis pedidos",
                navigationIcon = {
                    IconButton(onClick = onAbrirMenu) {
                        Icon(Icons.Default.Menu, contentDescription = "Abrir menu")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(pedidos) { pedido ->
                // En camino en morado, entregado en verde
                val colorEstado =
                    if (pedido.estado == "En camino") MaterialTheme.colorScheme.primary else Color(0xFF2E7D32)

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = LilaClaro)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Pedido ${pedido.codigo}", fontWeight = FontWeight.Bold)
                        Text("${pedido.producto} - ${pedido.fecha}", style = MaterialTheme.typography.bodySmall)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(pedido.estado, color = colorEstado, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
