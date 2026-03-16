package com.titoshvily.it_courses.feature.courses.presentation.courses

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.androidx.compose.koinViewModel

private val BgColor = Color(0xFF1C1C1E)
private val FieldColor = Color(0xFF2C2C2E)
private val GreenColor = Color(0xFF4CAF50)

@Composable
fun CoursesScreen(
    onCourseClick: (Int) -> Unit = {},
    viewModel: CoursesViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val isSorted by viewModel.isSorted.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = "",
                onValueChange = {},
                readOnly = true,
                placeholder = { Text("Search courses...", color = Color(0xFF8E8E93), fontSize = 14.sp) },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF8E8E93))
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = FieldColor,
                    unfocusedContainerColor = FieldColor,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = {},
                modifier = Modifier
                    .size(52.dp)
                    .background(FieldColor, RoundedCornerShape(12.dp))
            ) {
                Icon(Icons.Default.FilterList, contentDescription = "Фильтр", tint = Color.White)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = viewModel::toggleSort) {
                Text(
                    text = if (isSorted) "По дате добавления ↓" else "По дате добавления ↑↓",
                    color = GreenColor,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        when (val state = uiState) {
            is CoursesUiState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = GreenColor)
                }
            }
            is CoursesUiState.Error -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(state.message, color = MaterialTheme.colorScheme.error)
                }
            }
            is CoursesUiState.Success -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(state.courses, key = { it.id }) { course ->
                        CourseCard(
                            course = course,
                            onFavoriteClick = viewModel::toggleFavorite,
                            onClick = { onCourseClick(course.id) }
                        )
                    }
                }
            }
        }
    }
}
