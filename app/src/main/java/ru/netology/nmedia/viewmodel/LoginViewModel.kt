package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ru.netology.nmedia.auth.AppAuth

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    fun sendLoginData(login: String, password: String) {
        AppAuth.getInstance().setAuth(5, "x-token")
        true
    }
}