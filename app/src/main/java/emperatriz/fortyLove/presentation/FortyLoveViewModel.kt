package emperatriz.fortyLove.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import emperatriz.fortyLove.data.model.Color40Love
import emperatriz.fortyLove.data.model.Punto
import emperatriz.fortyLove.data.model.Tanteo
import emperatriz.fortyLove.data.model.GameScore
import emperatriz.fortyLove.data.model.Pantalla
import emperatriz.fortyLove.data.model.SetScore
import emperatriz.fortyLove.data.model.TieBreakScore
import emperatriz.fortyLove.data.model.isReseted
import emperatriz.fortyLove.data.model.reset
import emperatriz.fortyLove.data.repository.FortyLoveRepository
import emperatriz.fortyLove.data.repository.InMemoryFortyLoveRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.abs

class FortyLoveViewModel(
    private val repository: FortyLoveRepository = InMemoryFortyLoveRepository()
) : ViewModel() {
    private val _tanteo = MutableStateFlow(Tanteo(saque = true, nosotros = Punto.CERO, ellos = Punto.CERO))
    private val _historial = mutableListOf<Tanteo>()
    val tanteo: StateFlow<Tanteo> = _tanteo.asStateFlow()

    init {
        viewModelScope.launch {
            val current = repository.getTanteo()
            _tanteo.value = current
        }
    }

    fun updateTanteo(newTanteo: Tanteo) {
        viewModelScope.launch {
            repository.updateTanteo(newTanteo)
            _tanteo.value = newTanteo
        }
    }

    fun puntoPara(nosotros: Boolean) {
        viewModelScope.launch {
            val current = _tanteo.value
            _historial.add(current) 

            var ours = current.nosotros
            var theirs = current.ellos
            var juegos = current.juegos
            var sets = current.sets
            var saque = current.saque
            var inTieBreak = current.inTieBreak
            var tieBreak = current.tieBreak

            if (inTieBreak) {
                // --- LÓGICA DE TIE-BREAK ---
                var tbOurs = tieBreak.nosotros
                var tbTheirs = tieBreak.ellos
                if (nosotros) tbOurs++ else tbTheirs++
                tieBreak = TieBreakScore(tbOurs, tbTheirs)

                // Rotación de saque en Tie-Break: Cambia tras el 1er punto y luego cada 2 puntos
                val totalPoints = tbOurs + tbTheirs
                if (totalPoints % 2 != 0) {
                    saque = !saque
                }

                // Condición de victoria en Tie-Break (7 puntos y diferencia de 2)
                if ((tbOurs >= 7 || tbTheirs >= 7) && abs(tbOurs - tbTheirs) >= 2) {
                    if (tbOurs > tbTheirs) {
                        sets = SetScore(sets.nosotros + 1, sets.ellos)
                    } else {
                        sets = SetScore(sets.nosotros, sets.ellos + 1)
                    }
                    // Reset tras set ganado por TB
                    juegos = GameScore(0, 0)
                    ours = Punto.CERO
                    theirs = Punto.CERO
                    inTieBreak = false
                    tieBreak = TieBreakScore(0, 0)
                    saque = !current.saque // Cambio de saque tras set
                }
            } else {
                // --- LÓGICA NORMAL DE JUEGO ---
                if (nosotros) {
                    when {
                        ours == Punto.VENTAJA -> {
                            juegos = GameScore(juegos.nosotros + 1, juegos.ellos)
                            ours = Punto.CERO; theirs = Punto.CERO; saque = !saque
                        }
                        ours == Punto.CUARENTA && theirs == Punto.CUARENTA -> ours = Punto.VENTAJA
                        theirs == Punto.VENTAJA -> theirs = Punto.CUARENTA
                        ours == Punto.CUARENTA && theirs != Punto.CUARENTA -> {
                            juegos = GameScore(juegos.nosotros + 1, juegos.ellos)
                            ours = Punto.CERO; theirs = Punto.CERO; saque = !saque
                        }
                        else -> ours = Punto.fromCode(ours.code + 1)
                    }
                } else {
                    when {
                        theirs == Punto.VENTAJA -> {
                            juegos = GameScore(juegos.nosotros, juegos.ellos + 1)
                            ours = Punto.CERO; theirs = Punto.CERO; saque = !saque
                        }
                        theirs == Punto.CUARENTA && ours == Punto.CUARENTA -> theirs = Punto.VENTAJA
                        ours == Punto.VENTAJA -> ours = Punto.CUARENTA
                        theirs == Punto.CUARENTA && ours != Punto.CUARENTA -> {
                            juegos = GameScore(juegos.nosotros, juegos.ellos + 1)
                            ours = Punto.CERO; theirs = Punto.CERO; saque = !saque
                        }
                        else -> theirs = Punto.fromCode(theirs.code + 1)
                    }
                }

                // Condición de victoria (6 juegos y diferencia de 2)
                if ((juegos.nosotros >= 6 || juegos.ellos >= 6) && abs(juegos.nosotros - juegos.ellos) >= 2) {
                    if (juegos.nosotros > juegos.ellos) {
                        sets = SetScore(sets.nosotros + 1, sets.ellos)
                    } else {
                        sets = SetScore(sets.nosotros, sets.ellos + 1)
                    }
                    juegos = GameScore(0, 0)
                    ours = Punto.CERO
                    theirs = Punto.CERO
                }
            }

            val updated = current.copy(
                saque = saque,
                nosotros = ours,
                ellos = theirs,
                juegos = juegos,
                sets = sets,
                inTieBreak = inTieBreak,
                tieBreak = tieBreak,
                preguntaRespondida = false,
                undo = false
            )
            updateTanteo(updated)
        }
    }

    fun setTieBreak(value: Boolean){
        viewModelScope.launch {
            val updated = _tanteo.value.copy(inTieBreak = value, preguntaRespondida = true)
            updateTanteo(updated)
        }
    }

    fun reset() {
        viewModelScope.launch {
            _historial.clear()
            val current = _tanteo.value
            val resetTanteo = if (current.isReseted()) current.copy(saque = !current.saque) else current.reset()
            updateTanteo(resetTanteo)
        }
    }

    fun deshacer() {
        viewModelScope.launch {
            if (_historial.isNotEmpty()) {
                var tanteoAnterior = _historial.removeAt(_historial.size - 1)
                tanteoAnterior = tanteoAnterior.copy(undo = true)
                updateTanteo(tanteoAnterior)
            }
        }
    }

    fun cambiaColor() {
        viewModelScope.launch {
            val updated = _tanteo.value.copy(color = if (_tanteo.value.color == Color40Love.AZUL) Color40Love.VERDE else Color40Love.AZUL)
            updateTanteo(updated)
        }
    }

    fun setOpciones(){
        viewModelScope.launch {
            val updated = _tanteo.value.copy(pantalla = Pantalla.OPCIONES)
            updateTanteo(updated)
        }
    }

    fun setColor(color: Color40Love){
        viewModelScope.launch {
            val updated = _tanteo.value.copy(color = color, pantalla = Pantalla.PUNTUACION)
            updateTanteo(updated)
        }
    }
}
