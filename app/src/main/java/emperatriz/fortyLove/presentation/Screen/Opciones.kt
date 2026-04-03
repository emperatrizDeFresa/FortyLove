package emperatriz.fortyLove.presentation.Screen

import androidx.compose.animation.*
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.wear.tooling.preview.devices.WearDevices
import emperatriz.fortyLove.data.model.Color40Love
import emperatriz.fortyLove.data.model.Punto
import emperatriz.fortyLove.data.model.Tanteo
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AnimatedVisibilityScope.Opciones(
    tanteo: Tanteo,
    onColorSelected: (Color40Love) -> Unit = {}
) {
    val colors = listOf(
        Color40Love.AZUL,
        Color40Love.VERDE,
        Color40Love.DORADO,
        Color40Love.MORADO,
        Color40Love.MALVA,
        Color40Love.GRIS
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val radius = 63f // Radio del hexágono
        val circleSize = 58.dp

        for (i in 0 until 6) {
            val angleRad = Math.toRadians((i * 60.0) - 90.0) // Empezar desde arriba
            val xOffset = (radius * cos(angleRad)).dp
            val yOffset = (radius * sin(angleRad)).dp

            Box(
                modifier = Modifier
                    .offset(x = xOffset, y = yOffset)
                    .animateEnterExit(
                        enter = slideIn(tween(500, easing = rebote())) {
                            IntOffset(
                                (cos(angleRad) * 400).toInt(),
                                (sin(angleRad) * 400).toInt()
                            )
                        } + fadeIn(tween(500)),
                        exit = slideOut(tween(500)) {
                            IntOffset(
                                (cos(angleRad) * 400).toInt(),
                                (sin(angleRad) * 400).toInt()
                            )
                        } + fadeOut(tween(500))
                    )
                    .size(circleSize)
                    .clickable { onColorSelected(colors[i]) }
            ) {
                SplitCircle(
                    leftColor = Color(colors[i].izquierdo),
                    rightColor = Color(colors[i].derecho),
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun SplitCircle(
    leftColor: Color,
    rightColor: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        // Mitad izquierda
        drawArc(
            color = leftColor,
            startAngle = 90f,
            sweepAngle = 180f,
            useCenter = true,
            size = size
        )
        // Mitad derecha
        drawArc(
            color = rightColor,
            startAngle = 270f,
            sweepAngle = 180f,
            useCenter = true,
            size = size
        )
    }
}

@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
fun OpcionesPreview() {
    Box {
        AnimatedVisibility(visible = true) {
            Opciones(
                tanteo = Tanteo(saque = true, nosotros = Punto.CERO, ellos = Punto.CERO)
            )
        }
    }
}
