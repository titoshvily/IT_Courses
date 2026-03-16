package com.titoshvily.it_courses.feature.courses.domain.model

data class Course(
    val id: Int,
    val title: String,
    val text: String,
    val price: String,
    val rate: Double,
    val startDate: String?,
    val hasLike: Boolean,
    val publishDate: String,
    val isFavorite: Boolean
)
