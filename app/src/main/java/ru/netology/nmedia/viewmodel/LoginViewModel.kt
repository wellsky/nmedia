package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.model.FeedModelState
import ru.netology.nmedia.repository.UsersRepository
import ru.netology.nmedia.repository.UsersRepositoryServerImpl
import javax.inject.Inject

enum class LoginFormState {
    NONE, ERROR, SUCCESS
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    application: Application,
    private val repository: UsersRepository,
) : ViewModel() {
    private val _state = MutableLiveData<LoginFormState>()

    val state: LiveData<LoginFormState>
        get() = _state

//    private val repository: UsersRepository =
//        UsersRepositoryServerImpl()

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