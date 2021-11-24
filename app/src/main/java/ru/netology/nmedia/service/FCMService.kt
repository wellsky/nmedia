package ru.netology.nmedia.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nmedia.R
import ru.netology.nmedia.auth.AppAuth
import javax.inject.Inject
import kotlin.random.Random


@AndroidEntryPoint
class FCMService : FirebaseMessagingService() {
    val ACTION_LIKE = "LIKE"
    val ACTION_POST = "POST"

    private val action = "action"
    private val content = "content"
    private val channelId = "remote"
    private val recipientId = "recipientId"
    private val gson = Gson()

    @Inject
    lateinit var appAuth: AppAuth

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_remote_name)
            val descriptionText = getString(R.string.channel_remote_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        println("message invoked " + Gson().toJson(message))

        handleRecipientData(gson.fromJson(message.data[content], RecipientData::class.java))
    }

    override fun onNewToken(token: String) {
        appAuth.sendPushToken(token)
    }

    private fun handleRecipientData(data: RecipientData) {
        println("Handled: " + data.recipientId)
        when (data.recipientId) {
            null -> {
                showSimpleMessage(data.content)
            }

            appAuth.myId() -> {
                showSimpleMessage(data.content)
            }

            else -> {
                appAuth.sendPushToken()
            }
        }
    }

    private fun showSimpleMessage(text: String) {
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        NotificationManagerCompat.from(this)
            .notify(Random.nextInt(100_000), notification)
    }

    private fun handlePost(content: PostNotification) {
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(
                getString(
                    R.string.notification_user_posted,
                    content.userName
                )
            )
            .setContentText(content.postContent)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(content.postContent))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        NotificationManagerCompat.from(this)
            .notify(Random.nextInt(100_000), notification)
    }

    private fun handleLike(content: LikeNotification) {
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(
                getString(
                    R.string.notification_user_liked,
                    content.userName,
                    content.postAuthor,
                )
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        NotificationManagerCompat.from(this)
            .notify(Random.nextInt(100_000), notification)
    }

}

data class PostNotification (
    val userId: Long,
    val userName: String,
    val postId: Long,
    val postAuthor: String,
    val postContent: String
    )

data class LikeNotification (
    val userId: Long,
    val userName: String,
    val postId: Long,
    val postAuthor: String
    )

data class RecipientData (
    val recipientId: Long? = null,
    val content: String
    )