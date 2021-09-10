package ru.netology.nmedia.repository

import ru.netology.nmedia.api.UsersApi
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.error.ApiError

class UsersRepositoryServerImpl: UsersRepository {
    override suspend fun tryToSignIn(login: String, password: String): Boolean {
        val response = UsersApi.service.tryToSignIn(login, password)
        val body = response.body() ?: throw ApiError(response.code(), response.message())

        body.token?.let {
            AppAuth.getInstance().setAuth(body.id, body.token)
            return true
        }
        return false
    }
}