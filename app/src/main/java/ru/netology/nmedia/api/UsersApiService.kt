package ru.netology.nmedia.api

import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import ru.netology.nmedia.auth.AuthState

interface UsersApiService {
    @FormUrlEncoded
    @POST("users/authentication")
    suspend fun tryToSignIn(@Field("login") login: String, @Field("pass") pass: String): Response<AuthState>
}

object UsersApi {
    val service: UsersApiService by lazy {
        retrofit.create(UsersApiService::class.java)
    }
}