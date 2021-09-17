package ru.netology.nmedia.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryServerImpl
import javax.inject.Inject


class RefreshPostsWorker @Inject constructor(
    @ApplicationContext applicationContext: Context,
    params: WorkerParameters,
    private val repository: PostRepository,
) : CoroutineWorker(applicationContext, params) {
    companion object {
        const val name = "ru.netology.work.RefreshPostsWorker"
    }

    override suspend fun doWork(): Result = withContext(Dispatchers.Default) {
//        val repository: PostRepository =
//            PostRepositoryServerImpl(
//                AppDb.getInstance(context = applicationContext).postDao(),
//                AppDb.getInstance(context = applicationContext).postWorkDao(),
//            )

        try {
            repository.getAll()
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }
}