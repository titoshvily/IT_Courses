package com.titoshvily.it_courses.feature.courses.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.titoshvily.it_courses.feature.courses.data.local.dao.CourseDao
import com.titoshvily.it_courses.feature.courses.data.local.dao.FavoriteDao
import com.titoshvily.it_courses.feature.courses.data.local.dao.StartedCourseDao
import com.titoshvily.it_courses.feature.courses.data.local.entity.CourseEntity
import com.titoshvily.it_courses.feature.courses.data.local.entity.FavoriteEntity
import com.titoshvily.it_courses.feature.courses.data.local.entity.StartedCourseEntity

@Database(
    entities = [FavoriteEntity::class, CourseEntity::class, StartedCourseEntity::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
    abstract fun courseDao(): CourseDao
    abstract fun startedCourseDao(): StartedCourseDao
}
