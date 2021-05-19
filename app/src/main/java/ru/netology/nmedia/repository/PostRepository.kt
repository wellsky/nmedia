package ru.netology.nmedia.repository
import ru.netology.nmedia.dto.Post
import androidx.lifecycle.LiveData

interface PostRepository {
    val data: LiveData<List<Post>>
    fun save(post: Post)
    fun viewById(id: Long)
    fun likeById(id: Long)
    fun removeById(id: Long)
}