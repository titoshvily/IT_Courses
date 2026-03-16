package com.titoshvily.it_courses.feature.courses.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CourseDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("text") val text: String,
    @SerializedName("price") val price: String,
    @SerializedName("rate") val rate: Double,
    @SerializedName("startDate") val startDate: String?,
    @SerializedName("hasLike") val hasLike: Boolean,
    @SerializedName("publishDate") val publishDate: String
)
