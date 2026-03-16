package com.titoshvily.it_courses.feature.courses.domain.usecase

import com.titoshvily.it_courses.feature.courses.domain.repository.CourseRepository

class RefreshCoursesUseCase(private val repository: CourseRepository) {
    suspend operator fun invoke() = repository.refreshCourses()
}
