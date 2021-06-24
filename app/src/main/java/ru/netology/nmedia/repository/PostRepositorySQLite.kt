package ru.netology.nmedia.repository

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity

class PostRepositorySQLite(private val dao: PostDao): PostRepository {
    override val data: MutableLiveData<List<Post>>

    init {
        data = MutableLiveData(dao.getAll().value?.map {
            it.toDto()
        })
    }

    override fun getPostById(id: Long): Post? {
        TODO("Not yet implemented")
    }

    fun getAll() = Transformations.map(dao.getAll()) { list ->
        list.map {
            Post(it.id, it.author,it.content,it.published, 0, it.views, it.likes, it.likedByMe)
        }
    }

    override fun save(post: Post) {
        dao.save(PostEntity.fromDto(post))
    }

    override fun viewById(id: Long) {
        TODO("Not yet implemented")
    }

    override fun likeById(id: Long) {
        dao.likeById(id)
    }

    override fun removeById(id: Long) {
        dao.removeById(id)
    }

    /*
    override fun save(post: Post) {
        val id = post.id
        val saved = dao.save(PostEntity.fromDto(post))

        val posts = if (id == 0L) {
            listOf(saved) + data.value as List<Post>
        } else {
            data.value?.map {
                if (it.id != id) it else saved
            }
        }
        data.value = posts
    }
    */

    /*
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

    override fun getAll(id: Long): Post? {
        TODO("Not yet implemented")
    }
    */
}