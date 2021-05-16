package ru.netology.nmedia
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.databinding.PostItemBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel

interface AdapterListener {
    fun onView(post: Post)
    fun onLikeButtonClicked(post: Post)
    fun onShareButtonClicked(post: Post)
    fun onRemoveButtonClicked(post: Post)
    fun onEditButtonClicked(post: Post)
}

class PostAdapter (private val listener: AdapterListener):ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = PostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
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
            menu.setOnClickListener { it ->
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.post_options)
                    setOnMenuItemClickListener { menuItem ->
                        when (menuItem.itemId) {
                            R.id.menu_remove -> {
                                listener.onRemoveButtonClicked(post)
                                true
                            }
                            R.id.menu_edit -> {
                                listener.onEditButtonClicked(post)
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
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

class PostDiffCallback: DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }

}