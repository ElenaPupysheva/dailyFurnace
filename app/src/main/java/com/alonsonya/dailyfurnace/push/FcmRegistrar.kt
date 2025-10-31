package com.alonsonya.dailyfurnace.push

import android.content.Context
import androidx.core.content.edit
import com.alonsonya.dailyfurnace.data.api.RegisterPushReq
import com.alonsonya.dailyfurnace.data.api.TokenApi
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await
import java.security.MessageDigest

object FcmRegistrar {
    private const val PREFS = "push_prefs"
    private const val KEY_LAST_TOKEN_HASH = "last_token_hash"

    private fun String.sha256(): String {
        val d = MessageDigest.getInstance("SHA-256").digest(toByteArray())
        return d.joinToString("") { "%02x".format(it) }
    }

    suspend fun registerIfNeeded(context: Context, tokenApi: TokenApi) {
        val token = runCatching { FirebaseMessaging.getInstance().token.await() }.getOrNull() ?: return
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val newHash = token.sha256()
        val oldHash = prefs.getString(KEY_LAST_TOKEN_HASH, null)
        if (newHash == oldHash) {
            // уже регистрировали этот токен
            return
        }

        // отправляем токен на сервер
        tokenApi.register(RegisterPushReq(token = token))

        // подписываемся на базовые топики
        runCatching { FirebaseMessaging.getInstance().subscribeToTopic("facts").await() }
        runCatching { FirebaseMessaging.getInstance().subscribeToTopic("all").await() }

        // запоминаем токен
        prefs.edit { putString(KEY_LAST_TOKEN_HASH, newHash) }
    }
}