package ru.netology.nmedia.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.EditPostFragment.Companion.textArg
import ru.netology.nmedia.activity.PostDetailsFragment.Companion.postId
import ru.netology.nmedia.databinding.FragmentImageViewBinding
import ru.netology.nmedia.databinding.FragmentPostDetailsBinding
import ru.netology.nmedia.repository.PostRepositoryServerImpl
import ru.netology.nmedia.util.LongArg
import ru.netology.nmedia.util.StringArg
import ru.netology.nmedia.viewmodel.PostViewModel


class ImageViewFragment : Fragment() {
    companion object {
        var Bundle.imageUrl: String? by StringArg
    }

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        val binding = FragmentImageViewBinding.inflate(layoutInflater)

        val imageUrl = arguments?.imageUrl

        Glide.with(binding.image.context)
            .load(imageUrl)
            .timeout(10_000)
            .placeholder(R.drawable.ic_loading_100dp)
            .error(R.drawable.ic_error_100dp)
            .into(binding.image)

        return binding.root
    }
}