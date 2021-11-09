package ru.netology.nmedia.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.databinding.FragmentLoginBinding
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.viewmodel.LoginFormState
import ru.netology.nmedia.viewmodel.LoginViewModel
import ru.netology.nmedia.viewmodel.PostViewModel

@AndroidEntryPoint
class LoginFragment : Fragment() {
    @ExperimentalCoroutinesApi
    private val viewModel: LoginViewModel by viewModels (
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.submit.setOnClickListener {
            AndroidUtils.hideKeyboard(requireView())
            viewModel.tryToLogin(binding.login.text.toString(), binding.password.text.toString())
        }

        viewModel.state.observe(viewLifecycleOwner, {
            when (it) {
                LoginFormState.SUCCESS -> {
                    AndroidUtils.hideKeyboard(requireView())
                    findNavController().navigateUp()
                }
                LoginFormState.ERROR -> {
                    Snackbar.make(binding.root, R.string.error_auth, Snackbar.LENGTH_LONG)
                        .show()
                }
            }
        })

        return binding.root
    }
}