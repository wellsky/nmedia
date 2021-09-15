package ru.netology.nmedia.repository

import android.net.Uri
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.internal.wait
import okio.IOException
import ru.netology.nmedia.api.*
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dao.PostWorkDao
import ru.netology.nmedia.dto.Attachment
import ru.netology.nmedia.dto.Media
import ru.netology.nmedia.dto.MediaUpload
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity
import ru.netology.nmedia.entity.PostWorkEntity
import ru.netology.nmedia.entity.toDto
import ru.netology.nmedia.entity.toEntity
import ru.netology.nmedia.enumeration.AttachmentType
import ru.netology.nmedia.error.ApiError
import ru.netology.nmedia.error.AppError
import ru.netology.nmedia.error.NetworkError
import ru.netology.nmedia.error.UnknownError

class PostRepositoryServerImpl(private val dao: PostDao, private val postWorkDao: PostWorkDao,) : PostRepository {
    override val data: Flow<List<Post>> = dao.getAll().map(List<PostEntity>::toDto).flowOn(Dispatchers.Default)

    companion object {
        const val AVATARS_FOLDER_URL = "http://10.0.2.2:9999/avatars/"
        const val ATTACHMENTS_FOLDER_URL = "http://10.0.2.2:9999/media/"
    }

    override suspend fun getAll() {
        try {
            val response = PostsApi.service.getAll()
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(body.toEntity())
            dao.setAllVisible()
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun setAllPostsVisible() {
        dao.setAllVisible()
    }

    override suspend fun save(post: Post) {
        try {
            val response = PostsApi.service.save(post)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(PostEntity.fromDto(body))
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun saveWithAttachment(post: Post, upload: MediaUpload) {
        try {
            val media = upload(upload)
            // TODO: add support for other types
            val postWithAttachment = post.copy(attachment = Attachment(media.id, AttachmentType.IMAGE))
            save(postWithAttachment)
        } catch (e: AppError) {
            throw e
        } catch (e: java.io.IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun upload(upload: MediaUpload): Media {
        try {
            val media = MultipartBody.Part.createFormData(
                "file", upload.file.name, upload.file.asRequestBody()
            )

            val response = PostsApi.service.upload(media)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            return response.body() ?: throw ApiError(response.code(), response.message())
        } catch (e: java.io.IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun removeById(id: Long) {
        try {
            val response = PostsApi.service.removeById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.removeById(id)
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun likeById(id: Long, like:Boolean) {
        try {
            val response = if (like) PostsApi.service.likeById(id) else PostsApi.service.dislikeById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(PostEntity.fromDto(body))
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override fun getNewerCount(id: Long): Flow<Int> = flow {
        while (false) {
            delay(5_000L)
            val response = PostsApi.service.getNewer(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(body.toEntity())
            emit(body.size)
        }
    }

    /**
     * Сохраняет редактируемый пост и ссылку на вложение в БД
     * Возвращает id сохраненного поста
     */
    override suspend fun saveWork(post: Post, upload: MediaUpload?): Long {
        try {
            val entity = PostWorkEntity.fromDto(post).apply {
                if (upload != null) {
                    this.uri = upload.file.toUri().toString()
                }
            }
            return postWorkDao.insert(entity)
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun processWork(id: Long) {
        try {
            // TODO: handle this in homework
            val entity = postWorkDao.getById(id)
            if (entity.uri != null) {
                val upload = MediaUpload(Uri.parse(entity.uri).toFile())
                println("processWork with attach, entity: $entity")
                saveWithAttachment(entity.toDto(), upload)
            } else {
                println("processWork no attach, entity: $entity")
                save(entity.toDto())
            }

            postWorkDao.removeById(id)
            dao.setAllVisible()
            println("processWork done")
        } catch (e: Exception) {
            throw UnknownError
        }
    }
}
