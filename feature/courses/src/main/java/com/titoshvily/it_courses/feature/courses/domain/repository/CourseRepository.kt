package com.titoshvily.it_courses.feature.courses.domain.repository

import com.titoshvily.it_courses.feature.courses.domain.model.Course
import kotlinx.coroutines.flow.Flow

interface CourseRepository {
    fun getCourses(): Flow<Result<List<Course>>>
    fun getStartedCourses(): Flow<List<Course>>
    suspend fun refreshCourses()
    suspend fun toggleFavorite(courseId: Int, isFavorite: Boolean)
    suspend fun startCourse(courseId: Int)
}
