package emperatriz.fortyLove.presentation.Screen

import android.view.HapticFeedbackConstants
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.wear.compose.material.MaterialTheme
import emperatriz.fortyLove.data.model.Tanteo
import emperatriz.fortyLove.data.model.isSixSix


const val TRANSITION_MILLIS = 500

@Composable
fun Marcador(
    tanteo: Tanteo,
    puntoEllosClick: (() -> Unit),
    puntoNuestroClick: (() -> Unit),
    onReset: (() -> Unit),
    onUndo: (() -> Unit),
    cambiaColor: (() -> Unit),
    onNo: (() -> Unit),
    onSi: () -> Unit
){
    val view = LocalView.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        contentAlignment = Alignment.Center
    ) {
        AnimatedContent(
            targetState = tanteo.isSixSix(),
            transitionSpec = {
                EnterTransition.None togetherWith slideOutVertically(animationSpec = tween(TRANSITION_MILLIS * 2)) { 0 }
            },
            label = "TieBreakTransition"
        ) { isSixSix ->
            if (isSixSix){
                PreguntaTieBreak(
                    onNo = { 
                        view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                        onNo.invoke() 
                    },
                    onSi = { 
                        view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                        onSi.invoke() 
                    },
                    colorAzul = tanteo.colorAzul
                )
            }
            else{
                Puntuacion(
                    tanteo = tanteo,
                    puntoEllosClick = { 
                        view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                        puntoEllosClick.invoke() 
                    },
                    puntoNuestroClick = { 
                        view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                        puntoNuestroClick.invoke() 
                    },
                    onReset = { onReset.invoke() },
                    onUndo = { onUndo.invoke() },
                    cambiaColor = { cambiaColor.invoke() }
                )
            }
        }
    }
}
