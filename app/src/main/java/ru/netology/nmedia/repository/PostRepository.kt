package ru.netology.nmedia.repository
import ru.netology.nmedia.dto.Post
import androidx.lifecycle.LiveData

interface PostRepository {
    val data: LiveData<Post>
    fun view()
    fun like()
    fun share()
}