package ru.netology.nmedia.repository

import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.R
import ru.netology.nmedia.dto.Post

class PostRepositoryInMemory: PostRepository {
    override val data: MutableLiveData<Post>

    init {
        data = MutableLiveData(
            Post(
                id = 1,
                author = "Автор с длинным именем и длинной фамилией",
                content = "По отношению к языку лингвистический термин «текст» представляет собой единство значимых единиц речи — предложений. Наша речь состоит не только из слов как минимальных значимых единиц, а из предложений, которые объединяются в высказывание и образуют более крупную единицу речи — текст. Единство предложений в тексте оформляется общим содержанием и грамматически. С этой точки зрения дадим следующее определение, что такое текст.",
                published = "21 мая в 18:0",
                avatar = R.mipmap.ic_launcher_round,
                views = 10099997,
                likes = 99,
                shares = 995
            )
        )
    }

    override fun view() {
        val post = data.value ?: return
        data.value = post.copy(
            views = post.views + 1
        )
    }

    override fun like() {
        val post = data.value ?: return
        data.value = post.copy(
            likedByMe = !post.likedByMe,
            likes = if (post.likedByMe) post.likes - 1 else post.likes + 1
        )
    }

    override fun share() {
        val post = data.value ?: return
        data.value = post.copy(
            shares = post.shares + 1
        )
    }
}