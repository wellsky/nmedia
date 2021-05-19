package ru.netology.nmedia

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.EditPostFragment.Companion.textArg
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.viewmodel.PostViewModel

class FeedFragment : Fragment() {
    val viewModel: PostViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreate(savedInstanceState)

        val binding = FragmentFeedBinding.inflate(inflater, container, false)
        //setContentView(binding.root)

        val adapter = PostAdapter(object : AdapterListener {
            override fun onLikeButtonClicked(post: Post) = viewModel.onLikeButtonClicked(post)
            override fun onRemoveButtonClicked(post: Post) = viewModel.onRemoveButtonClicked(post)
            override fun onView(post: Post) = viewModel.onView(post)

            override fun onShareButtonClicked(post: Post) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, post.content)
                }
                val shareIntent = Intent.createChooser(intent, getString(R.string.share_message_title))
                startActivity(shareIntent)
                //viewModel.onShareButtonClicked(post)
            }

            override fun onEditButtonClicked(post: Post) {
                findNavController().navigate(
                    R.id.action_feedFragment_to_editPostFragment,
                    Bundle().apply {
                        textArg = post.content
                    }
                )
                viewModel.onEditButtonClicked(post)
                //editPostLauncher.launch(post.content)
            }

            override fun onVideoPreviewClicked(post: Post) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.attachedVideo))
                val videoIntent = Intent.createChooser(intent, getString(R.string.video_view_title))
                startActivity(videoIntent)
            }
        })

        binding.list.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) { posts ->
            adapter.submitList(posts)
            // binding.list.smoothScrollToPosition(0)
        }

        /**
         * Следующий код понадобится при редактировании постов в нежней панели, а не в отдельном фрагменте
        viewModel.edited.observe(viewLifecycleOwner) {
            if (it.id != 0L) {
                with (binding.content) {
                    requestFocus()
                    setText(it.content)
                }
                binding.editMessageGroup.visibility = View.VISIBLE
                binding.editMessageContent.text = it.content
            }
        }


        binding.save.setOnClickListener {
            with(binding.content) {
                if (text.isNullOrBlank()) {
                    Toast.makeText(context, R.string.error_empty_content, Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                viewModel.changeContent(text.toString())
                viewModel.save()

                setText("")
                clearFocus()
                AndroidUtils.hideKeyboard(it)
            }
            binding.editMessageGroup.visibility = View.GONE
        }

        binding.cancelEdition.setOnClickListener {
            with(binding.content) {
                viewModel.cancelEdit()
                setText("")
                clearFocus()
                AndroidUtils.hideKeyboard(it)
            }
            binding.editMessageGroup.visibility = View.GONE
        }
        */

        binding.postEditorButton.setOnClickListener {
            //editPostLauncher.launch(null)
            findNavController().navigate(R.id.action_feedFragment_to_editPostFragment)
        }

        return binding.root
    }
}