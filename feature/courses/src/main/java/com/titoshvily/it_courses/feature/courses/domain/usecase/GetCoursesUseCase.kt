package com.titoshvily.it_courses.feature.courses.domain.usecase

import com.titoshvily.it_courses.feature.courses.domain.model.Course
import com.titoshvily.it_courses.feature.courses.domain.repository.CourseRepository
import kotlinx.coroutines.flow.Flow

class GetCoursesUseCase(private val repository: CourseRepository) {
    operator fun invoke(): Flow<Result<List<Course>>> = repository.getCourses()
}
