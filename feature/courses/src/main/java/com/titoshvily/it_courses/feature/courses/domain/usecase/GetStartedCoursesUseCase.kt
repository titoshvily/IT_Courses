package com.titoshvily.it_courses.feature.courses.domain.usecase

import com.titoshvily.it_courses.feature.courses.domain.model.Course
import com.titoshvily.it_courses.feature.courses.domain.repository.CourseRepository
import kotlinx.coroutines.flow.Flow

class GetStartedCoursesUseCase(private val repository: CourseRepository) {
    operator fun invoke(): Flow<List<Course>> = repository.getStartedCourses()
}
