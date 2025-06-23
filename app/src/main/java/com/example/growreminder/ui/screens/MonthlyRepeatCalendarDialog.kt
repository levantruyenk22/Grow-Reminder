package com.example.growreminder.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.time.LocalDate

@Composable
fun MonthlyRepeatCalendarDialog(
    selectedDates: SnapshotStateList<LocalDate>,
    onDismiss: () -> Unit
) {
    var currentMonth by remember { mutableStateOf(LocalDate.now().withDayOfMonth(1)) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { currentMonth = currentMonth.minusMonths(1) }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Tháng trước")
                }
                Text(
                    text = "${currentMonth.month.name.lowercase().replaceFirstChar { it.uppercase() }} ${currentMonth.year}",
                    style = MaterialTheme.typography.titleMedium
                )
                IconButton(onClick = { currentMonth = currentMonth.plusMonths(1) }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Tháng sau")
                }
            }
        },
        text = {
            Column {
                // Header các thứ trong tuần
                Row(modifier = Modifier.fillMaxWidth()) {
                    listOf("T2", "T3", "T4", "T5", "T6", "T7", "CN").forEach {
                        Text(
                            text = it,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                val startDayOfWeek = (currentMonth.dayOfWeek.value + 6) % 7 // CN = 0
                val daysInMonth = currentMonth.lengthOfMonth()
                val totalBoxes = startDayOfWeek + daysInMonth
                val weeks = (0 until totalBoxes).chunked(7)

                weeks.forEach { week ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        week.forEach { index ->
                            val dayOffset = index - startDayOfWeek + 1
                            if (dayOffset in 1..daysInMonth) {
                                val date = currentMonth.withDayOfMonth(dayOffset)
                                val isSelected = selectedDates.contains(date)
                                val bgColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
                                val textColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f)
                                        .padding(4.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(bgColor)
                                        .clickable {
                                            if (isSelected) selectedDates.remove(date)
                                            else selectedDates.add(date)
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "$dayOffset", color = textColor)
                                }
                            } else {
                                Spacer(modifier = Modifier.weight(1f).aspectRatio(1f))
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Xong")
            }
        }
    )
}
