package ru.netology.nmedia.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.internal.wait
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.EditPostFragment.Companion.textArg
import ru.netology.nmedia.databinding.FragmentPostDetailsBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.LongArg
import ru.netology.nmedia.viewmodel.PostViewModel


class PostDetailsFragment : Fragment() {
    companion object {
        var Bundle.postId: Long by LongArg
    }

    @ExperimentalCoroutinesApi
    private val viewModel: PostViewModel by viewModels (
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreate(savedInstanceState)
        val binding = FragmentPostDetailsBinding.inflate(layoutInflater)

        val postId = arguments?.postId

        viewModel.loadEditedPostById(postId!!)

        viewModel.edited.observe(viewLifecycleOwner) { post ->
            with(binding) {
                //avatar.setImageResource(post.authorAvatar)
                author.text = post.author
                published.text = post.published
                content.text = post.content
            }

            binding.editButton.setOnClickListener {
                viewModel.edit(post)

                findNavController().navigate(
                    R.id.action_viewPostFragment_to_editPostFragment,
                    Bundle().apply {
                        textArg = post.content
                    }
                )
            }

            binding.cancelButton.setOnClickListener {
                findNavController().navigateUp()
            }

            binding.removeButton.setOnClickListener {
                viewModel.removeById(post.id)
                findNavController().navigateUp()
            }
        }

        return binding.root
    }
}