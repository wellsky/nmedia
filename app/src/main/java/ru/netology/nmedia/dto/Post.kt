package ru.netology.nmedia.dto

import ru.netology.nmedia.R

data class Post (
    val id: Long = 0,
    val author: String = "",
    val content: String = "",
    val published: String = "",
    val avatar: Int = R.mipmap.ic_launcher_round,
    val views: Int = 0,
    val likes: Int = 0,
    val shares: Int = 0,
    val likedByMe: Boolean = false
)