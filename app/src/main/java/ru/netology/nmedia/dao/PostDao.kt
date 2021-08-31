package ru.netology.nmedia.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity

@Dao
interface PostDao {
    @Query("SELECT * FROM PostEntity WHERE id = :id")
    fun getPostById(id: Long): PostEntity?

    @Query("SELECT * FROM PostEntity ORDER BY id DESC")
    fun getAll(): LiveData<List<PostEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(post: PostEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(posts: List<PostEntity>)

    @Query("UPDATE PostEntity SET content = :content WHERE id = :id")
    fun updateContentById(id: Long, content: String)

    @Insert
    fun save(post: PostEntity) =
        if (post.id == 0L) insert(post) else updateContentById(post.id, post.content)

    @Query("""
                    UPDATE PostEntity SET 
                    likes = likes + CASE WHEN likedByMe THEN -1 ELSE 1 END,
                    likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
                    WHERE id = :id
                     
           """)
    fun likeById(id: Long)

    @Query("DELETE FROM PostEntity WHERE id = :id")
    fun removeById(id: Long)

    @Query("UPDATE PostEntity SET views = views + 1 WHERE id = :id")
    fun viewById(id: Long)
}