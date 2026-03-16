package com.titoshvily.it_courses.feature.courses.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.titoshvily.it_courses.feature.courses.domain.model.Course
import com.titoshvily.it_courses.feature.courses.domain.usecase.GetCoursesUseCase
import com.titoshvily.it_courses.feature.courses.domain.usecase.GetStartedCoursesUseCase
import com.titoshvily.it_courses.feature.courses.domain.usecase.RefreshCoursesUseCase
import com.titoshvily.it_courses.feature.courses.domain.usecase.StartCourseUseCase
import com.titoshvily.it_courses.feature.courses.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class CourseDetailUiState {
    object Loading : CourseDetailUiState()
    data class Success(val course: Course) : CourseDetailUiState()
    data class Error(val message: String) : CourseDetailUiState()
}

class CourseDetailViewModel(
    private val courseId: Int,
    private val getCoursesUseCase: GetCoursesUseCase,
    private val refreshCoursesUseCase: RefreshCoursesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val startCourseUseCase: StartCourseUseCase,
    private val getStartedCoursesUseCase: GetStartedCoursesUseCase
) : ViewModel() {

    val uiState: StateFlow<CourseDetailUiState> = getCoursesUseCase()
        .map { result ->
            result.fold(
                onSuccess = { courses ->
                    val course = courses.find { it.id == courseId }
                    if (course != null) CourseDetailUiState.Success(course)
                    else CourseDetailUiState.Error("Курс не найден")
                },
                onFailure = { CourseDetailUiState.Error(it.message ?: "Ошибка загрузки") }
            )
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, CourseDetailUiState.Loading)

    val isStarted: StateFlow<Boolean> = getStartedCoursesUseCase()
        .map { started -> started.any { it.id == courseId } }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    fun toggleFavorite(courseId: Int, isFavorite: Boolean) {
        viewModelScope.launch { toggleFavoriteUseCase(courseId, isFavorite) }
    }

    fun startCourse() {
        viewModelScope.launch { startCourseUseCase(courseId) }
    }
}
