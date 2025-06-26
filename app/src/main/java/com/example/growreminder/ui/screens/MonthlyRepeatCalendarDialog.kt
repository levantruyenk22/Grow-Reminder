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
import androidx.compose.ui.unit.sp // ✅ THÊM DÒNG IMPORT CÒN THIẾU
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

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
                    // Hiển thị tên tháng và năm
                    text = "${currentMonth.month.getDisplayName(TextStyle.FULL_STANDALONE, Locale("vi"))
                        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale("vi")) else it.toString() }} ${currentMonth.year}",
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    listOf("T2", "T3", "T4", "T5", "T6", "T7", "CN").forEach {
                        Text(
                            text = it,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                val daysInMonth = currentMonth.lengthOfMonth()
                val firstDayOfWeek = currentMonth.dayOfWeek.value
                val offset = firstDayOfWeek - 1 // MONDAY là 0, SUNDAY là 6

                val totalGridCells = ((daysInMonth + offset + 6) / 7) * 7

                val weeks = (0 until totalGridCells).chunked(7)

                weeks.forEach { week ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        week.forEach { dayIndex ->
                            val dayOfMonth = dayIndex - offset + 1
                            if (dayOfMonth in 1..daysInMonth) {
                                val date = currentMonth.withDayOfMonth(dayOfMonth)
                                val isSelected = selectedDates.contains(date)
                                val bgColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
                                val textColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(bgColor)
                                        .clickable {
                                            if (isSelected) selectedDates.remove(date)
                                            else selectedDates.add(date)
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "$dayOfMonth", color = textColor, fontSize = 14.sp)
                                }
                            } else {
                                Spacer(modifier = Modifier.weight(1f).aspectRatio(1f))
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
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