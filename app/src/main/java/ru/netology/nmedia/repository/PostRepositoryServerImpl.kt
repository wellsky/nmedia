package ru.netology.nmedia.repository

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.netology.nmedia.api.PostsApi
import ru.netology.nmedia.dto.Post
import java.lang.RuntimeException


class PostRepositoryServerImpl: PostRepository {
    //private val client = OkHttpClient.Builder()
    //    .connectTimeout(30, TimeUnit.SECONDS)
    //    .build()
    //private val gson = Gson()
    //private val typeToken = object : TypeToken<List<Post>>() {}
    //private val typePost = object : TypeToken<Post>() {}

    companion object {
        //private const val BASE_URL = "http://10.0.2.2:9999"
        //private val jsonType = "application/json".toMediaType()

        const val AVATARS_FOLDER_URL = "http://10.0.2.2:9999/avatars/"
    }

    override fun getAll(callback: PostRepository.Callback<List<Post>>) {
        PostsApi.retrofitService.getAll().enqueue(object : Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (!response.isSuccessful) {
                    callback.onError(RuntimeException(response.message()))
                    return
                }

                callback.onSuccess(response.body() ?: throw RuntimeException("body is null"))
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                callback.onError(RuntimeException("Error loading posts"))
            }
        })

        /*
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/posts")
            .build()

        client.newCall(request)
            .enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    val body = response.body?.string() ?: throw RuntimeException("Body is null")

                    try {
                        callback.onSuccess(gson.fromJson(body, typeToken.type))
                    } catch (e: Exception ){
                        callback.onError(e)
                    }

                }
            })
        */
    }

    override fun save(post: Post, callback: PostRepository.Callback<Post>) {
        PostsApi.retrofitService.save(post).enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (!response.isSuccessful) {
                    callback.onError(RuntimeException(response.message()))
                    return
                }

                callback.onSuccess(response.body() ?: throw RuntimeException("body is null"))
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun removeById(id: Long, callback: PostRepository.Callback<Unit>) {
        PostsApi.retrofitService.removeById(id).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (!response.isSuccessful) {
                    callback.onError(RuntimeException(response.message()))
                    return
                }
                callback.onSuccess(response.body() ?: throw RuntimeException("body is null"))
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                callback.onError(RuntimeException("Error removing post"))
            }
        })
    }

    override fun likeById(id: Long, like: Boolean, callback: PostRepository.Callback<Post>) {
        if (like) {
            PostsApi.retrofitService.likeById(id).enqueue(object : Callback<Post> {
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException(response.message()))
                        return
                    }
                    callback.onSuccess(response.body() ?: throw RuntimeException("body is null"))
                }

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
        } else {
            PostsApi.retrofitService.dislikeById(id).enqueue(object : Callback<Post> {
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException(response.message()))
                        return
                    }
                    callback.onSuccess(response.body() ?: throw RuntimeException("body is null"))
                }

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
        }
    }

    /*
    override fun saveAsync(post: Post, callback: PostRepository.SaveCallback) {
        val request: Request = Request.Builder()
            .post(gson.toJson(post).toRequestBody(jsonType))
            .url("${BASE_URL}/api/posts")
            .build()

        client.newCall(request)
            .enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    callback.onSuccess()
                }

            })
    }

    override fun save(post: Post) {
        val request: Request = Request.Builder()
            .post(gson.toJson(post).toRequestBody(jsonType))
            .url("${BASE_URL}/api/posts")
            .build()

        client.newCall(request)
            .execute()
            .close()
    }

    override fun getById(id: Long): Post {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/posts/${id}")
            .build()

        return client.newCall(request)
            .execute()
            .let { it.body?.string() ?: throw RuntimeException("body is null") }
            .let {
                gson.fromJson(it, typePost.type)
            }
    }

    override fun likeById(id: Long, like: Boolean) {
        if (like) {
            val request: Request = Request.Builder()
                .url("${BASE_URL}/api/posts/${id}/likes")
                .method("POST", EMPTY_REQUEST)
                .build()

                client.newCall(request)
                    .execute()
                    .close()
        } else {
            val request: Request = Request.Builder()
                .url("${BASE_URL}/api/posts/${id}/likes")
                .method("DELETE", null)
                .build()

                client.newCall(request)
                    .execute()
                    .close()
        }
    }

    override fun likeByIdAsync(id: Long, like: Boolean, callback: PostRepository.LikeByIdCallback) {
        val method: String = if (like) "POST" else "DELETE"

        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/posts/${id}/likes")
            .method(method, EMPTY_REQUEST)
            .build()

        client.newCall(request)
            .enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    callback.onSuccess()
                }

            })
    }

    override fun removeById(id: Long) {
        val request: Request = Request.Builder()
            .delete()
            .url("${BASE_URL}/api/posts/$id")
            .build()

        client.newCall(request)
            .execute()
            .close()
    }

    override fun removeByIdAsync(id: Long, callback: PostRepository.RemoveByIdCallback) {
        val request: Request = Request.Builder()
            .delete()
            .url("${BASE_URL}/api/posts/$id")
            .build()

        client.newCall(request)
            .enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    callback.onSuccess()
                }
            })
    }
    */
}