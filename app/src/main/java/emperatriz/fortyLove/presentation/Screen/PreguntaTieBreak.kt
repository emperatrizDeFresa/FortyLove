package emperatriz.fortyLove.presentation.Screen

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import emperatriz.fortyLove.data.model.Tanteo

@Composable
fun AnimatedVisibilityScope.PreguntaTieBreak(
    onSi: () -> Unit,
    onNo: () -> Unit,
    tanteo: Tanteo
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "TIE BREAK?",
            color = Color.White,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.animateEnterExit(
                enter = slideInVertically(initialOffsetY = { -it - 40 }, animationSpec = tween(TRANSITION_MILLIS, delayMillis = TRANSITION_MILLIS, easing = rebote())),
                exit = slideOutVertically(targetOffsetY = { -it - 40 }, animationSpec = tween(TRANSITION_MILLIS))
            )
        )

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 18.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Botón SÍ
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .fillMaxHeight()
                    .padding(end = 4.dp)
                    .animateEnterExit(
                        enter = slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(TRANSITION_MILLIS, delayMillis = TRANSITION_MILLIS, easing = rebote())),
                        exit = slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(TRANSITION_MILLIS))
                    )
                    .background(
                        color = Color(tanteo.color.izquierdo),
                        shape = RoundedCornerShape(topStart = 0.dp, topEnd = 20.dp, bottomEnd = 20.dp, bottomStart = 0.dp)
                    )
                    .clickable { onSi() },
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = "SÍ",
                    style = MaterialTheme.typography.display1,
                    fontSize = 100.sp,
                    color = Color.White,
                    textAlign = TextAlign.End,
                    modifier = Modifier.width(50.dp).padding(end = 12.dp)
                )
            }

            // Botón NO
            Box(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .fillMaxHeight()
                    .padding(start = 4.dp)
                    .animateEnterExit(
                        enter = slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(TRANSITION_MILLIS, delayMillis = TRANSITION_MILLIS, easing = rebote())),
                        exit = slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(TRANSITION_MILLIS))
                    )
                    .background(
                        color = Color(tanteo.color.derecho),
                        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 0.dp, bottomEnd = 0.dp, bottomStart = 20.dp)
                    )
                    .clickable { onNo() },
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "NO",
                    style = MaterialTheme.typography.display1,
                    fontSize = 100.sp,
                    color = Color.White,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.width(50.dp).padding(start = 12.dp)
                )
            }
        }
    }
}
