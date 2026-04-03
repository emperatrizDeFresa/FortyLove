package emperatriz.fortyLove.data.model

import kotlin.math.abs

/**
 * Modelo que representa el tanteo (estado de la pantalla).
 */

/**
 * Enum que modela los posibles valores de puntuación con su etiqueta y código entero.
 */
enum class Punto(val label: String, val code: Int) {
    CERO("0", 0),
    QUINCE("15", 1),
    TREINTA("30", 2),
    CUARENTA("40", 3),
    VENTAJA("V", 4);

    override fun toString(): String = label

    companion object {
        fun fromCode(code: Int): Punto = values().firstOrNull { it.code == code } ?: CERO
        fun fromLabel(label: String): Punto = values().firstOrNull { it.label == label } ?: CERO
    }
}

enum class Pantalla() {
    PUNTUACION(),
    TIE_BREAK(),
    OPCIONES()
}


data class GameScore(
    val nosotros: Int,
    val ellos: Int
)

data class SetScore(
    val nosotros: Int,
    val ellos: Int
)

data class TieBreakScore(
    val nosotros: Int,
    val ellos: Int
)


data class Tanteo(
    val saque: Boolean,
    val nosotros: Punto,
    val ellos: Punto,
    val juegos: GameScore = GameScore(0, 0),
    val colorAzul: Boolean = true,
    val sets: SetScore = SetScore(0, 0),
    val inTieBreak: Boolean = false,
    val tieBreak: TieBreakScore = TieBreakScore(0, 0),
    val preguntaRespondida: Boolean = false
)

fun Tanteo.is40Love(): Boolean {
    if (saque && nosotros == Punto.CUARENTA && ellos == Punto.CERO) return true
    if (!saque && nosotros == Punto.CERO && ellos == Punto.CUARENTA) return true
    return false
}

fun Tanteo.isSixSix(): Boolean {
    return juegos.nosotros == 6 && juegos.ellos == 6 && nosotros == Punto.CERO && ellos == Punto.CERO && tieBreak.nosotros == 0 && tieBreak.ellos == 0 && !preguntaRespondida
}

fun Tanteo.isReseted(): Boolean {
    return juegos.nosotros == 0 && juegos.ellos == 0 && nosotros == Punto.CERO && ellos == Punto.CERO && tieBreak.nosotros == 0 && tieBreak.ellos == 0 && sets.nosotros == 0 && sets.ellos == 0
}

fun Tanteo.screen(): Pantalla{
    if (isSixSix()) return Pantalla.TIE_BREAK

    return Pantalla.PUNTUACION
}

fun Tanteo.isSetBall(): Boolean {
    if (inTieBreak) {
        // En Tie-break, ganas con 7 puntos y diferencia de 2.
        // Tienes bola de set si tienes 6 o más puntos y vas por delante (6-0...6-5, 7-6, etc.)
        return (tieBreak.nosotros >= 6 && tieBreak.nosotros > tieBreak.ellos) ||
               (tieBreak.ellos >= 6 && tieBreak.ellos > tieBreak.nosotros)
    } else {
        // Un jugador gana el juego si tiene 40 y el otro menos, o si tiene Ventaja
        val nosotrosGanaJuego = (nosotros == Punto.CUARENTA && ellos.code < Punto.CUARENTA.code) || (nosotros == Punto.VENTAJA)
        val ellosGanaJuego = (ellos == Punto.CUARENTA && nosotros.code < Punto.CUARENTA.code) || (ellos == Punto.VENTAJA)

        // Gana el set si al ganar este juego llega a más de 6 (con dif de 2)
        val nosotrosGanaSet = nosotrosGanaJuego && (juegos.nosotros > juegos.ellos && juegos.nosotros >= 5 && abs(juegos.nosotros - juegos.ellos) >= 1)
        val ellosGanaSet = ellosGanaJuego && (juegos.ellos > juegos.nosotros && juegos.ellos >= 5 && abs(juegos.nosotros - juegos.ellos) >= 1)

        return nosotrosGanaSet || ellosGanaSet
    }
}

fun Tanteo.reset(): Tanteo {
    return Tanteo(
        saque = true,
        nosotros = Punto.CERO,
        ellos = Punto.CERO,
        juegos = GameScore(0, 0),
        colorAzul = true,
        sets = SetScore(0, 0),
        inTieBreak = false,
        tieBreak = TieBreakScore(0, 0),
        preguntaRespondida = false
    )
}
