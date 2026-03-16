package com.titoshvily.it_courses.feature.courses.presentation.detail

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.titoshvily.it_courses.feature.courses.domain.model.Course
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

private val cardGradients = listOf(
    listOf(Color(0xFFFF8C42), Color(0xFFFFD166)),
    listOf(Color(0xFF6B48FF), Color(0xFFB06AB3)),
    listOf(Color(0xFF11998E), Color(0xFF38EF7D)),
    listOf(Color(0xFF2193B0), Color(0xFF6DD5ED)),
    listOf(Color(0xFFEB5757), Color(0xFFF7971E))
)

private val avatarColors = listOf(
    Color(0xFF6B48FF), Color(0xFF11998E), Color(0xFF2193B0),
    Color(0xFFEB5757), Color(0xFFFF8C42)
)

@Composable
fun CourseDetailScreen(
    courseId: Int,
    onBack: () -> Unit,
    viewModel: CourseDetailViewModel = koinViewModel { parametersOf(courseId) }
) {
    val uiState by viewModel.uiState.collectAsState()
    val isStarted by viewModel.isStarted.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1C1C1E))
    ) {
        when (val state = uiState) {
            is CourseDetailUiState.Loading -> {
                CircularProgressIndicator(
                    color = Color(0xFF4CAF50),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            is CourseDetailUiState.Error -> {
                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            is CourseDetailUiState.Success -> {
                CourseDetailContent(
                    course = state.course,
                    isStarted = isStarted,
                    onBack = onBack,
                    onFavoriteClick = viewModel::toggleFavorite,
                    onStartCourse = viewModel::startCourse
                )
            }
        }
    }
}

@Composable
private fun CourseDetailContent(
    course: Course,
    isStarted: Boolean,
    onBack: () -> Unit,
    onFavoriteClick: (Int, Boolean) -> Unit,
    onStartCourse: () -> Unit
) {
    val gradient = cardGradients[course.id % cardGradients.size]
    val avatarColor = avatarColors[course.id % avatarColors.size]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .background(Brush.linearGradient(gradient))
        ) {
            Box(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(16.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0x66000000))
                    .clickable { onBack() }
                    .align(Alignment.TopStart),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Назад",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            Box(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(16.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        if (course.isFavorite) Color(0xFF4CAF50) else Color(0x66000000)
                    )
                    .clickable { onFavoriteClick(course.id, !course.isFavorite) }
                    .align(Alignment.TopEnd),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (course.isFavorite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            Surface(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                color = Color(0xCC000000)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text = String.format("%.1f", course.rate),
                        fontSize = 13.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                    if (course.startDate != null) {
                        Text("·", fontSize = 13.sp, color = Color(0xFF8E8E93))
                        Text(
                            text = formatDateRu(course.startDate),
                            fontSize = 13.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }

        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp)) {

            Text(
                text = course.title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(avatarColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = course.title.firstOrNull()?.uppercase() ?: "A",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = "Автор", fontSize = 12.sp, color = Color(0xFF8E8E93))
                    Text(text = "Merion Academy", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            val buttonColor by animateColorAsState(
                targetValue = if (isStarted) Color(0xFF2C2C2E) else Color(0xFF4CAF50),
                animationSpec = tween(durationMillis = 400),
                label = "buttonColor"
            )
            val buttonScale by animateFloatAsState(
                targetValue = if (isStarted) 1f else 1f,
                animationSpec = spring(dampingRatio = 0.5f, stiffness = 400f),
                label = "buttonScale"
            )

            Button(
                onClick = { if (!isStarted) onStartCourse() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .scale(buttonScale),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
            ) {
                AnimatedContent(
                    targetState = isStarted,
                    transitionSpec = {
                        (fadeIn(tween(300)) + scaleIn(tween(300), initialScale = 0.8f))
                            .togetherWith(fadeOut(tween(200)))
                    },
                    label = "startButtonContent"
                ) { started ->
                    if (started) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color(0xFF4CAF50),
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Курс начат",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    } else {
                        Text(
                            text = "Начать курс",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C2C2E))
            ) {
                Text(
                    text = "Перейти на платформу",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "О курсе",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = course.text,
                fontSize = 15.sp,
                color = Color(0xFF8E8E93),
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Стоимость: ${course.price}",
                fontSize = 15.sp,
                color = Color(0xFF8E8E93)
            )

            Spacer(modifier = Modifier.height(80.dp))
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
