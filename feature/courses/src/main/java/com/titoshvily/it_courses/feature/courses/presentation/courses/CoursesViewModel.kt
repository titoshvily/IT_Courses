package com.titoshvily.it_courses.feature.courses.presentation.courses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.titoshvily.it_courses.feature.courses.domain.usecase.GetCoursesUseCase
import com.titoshvily.it_courses.feature.courses.domain.usecase.RefreshCoursesUseCase
import com.titoshvily.it_courses.feature.courses.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CoursesViewModel(
    private val getCoursesUseCase: GetCoursesUseCase,
    private val refreshCoursesUseCase: RefreshCoursesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val _isSorted = MutableStateFlow(false)
    val isSorted: StateFlow<Boolean> = _isSorted.asStateFlow()

    val uiState: StateFlow<CoursesUiState> = combine(
        getCoursesUseCase(),
        _isSorted
    ) { result, sorted ->
        result.fold(
            onSuccess = { courses ->
                val list = if (sorted) courses.sortedByDescending { it.publishDate } else courses
                if (list.isEmpty()) CoursesUiState.Loading else CoursesUiState.Success(list)
            },
            onFailure = { CoursesUiState.Error(it.message ?: "Ошибка загрузки") }
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, CoursesUiState.Loading)

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            refreshCoursesUseCase()
        }
    }

    fun toggleSort() {
        _isSorted.value = !_isSorted.value
    }

    fun toggleFavorite(courseId: Int, isFavorite: Boolean) {
        viewModelScope.launch {
            toggleFavoriteUseCase(courseId, isFavorite)
        }
    }
}
