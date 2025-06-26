package com.example.growreminder.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    val selectedDate = remember { mutableStateOf(currentDate) }
    val selectedHour = remember { mutableIntStateOf(LocalTime.now().hour) }
    val selectedMinute = remember { mutableIntStateOf(LocalTime.now().minute) }

    val task = remember { mutableStateOf(taskName) }
    val bookDescription = remember { mutableStateOf("") }

    val selectedRepeatDates = remember { mutableStateListOf<LocalDate>() }
    var showCalendarDialog by remember { mutableStateOf(false) }

    val alarmScheduler = remember { AlarmScheduler(context) }
    val scrollState = rememberScrollState()

    MainAppLayout {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 24.dp)
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        // ✅ Đồng bộ màu icon với màu chữ
                        tint = Color(0xFF1565C0)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                // ✅ Áp dụng style giống hệt trang DailyMotivationScreen
                Text(
                    text = "Thêm Lịch",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1565C0)
                )
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
                val timeFormatted = String.format("%02d:%02d", selectedHour.intValue, selectedMinute.intValue)
                Text("Giờ đã chọn:", fontWeight = FontWeight.Bold)
                Spacer(Modifier.width(8.dp))
                Text(timeFormatted)
            }

            Spacer(modifier = Modifier.weight(1f, fill = true)) // Đẩy nút xuống dưới

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                onClick = {
                    val dbRef = Firebase.database.reference
                    val timeFormatted = String.format("%02d:%02d", selectedHour.intValue, selectedMinute.intValue)

                    val datesToUse: List<LocalDate> = if (selectedRepeatDates.isNotEmpty()) {
                        selectedRepeatDates
                    } else {
                        listOf(selectedDate.value) // Nếu không lặp lại, chỉ dùng ngày đã chọn
                    }

                    datesToUse.forEach { date ->
                        val formattedDate = "${date.dayOfMonth}/${date.monthValue}/${date.year}"
                        val scheduleData = mapOf(
                            "task" to task.value,
                            "description" to bookDescription.value.trim(),
                            "date" to formattedDate,
                            "time" to timeFormatted,
                            "timestamp" to System.currentTimeMillis()
                        )
                        val id = dbRef.child("schedules").push().key ?: UUID.randomUUID().toString()
                        dbRef.child("schedules").child(id).setValue(scheduleData)
                            .addOnSuccessListener {
                                alarmScheduler.scheduleAlarm(
                                    id = id,
                                    task = scheduleData["task"].toString(),
                                    description = scheduleData["description"].toString(),
                                    date = scheduleData["date"].toString(),
                                    time = scheduleData["time"].toString()
                                )
                            }
                    }

                    Toast.makeText(context, "Đã thêm ${datesToUse.size} lịch thành công", Toast.LENGTH_SHORT).show()
                    navController.navigate("schedule_list") {
                        popUpTo("schedule") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            ) {
                Text("Thêm lịch")
            }
        }
    }

    if (showCalendarDialog) {
        MonthlyRepeatCalendarDialog(
            selectedDates = selectedRepeatDates,
            onDismiss = { showCalendarDialog = false }
        )
    }
}