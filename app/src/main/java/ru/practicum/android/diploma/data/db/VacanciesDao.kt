package ru.practicum.android.diploma.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface VacanciesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVacancy(vacancy: VacancyEntity)

    @Query("Delete FROM vacancies_table WHERE id = :id")
    suspend fun deleteVacancy(id: Long)

    @Query("Select * FROM vacancies_table ORDER BY additionTime DESC")
    fun getAllVacancies(): Flow<List<VacancyEntity>>

    @Query("SELECT * FROM vacancies_table WHERE id = :id")
    fun getOneVacancy(id: Long): Flow<VacancyEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM vacancies_table WHERE id = :id)")
    suspend fun isFavorite(id: Long): Boolean
}
