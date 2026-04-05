package emperatriz.fortyLove.presentation.Screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.EaseOutCirc
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.InfiniteTransition
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.SportsBaseball
import androidx.compose.material.icons.filled.Undo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.tooling.preview.devices.WearDevices
import emperatriz.fortyLove.data.model.GameScore
import emperatriz.fortyLove.data.model.Punto
import emperatriz.fortyLove.data.model.Tanteo
import emperatriz.fortyLove.data.model.is40Love
import emperatriz.fortyLove.data.model.isSetBall

@Composable
fun AnimatedVisibilityScope.Puntuacion(
    tanteo: Tanteo,
    modifier: Modifier = Modifier,
    puntoEllosClick: (() -> Unit)? = null,
    puntoNuestroClick: (() -> Unit)? = null,
    onReset: (() -> Unit)? = null,
    onUndo: (() -> Unit)? = null,
    onOpciones: (() -> Unit),
) {
    val infiniteTransition = rememberInfiniteTransition(label = "MarcadorTransition")

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.animateEnterExit(
                enter = slideInVertically(initialOffsetY = { -it - 40 }, animationSpec = tween(TRANSITION_MILLIS-100, delayMillis = TRANSITION_MILLIS)),
                exit = slideOutVertically(targetOffsetY = { -it - 40 }, animationSpec = tween(TRANSITION_MILLIS))
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clickable(enabled = onReset != null) { onReset?.invoke() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Replay,
                    contentDescription = "Reset puntuación",
                    tint = Color.White.copy(alpha = 0.3f),
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(18.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable(enabled = true) { onOpciones.invoke() }
            ){
                Text(
                    text = "${tanteo.sets.nosotros} SET ${tanteo.sets.ellos}",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W400,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = "${tanteo.juegos.nosotros} - ${tanteo.juegos.ellos}",
                    color = Color.White,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 2.sp
                )
            }
            Spacer(modifier = Modifier.width(18.dp))

            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clickable(enabled = onUndo != null) { onUndo?.invoke() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Undo,
                    contentDescription = "Deshacer último punto",
                    tint = Color.White.copy(alpha = 0.3f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp, bottom = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .fillMaxHeight()
                    .padding(end = 4.dp)
                    .animateEnterExit(
                        enter = slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(TRANSITION_MILLIS-100, delayMillis = TRANSITION_MILLIS, easing = rebote())),
                        exit = slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(TRANSITION_MILLIS))
                    )
                    .background(
                        color = Color(tanteo.color.izquierdo),
                        shape = RoundedCornerShape(topStart = 0.dp, topEnd = 20.dp, bottomEnd = 20.dp, bottomStart = 0.dp)
                    )
                    .clickable(enabled = puntoNuestroClick != null) { puntoNuestroClick?.invoke() }
                    .padding(end = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                IconoDeSaque(tanteo.saque, Modifier.padding(bottom = 14.dp), infiniteTransition, tanteo.is40Love(), tanteo.isSetBall(), tanteo.color.atencion)
                Spacer(Modifier.size(0.dp))
                AnimatedContent(
                    targetState = if (tanteo.inTieBreak) tanteo.tieBreak.nosotros else tanteo.nosotros,
                    modifier = Modifier.fillMaxHeight(),
                    contentAlignment = Alignment.Center,
                    transitionSpec = { pointAnimation(tanteo.undo) },
                    label = "ScoreNosotrosAnimation"
                ) { score ->
                    Text(
                        text = "$score",
                        style = MaterialTheme.typography.display1,
                        fontSize = 110.sp,
                        textAlign = TextAlign.End,
                        modifier = Modifier
                            .width(50.dp)
                            .padding(top = 52.dp)
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .fillMaxHeight()
                    .padding(start = 4.dp)
                    .animateEnterExit(
                        enter = slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(TRANSITION_MILLIS-100, delayMillis = TRANSITION_MILLIS, easing = rebote())),
                        exit = slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(TRANSITION_MILLIS))
                    )
                    .background(
                        color = Color(tanteo.color.derecho),
                        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 0.dp, bottomEnd = 0.dp, bottomStart = 20.dp)
                    )
                    .clickable(enabled = puntoEllosClick != null) { puntoEllosClick?.invoke() }
                    .padding(start = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                AnimatedContent(
                    targetState = if (tanteo.inTieBreak) tanteo.tieBreak.ellos else tanteo.ellos,
                    modifier = Modifier.fillMaxHeight(),
                    contentAlignment = Alignment.Center,
                    transitionSpec = { pointAnimation(tanteo.undo) },
                    label = "ScoreEllosAnimation"
                ) { score ->
                    Text(
                        text = "$score",
                        style = MaterialTheme.typography.display1,
                        fontSize = 110.sp,
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .width(50.dp)
                            .padding(top = 52.dp)
                    )
                }
                IconoDeSaque(!tanteo.saque, Modifier.padding(bottom = 14.dp), infiniteTransition, tanteo.is40Love(), tanteo.isSetBall(), tanteo.color.atencion)
            }
        }
    }
}

@Composable
private fun IconoDeSaque(saque: Boolean, modifier: Modifier, infiniteTransition: InfiniteTransition, is40Love: Boolean, isSetBall: Boolean, atencion:Long) {
    val size by infiniteTransition.animateFloat(
        initialValue = if (is40Love) 28f else 16f,
        targetValue = 35f,
        animationSpec = infiniteRepeatable(tween(if (is40Love) 450 else 650, easing = EaseOutCubic), RepeatMode.Reverse),
        label = "sizeAnimation"
    )
    if (saque) {
        Icon(
            imageVector = if (is40Love) Icons.Filled.Favorite else Icons.Filled.SportsBaseball,
            contentDescription = "Indicador de saque",
            tint = if (isSetBall) Color(atencion) else Color.Yellow,
            modifier = modifier
                .size(size.dp)
                .rotate(if (is40Love) 0f else 90f),
        )
    }
}

const val TRANSITION_MILLIS_POINTS = 400
fun pointAnimation(reverse: Boolean = false): ContentTransform{
    return (
            slideInVertically(animationSpec = tween(TRANSITION_MILLIS_POINTS, easing = EaseOutBack)) { if (reverse) it else -it } + fadeIn(animationSpec = tween(TRANSITION_MILLIS_POINTS/2))
    )
    .togetherWith(
        slideOutVertically(animationSpec = tween(TRANSITION_MILLIS_POINTS, easing = EaseOutCirc)) { if (reverse) -it else it  } + fadeOut(animationSpec = tween(TRANSITION_MILLIS_POINTS/2, easing = EaseOutCirc))
    )
}

@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
fun PuntuacionPreview() {
    Box {
        AnimatedVisibility(visible = true) {
            Puntuacion(
                tanteo = Tanteo(saque = true, nosotros = Punto.TREINTA, ellos = Punto.QUINCE, juegos = GameScore(1, 4)),
                puntoEllosClick = {},
                puntoNuestroClick = {},
                onOpciones = {}
            )
        }
    }
}
