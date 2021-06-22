package ru.netology.nmedia
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.databinding.PostItemBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.NMediaHelpers
import ru.netology.nmedia.viewmodel.PostViewModel

interface AdapterListener {
    fun onView(post: Post)
    fun onLikeButtonClicked(post: Post)
    fun onShareButtonClicked(post: Post)
    fun onRemoveButtonClicked(post: Post)
    fun onEditButtonClicked(post: Post)
    fun onViewButtonClicked(post: Post)
    fun onVideoPreviewClicked(post: Post)
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
            //author.text = post.id.toString()
            published.text = post.published
            content.text = post.content
            views.text = NMediaHelpers.optimalCount(post.views)
            shares.text = NMediaHelpers.optimalCount(post.shares)
            like.text = NMediaHelpers.optimalCount(post.likes)
            like.isChecked = post.likedByMe

            if (post.attachedVideo != null) {
                videoPreview.visibility = View.VISIBLE

                videoPreview.setOnClickListener {
                    listener.onVideoPreviewClicked(post)
                }
            }

            views.setOnClickListener {
                listener.onView(post)
            }
            like.setOnClickListener {
                listener.onLikeButtonClicked(post)
            }
            shares.setOnClickListener {
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
                            R.id.menu_details -> {
                                listener.onViewButtonClicked(post)
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }
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