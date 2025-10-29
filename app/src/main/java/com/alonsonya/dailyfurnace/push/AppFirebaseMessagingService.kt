package com.alonsonya.dailyfurnace.push

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavDeepLinkBuilder
import com.alonsonya.dailyfurnace.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

private const val CHANNEL_FACTS = "facts_channel"

class AppFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val data = message.data
        val title = data["title"] ?: getString(R.string.app_name)
        val body  = data["body"] ?: "Новый факт дня"
        val furnaceId = data["furnaceId"]?.toIntOrNull() ?: -1

        val args = Bundle().apply { putInt("furnace_id", furnaceId) }

        val pendingIntent = NavDeepLinkBuilder(this)
            .setGraph(R.navigation.root_navigation_graph)
            .setDestination(R.id.furnaceFragment)
            .setArguments(args)
            .createPendingIntent()

        val notification = NotificationCompat.Builder(this, CHANNEL_FACTS)
            .setSmallIcon(R.drawable.fireplace)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        val canNotify = Build.VERSION.SDK_INT < 33 ||
                ContextCompat.checkSelfPermission(
                    this, Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED

        if (canNotify) {
            NotificationManagerCompat.from(this)
                .notify(if (furnaceId > 0) furnaceId else 0, notification)
        }
    }
}