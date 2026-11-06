package com.tecsup.authfirebaseappleon

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun HomeScreen(
    onLogout: () -> Unit
) {
    val auth = FirebaseAuth.getInstance()
    val db = remember { FirebaseFirestore.getInstance() }
    val uid = auth.currentUser?.uid ?: ""
    val email = auth.currentUser?.email ?: "Usuario"

    var cursos by remember { mutableStateOf<List<Curso>>(emptyList()) }
    var error by remember { mutableStateOf<String?>(null) }
    var editando by remember { mutableStateOf<Curso?>(null) }
    var mostrarForm by remember { mutableStateOf(false) }

    // Lectura en tiempo real: solo los cursos del usuario autenticado
    DisposableEffect(uid) {
        val reg = db.collection("cursos").whereEqualTo("uid", uid)
            .addSnapshotListener { snap, e ->
                if (e != null) { error = e.localizedMessage; return@addSnapshotListener }
                cursos = snap?.documents?.map { d ->
                    Curso(
                        id = d.id,
                        nombre = d.getString("nombre") ?: "",
                        descripcion = d.getString("descripcion") ?: "",
                        creditos = (d.getLong("creditos") ?: 0L).toInt(),
                        uid = d.getString("uid") ?: ""
                    )
                }?.sortedBy { it.nombre.lowercase() } ?: emptyList()
            }
        onDispose { reg.remove() }
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { editando = null; mostrarForm = true },
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Nuevo curso") }
            )
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.TopCenter) {
            LazyColumn(
                modifier = Modifier.widthIn(max = 560.dp).fillMaxWidth(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 100.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Bienvenida (igual a la guía)
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Bienvenido/a 🎉", style = MaterialTheme.typography.headlineMedium)
                        Spacer(Modifier.height(8.dp))
                        Text("Sesión iniciada como: $email", textAlign = TextAlign.Center)
                        Spacer(Modifier.height(24.dp))
                        Button(
                            onClick = {
                                auth.signOut()
                                onLogout()
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Cerrar sesión")
                        }
                    }
                }
                item {
                    Text(
                        "Mis Cursos",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                }
                error?.let { msg ->
                    item { Text(msg, color = MaterialTheme.colorScheme.error, textAlign = TextAlign.Center) }
                }
                if (cursos.isEmpty()) {
                    item {
                        Text(
                            "Aún no tienes cursos. Toca «Nuevo curso» para agregar uno.",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                items(cursos, key = { it.id }) { c ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(Modifier.fillMaxWidth().padding(18.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    c.nombre,
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.weight(1f)
                                )
                                AssistChip(onClick = {}, label = { Text("${c.creditos} créditos") })
                            }
                            if (c.descripcion.isNotBlank()) {
                                Spacer(Modifier.height(4.dp))
                                Text(c.descripcion, style = MaterialTheme.typography.bodyMedium)
                            }
                            Spacer(Modifier.height(14.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                OutlinedButton(
                                    onClick = { editando = c; mostrarForm = true },
                                    modifier = Modifier.weight(1f)
                                ) { Text("Editar") }
                                OutlinedButton(
                                    onClick = {
                                        db.collection("cursos").document(c.id).delete()
                                            .addOnFailureListener { error = it.localizedMessage }
                                    },
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = MaterialTheme.colorScheme.error
                                    ),
                                    modifier = Modifier.weight(1f)
                                ) { Text("Eliminar") }
                            }
                        }
                    }
                }
            }
        }
    }

    if (mostrarForm) {
        CursoDialog(
            inicial = editando,
            onCancelar = { mostrarForm = false },
            onGuardar = { nombre, desc, cred ->
                val datos = hashMapOf(
                    "nombre" to nombre, "descripcion" to desc,
                    "creditos" to cred, "uid" to uid
                )
                val ref = db.collection("cursos")
                val tarea = if (editando == null) ref.add(datos)
                else ref.document(editando!!.id).set(datos)
                tarea.addOnFailureListener { error = it.localizedMessage }
                mostrarForm = false
            }
        )
    }
}

@Composable
private fun CursoDialog(
    inicial: Curso?,
    onCancelar: () -> Unit,
    onGuardar: (String, String, Int) -> Unit
) {
    var nombre by remember { mutableStateOf(inicial?.nombre ?: "") }
    var desc by remember { mutableStateOf(inicial?.descripcion ?: "") }
    var cred by remember { mutableStateOf(inicial?.creditos?.toString() ?: "") }

    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text(if (inicial == null) "Nuevo curso" else "Editar curso") },
        text = {
            Column {
                OutlinedTextField(nombre, { nombre = it }, label = { Text("Nombre") }, singleLine = true)
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(desc, { desc = it }, label = { Text("Descripción") })
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(cred, { cred = it.filter(Char::isDigit) }, label = { Text("Créditos") }, singleLine = true)
            }
        },
        confirmButton = {
            TextButton(
                enabled = nombre.isNotBlank(),
                onClick = { onGuardar(nombre.trim(), desc.trim(), cred.toIntOrNull() ?: 0) }
            ) { Text("Guardar") }
        },
        dismissButton = { TextButton(onClick = onCancelar) { Text("Cancelar") } }
    )
}
