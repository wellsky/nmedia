package ru.netology.nmedia.application

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class NMediaApplication : Application() {
//    private val appScope = CoroutineScope(Dispatchers.Default)
//
//    @Inject
//    lateinit var auth: AppAuth
//
//    override fun onCreate() {
//        super.onCreate()
//        setupAuth()
//        setupWork()
//    }
//
//    private fun setupAuth() {
//        appScope.launch {
//            // AppAuth.initApp(this@NMediaApplication)
//            auth.sendPushToken()
//        }
//    }
//
//    private fun setupWork() {
//        appScope.launch {
//            val constraints = Constraints.Builder()
//                .setRequiredNetworkType(NetworkType.CONNECTED)
//                .build()
//            val request = PeriodicWorkRequestBuilder<RefreshPostsWorker>(1, TimeUnit.MINUTES)
//                .setConstraints(constraints)
//                .build()
//            WorkManager.getInstance(this@NMediaApplication).enqueueUniquePeriodicWork(
//                RefreshPostsWorker.name,
//                ExistingPeriodicWorkPolicy.KEEP,
//                request
//            )
//        }
//    }
}