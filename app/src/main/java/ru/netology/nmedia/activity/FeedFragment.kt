package ru.netology.nmedia.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.EditPostFragment.Companion.textArg
import ru.netology.nmedia.activity.PostDetailsFragment.Companion.postId
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedError
import ru.netology.nmedia.viewmodel.PostViewModel

class FeedFragment : Fragment() {
    //val viewModel: PostViewModel by viewModels()
    private val viewModel: PostViewModel by viewModels (
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreate(savedInstanceState)

        val binding = FragmentFeedBinding.inflate(inflater, container, false)

        val adapter = PostsAdapter(object : OnInteractionListener {
            override fun onEdit(post: Post) {
                viewModel.edit(post)
                findNavController().navigate(R.id.action_feedFragment_to_editPostFragment)
            }

            override fun onDetails(post: Post) {
                findNavController().navigate(
                    R.id.action_feedFragment_to_viewPostFragment,
                    Bundle().apply {
                        postId = post.id
                    }
                )
            }

            override fun onLike(post: Post, like: Boolean) {
                viewModel.likeById(post.id, like)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onShare(post: Post) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }

                val shareIntent =
                    Intent.createChooser(intent, getString(R.string.share_message_title))
                startActivity(shareIntent)
            }
        })

        binding.list.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner, { state ->
            adapter.submitList(state.posts)
            binding.progress.isVisible = state.loading
            binding.emptyText.isVisible = state.empty

            binding.errorGroup.isVisible = false

            when (state.error) {
                FeedError.ERROR_LOAD -> {
                    binding.errorGroup.isVisible = true
                }

                FeedError.ERROR_REMOVE -> {
                    Snackbar.make(requireView(), R.string.error_post_remove, Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.close_snackbar) {
                            // Responds to click on the action
                        }
                        .show()
                }

                FeedError.ERROR_SAVE -> {
                    Snackbar.make(requireView(), R.string.error_post_save, Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.close_snackbar) {
                            // Responds to click on the action
                        }
                        .show()
                }

                FeedError.ERROR_LIKE -> {
                    Snackbar.make(requireView(), R.string.error_post_like, Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.close_snackbar) {
                            // Responds to click on the action
                        }
                        .show()
                }
            }

        })

        binding.retryButton.setOnClickListener {
            viewModel.loadPosts()
        }

        binding.postEditorButton.setOnClickListener {
            viewModel.stopEdit()
            findNavController().navigate(R.id.action_feedFragment_to_editPostFragment)
        }

        binding.swiperefresh.setOnRefreshListener {
            binding.swiperefresh.setRefreshing(false)
            binding.progress.isVisible = true
            viewModel.loadPosts()
        }

        return binding.root
    }
}