package com.example.growreminder.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.growreminder.ui.alarm.AlarmScheduler
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

@Composable
fun ScheduleScreen(
    navController: NavController,
    taskName: String = "Đọc sách"
) {
    val context = LocalContext.current
    val currentDate = remember { LocalDate.now() }
    val today = remember { LocalDate.now() }

    val selectedDate = remember { mutableStateOf(currentDate) }
    val selectedHour = remember { mutableIntStateOf(LocalTime.now().hour) }
    val selectedMinute = remember { mutableIntStateOf(LocalTime.now().minute) }

    val task = remember { mutableStateOf(taskName) }
    val bookDescription = remember { mutableStateOf("") }

    val lazyListState = rememberLazyListState()
    val days = remember { List(365 * 5) { offset -> currentDate.plusDays(offset.toLong()) } }
    val alarmScheduler = remember { AlarmScheduler(context) }

    LaunchedEffect(selectedDate.value) {
        val index = days.indexOfFirst { it == selectedDate.value }
        if (index >= 0) {
            lazyListState.animateScrollToItem(index)
        }
    }

    val dateFormatted = "${selectedDate.value.dayOfMonth}/${selectedDate.value.monthValue}/${selectedDate.value.year}"
    val timeFormatted = String.format("%02d:%02d", selectedHour.intValue, selectedMinute.intValue)

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text("Ngày đã chọn: $dateFormatted",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Top Bar
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text("Thêm Lịch", style = MaterialTheme.typography.titleLarge)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Horizontal Date Picker
        LazyRow(state = lazyListState) {
            items(days) { date ->
                val isSelected = selectedDate.value == date
                val isPast = date.isBefore(today)

                val bgColor = when {
                    isSelected -> MaterialTheme.colorScheme.primary
                    isPast -> Color.Gray.copy(0.1f)
                    else -> Color.LightGray.copy(0.2f)
                }

                val textColor = when {
                    isSelected -> Color.White
                    isPast -> Color.Gray
                    else -> Color.Black
                }

                Column(
                    modifier = Modifier
                        .padding(horizontal = 6.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(bgColor)
                        .clickable(enabled = !isPast) { selectedDate.value = date }
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("${date.dayOfMonth}", color = textColor)
                    Text(date.dayOfWeek.name.take(3), color = textColor)
                    Text("${date.monthValue}/${date.year}", color = textColor)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            val calendar = Calendar.getInstance()
            android.app.DatePickerDialog(
                context,
                { _, y, m, d ->
                    val picked = LocalDate.of(y, m + 1, d)
                    if (!picked.isBefore(today)) selectedDate.value = picked
                },
                selectedDate.value.year,
                selectedDate.value.monthValue - 1,
                selectedDate.value.dayOfMonth
            ).show()
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Chọn ngày cụ thể")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text("Chọn giờ ${task.value}", style = MaterialTheme.typography.titleMedium)

        SpinnerTimePicker(
            selectedDate = selectedDate.value,
            selectedHour = selectedHour.intValue,
            selectedMinute = selectedMinute.intValue,
            onHourChanged = { selectedHour.intValue = it },
            onMinuteChanged = { selectedMinute.intValue = it },
            currentTime = LocalTime.now()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row {
            Text("Việc cần làm:", fontWeight = FontWeight.Bold)
            Spacer(Modifier.width(8.dp))
            Text(task.value)
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = bookDescription.value,
            onValueChange = { bookDescription.value = it },
            label = { Text("Mô tả nội dung") },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Nhập chi tiết về ${task.value}...") },
            maxLines = 3,
            singleLine = false,
            textStyle = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row {
            Text("Ngày và giờ đã chọn:", fontWeight = FontWeight.Bold)
            Spacer(Modifier.width(8.dp))
            Text("$dateFormatted - $timeFormatted")
        }

        Spacer(modifier = Modifier.height(130.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            onClick = {
                val dbRef = Firebase.database.reference
                val scheduleId = dbRef.child("schedules").push().key ?: UUID.randomUUID().toString()

                val newSchedule = mapOf(
                    "task" to task.value,
                    "description" to bookDescription.value.trim(),
                    "date" to dateFormatted,
                    "time" to timeFormatted,
                    "timestamp" to System.currentTimeMillis()
                )

                dbRef.child("schedules").child(scheduleId).setValue(newSchedule)
                    .addOnSuccessListener {
                        alarmScheduler.scheduleAlarm(
                            id = scheduleId,
                            task = task.value,
                            description = bookDescription.value.trim(),
                            date = dateFormatted,
                            time = timeFormatted
                        )

                        Toast.makeText(context, "Thêm lịch và đặt nhắc nhở thành công", Toast.LENGTH_SHORT).show()
                        navController.navigate("schedule_list") {
                            popUpTo("schedule") { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Thêm lịch thất bại", Toast.LENGTH_SHORT).show()
                    }
            }
        ) {
            Text("Thêm lịch")
        }
    }
}

@Composable
fun CustomBottomBar(
    modifier: Modifier = Modifier,
    onFabClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .align(Alignment.BottomCenter),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = Color(0xFFF3EDFF),
            tonalElevation = 4.dp,
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val iconTint = Color(0xFF7E57C2)

                Icon(Icons.Filled.Home, contentDescription = "Home", tint = iconTint)
                Icon(Icons.Filled.Event, contentDescription = "Calendar", tint = iconTint)
                Spacer(modifier = Modifier.width(56.dp))
                Icon(Icons.Filled.Description, contentDescription = "Docs", tint = iconTint)
                Icon(Icons.Filled.Group, contentDescription = "People", tint = iconTint)
            }
        }

        FloatingActionButton(
            onClick = onFabClick,
            containerColor = Color(0xFF7E57C2),
            contentColor = Color.White,
            modifier = Modifier
                .size(64.dp)
                .align(Alignment.TopCenter),
            shape = CircleShape,
            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Add")
        }
    }
}
