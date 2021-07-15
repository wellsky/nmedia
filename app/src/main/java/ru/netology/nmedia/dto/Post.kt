package ru.netology.nmedia.dto

import ru.netology.nmedia.R

data class Post (
    val id: Long = 0,
    val author: String = "",
    val content: String = "",
    val published: String = "",
    val avatar: Int = R.drawable.ic_user_avatar,
    val views: Int = 0,
    val likes: Int = 0,
    val likedByMe: Boolean = false,
    val attachedVideo: String? = null
)