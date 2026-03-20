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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.NotificationCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.TimeText
import androidx.wear.ongoing.OngoingActivity
import androidx.wear.ongoing.Status
import androidx.wear.tooling.preview.devices.WearDevices
import emperatriz.fortyLove.R
import emperatriz.fortyLove.data.model.Punto
import emperatriz.fortyLove.data.model.Tanteo
import emperatriz.fortyLove.data.model.isSixSix
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
           cambiaColor = { viewModel.cambiaColor() },
           onNo = { viewModel.setTieBreak(false) },
           onSi = { viewModel.setTieBreak(true) }
       )
    }
}
