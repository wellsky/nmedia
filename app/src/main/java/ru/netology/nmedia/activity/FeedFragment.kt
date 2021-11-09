package ru.netology.nmedia.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.ImageViewFragment.Companion.imageUrl
import ru.netology.nmedia.activity.PostDetailsFragment.Companion.postId
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel

@AndroidEntryPoint
class FeedFragment : Fragment() {
    //val viewModel: PostViewModel by viewModels()
    private var scrollToTop = false

    @ExperimentalCoroutinesApi
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

            override fun onAttachedImageView(attachmentUrl: String) {
                findNavController().navigate(
                    R.id.action_feedFragment_to_imageViewFragment,
                    Bundle().apply {
                        imageUrl = attachmentUrl
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

        lifecycleScope.launchWhenCreated {
            viewModel.data.collectLatest(adapter::submitData)
        }

        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow.collectLatest { state ->
                binding.swiperefresh.isRefreshing =
                    state.refresh is LoadState.Loading ||
                    state.prepend is LoadState.Loading ||
                    state.append is LoadState.Loading
            }
        }

        binding.newPostsButton.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.setAllPostsVisible().join()
                scrollToTop = true
                it.isVisible = false
            }
        }

//        binding.retryButton.setOnClickListener {
//            viewModel.loadPosts()
//        }

        binding.postEditorButton.setOnClickListener {
            viewModel.stopEdit()
            findNavController().navigate(R.id.action_feedFragment_to_editPostFragment)
        }

        binding.swiperefresh.setOnRefreshListener {
            adapter.refresh()
        }

        return binding.root
    }
}