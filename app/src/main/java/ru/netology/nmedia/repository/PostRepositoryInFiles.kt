package ru.netology.nmedia.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.R
import ru.netology.nmedia.dto.Post

class PostRepositoryInFiles(private val context: Context): PostRepository {
    val FILE_NAME = "posts.json"

    private var nextId = 1L
    private val gson = Gson()
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type

    override val data: MutableLiveData<List<Post>>

    init {
        var posts: List<Post> = emptyList<Post>()
        val file = context.filesDir.resolve(FILE_NAME)

        if (file.exists()) {
            context.openFileInput(FILE_NAME).bufferedReader().use {
                posts = gson.fromJson(it, type)
            }
        }

        // Временно для тестирования Fragments посты создаются вручную
        /*
        posts = listOf(
            Post(
                id = nextId++,
                author = "Владимир Андреевич",
                //avatar = R.drawable.ic_user_avatar,
                content = "Короткий текст. Видео во вложении.",
                published = "22 мая в 18:0",
                views = 0,
                likes = 0,
                attachedVideo = "https://www.youtube.com/watch?v=WhWc3b3KhnY",
            ),
            Post(
                id = nextId++,
                author = "Второй автор с длинным именем и длинной фамилией",
                content = "Длинный текст. По отношению к языку лингвистический термин «текст» представляет собой единство значимых единиц речи — предложений. Наша речь состоит не только из слов как минимальных значимых единиц, а из предложений, которые объединяются в высказывание и образуют более крупную единицу речи — текст. Единство предложений в тексте оформляется общим содержанием и грамматически. С этой точки зрения дадим следующее определение, что такое текст. По отношению к языку лингвистический термин «текст» представляет собой единство значимых единиц речи — предложений. Наша речь состоит не только из слов как минимальных значимых единиц, а из предложений, которые объединяются в высказывание и образуют более крупную единицу речи — текст. Единство предложений в тексте оформляется общим содержанием и грамматически. С этой точки зрения дадим следующее определение, что такое текст. По отношению к языку лингвистический термин «текст» представляет собой единство значимых единиц речи — предложений. Наша речь состоит не только из слов как минимальных значимых единиц, а из предложений, которые объединяются в высказывание и образуют более крупную единицу речи — текст. Единство предложений в тексте оформляется общим содержанием и грамматически. С этой точки зрения дадим следующее определение, что такое текст. По отношению к языку лингвистический термин «текст» представляет собой единство значимых единиц речи — предложений. Наша речь состоит не только из слов как минимальных значимых единиц, а из предложений, которые объединяются в высказывание и образуют более крупную единицу речи — текст. Единство предложений в тексте оформляется общим содержанием и грамматически. С этой точки зрения дадим следующее определение, что такое текст. По отношению к языку лингвистический термин «текст» представляет собой единство значимых единиц речи — предложений. Наша речь состоит не только из слов как минимальных значимых единиц, а из предложений, которые объединяются в высказывание и образуют более крупную единицу речи — текст. Единство предложений в тексте оформляется общим содержанием и грамматически. С этой точки зрения дадим следующее определение, что такое текст.",
                published = "22 мая в 18:0",
                views = 1,
                likes = 9,
            ),
            Post(
                id = nextId++,
                author = "Вася Пупкин",
                content = "Небольшой текст третьего сообщения",
                published = "23 мая в 18:0",
                views = 0,
                likes = 0,
            )
        )
        */

        data = MutableLiveData(posts)
        nextId = getMaxPostId() + 1;
    }

    override fun getAll(): LiveData<List<Post>> = data

    private fun getMaxPostId(): Long {
        var lastId = 1L;
        data.value?.forEach {
            if (it.id > lastId) {
                lastId = it.id
            }
        }
        return lastId;
    }

    override fun getPostById(id: Long): Post? {
        //val post = data.value?.takeIf { it.id == id }
        //return post
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
        val file = context.filesDir.resolve(FILE_NAME)
        context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).bufferedWriter().use {
            it.write(gson.toJson(data.value))
        }
    }

}