package ru.netology.nmedia

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()

        viewModel.data.observe(this) { post ->
            with(binding) {
                avatar.setImageResource(post.avatar)
                author.text = post.author
                published.text = post.published
                content.text = post.content
                viewsCount.text = optimalCount(post.views)
                likesCount.text = optimalCount(post.likes)
                sharesCount.text = optimalCount(post.shares)
                likes.setImageResource(if (post.likedByMe) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_favorite_border_24)
            }
        }

        with(binding) {
            views.setOnClickListener {
                viewModel.view()
            }
            likes.setOnClickListener {
                viewModel.like()
            }
            share.setOnClickListener {
                viewModel.share()
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