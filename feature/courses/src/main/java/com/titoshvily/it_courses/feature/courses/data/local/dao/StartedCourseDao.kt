package com.titoshvily.it_courses.feature.courses.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.titoshvily.it_courses.feature.courses.data.local.entity.StartedCourseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StartedCourseDao {
    @Query("SELECT * FROM started_courses")
    fun getAllStarted(): Flow<List<StartedCourseEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: StartedCourseEntity)

    @Query("DELETE FROM started_courses WHERE courseId = :courseId")
    suspend fun delete(courseId: Int)
}
