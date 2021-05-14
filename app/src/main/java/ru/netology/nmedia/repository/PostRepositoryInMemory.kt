package ru.netology.nmedia.repository

import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.R
import ru.netology.nmedia.dto.Post

class PostRepositoryInMemory: PostRepository {
    override val data: MutableLiveData<List<Post>>

    init {
        data = MutableLiveData(
            listOf(
                Post(
                    id = 1,
                    author = "Автор с длинным именем и длинной фамилией",
                    content = "По отношению к языку лингвистический термин «текст» представляет собой единство значимых единиц речи — предложений. Наша речь состоит не только из слов как минимальных значимых единиц, а из предложений, которые объединяются в высказывание и образуют более крупную единицу речи — текст. Единство предложений в тексте оформляется общим содержанием и грамматически. С этой точки зрения дадим следующее определение, что такое текст.",
                    published = "21 мая в 18:0",
                    avatar = R.mipmap.ic_launcher_round,
                    views = 10099997,
                    likes = 99,
                    shares = 995
                ),
                Post(
                    id = 2,
                    author = "Второй автор с длинным именем и длинной фамилией",
                    content = "По отношению к языку лингвистический термин «текст» представляет собой единство значимых единиц речи — предложений. Наша речь состоит не только из слов как минимальных значимых единиц, а из предложений, которые объединяются в высказывание и образуют более крупную единицу речи — текст. Единство предложений в тексте оформляется общим содержанием и грамматически. С этой точки зрения дадим следующее определение, что такое текст.",
                    published = "22 мая в 18:0",
                    avatar = R.mipmap.ic_launcher_round,
                    views = 1,
                    likes = 9,
                    shares = 0
                ),
                Post(
                    id = 3,
                    author = "Вася Пупкин",
                    content = "Небольшой текст третьего сообщения",
                    published = "23 мая в 18:0",
                    avatar = R.mipmap.ic_launcher_round,
                    views = 0,
                    likes = 0,
                    shares = 0
                ),
                Post(
                    id = 4,
                    author = "Владимир Андреевич",
                    content = "По отношению к языку лингвистический термин «текст» представляет собой единство значимых единиц речи — предложений. Наша речь состоит не только из слов как минимальных значимых единиц, а из предложений, которые объединяются в высказывание и образуют более крупную единицу речи — текст. Единство предложений в тексте оформляется общим содержанием и грамматически. С этой точки зрения дадим следующее определение, что такое текст.",
                    published = "22 мая в 18:0",
                    avatar = R.mipmap.ic_launcher_round,
                    views = 0,
                    likes = 0,
                    shares = 0
                ),
            )
        )
    }

    override fun viewById(id: Long) {
        val posts = data.value?.map {
            if (it.id != id) it else it.copy(views = it.views + 1)
        }
        data.value = posts
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
    }

    override fun shareById(id: Long) {
        val posts = data.value?.map {
            if (it.id != id) it else it.copy(shares = it.shares + 1)
        }
        data.value = posts
    }
}