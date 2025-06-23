package com.example.growreminder.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    taskName: String = "Lịch dự kiến"
) {
    val context = LocalContext.current
    val currentDate = remember { LocalDate.now() }
    val today = remember { LocalDate.now() }

    val selectedDate = remember { mutableStateOf(currentDate) }
    val selectedHour = remember { mutableIntStateOf(LocalTime.now().hour) }
    val selectedMinute = remember { mutableIntStateOf(LocalTime.now().minute) }

    val task = remember { mutableStateOf(taskName) }
    val bookDescription = remember { mutableStateOf("") }

    val selectedRepeatDates = remember { mutableStateListOf<LocalDate>() }
    var showCalendarDialog by remember { mutableStateOf(false) }

    val days = remember { List(365 * 5) { offset -> currentDate.plusDays(offset.toLong()) } }

    val alarmScheduler = remember { AlarmScheduler(context) }

    val dateFormatted = "${selectedDate.value.dayOfMonth}/${selectedDate.value.monthValue}/${selectedDate.value.year}"
    val timeFormatted = String.format("%02d:%02d", selectedHour.intValue, selectedMinute.intValue)

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 24.dp)
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text("Thêm Lịch", style = MaterialTheme.typography.titleLarge)
        }

        Spacer(modifier = Modifier.height(24.dp))

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

        Text("Lặp lại theo ngày trong tháng", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { showCalendarDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Chọn ngày lặp trong tháng")
        }

        Spacer(modifier = Modifier.height(4.dp))

        if (selectedRepeatDates.isNotEmpty()) {
            Text("Đã chọn: ${selectedRepeatDates.sorted().joinToString { "${it.dayOfMonth}/${it.monthValue}" }}")
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
            Text("Giờ đã chọn:", fontWeight = FontWeight.Bold)
            Spacer(Modifier.width(8.dp))
            Text("$timeFormatted")
        }

        Spacer(modifier = Modifier.height(130.dp))

        Button(
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp),
            onClick = {
                val dbRef = Firebase.database.reference
                val newSchedule = mutableListOf<Map<String, Any>>()

                val datesToUse = if (selectedRepeatDates.isNotEmpty()) {
                    selectedRepeatDates
                } else listOf(selectedDate.value)

                datesToUse.forEach { date ->
                    val formattedDate = "${date.dayOfMonth}/${date.monthValue}/${date.year}"
                    val data = mapOf(
                        "task" to task.value,
                        "description" to bookDescription.value.trim(),
                        "date" to formattedDate,
                        "time" to timeFormatted,
                        "timestamp" to System.currentTimeMillis()
                    )
                    newSchedule.add(data)
                }

                newSchedule.forEach { data ->
                    val id = dbRef.child("schedules").push().key ?: UUID.randomUUID().toString()
                    dbRef.child("schedules").child(id).setValue(data)
                        .addOnSuccessListener {
                            alarmScheduler.scheduleAlarm(
                                id = id,
                                task = data["task"].toString(),
                                description = data["description"].toString(),
                                date = data["date"].toString(),
                                time = data["time"].toString()
                            )
                        }
                }

                Toast.makeText(context, "Đã thêm ${newSchedule.size} lịch lặp", Toast.LENGTH_SHORT).show()
                navController.navigate("schedule_list") {
                    popUpTo("schedule") { inclusive = true }
                    launchSingleTop = true
                }
            }
        ) {
            Text("Thêm lịch")
        }
    }

    if (showCalendarDialog) {
        MonthlyRepeatCalendarDialog(
            selectedDates = selectedRepeatDates,
            onDismiss = { showCalendarDialog = false }
        )
    }
}
