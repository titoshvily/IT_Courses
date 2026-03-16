package com.titoshvily.it_courses.feature.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.titoshvily.it_courses.feature.courses.domain.model.Course
import com.titoshvily.it_courses.feature.courses.domain.usecase.GetStartedCoursesUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class AccountViewModel(
    getStartedCoursesUseCase: GetStartedCoursesUseCase
) : ViewModel() {

    val startedCourses: StateFlow<List<Course>> = getStartedCoursesUseCase()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
}
