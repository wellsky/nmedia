package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
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

    private var fragmentBinding: FragmentEditPostBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_new_post, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save -> {
                fragmentBinding?.let {
                    viewModel.changeContent(it.editorText.text.toString())
                    viewModel.save()
                    AndroidUtils.hideKeyboard(requireView())
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreate(savedInstanceState)
        val binding = FragmentEditPostBinding.inflate(layoutInflater)

        //arguments?.textArg?.let(binding.editorText::setText)
        binding.editorText.setText(viewModel.edited.value?.content ?: "")

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