// Pide la ruta a pie a la Routes API de Google (computeRoutes, modo WALK).
package com.leon.campusgps.data

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import com.leon.campusgps.model.PasoRuta
import com.leon.campusgps.model.Posicion
import com.leon.campusgps.model.Ruta
import com.leon.campusgps.util.decodificarPolilinea
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.security.MessageDigest

class RutasRepositoryGoogle(private val context: Context) : RutasRepository {

    // La misma clave del mapa, leída del Manifest (viene de local.properties)
    private val clave: String by lazy {
        val info = context.packageManager.getApplicationInfo(
            context.packageName,
            PackageManager.GET_META_DATA
        )
        info.metaData?.getString("com.google.android.geo.API_KEY").orEmpty()
    }

    // Huella SHA-1 del certificado: Google la pide si la clave está restringida a esta app
    private val huella: String? by lazy { huellaCertificado() }

    override suspend fun rutaAPie(origen: Posicion, destino: Posicion): Result<Ruta> =
        withContext(Dispatchers.IO) {
            runCatching {
                check(clave.isNotBlank()) { "Falta MAPS_API_KEY" }
                val conexion = (URL(URL_RUTAS).openConnection() as HttpURLConnection).apply {
                    requestMethod = "POST"
                    connectTimeout = 8000
                    readTimeout = 8000
                    doOutput = true
                    setRequestProperty("Content-Type", "application/json; charset=utf-8")
                    setRequestProperty("X-Goog-Api-Key", clave)
                    // Solo se piden los campos que se usan (Google cobra según lo que se pide)
                    setRequestProperty("X-Goog-FieldMask", CAMPOS)
                    setRequestProperty("X-Android-Package", context.packageName)
                    huella?.let { setRequestProperty("X-Android-Cert", it) }
                }
                try {
                    conexion.outputStream.use { it.write(cuerpo(origen, destino).toByteArray()) }
                    val codigo = conexion.responseCode
                    val texto = (if (codigo in 200..299) conexion.inputStream else conexion.errorStream)
                        ?.bufferedReader()?.use { it.readText() }.orEmpty()
                    check(codigo in 200..299) { "Routes API respondió $codigo: ${texto.take(300)}" }
                    leerRuta(JSONObject(texto))
                } finally {
                    conexion.disconnect()
                }
            }.onFailure { Log.w(TAG, "No se pudo obtener la ruta a pie", it) }
        }

    private fun cuerpo(origen: Posicion, destino: Posicion): String = JSONObject().apply {
        put("origin", punto(origen))
        put("destination", punto(destino))
        put("travelMode", "WALK")
        put("languageCode", "es")
        put("units", "METRIC")
    }.toString()

    private fun punto(p: Posicion) = JSONObject().put(
        "location",
        JSONObject().put("latLng", JSONObject().put("latitude", p.latitud).put("longitude", p.longitud))
    )

    private fun leerRuta(json: JSONObject): Ruta {
        val ruta = json.optJSONArray("routes")?.optJSONObject(0)
            ?: error("No hay ruta a pie hasta ese lugar")
        val puntos = decodificarPolilinea(
            ruta.getJSONObject("polyline").getString("encodedPolyline")
        )
        // La duración llega como texto, por ejemplo "302s"
        val segundos = ruta.optString("duration", "0s").removeSuffix("s").toIntOrNull() ?: 0
        val pasos = mutableListOf<PasoRuta>()
        val tramos = ruta.optJSONArray("legs")
        for (i in 0 until (tramos?.length() ?: 0)) {
            val lista = tramos!!.getJSONObject(i).optJSONArray("steps") ?: continue
            for (j in 0 until lista.length()) {
                val paso = lista.getJSONObject(j)
                val indicacion = paso.optJSONObject("navigationInstruction") ?: continue
                pasos.add(
                    PasoRuta(
                        // Algunas indicaciones traen una segunda línea; se junta en una sola
                        instruccion = indicacion.optString("instructions").replace("\n", ". "),
                        maniobra = indicacion.optString("maneuver"),
                        metros = paso.optInt("distanceMeters")
                    )
                )
            }
        }
        return Ruta(puntos, ruta.optInt("distanceMeters"), segundos, pasos)
    }

    @Suppress("DEPRECATION")
    private fun huellaCertificado(): String? = runCatching {
        val pm = context.packageManager
        val firma = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            pm.getPackageInfo(context.packageName, PackageManager.GET_SIGNING_CERTIFICATES)
                .signingInfo?.apkContentsSigners?.firstOrNull()
        } else {
            pm.getPackageInfo(context.packageName, PackageManager.GET_SIGNATURES)
                .signatures?.firstOrNull()
        } ?: return@runCatching null
        MessageDigest.getInstance("SHA-1").digest(firma.toByteArray())
            .joinToString("") { "%02X".format(it) }
    }.getOrNull()

    private companion object {
        const val TAG = "RutasRepository"
        const val URL_RUTAS = "https://routes.googleapis.com/directions/v2:computeRoutes"
        const val CAMPOS = "routes.distanceMeters,routes.duration,routes.polyline.encodedPolyline," +
            "routes.legs.steps.distanceMeters,routes.legs.steps.navigationInstruction"
    }
}
