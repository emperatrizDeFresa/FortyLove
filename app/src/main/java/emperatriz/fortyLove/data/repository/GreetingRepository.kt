package emperatriz.fortyLove.data.repository

import emperatriz.fortyLove.data.model.Tanteo

/**
 * Contrato del repositorio de Tanteo (estado de la pantalla).
 */
interface FortyLoveRepository {
    suspend fun getTanteo(): Tanteo
    suspend fun updateTanteo(newTanteo: Tanteo)
}
