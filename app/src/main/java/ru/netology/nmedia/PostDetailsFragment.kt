package ru.netology.nmedia

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.EditPostFragment.Companion.textArg
import ru.netology.nmedia.databinding.FragmentPostDetailsBinding
import ru.netology.nmedia.util.LongArg
import ru.netology.nmedia.util.NMediaHelpers
import ru.netology.nmedia.viewmodel.PostViewModel


class PostDetailsFragment : Fragment() {
    companion object {
        var Bundle.postId: Long by LongArg
    }

    private val viewModel: PostViewModel by viewModels (
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreate(savedInstanceState)
        val binding = FragmentPostDetailsBinding.inflate(layoutInflater)

        val postId = arguments?.postId
        val post = viewModel.repository.getPostById(postId!!)

        if (post != null) {
            with(binding) {
                avatar.setImageResource(post.avatar)
                author.text = post.author
                published.text = post.published
                content.text = post.content
                //views.text = NMediaHelpers.optimalCount(post.views)
                //shares.text = NMediaHelpers.optimalCount(post.shares)
                //like.text = NMediaHelpers.optimalCount(post.likes)
                //like.isChecked = post.likedByMe

                if (post.attachedVideo != null) {
                    videoPreview.visibility = View.VISIBLE
                }
            }

            binding.editButton.setOnClickListener {
                viewModel.onEditButtonClicked(post)

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
                viewModel.onRemoveButtonClicked(post)
                findNavController().navigateUp()
            }
        }

        return binding.root
    }
}