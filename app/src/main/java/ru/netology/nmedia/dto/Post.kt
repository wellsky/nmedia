package ru.netology.nmedia.dto

data class Post (
    val id: Int,
    val author: String,
    val content: String,
    val published: String,
    val avatar: Int,

    var likes: Int = 0,
    var views: Int = 0,
    var likedByMe: Boolean = false
)