package ru.netology.nmedia.repository
import ru.netology.nmedia.dto.Post
import androidx.lifecycle.LiveData

interface PostRepository {
    val data: LiveData<List<Post>>
    fun viewById(id: Long)
    fun shareById(id: Long)
    fun likeById(id: Long)
}