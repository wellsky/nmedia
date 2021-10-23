package ru.netology.nmedia.repository

import android.content.SharedPreferences
import ru.netology.nmedia.api.ApiService
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.error.ApiError
import javax.inject.Inject

class UsersRepositoryServerImpl @Inject constructor(
    private val apiService: ApiService,
    private val appAuth: AppAuth
): UsersRepository {

    override suspend fun tryToSignIn(login: String, password: String): Boolean {
        val response = apiService.tryToSignIn(login, password)
        val body = response.body() ?: throw ApiError(response.code(), response.message())

        body.token?.let {
            appAuth.setAuth(body.id, body.token)
            return true
        }
        return false
    }
}