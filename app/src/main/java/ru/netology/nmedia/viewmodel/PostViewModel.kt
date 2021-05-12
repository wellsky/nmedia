package ru.netology.nmedia.viewmodel

import androidx.lifecycle.ViewModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryInMemory

class PostViewModel: ViewModel() {
    private val repository:PostRepository = PostRepositoryInMemory()
    val data = repository.data // так работает
    // val data by repository::data // так не работает

    fun onLikeButtonClicked() = repository.like()
    fun onShareButtonClicked() = repository.share()
    fun onView() = repository.view()
}
