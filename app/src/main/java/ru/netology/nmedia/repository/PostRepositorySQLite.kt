package ru.netology.nmedia.repository

import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.R
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post

class PostRepositorySQLite(private val dao: PostDao): PostRepository {
    override val data: MutableLiveData<List<Post>>

    init {
        data = MutableLiveData(dao.getAll())
    }

    override fun save(post: Post) {
        val id = post.id
        val saved = dao.save(post)

        val posts = if (id == 0L) {
            listOf(saved) + data.value as List<Post>
            //listOf(saved) + data.value!!
        } else {
            data.value?.map {
                if (it.id != id) it else saved
            }
        }
        data.value = posts
    }

    override fun viewById(id: Long) {
        dao.viewById(id)
        val posts = data.value?.map {
            if (it.id != id) it else it.copy(
                views = it.views + 1
            )
        }
        data.value = posts
    }

    override fun likeById(id: Long) {
        dao.likeById(id)
        val posts = data.value?.map {
            if (it.id != id) it else it.copy(
                likedByMe = !it.likedByMe,
                likes = if (it.likedByMe) it.likes - 1 else it.likes + 1
            )
        }
        data.value = posts
    }

    override fun removeById(id: Long) {
        dao.removeById(id)
        val posts = data.value?.filter { it.id != id }
        data.value = posts
    }

    override fun getPostById(id: Long): Post? {
        TODO("Not yet implemented")
    }

}