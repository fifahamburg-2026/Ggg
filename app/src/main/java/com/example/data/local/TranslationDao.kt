package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.TranslationRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface TranslationDao {
    @Query("SELECT * FROM translation_history ORDER BY timestamp DESC")
    fun getAllHistory(): Flow<List<TranslationRecord>>

    @Query("SELECT * FROM translation_history WHERE isFavorite = 1 ORDER BY timestamp DESC")
    fun getFavorites(): Flow<List<TranslationRecord>>

    @Query("SELECT * FROM translation_history WHERE sourceText LIKE '%' || :query || '%' OR translatedText LIKE '%' || :query || '%' ORDER BY timestamp DESC")
    fun searchHistory(query: String): Flow<List<TranslationRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: TranslationRecord): Long

    @Update
    suspend fun update(record: TranslationRecord)

    @Delete
    suspend fun delete(record: TranslationRecord)

    @Query("DELETE FROM translation_history WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM translation_history WHERE isFavorite = 0")
    suspend fun clearHistoryNonFavorites()

    @Query("DELETE FROM translation_history")
    suspend fun clearAll()

    @Query("UPDATE translation_history SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun setFavorite(id: Long, isFavorite: Boolean)

    @Query("SELECT * FROM translation_history WHERE sourceText = :sourceText AND sourceLang = :sourceLang LIMIT 1")
    suspend fun findExisting(sourceText: String, sourceLang: String): TranslationRecord?
}
