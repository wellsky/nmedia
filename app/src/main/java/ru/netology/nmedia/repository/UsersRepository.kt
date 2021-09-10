package ru.netology.nmedia.repository

interface UsersRepository {
    suspend fun tryToSignIn(login: String, password: String): Boolean
}