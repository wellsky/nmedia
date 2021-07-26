package ru.netology.nmedia.activity

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

        arguments?.textArg?.let(binding.editorText::setText)

        binding.saveButton.setOnClickListener {
            viewModel.changeContent(binding.editorText.text.toString())
            viewModel.save()
            AndroidUtils.hideKeyboard(requireView())
            viewModel.loadPosts()
            findNavController().navigateUp()
        }

        binding.cancelButton.setOnClickListener {
            viewModel.loadPosts()
            findNavController().navigateUp()
        }

        return binding.root
    }
}