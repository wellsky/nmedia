package ru.netology.nmedia.model

import ru.netology.nmedia.dto.Post

data class FeedModel(
    val posts: List<Post> = emptyList(),
    val loading: Boolean = false,
    val error: FeedError = FeedError.NO_ERROR,
    val empty: Boolean = false,
    val refreshing: Boolean = false,
)

enum class FeedError {
    NO_ERROR, ERROR_LOAD, ERROR_REMOVE, ERROR_SAVE, ERROR_LIKE
}
