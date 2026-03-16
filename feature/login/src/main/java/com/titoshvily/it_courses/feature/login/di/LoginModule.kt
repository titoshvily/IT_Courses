package com.titoshvily.it_courses.feature.login.di

import com.titoshvily.it_courses.feature.login.presentation.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val loginModule = module {
    viewModel { LoginViewModel() }
}
