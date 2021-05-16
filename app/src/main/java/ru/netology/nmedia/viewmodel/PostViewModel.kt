package ru.netology.nmedia.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryInMemory

private val empty = Post()

class PostViewModel: ViewModel() {
    private val repository: PostRepository = PostRepositoryInMemory()

    val data by repository::data
    val edited = MutableLiveData(empty)

    fun save() {
        edited.value?.let {
            repository.save(it)
        }
        edited.value = empty
    }

    fun changeContent(content: String) {
        edited.value?.let {
            val text = content.trim()
            if (it.content != text) {
                edited.value = edited.value?.copy(content = text)
            }
        }
    }

    fun cancelEdit() {
        edited.value = empty
    }

    fun onEditButtonClicked(post: Post) {
        edited.value = post
    }

    fun onLikeButtonClicked(post: Post) = repository.likeById(post.id)
    fun onShareButtonClicked(post: Post) = repository.shareById(post.id)
    fun onRemoveButtonClicked(post: Post) = repository.removeById(post.id)
    fun onView(post: Post) = repository.viewById(post.id)
}
