package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.*

private val empty = Post()

class PostViewModel(application: Application): AndroidViewModel(application) {
    //val repository: PostRepository = PostRepositoryInFiles(application)
    val repository: PostRepository = PostRepositorySQLite(AppDb.getInstance(context = application).postDao())

    val data = repository.getAll()
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

    fun onViewButtonClicked(post: Post) {

    }

    fun onLikeButtonClicked(post: Post) = repository.likeById(post.id)
    fun onRemoveButtonClicked(post: Post) = repository.removeById(post.id)
    fun onView(post: Post) = repository.viewById(post.id)
}
