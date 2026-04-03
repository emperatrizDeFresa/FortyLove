/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter to find the
 * most up to date changes to the libraries and their usages.
 */

package emperatriz.fortyLove.presentation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.app.NotificationCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.wear.ongoing.OngoingActivity
import androidx.wear.ongoing.Status
import emperatriz.fortyLove.R
import emperatriz.fortyLove.presentation.Screen.Marcador
import emperatriz.fortyLove.presentation.theme.FortyLoveTheme

class MainActivity : ComponentActivity() {

    private val vm: FortyLoveViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        setTheme(android.R.style.Theme_DeviceDefault)

        setContent {
            WearApp(viewModel = vm)
        }
    }

    override fun onStart() {
        super.onStart()
        val channel = NotificationChannel(
            "ongoing_activity_channel",
            getString(R.string.ongoing_notification_channel_name),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)



        val ongoingActivity = OngoingActivity.Builder(
            this,
            1,
            NotificationCompat.Builder(this, channel.id)
                .setSmallIcon(R.drawable.sports_baseball_48dp_ffffff_fill0_wght400_grad0_opsz48)
                .setContentTitle(getString(R.string.ongoing_notification_title))
                .setContentText(getString(R.string.ongoing_notification_text))
        )
            .setTouchIntent(
                PendingIntent.getActivity(
                    this,
                    0,
                    Intent(this, MainActivity::class.java),
                    PendingIntent.FLAG_IMMUTABLE
                )
            )
            .setStatus(
                Status.Builder()
                    .addTemplate(getString(R.string.ongoing_notification_text))
                    .build()
            )
            .build()
        ongoingActivity.apply(this)
    }

    override fun onStop() {
        super.onStop()
        if (!isChangingConfigurations) {
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(1)
        }
    }
}

@Composable
fun WearApp(viewModel: FortyLoveViewModel) {
    val tanteoState by viewModel.tanteo.collectAsState()

    FortyLoveTheme {
        Marcador(
            tanteo = tanteoState,
            puntoEllosClick = { viewModel.puntoPara(false) },
            puntoNuestroClick = { viewModel.puntoPara(true) },
            onReset = { viewModel.reset() },
            onUndo = { viewModel.deshacer() },
            onOpciones = { viewModel.setOpciones() },
            onNo = { viewModel.setTieBreak(false) },
            onSi = { viewModel.setTieBreak(true) },
            onColorSelected = { viewModel.setColor(it) }
        )
    }
}
