package ru.netology.nmedia.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryServerImpl
import javax.inject.Inject

class RemovePostWorker @Inject constructor(
    @ApplicationContext applicationContext: Context,
    params: WorkerParameters,
    private val repository: PostRepository,
) : CoroutineWorker(applicationContext, params) {
    companion object {
        const val postKey = "post"
    }

    override suspend fun doWork(): Result {
        val id = inputData.getLong(postKey, 0L)
        if (id == 0L) {
            return Result.failure()
        }
//        val repository: PostRepository =
//            PostRepositoryServerImpl(
//                AppDb.getInstance(context = applicationContext).postDao(),
//                AppDb.getInstance(context = applicationContext).postWorkDao(),
//            )
        return try {
            repository.removeById(id)
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
