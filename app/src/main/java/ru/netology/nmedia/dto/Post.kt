package ru.netology.nmedia.dto

data class Post (
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val avatar: Int,

    val views: Int = 0,
    val likes: Int = 0,
    val shares: Int = 0,

    val likedByMe: Boolean = false
)