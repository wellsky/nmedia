package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.*
import ru.netology.nmedia.dto.Post
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
        repository.getAllAsync(object: PostRepository.GetAllCallback {
            override fun onSuccess(posts: List<Post>) {
                _data.postValue(FeedModel(posts = posts, empty = posts.isEmpty()))
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }

        })
    }

    fun save() {
        edited.value?.let {
            repository.saveAsync(it, object : PostRepository.SaveCallback {
                override fun onSuccess() {
                    // Успешно сохранен
                    _postCreated.postValue(Unit)
                }

                override fun onError(e: Exception) {
                    _data.postValue(FeedModel(error = true))
                }

            })
        }
        /*
        edited.value?.let {
            thread {
                repository.save(it)
                _postCreated.postValue(Unit)
            }
        }
        edited.value = empty
         */
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

    fun likeById(id: Long, like: Boolean) {

            repository.likeByIdAsync(id, like, object : PostRepository.LikeByIdCallback {
                override fun onSuccess() {
                    // Успешно сохранен
                    _data.postValue(_data.value?.copy(posts = _data.value?.posts.orEmpty().map {
                        if (it.id != id) it else it.copy(
                            likedByMe = !it.likedByMe,
                            likes = if (it.likedByMe) it.likes - 1 else it.likes + 1
                        )
                    }))
                }

                override fun onError(e: Exception) {
                    _data.postValue(FeedModel(error = true))
                }

            })

        /*
        thread {
            val old = _data.value?.posts.orEmpty()

            _data.postValue(_data.value?.copy(posts = _data.value?.posts.orEmpty().map {
                if (it.id != id) it else it.copy(
                    likedByMe = !it.likedByMe,
                    likes = if (it.likedByMe) it.likes - 1 else it.likes + 1
                )
            }))

            try {
                repository.likeById(id, like)
            } catch (e: IOException) {
                _data.postValue(_data.value?.copy(posts = old))
            }
        }
        */
    }

    fun removeById(id: Long) {
        val old = _data.value?.posts.orEmpty()
        repository.removeByIdAsync(id, object: PostRepository.RemoveByIdCallback {
            override fun onSuccess() {
                _data.postValue(
                    _data.value?.copy(posts = _data.value?.posts.orEmpty()
                        .filter { it.id != id }
                    )
                )
            }

            override fun onError(e: Exception) {
                _data.postValue(_data.value?.copy(posts = old))
            }
        })
    /*
        thread {
            // Оптимистичная модель
            val old = _data.value?.posts.orEmpty()
            _data.postValue(
                _data.value?.copy(posts = _data.value?.posts.orEmpty()
                    .filter { it.id != id }
                )
            )
            try {
                repository.removeById(id)
            } catch (e: IOException) {
                _data.postValue(_data.value?.copy(posts = old))
            }
        }
         */
    }
}
