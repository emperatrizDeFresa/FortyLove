package emperatriz.fortyLove.data.repository

import emperatriz.fortyLove.data.model.Punto
import emperatriz.fortyLove.data.model.Tanteo
import emperatriz.fortyLove.data.model.GameScore
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Implementación en memoria del repositorio de Tanteo, útil para desarrollo y tests.
 */
class InMemoryFortyLoveRepository : FortyLoveRepository {
    private val mutex = Mutex()
    private var current = Tanteo(saque = true, nosotros = Punto.CERO, ellos = Punto.CERO, juegos = GameScore(0,0))

    override suspend fun getTanteo(): Tanteo = mutex.withLock { current }

    override suspend fun updateTanteo(newTanteo: Tanteo) {
        mutex.withLock { current = newTanteo }
    }
}
