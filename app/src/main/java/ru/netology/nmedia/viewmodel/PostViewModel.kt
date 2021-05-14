package ru.netology.nmedia.viewmodel

import androidx.lifecycle.ViewModel
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryInMemory

class PostViewModel: ViewModel() {
    private val repository: PostRepository = PostRepositoryInMemory()

    val data by repository::data

    fun onLikeButtonClicked(post: Post) = repository.likeById(post.id)
    fun onShareButtonClicked(post: Post) = repository.shareById(post.id)
    fun onView(post: Post) = repository.viewById(post.id)
}
