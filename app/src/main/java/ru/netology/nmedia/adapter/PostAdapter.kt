package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostItemBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepositoryServerImpl

interface OnInteractionListener {
    fun onLike(post: Post, like: Boolean) {}
    fun onEdit(post: Post) {}
    fun onDetails(post: Post) {}
    fun onRemove(post: Post) {}
    fun onShare(post: Post) {}
}

class PostsAdapter (
    private val onInteractionListener: OnInteractionListener,
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = PostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}

class PostViewHolder(
    private val binding: PostItemBinding,
    private val onInteractionListener: OnInteractionListener,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            like.isChecked = post.likedByMe
            like.text = "${post.likes}"


            // avatar.setImageResource(R.drawable.ic_user_avatar)
            // val url = "https://electro.club/data/users/2308/avatar.jpg"
            // val url = "http://u21.plpstatic.ru/s/1oalcp051/74936e8efbd89f32480c9e41d2f93de4/56d75c0b9e80337f0375a73d9831e4e2.jpg"
            // val url = PostRepositoryServerImpl.AVATARS_FOLDER_URL + "notexists/" + post.authorAvatar
            val url = PostRepositoryServerImpl.AVATARS_FOLDER_URL + post.authorAvatar

            Glide.with(avatar.context)
                .load(url)
                .circleCrop()
                .timeout(10_000)
                .placeholder(R.drawable.ic_loading_100dp)
                .error(R.drawable.ic_error_100dp)
                .into(avatar)

            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.post_options)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.menu_remove -> {
                                onInteractionListener.onRemove(post)
                                true
                            }
                            R.id.menu_edit -> {
                                onInteractionListener.onEdit(post)
                                true
                            }
                            R.id.menu_details -> {
                                onInteractionListener.onDetails(post)
                                true
                            }

                            else -> false
                        }
                    }
                }.show()
            }

            like.setOnClickListener {
                onInteractionListener.onLike(post, like.isChecked) // В этой строке isChecked уже изменился после нажатия!!
            }

            share.setOnClickListener {
                onInteractionListener.onShare(post)
            }
        }
    }
}

class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}