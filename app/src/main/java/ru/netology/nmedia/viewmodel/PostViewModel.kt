package ru.netology.nmedia.viewmodel

import androidx.lifecycle.ViewModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryInMemory

class PostViewModel: ViewModel() {
    private val repository: PostRepository = PostRepositoryInMemory()

    val data by repository::data

    fun onLikeButtonClicked() = repository.like()
    fun onShareButtonClicked() = repository.share()
    fun onView() = repository.view()
}
