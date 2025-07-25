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

    @Query("Delete FROM vacancies WHERE id = :id")
    suspend fun deleteVacancy(id: String)

    @Query("Select * FROM vacancies ORDER BY additionTime DESC")
    fun getAllVacancies(): Flow<List<VacancyEntity>>

    @Query("SELECT * FROM vacancies WHERE id = :id")
    suspend fun getVacancy(id: String): VacancyEntity

    @Query("SELECT EXISTS(SELECT 1 FROM vacancies WHERE id = :id)")
    fun isFavorite(id: String): Flow<Boolean>
}
