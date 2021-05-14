package ru.netology.nmedia
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.databinding.PostItemBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel

interface AdapterListener {
    fun onLikeButtonClicked(post: Post)
    fun onShareButtonClicked(post: Post)
    fun onView(post: Post)
}

class PostAdapterListener(val viewModel: PostViewModel): AdapterListener {
    override fun onLikeButtonClicked(post: Post) = viewModel.onLikeButtonClicked(post)
    override fun onShareButtonClicked(post: Post) = viewModel.onShareButtonClicked(post)
    override fun onView(post: Post) = viewModel.onView(post)
}

class PostAdapter (private val listener: AdapterListener):RecyclerView.Adapter<PostViewHolder>() {
    var list = emptyList<Post>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = PostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = list[position]
        holder.bind(post)
    }

    override fun getItemCount(): Int {
        return list.size
    }


}

class PostViewHolder(val binding: PostItemBinding, val listener: AdapterListener) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        with (binding) {
            avatar.setImageResource(post.avatar)
            author.text = post.author
            published.text = post.published
            content.text = post.content
            viewsCount.text = optimalCount(post.views)
            likesCount.text = optimalCount(post.likes)
            sharesCount.text = optimalCount(post.shares)
            likes.setImageResource(if (post.likedByMe) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_favorite_border_24)

            views.setOnClickListener {
                listener.onView(post)
            }
            likes.setOnClickListener {
                listener.onLikeButtonClicked(post)
            }
            share.setOnClickListener {
                listener.onShareButtonClicked(post)
            }
        }
    }

    fun optimalCount(count: Int): String {
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