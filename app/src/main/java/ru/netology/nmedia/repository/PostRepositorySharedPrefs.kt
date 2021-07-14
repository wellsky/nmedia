package ru.netology.nmedia.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.R
import ru.netology.nmedia.dto.Post

class PostRepositorySharedPrefs(private val context: Context): PostRepository {
    val KEY = "posts"

    private var nextId = 1L
    private val prefs = context.getSharedPreferences("repository", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type

    override val data: MutableLiveData<List<Post>>

    init {
        var posts: List<Post> = emptyList<Post>()
        prefs.getString(KEY, null)?.let {
            posts = gson.fromJson(it, type)
        }
        data = MutableLiveData(posts)
    }

    override fun getAll(): LiveData<List<Post>> = data

    override fun getPostById(id: Long): Post? {
        data.value?.map {
            if (it.id == id) {
                return it;
            }
        }
        return null
    }

    override fun viewById(id: Long) {
        val posts = data.value?.map {
            if (it.id != id) it else it.copy(views = it.views + 1)
        }
        data.value = posts
        saveData()
    }

    override fun likeById(id: Long) {
        val posts = data.value?.map {
            if (it.id != id) {
                it
            } else {
                val newLikes = if (it.likedByMe) -1 else 1
                it.copy(likes = it.likes + newLikes, likedByMe = !it.likedByMe)
            }
        }
        data.value = posts
        saveData()
    }

    override fun removeById(id: Long) {
        val posts = data.value?.filter{ it.id!=id }
        data.value = posts
        saveData()
    }

    override fun save(post: Post) {
        if (post.id == 0L) {
            val posts = listOf(post.copy(
                id = nextId++,
                author = "Новый автор",
                published = "Сейчас",
                likedByMe = false
            ))
            data.value = if (data.value != null) posts + data.value!! else posts
        } else {
            val posts = data.value?.map {
                if (it.id != post.id) it else it.copy(content = post.content)
            }
            data.value = posts
        }
        saveData()
    }

    private fun saveData() {
        with (prefs.edit()) {
            putString(KEY, gson.toJson(data.value))
            apply()
        }
    }

}