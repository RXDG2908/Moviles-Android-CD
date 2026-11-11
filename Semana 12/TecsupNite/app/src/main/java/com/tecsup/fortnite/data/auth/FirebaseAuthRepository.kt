package com.tecsup.fortnite.data.auth

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.tecsup.fortnite.domain.model.Rol
import com.tecsup.fortnite.domain.model.Sesion
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : AuthRepository {

    override suspend fun iniciarSesion(correo: String, clave: String): Result<Sesion> = try {
        val uid = auth.signInWithEmailAndPassword(correo, clave).await().user!!.uid
        Result.success(leerSesion(uid, correo))
    } catch (e: Exception) {
        // Si no se pudo leer el rol, no queda una sesión a medias
        auth.signOut()
        Result.failure(FalloIngreso(motivoDe(e), e))
    }

    // Traduce los errores de Firebase a un motivo que entiende el ViewModel
    private fun motivoDe(e: Exception): MotivoFallo = when (e) {
        is FirebaseAuthInvalidCredentialsException, is FirebaseAuthInvalidUserException -> MotivoFallo.CREDENCIALES
        is FirebaseNetworkException -> MotivoFallo.SIN_CONEXION
        is FirebaseTooManyRequestsException -> MotivoFallo.DEMASIADOS_INTENTOS
        is FirebaseFirestoreException ->
            if (e.code == FirebaseFirestoreException.Code.UNAVAILABLE) MotivoFallo.SIN_CONEXION else MotivoFallo.OTRO
        else -> MotivoFallo.OTRO
    }

    override suspend fun sesionActual(): Sesion? {
        val u = auth.currentUser ?: return null
        return try {
            leerSesion(u.uid, u.email ?: "")
        } catch (e: Exception) {
            // Sin conexión la sesión sigue activa; el panel Admin no se muestra hasta leer el rol
            Sesion(u.uid, u.email ?: "", Rol.USUARIO)
        }
    }

    override fun cerrarSesion() = auth.signOut()

    // El rol no se decide en la app: se lee de Firestore (usuarios/{uid})
    private suspend fun leerSesion(uid: String, correo: String): Sesion {
        val doc = db.collection("usuarios").document(uid).get().await()
        val rol = if (doc.getString("rol") == "admin") Rol.ADMIN else Rol.USUARIO
        return Sesion(uid, correo, rol)
    }
}
