package com.titoshvily.it_courses.feature.account

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val accountModule = module {
    viewModel { AccountViewModel(get()) }
}
