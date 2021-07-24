package ru.netology.nmedia.repository
import ru.netology.nmedia.dto.Post
import androidx.lifecycle.LiveData

interface PostRepository {
    fun getAll(): List<Post>
    fun getById(id: Long): Post
    fun likeById(id: Long)
    fun save(post: Post)
    fun removeById(id: Long)
}