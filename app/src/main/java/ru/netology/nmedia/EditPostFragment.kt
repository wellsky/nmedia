package ru.netology.nmedia

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentEditPostBinding
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.util.StringArg
import ru.netology.nmedia.viewmodel.PostViewModel

class EditPostFragment : Fragment() {
    companion object {
        var Bundle.textArg: String? by StringArg
    }

    private val viewModel: PostViewModel by viewModels (
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreate(savedInstanceState)
        val binding = FragmentEditPostBinding.inflate(layoutInflater)
        //setContentView(binding.root)

        arguments?.textArg?.let(binding.editorText::setText)
        /*
        intent?.let {
            val text = it.getStringExtra(Intent.EXTRA_TEXT)

            if (!text.isNullOrBlank()) {
                binding.editorText.setText(text)
            }
        }
        */

        binding.saveButton.setOnClickListener {
            viewModel.changeContent(binding.editorText.text.toString())
            viewModel.save()
            AndroidUtils.hideKeyboard(requireView())
            findNavController().navigateUp()
        }

        /*
        binding.saveButton.setOnClickListener {
            val intent = Intent()

            if (binding.editorText.text.isNullOrBlank()) {
                setResult(Activity.RESULT_CANCELED, intent)
            } else {
                val content = binding.editorText.text.toString()
                intent.putExtra(Intent.EXTRA_TEXT, content)
                setResult(Activity.RESULT_OK, intent)
            }
            finish()
        }
        */

        /*
        binding.cancelButton.setOnClickListener {
            finish()
        }
        */

        binding.cancelButton.setOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }
}