package com.example.growreminder.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun SpinnerTimePicker(
    selectedDate: LocalDate,
    selectedHour: Int,
    selectedMinute: Int,
    onHourChanged: (Int) -> Unit,
    onMinuteChanged: (Int) -> Unit,
    currentTime: LocalTime = LocalTime.now()
) {
    val hours = (0..23).toList()
    val minutes = (0..59).toList()

    val repeatedHours = List(1000) { hours[it % 24] }
    val repeatedMinutes = List(1000) { minutes[it % 60] }

    val hourStartIndex = 500
    val minuteStartIndex = 500

    val hourState = rememberLazyListState(hourStartIndex + selectedHour)
    val minuteState = rememberLazyListState(minuteStartIndex + selectedMinute)

    val coroutineScope = rememberCoroutineScope()

    Row(
        modifier = Modifier
            .height(150.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        LazyColumn(
            state = hourState,
            modifier = Modifier.width(80.dp)
        ) {
            items(repeatedHours.size) { index ->
                val hour = repeatedHours[index]
                val isSelected = hour == selectedHour
                val isPastHour = false // ✅ Cho phép tất cả giờ

                Text(
                    text = hour.toString().padStart(2, '0'),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                            onHourChanged(hour)
                            coroutineScope.launch {
                                hourState.animateScrollToItem(index)
                            }
                        },
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = if (isSelected) MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    else MaterialTheme.typography.bodyLarge
                )
            }
        }

        Text(":", modifier = Modifier
            .padding(horizontal = 12.dp)
            .align(Alignment.CenterVertically),
            style = MaterialTheme.typography.headlineMedium
        )

        LazyColumn(
            state = minuteState,
            modifier = Modifier.width(80.dp)
        ) {
            items(repeatedMinutes.size) { index ->
                val minute = repeatedMinutes[index]
                val isSelected = minute == selectedMinute
                val disabled = false // ✅ Cho phép tất cả phút

                Text(
                    text = minute.toString().padStart(2, '0'),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                            onMinuteChanged(minute)
                            coroutineScope.launch {
                                minuteState.animateScrollToItem(index)
                            }
                        },
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = if (isSelected) MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    else MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
