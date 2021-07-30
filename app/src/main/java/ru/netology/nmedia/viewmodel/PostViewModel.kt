package ru.netology.nmedia.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.*
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedError
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.repository.*
import ru.netology.nmedia.util.SingleLiveEvent
import java.io.IOException
import kotlin.concurrent.thread

private val empty = Post(
    id = 0,
    content = "",
    author = "",
    likedByMe = false,
    likes = 0,
    published = ""
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    // упрощённый вариант
    private val repository: PostRepository = PostRepositoryServerImpl()
    private val _data = MutableLiveData(FeedModel())
    val data: LiveData<FeedModel>
        get() = _data
    val edited = MutableLiveData(empty)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    init {
        loadPosts()
    }

    fun getById(id :Long): Post? {
        data.value?.posts?.map {
            if (it.id == id) {
                return it;
            }
        }
        return null
    }

    fun loadPosts() {
        _data.value = FeedModel(loading = true)
        repository.getAll(object : PostRepository.Callback<List<Post>> {
            override fun onSuccess(posts: List<Post>) {
                _data.value = FeedModel(posts = posts, empty = posts.isEmpty())
            }

            override fun onError(e: Exception) {
                _data.value = FeedModel(error = FeedError.ERROR_LOAD)
            }
        })
    }

    fun save() {
        edited.value?.let {
            repository.save(it, object : PostRepository.Callback<Post> {
                override fun onSuccess(post: Post) {
                    _postCreated.postValue(Unit)
                }
                override fun onError(e: Exception) {
                    _data.postValue(FeedModel(error = FeedError.ERROR_SAVE))
                }
            })
        }
        edited.value = empty
    }

    fun likeById(id: Long, like: Boolean) {
            repository.likeById(id, like, object : PostRepository.Callback<Post> {
                override fun onSuccess(post: Post) {
                    // Успешно сохранен
                    _data.postValue(_data.value?.copy(posts = _data.value?.posts.orEmpty().map {
                        if (it.id != id) it else it.copy(
                            likedByMe = !it.likedByMe,
                            likes = if (it.likedByMe) it.likes - 1 else it.likes + 1
                        )
                    }))
                }

                override fun onError(e: Exception) {
                    _data.postValue(FeedModel(error = FeedError.ERROR_LIKE))
                }

            })
    }

    fun removeById(id: Long) {
        val old = _data.value?.posts.orEmpty()
        repository.removeById(id, object : PostRepository.Callback<Unit> {
            override fun onSuccess(nothing: Unit) {
                _data.postValue(
                    _data.value?.copy(posts = _data.value?.posts.orEmpty()
                        .filter { it.id != id }
                    )
                )
            }

            override fun onError(e: Exception) {
                _data.postValue(_data.value?.copy(posts = old, error = FeedError.ERROR_REMOVE))
            }
        })
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun stopEdit() {
        edited.value = empty
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }
}
