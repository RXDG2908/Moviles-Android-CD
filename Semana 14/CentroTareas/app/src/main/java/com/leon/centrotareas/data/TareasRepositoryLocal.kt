// Repository con datos ficticios: cargas simuladas con delay y el conteo de primos.
package com.leon.centrotareas.data

import android.util.Log
import com.leon.centrotareas.model.AvancePrimos
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.io.IOException

class TareasRepositoryLocal : TareasRepository {

    // 100 vueltas de 40 ms (unos 4 s); flowOn hace que corra en Dispatchers.Default
    override fun tareaConProgreso(): Flow<Float> = flow {
        Log.d("HILO", "Dentro: ${Thread.currentThread().name}")
        for (i in 1..100) {
            delay(40)
            emit(i / 100f)
        }
    }.flowOn(Dispatchers.Default)

    override suspend fun cargarPerfil(): String {
        delay(2000)
        return "Ana Torres (ficticia)"
    }

    override suspend fun cargarPedidos(simularError: Boolean): List<String> {
        delay(3000)
        if (simularError) throw IOException("Sin conexión")
        return listOf("Pedido 101", "Pedido 102", "Pedido 103")
    }

    // Cálculo pesado: va en Dispatchers.Default para no bloquear el hilo principal
    override suspend fun contarPrimos(hasta: Int): Int = withContext(Dispatchers.Default) {
        var cantidad = 0
        for (n in 2..hasta) {
            // Revisa si cancelaron para cortar el cálculo a mitad
            ensureActive()
            if (esPrimo(n)) cantidad++
        }
        cantidad
    }

    // Emite el avance cada 1 % y al final la cantidad de primos
    override fun contarPrimosConProgreso(hasta: Int): Flow<AvancePrimos> = flow {
        var cantidad = 0
        val paso = maxOf(1, hasta / 100)
        for (n in 2..hasta) {
            currentCoroutineContext().ensureActive()
            if (esPrimo(n)) cantidad++
            if (n % paso == 0) emit(AvancePrimos(n.toFloat() / hasta))
        }
        emit(AvancePrimos(1f, cantidad))
    }.flowOn(Dispatchers.Default)

    // Prueba dividir entre 2 y la raíz de n
    private fun esPrimo(n: Int): Boolean {
        var d = 2
        while (d.toLong() * d <= n) {
            if (n % d == 0) return false
            d++
        }
        return true
    }
}
