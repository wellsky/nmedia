package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.R
import ru.netology.nmedia.dto.Post

class PostRepositoryInMemory: PostRepository {
    private var post = Post(
        id = 1,
        author = "Автор с длинным именем и длинной фамилией",
        content =  "По отношению к языку лингвистический термин «текст» представляет собой единство значимых единиц речи — предложений. Наша речь состоит не только из слов как минимальных значимых единиц, а из предложений, которые объединяются в высказывание и образуют более крупную единицу речи — текст. Единство предложений в тексте оформляется общим содержанием и грамматически. С этой точки зрения дадим следующее определение, что такое текст.",
        published = "21 мая в 18:0",
        avatar = R.mipmap.ic_launcher_round,
        views = 10099997,
        likes = 99,
        shares = 995
    )

    override val data = MutableLiveData<Post>(post)

    override fun view() {
        data.value = data.value?.copy(
            views = data.value!!.views + 1
        )
    }

    override fun like() {
        data.value = data.value?.copy(
            likedByMe = !data.value!!.likedByMe,
            likes = if (data.value!!.likedByMe) data.value!!.likes - 1 else data.value!!.likes + 1
        )
    }

    override fun share() {
        data.value = data.value?.copy(
            shares = data.value!!.shares + 1
        )
    }
}