package ru.netology.nmedia

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.launch
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    val viewModel: PostViewModel by viewModels()

    val editPostLauncher = registerForActivityResult(EditPostResultContract()) { result ->
        result?: return@registerForActivityResult
        viewModel.changeContent(result)
        viewModel.save()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



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
                viewModel.onEditButtonClicked(post)
                editPostLauncher.launch(post.content)
            }

            override fun onVideoPreviewClicked(post: Post) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.attachedVideo))
                val videoIntent = Intent.createChooser(intent, getString(R.string.video_view_title))
                startActivity(videoIntent)
            }
        })

        binding.list.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
            // binding.list.smoothScrollToPosition(0)
        }

        viewModel.edited.observe(this) {
            /*
            if (it.id != 0L) {
                with (binding.content) {
                    requestFocus()
                    setText(it.content)
                }
                binding.editMessageGroup.visibility = View.VISIBLE
                binding.editMessageContent.text = it.content
            }
            */
        }

        binding.save.setOnClickListener {
            with(binding.content) {
                if (text.isNullOrBlank()) {
                    Toast.makeText(this@MainActivity, R.string.error_empty_content, Toast.LENGTH_SHORT).show()
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

        binding.postEditorButton.setOnClickListener {
            editPostLauncher.launch(null)
        }
    }
}