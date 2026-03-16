package com.titoshvily.it_courses.feature.courses.presentation.courses

import com.titoshvily.it_courses.feature.courses.domain.model.Course

sealed class CoursesUiState {
    object Loading : CoursesUiState()
    data class Success(val courses: List<Course>) : CoursesUiState()
    data class Error(val message: String) : CoursesUiState()
}
