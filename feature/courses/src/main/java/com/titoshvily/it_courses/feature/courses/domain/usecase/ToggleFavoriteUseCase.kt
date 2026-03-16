package com.titoshvily.it_courses.feature.courses.domain.usecase

import com.titoshvily.it_courses.feature.courses.domain.repository.CourseRepository

class ToggleFavoriteUseCase(private val repository: CourseRepository) {
    suspend operator fun invoke(courseId: Int, isFavorite: Boolean) =
        repository.toggleFavorite(courseId, isFavorite)
}
