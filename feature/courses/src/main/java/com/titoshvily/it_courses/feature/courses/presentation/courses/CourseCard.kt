package com.titoshvily.it_courses.feature.courses.presentation.courses

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.titoshvily.it_courses.feature.courses.domain.model.Course

private val cardGradients = listOf(
    listOf(Color(0xFFFF8C42), Color(0xFFFFD166)),
    listOf(Color(0xFF6B48FF), Color(0xFFB06AB3)),
    listOf(Color(0xFF11998E), Color(0xFF38EF7D)),
    listOf(Color(0xFF2193B0), Color(0xFF6DD5ED)),
    listOf(Color(0xFFEB5757), Color(0xFFF7971E))
)

@Composable
fun CourseCard(
    course: Course,
    onFavoriteClick: (Int, Boolean) -> Unit,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val gradient = cardGradients[course.id % cardGradients.size]

    Card(
        modifier = modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2C2E)),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(Brush.linearGradient(gradient))
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (course.isFavorite) Color(0xFF4CAF50) else Color(0x66000000)
                        )
                        .clickable { onFavoriteClick(course.id, !course.isFavorite) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (course.isFavorite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(10.dp),
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xCC000000)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = String.format("%.1f", course.rate),
                            fontSize = 12.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Medium
                        )
                        if (course.startDate != null) {
                            Text("·", fontSize = 12.sp, color = Color(0xFF8E8E93))
                            Text(
                                text = formatDateRu(course.startDate),
                                fontSize = 12.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = course.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = course.text,
                    fontSize = 14.sp,
                    color = Color(0xFF8E8E93),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = course.price,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Подробнее →",
                        fontSize = 14.sp,
                        color = Color(0xFF4CAF50)
                    )
                }
            }
        }
    }
}

private val ruMonths = listOf(
    "Января", "Февраля", "Марта", "Апреля", "Мая", "Июня",
    "Июля", "Августа", "Сентября", "Октября", "Ноября", "Декабря"
)

private fun formatDateRu(dateStr: String): String {
    return try {
        val parts = dateStr.substringBefore("T").split("-")
        if (parts.size == 3) {
            val month = parts[1].toIntOrNull() ?: return dateStr
            "${parts[2].trimStart('0')} ${ruMonths[month - 1]} ${parts[0]}"
        } else dateStr
    } catch (e: Exception) { dateStr }
}
