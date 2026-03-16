package com.titoshvily.it_courses.feature.courses.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "started_courses")
data class StartedCourseEntity(
    @PrimaryKey val courseId: Int
)
