package com.titoshvily.it_courses.feature.courses.di

import androidx.room.Room
import com.titoshvily.it_courses.feature.courses.data.local.db.AppDatabase
import com.titoshvily.it_courses.feature.courses.data.remote.api.CourseApiService
import com.titoshvily.it_courses.feature.courses.data.repository.CourseRepositoryImpl
import com.titoshvily.it_courses.feature.courses.domain.repository.CourseRepository
import com.titoshvily.it_courses.feature.courses.domain.usecase.GetCoursesUseCase
import com.titoshvily.it_courses.feature.courses.domain.usecase.GetStartedCoursesUseCase
import com.titoshvily.it_courses.feature.courses.domain.usecase.RefreshCoursesUseCase
import com.titoshvily.it_courses.feature.courses.domain.usecase.StartCourseUseCase
import com.titoshvily.it_courses.feature.courses.domain.usecase.ToggleFavoriteUseCase
import com.titoshvily.it_courses.feature.courses.presentation.courses.CoursesViewModel
import com.titoshvily.it_courses.feature.courses.presentation.detail.CourseDetailViewModel
import com.titoshvily.it_courses.feature.courses.presentation.favorites.FavoritesViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val coursesModule = module {

    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://drive.usercontent.google.com/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single { get<Retrofit>().create(CourseApiService::class.java) }

    single {
        Room.databaseBuilder(get(), AppDatabase::class.java, "it_courses_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    single { get<AppDatabase>().favoriteDao() }
    single { get<AppDatabase>().courseDao() }
    single { get<AppDatabase>().startedCourseDao() }

    single<CourseRepository> { CourseRepositoryImpl(get(), get(), get(), get()) }

    factory { GetCoursesUseCase(get()) }
    factory { GetStartedCoursesUseCase(get()) }
    factory { RefreshCoursesUseCase(get()) }
    factory { StartCourseUseCase(get()) }
    factory { ToggleFavoriteUseCase(get()) }

    viewModel { CoursesViewModel(get(), get(), get()) }
    viewModel { FavoritesViewModel(get(), get(), get()) }
    viewModel { (courseId: Int) -> CourseDetailViewModel(courseId, get(), get(), get(), get(), get()) }
}
