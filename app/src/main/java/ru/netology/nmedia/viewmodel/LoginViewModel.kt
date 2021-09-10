package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.model.FeedModelState
import ru.netology.nmedia.repository.UsersRepository
import ru.netology.nmedia.repository.UsersRepositoryServerImpl

enum class LoginFormState {
    NONE, ERROR, SUCCESS
}

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val _state = MutableLiveData<LoginFormState>()

    val state: LiveData<LoginFormState>
        get() = _state


    private val repository: UsersRepository =
        UsersRepositoryServerImpl()

    fun tryToLogin(login: String, password: String) = viewModelScope.launch {
        try {
            repository.tryToSignIn(login, password)
            _state.value = LoginFormState.SUCCESS
        } catch (e: Exception) {
            _state.value = LoginFormState.ERROR
            println("Ошибка авторизации")
        }
    }
}