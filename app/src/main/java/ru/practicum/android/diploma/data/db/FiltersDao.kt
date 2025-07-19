package ru.practicum.android.diploma.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FiltersDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: FiltersEntity)

    @Query("SELECT * FROM filters WHERE id = 1")
    fun getFilters(): Flow<FiltersEntity>

    @Update
    suspend fun update(entity: FiltersEntity)
}
