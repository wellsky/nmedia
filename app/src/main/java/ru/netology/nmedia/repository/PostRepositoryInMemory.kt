package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.R
import ru.netology.nmedia.dto.Post

class PostRepositoryInMemory: PostRepository {
    private var post = Post(
        1,
        "Автор с длинным именем и длинной фамилией",
        "По отношению к языку лингвистический термин «текст» представляет собой единство значимых единиц речи — предложений. Наша речь состоит не только из слов как минимальных значимых единиц, а из предложений, которые объединяются в высказывание и образуют более крупную единицу речи — текст. Единство предложений в тексте оформляется общим содержанием и грамматически. С этой точки зрения дадим следующее определение, что такое текст.",
        "21 мая в 18:0",
         R.mipmap.ic_launcher_round,
        10099997,
        99,
        995
    )

    private val data = MutableLiveData<Post>(post)

    override fun get(): LiveData<Post> = data

    override fun view() {
        post = post.copy(
            views = post.views + 1
        )
        data.value = post
    }

    override fun like() {
        post = post.copy(
                likedByMe = !post.likedByMe,
                likes = if (post.likedByMe) post.likes - 1 else post.likes + 1
        )
        data.value = post
    }

    override fun share() {
        post = post.copy(
            shares = post.shares + 1
        )
        data.value = post
    }
}