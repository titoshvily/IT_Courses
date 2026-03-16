package com.titoshvily.it_courses.feature.courses.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.titoshvily.it_courses.feature.courses.domain.model.Course
import com.titoshvily.it_courses.feature.courses.domain.usecase.GetCoursesUseCase
import com.titoshvily.it_courses.feature.courses.domain.usecase.RefreshCoursesUseCase
import com.titoshvily.it_courses.feature.courses.domain.usecase.ToggleFavoriteUseCase
import com.titoshvily.it_courses.feature.courses.presentation.courses.CoursesUiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val getCoursesUseCase: GetCoursesUseCase,
    private val refreshCoursesUseCase: RefreshCoursesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    val uiState: StateFlow<CoursesUiState> = getCoursesUseCase()
        .map { result ->
            result.fold(
                onSuccess = { CoursesUiState.Success(it.filter(Course::isFavorite)) },
                onFailure = { CoursesUiState.Error(it.message ?: "Ошибка загрузки") }
            )
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, CoursesUiState.Loading)

    init {
        viewModelScope.launch { refreshCoursesUseCase() }
    }

    fun toggleFavorite(courseId: Int, isFavorite: Boolean) {
        viewModelScope.launch {
            toggleFavoriteUseCase(courseId, isFavorite)
        }
    }
}
