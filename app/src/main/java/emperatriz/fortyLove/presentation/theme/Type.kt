package emperatriz.fortyLove.presentation.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Typography
import emperatriz.fortyLove.R

val moviePosterFontFamily = FontFamily(
    Font(R.font.movieposter, FontWeight.Normal)
)

// typography for wear
val typography = Typography(
    display1 = TextStyle(
        fontFamily = moviePosterFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 50.sp
    )
)
