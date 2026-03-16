package com.alonsonya.dailyfurnace.push

import android.Manifest
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavDeepLinkBuilder
import com.alonsonya.dailyfurnace.R
import com.alonsonya.dailyfurnace.data.api.RegisterPushReq
import com.alonsonya.dailyfurnace.data.api.TokenApi
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

private const val CHANNEL_FACTS = "facts_channel"

class AppFirebaseMessagingService : FirebaseMessagingService() {

    private val tokenApi: TokenApi by inject()

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "Refreshed token: $token")
        serviceScope.launch {
            runCatching {
                tokenApi.register(RegisterPushReq(token))
                FirebaseMessaging.getInstance().subscribeToTopic("facts")
                FirebaseMessaging.getInstance().subscribeToTopic("all")
            }
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        Log.d("FCM", "onMessageReceived data=${message.data} notif=${message.notification}")

        val title = message.data["title"]
            ?: message.notification?.title
            ?: getString(R.string.app_name)

        val body = message.data["body"]
            ?: message.notification?.body
            ?: "Новая печь дня"

        val furnaceId = message.data["furnaceId"]?.toIntOrNull() ?: -1
        val args = Bundle().apply { putInt("furnace_id", furnaceId) }

        val pendingIntent = NavDeepLinkBuilder(this)
            .setGraph(R.navigation.root_navigation_graph)
            .setDestination(R.id.furnaceFragment)
            .setArguments(args)
            .createPendingIntent()

        val notification = NotificationCompat.Builder(this, CHANNEL_FACTS)
            .setSmallIcon(R.drawable.fire)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        val canNotify = Build.VERSION.SDK_INT < 33 ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED

        Log.d("FCM", "canNotify=$canNotify sdk=${Build.VERSION.SDK_INT}")

        if (canNotify) {
            NotificationManagerCompat.from(this)
                .notify(if (furnaceId > 0) furnaceId else 1, notification)
        }
    }

}