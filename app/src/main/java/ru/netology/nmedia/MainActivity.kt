package ru.netology.nmedia

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = Post(
            1,
            "Автор с длинным именем и длинной фамилией",
            "По отношению к языку лингвистический термин «текст» представляет собой единство значимых единиц речи — предложений. Наша речь состоит не только из слов как минимальных значимых единиц, а из предложений, которые объединяются в высказывание и образуют более крупную единицу речи — текст. Единство предложений в тексте оформляется общим содержанием и грамматически. С этой точки зрения дадим следующее определение, что такое текст.",
            "21 мая в 18:0",
             R.mipmap.ic_launcher_round,
            2,
            101200
        )

        with (binding) {
            avatar.setImageResource(post.avatar)
            author.text = post.author
            published.text = post.published
            content.text = post.content
            likesCount.text = optimalCount(post.likes)
            viewsCount.text = optimalCount(post.views)

            likeIcon.setImageResource(if (post.likedByMe) R.drawable.ic_baseline_favorite_24  else R.drawable.ic_baseline_favorite_border_24)

            likeIcon.setOnClickListener {
                post.likedByMe = !post.likedByMe
                if (post.likedByMe) post.likes++ else post.likes--

                likeIcon.setImageResource(if (post.likedByMe) R.drawable.ic_baseline_favorite_24  else R.drawable.ic_baseline_favorite_border_24)
                likesCount.text = optimalCount(post.likes)
            }

            views.setOnClickListener {
                post.views ++
                viewsCount.text = optimalCount(post.views)
            }
        }
    }

    fun optimalCount(count: Int): String? {
       return when {
           (count >= 1000) -> {
               if ((count % 1000 < 100) || (count >= 100000)) {
                   val exp = (Math.log(count.toDouble()) / Math.log(1000.0)).toInt()
                   String.format(
                       "%d %c",
                       (count / Math.pow(1000.0, exp.toDouble())).toInt(),
                       "kMGTPE"[exp - 1]
                   )
               } else {
                   val exp = (Math.log(count.toDouble()) / Math.log(1000.0)).toInt()
                   String.format(
                       "%.1f %c",
                       count / Math.pow(1000.0, exp.toDouble()),
                       "kMGTPE"[exp - 1]
                   )
               }
           }
           else -> count.toString()
       }
    }
}