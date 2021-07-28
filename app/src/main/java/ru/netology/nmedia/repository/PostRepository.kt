package ru.netology.nmedia.repository
import ru.netology.nmedia.dto.Post
import androidx.lifecycle.LiveData

interface PostRepository {
    fun getAll(): List<Post>
    fun getById(id: Long): Post
    fun likeById(id: Long, like: Boolean)
    fun save(post: Post)
    fun removeById(id: Long)

    fun getAllAsync(callback: GetAllCallback)
    fun likeByIdAsync(id: Long, like: Boolean, callback:LikeByIdCallback)
    fun saveAsync(post:Post, callback: SaveCallback)
    fun removeByIdAsync(id: Long, callback: RemoveByIdCallback)

    interface GetAllCallback {
        fun onSuccess(posts: List<Post>) {}
        fun onError(e: Exception) {}
    }

    interface LikeByIdCallback {
        fun onSuccess() {}
        fun onError(e: Exception) {}
    }

    interface RemoveByIdCallback {
        fun onSuccess() {}
        fun onError(e: Exception) {}
    }

    interface SaveCallback {
        fun onSuccess() {}
        fun onError(e: Exception) {}
    }

}