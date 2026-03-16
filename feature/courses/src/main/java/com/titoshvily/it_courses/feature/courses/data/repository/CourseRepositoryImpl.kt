package com.titoshvily.it_courses.feature.courses.data.repository

import com.titoshvily.it_courses.feature.courses.data.local.dao.CourseDao
import com.titoshvily.it_courses.feature.courses.data.local.dao.FavoriteDao
import com.titoshvily.it_courses.feature.courses.data.local.dao.StartedCourseDao
import com.titoshvily.it_courses.feature.courses.data.local.entity.CourseEntity
import com.titoshvily.it_courses.feature.courses.data.local.entity.FavoriteEntity
import com.titoshvily.it_courses.feature.courses.data.local.entity.StartedCourseEntity
import com.titoshvily.it_courses.feature.courses.data.remote.api.CourseApiService
import com.titoshvily.it_courses.feature.courses.domain.model.Course
import com.titoshvily.it_courses.feature.courses.domain.repository.CourseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class CourseRepositoryImpl(
    private val apiService: CourseApiService,
    private val favoriteDao: FavoriteDao,
    private val courseDao: CourseDao,
    private val startedCourseDao: StartedCourseDao
) : CourseRepository {

    override fun getCourses(): Flow<Result<List<Course>>> =
        combine(
            courseDao.getAllCourses(),
            favoriteDao.getAllFavorites()
        ) { cachedCourses, favorites ->
            val favoriteIds = favorites.map { it.courseId }.toSet()
            Result.success(cachedCourses.map { it.toDomain(favoriteIds) })
        }

    override fun getStartedCourses(): Flow<List<Course>> =
        combine(
            courseDao.getAllCourses(),
            favoriteDao.getAllFavorites(),
            startedCourseDao.getAllStarted()
        ) { cachedCourses, favorites, started ->
            val favoriteIds = favorites.map { it.courseId }.toSet()
            val startedIds = started.map { it.courseId }.toSet()
            cachedCourses
                .filter { it.id in startedIds }
                .map { it.toDomain(favoriteIds) }
        }

    override suspend fun refreshCourses() {
        try {
            val courseDtos = apiService.getCourses().courses
            val cachedIds = courseDao.getAllIds().toSet()
            val newCourseIds = courseDtos.filter { it.id !in cachedIds }.map { it.id }.toSet()

            courseDao.upsertAll(courseDtos.map { dto ->
                CourseEntity(
                    id = dto.id,
                    title = dto.title,
                    text = dto.text,
                    price = dto.price,
                    rate = dto.rate,
                    startDate = dto.startDate,
                    hasLike = dto.hasLike,
                    publishDate = dto.publishDate
                )
            })

            val favoritesToSeed = courseDtos
                .filter { it.id in newCourseIds && it.hasLike }
                .map { FavoriteEntity(it.id) }
            if (favoritesToSeed.isNotEmpty()) {
                favoriteDao.insertAll(favoritesToSeed)
            }
        } catch (_: Exception) {
        }
    }

    override suspend fun toggleFavorite(courseId: Int, isFavorite: Boolean) {
        if (isFavorite) {
            favoriteDao.insert(FavoriteEntity(courseId))
        } else {
            favoriteDao.delete(courseId)
            startedCourseDao.delete(courseId)
        }
    }

    override suspend fun startCourse(courseId: Int) {
        startedCourseDao.insert(StartedCourseEntity(courseId))
    }

    private fun CourseEntity.toDomain(favoriteIds: Set<Int>) = Course(
        id = id,
        title = title,
        text = text,
        price = price,
        rate = rate,
        startDate = startDate,
        hasLike = hasLike,
        publishDate = publishDate,
        isFavorite = id in favoriteIds
    )
}
