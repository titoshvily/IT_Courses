package com.titoshvily.it_courses.feature.courses.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "courses")
data class CourseEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val text: String,
    val price: String,
    val rate: Double,
    val startDate: String?,
    val hasLike: Boolean,
    val publishDate: String
)
