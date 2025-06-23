package com.example.growreminder.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.navigation.NavController
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.ktx.database
import com.example.growreminder.ui.alarm.AlarmScheduler
import java.text.SimpleDateFormat
import java.util.*

data class ScheduleItem(
    val id: String = "",
    val task: String = "",
    val description: String = "",
    val date: String = "",
    val time: String = "",
    val timestamp: Long = 0L,
    val isCompleted: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleListScreen(navController: NavController) {
    val context = LocalContext.current
    val scheduleList = remember { mutableStateListOf<ScheduleItem>() }
    val alarmScheduler = remember { AlarmScheduler(context) }

    val infiniteTransition = rememberInfiniteTransition(label = "background")
    val backgroundColors = listOf(
        infiniteTransition.animateColor(
            initialValue = Color(0xFFE3F2FD),
            targetValue = Color(0xFFBBDEFB),
            animationSpec = infiniteRepeatable(
                animation = tween(6000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "bgColor1"
        ),
        infiniteTransition.animateColor(
            initialValue = Color(0xFFBBDEFB),
            targetValue = Color(0xFF90CAF9),
            animationSpec = infiniteRepeatable(
                animation = tween(8000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "bgColor2"
        ),
        infiniteTransition.animateColor(
            initialValue = Color(0xFF90CAF9),
            targetValue = Color(0xFFE1F5FE),
            animationSpec = infiniteRepeatable(
                animation = tween(7000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "bgColor3"
        )
    )

    val backgroundGradient = Brush.verticalGradient(
        colors = backgroundColors.map { it.value }
    )

    val textColor = Color(0xFF1565C0)

    // Dialog states
    val showEditDialog = remember { mutableStateOf(false) }
    val selectedItem = remember { mutableStateOf<ScheduleItem?>(null) }
    val editedTask = remember { mutableStateOf("") }
    val editedDescription = remember { mutableStateOf("") }
    val editedDate = remember { mutableStateOf("") }
    val editedTime = remember { mutableStateOf("") }

    val showDatePicker = remember { mutableStateOf(false) }
    val showTimePicker = remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState(
        initialHour = 0,
        initialMinute = 0,
        is24Hour = true
    )

    var isFirstLoad by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val dbRef = Firebase.database.reference.child("schedules")
        dbRef.get().addOnSuccessListener { snapshot ->
            val tempList = mutableListOf<ScheduleItem>()
            for (child in snapshot.children) {
                val item = child.getValue(ScheduleItem::class.java)
                val id = child.key ?: continue
                if (item != null) {
                    tempList.add(item.copy(id = id))
                }
            }

            val sortedList = tempList.sortedByDescending { it.timestamp }
            scheduleList.clear()
            scheduleList.addAll(sortedList)
            isFirstLoad = false
        }
    }

    // DatePicker Dialog
    if (showDatePicker.value) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker.value = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val selectedCalendar = Calendar.getInstance().apply {
                                timeInMillis = millis
                            }
                            val todayCalendar = Calendar.getInstance()

                            selectedCalendar.set(Calendar.HOUR_OF_DAY, 0)
                            selectedCalendar.set(Calendar.MINUTE, 0)
                            selectedCalendar.set(Calendar.SECOND, 0)
                            selectedCalendar.set(Calendar.MILLISECOND, 0)

                            todayCalendar.set(Calendar.HOUR_OF_DAY, 0)
                            todayCalendar.set(Calendar.MINUTE, 0)
                            todayCalendar.set(Calendar.SECOND, 0)
                            todayCalendar.set(Calendar.MILLISECOND, 0)

                            if (selectedCalendar.timeInMillis >= todayCalendar.timeInMillis) {
                                val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                                editedDate.value = formatter.format(Date(millis))
                                showDatePicker.value = false
                            } else {
                                Toast.makeText(context, "❌ Không thể chọn ngày trong quá khứ!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                ) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker.value = false }) {
                    Text("Hủy")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // TimePicker Dialog
    if (showTimePicker.value) {
        AlertDialog(
            onDismissRequest = { showTimePicker.value = false },
            title = { Text("Chọn giờ và phút", fontWeight = FontWeight.Bold) },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Thời gian: ${String.format("%02d:%02d", timePickerState.hour, timePickerState.minute)}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    TimePicker(
                        state = timePickerState,
                        modifier = Modifier.padding(16.dp)
                    )
                    Text(
                        "💡 Bạn có thể chỉnh sửa từng phút bằng cách quay số hoặc nhập trực tiếp",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val selectedTime = String.format(
                            "%02d:%02d",
                            timePickerState.hour,
                            timePickerState.minute
                        )

                        if (editedDate.value.isNotEmpty()) {
                            try {
                                val selectedDateTimeString = "${editedDate.value} $selectedTime"
                                val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                                val selectedDateTime = formatter.parse(selectedDateTimeString)

                                if (selectedDateTime != null && selectedDateTime.before(Date())) {
                                    Toast.makeText(context, "⏰ Không thể chọn thời gian trong quá khứ!", Toast.LENGTH_LONG).show()
                                    return@Button
                                }
                            } catch (e: Exception) {
                                // Continue if parsing fails
                            }
                        }

                        editedTime.value = selectedTime
                        showTimePicker.value = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Xác nhận", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker.value = false }) {
                    Text("Hủy")
                }
            }
        )
    }

    // Edit Dialog
    if (showEditDialog.value && selectedItem.value != null) {
        AlertDialog(
            onDismissRequest = { showEditDialog.value = false },
            title = { Text("Chỉnh sửa lịch") },
            text = {
                Column {
                    Text("Thông tin ban đầu:",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp))
                    Text("Thời gian: ${selectedItem.value?.time}; ${selectedItem.value?.date}",
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 16.dp))

                    OutlinedTextField(
                        value = editedTask.value,
                        onValueChange = { editedTask.value = it },
                        label = { Text("Việc cần làm") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    )
                    OutlinedTextField(
                        value = editedDescription.value,
                        onValueChange = { editedDescription.value = it },
                        label = { Text("Mô tả") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    )

                    OutlinedTextField(
                        value = editedDate.value,
                        onValueChange = { },
                        label = { Text("Ngày") },
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { showDatePicker.value = true }) {
                                Icon(Icons.Default.DateRange, contentDescription = "Chọn ngày")
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { showDatePicker.value = true }
                    )

                    OutlinedTextField(
                        value = editedTime.value,
                        onValueChange = { },
                        label = { Text("Giờ") },
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { showTimePicker.value = true }) {
                                Icon(Icons.Default.Schedule, contentDescription = "Chọn giờ")
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { showTimePicker.value = true }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val selectedDateTime = try {
                            val dateTimeString = "${editedDate.value} ${editedTime.value}"
                            val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                            formatter.parse(dateTimeString)
                        } catch (e: Exception) {
                            null
                        }

                        val currentTime = Date()

                        if (selectedDateTime != null && selectedDateTime.before(currentTime)) {
                            Toast.makeText(context, "⏰ Không thể đặt lịch trong quá khứ!", Toast.LENGTH_LONG).show()
                            return@Button
                        }

                        if (editedTask.value.isBlank()) {
                            Toast.makeText(context, "📝 Vui lòng nhập việc cần làm!", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        if (editedDate.value.isBlank() || editedTime.value.isBlank()) {
                            Toast.makeText(context, "📅 Vui lòng chọn đầy đủ ngày và giờ!", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        selectedItem.value?.let { originalItem ->
                            val itemIndex = scheduleList.indexOf(originalItem)
                            if (itemIndex == -1) return@Button

                            val newTimestamp = try {
                                val dateTimeString = "${editedDate.value} ${editedTime.value}"
                                val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                                formatter.parse(dateTimeString)?.time ?: System.currentTimeMillis()
                            } catch (e: Exception) {
                                System.currentTimeMillis()
                            }

                            val updatedItem = originalItem.copy(
                                task = editedTask.value,
                                description = editedDescription.value,
                                date = editedDate.value,
                                time = editedTime.value,
                                timestamp = newTimestamp
                            )

                            // *** OPTIMISTIC UI UPDATE ***
                            scheduleList[itemIndex] = updatedItem
                            showEditDialog.value = false

                            val updates = mapOf(
                                "task" to updatedItem.task,
                                "description" to updatedItem.description,
                                "date" to updatedItem.date,
                                "time" to updatedItem.time,
                                "timestamp" to updatedItem.timestamp
                            )

                            Firebase.database.reference
                                .child("schedules")
                                .child(originalItem.id)
                                .updateChildren(updates)
                                .addOnSuccessListener {
                                    alarmScheduler.cancelAlarm(originalItem.id)
                                    alarmScheduler.scheduleAlarm(
                                        id = updatedItem.id,
                                        task = updatedItem.task,
                                        description = updatedItem.description,
                                        date = updatedItem.date,
                                        time = updatedItem.time
                                    )
                                    Toast.makeText(context, "✅ Đã cập nhật lịch và nhắc nhở", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener {
                                    // *** REVERT ON FAILURE ***
                                    scheduleList[itemIndex] = originalItem
                                    Toast.makeText(context, "❌ Cập nhật thất bại, đã hoàn tác.", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }
                ) {
                    Text("Lưu thay đổi")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog.value = false }) {
                    Text("Hủy")
                }
            }
        )
    }

    // Main UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.Start
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp)
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF64B5F6),
                                Color(0xFF42A5F5)
                            )
                        ),
                        shape = RoundedCornerShape(50)
                    )
                    .shadow(8.dp, RoundedCornerShape(50))
            ) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            Text(
                text = "Lịch cần làm",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                modifier = Modifier.padding(start = 8.dp))
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Schedule List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(scheduleList, key = { it.id }) { item ->
                val currentItem by rememberUpdatedState(item)

                OptimizedScheduleCard(
                    item = currentItem,
                    textColor = textColor,
                    onEditClick = {
                        selectedItem.value = currentItem
                        editedTask.value = currentItem.task
                        editedDescription.value = currentItem.description
                        editedDate.value = currentItem.date
                        editedTime.value = currentItem.time

                        try {
                            val timeParts = currentItem.time.split(":")
                            if (timeParts.size == 2) {
                                val hour = timeParts[0].toIntOrNull() ?: 0
                                val minute = timeParts[1].toIntOrNull() ?: 0
                                timePickerState.hour = hour
                                timePickerState.minute = minute
                            }
                        } catch (e: Exception) {
                            // Keep default values
                        }

                        showEditDialog.value = true
                    },
                    onDeleteClick = {
                        val itemToRemove = currentItem
                        val itemIndex = scheduleList.indexOf(itemToRemove)

                        // *** OPTIMISTIC UI UPDATE ***
                        if (itemIndex != -1) {
                            scheduleList.removeAt(itemIndex)
                        }

                        Firebase.database.reference
                            .child("schedules")
                            .child(itemToRemove.id)
                            .removeValue()
                            .addOnSuccessListener {
                                alarmScheduler.cancelAlarm(itemToRemove.id)
                                Toast.makeText(context, "Đã xóa lịch và hủy nhắc nhở", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener {
                                // *** REVERT ON FAILURE ***
                                if (itemIndex != -1) {
                                    scheduleList.add(itemIndex, itemToRemove)
                                }
                                Toast.makeText(context, "Xóa thất bại, đã hoàn tác.", Toast.LENGTH_SHORT).show()
                            }
                    },
                    onToggleComplete = {
                        val originalItem = currentItem
                        val itemIndex = scheduleList.indexOfFirst { it.id == originalItem.id }
                        if (itemIndex < 0) return@OptimizedScheduleCard

                        val updatedItem = originalItem.copy(isCompleted = !originalItem.isCompleted)

                        // *** OPTIMISTIC UI UPDATE ***
                        scheduleList[itemIndex] = updatedItem

                        if (updatedItem.isCompleted) {
                            alarmScheduler.cancelAlarm(updatedItem.id)
                            Toast.makeText(context, "✅ Đã hoàn thành: ${updatedItem.task}", Toast.LENGTH_SHORT).show()
                        } else {
                            alarmScheduler.scheduleAlarm(
                                id = updatedItem.id,
                                task = updatedItem.task,
                                description = updatedItem.description,
                                date = updatedItem.date,
                                time = updatedItem.time
                            )
                            Toast.makeText(context, "🔄 Đã khôi phục: ${updatedItem.task}", Toast.LENGTH_SHORT).show()
                        }

                        // Cập nhật Firebase trong nền
                        Firebase.database.reference
                            .child("schedules")
                            .child(originalItem.id)
                            .child("isCompleted")
                            .setValue(updatedItem.isCompleted)
                            .addOnFailureListener {
                                // *** REVERT ON FAILURE ***
                                scheduleList[itemIndex] = originalItem
                                Toast.makeText(context, "❌ Cập nhật thất bại, đã hoàn tác.", Toast.LENGTH_SHORT).show()
                            }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun OptimizedScheduleCard(
    item: ScheduleItem,
    textColor: Color,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onToggleComplete: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "card_${item.id}")
    val cardShimmer by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "cardShimmer_${item.id}"
    )

    val cardGradient = if (item.isCompleted) {
        Brush.horizontalGradient(
            colors = listOf(
                Color(0xFFE8F5E8).copy(alpha = 0.95f),
                Color(0xFFF1F8E9),
                Color(0xFFE8F5E8).copy(alpha = 0.98f)
            )
        )
    } else {
        Brush.horizontalGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.95f),
                Color(0xFFF8FAFF),
                Color(0xFFE8F4FD),
                Color.White.copy(alpha = 0.98f)
            )
        )
    }

    val glowIntensity = 6.dp + (cardShimmer * 1.5.dp)

    AnimatedVisibility(
        visible = true,
        enter = fadeIn(animationSpec = tween(300)) + slideInVertically(
            initialOffsetY = { it / 4 },
            animationSpec = tween(300)
        ),
        exit = fadeOut(animationSpec = tween(200)) + slideOutVertically(
            targetOffsetY = { -it / 4 },
            animationSpec = tween(200)
        ),
        modifier = Modifier.animateContentSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(glowIntensity, RoundedCornerShape(20.dp))
                .background(cardGradient, RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color(0xFF64B5F6).copy(alpha = 0.015f + cardShimmer * 0.005f),
                                Color.Transparent
                            ),
                            radius = 300f
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
            )

            Row(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    AnimatedContent(
                        targetState = item.isCompleted,
                        transitionSpec = {
                            fadeIn(animationSpec = tween(200)) with
                                    fadeOut(animationSpec = tween(200))
                        },
                        label = "textDecoration_${item.id}"
                    ) { isCompleted ->
                        Column {
                            Text(
                                "${item.time} • ${item.date}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                textDecoration = if (isCompleted) TextDecoration.LineThrough else null,
                                color = if (isCompleted) Color(0xFF666666) else textColor
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                "📋 ${item.task}",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                textDecoration = if (isCompleted) TextDecoration.LineThrough else null,
                                color = if (isCompleted) Color(0xFF666666) else textColor.copy(alpha = 0.9f)
                            )

                            if (item.description.isNotBlank()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "💭 ${item.description}",
                                    fontSize = 14.sp,
                                    textDecoration = if (isCompleted) TextDecoration.LineThrough else null,
                                    color = if (isCompleted) Color(0xFF666666) else textColor.copy(
                                        alpha = 0.7f
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    AnimatedContent(
                        targetState = item.isCompleted,
                        transitionSpec = {
                            slideInHorizontally(animationSpec = tween(300)) with
                                    slideOutHorizontally(animationSpec = tween(300))
                        },
                        label = "actionButtons_${item.id}"
                    ) { isCompleted ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            if (!isCompleted) {
                                Button(
                                    onClick = onEditClick,
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                                    modifier = Modifier
                                        .weight(1f)
                                        .background(
                                            brush = Brush.horizontalGradient(
                                                colors = listOf(
                                                    Color(0xFF42A5F5),
                                                    Color(0xFF64B5F6)
                                                )
                                            ),
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .shadow(4.dp, RoundedCornerShape(12.dp)),
                                    contentPadding = PaddingValues(vertical = 12.dp)
                                ) {
                                    Text(
                                        "✏️ Chỉnh sửa",
                                        color = Color.White,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }

                            Button(
                                onClick = onDeleteClick,
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                                modifier = Modifier
                                    .weight(1f)
                                    .background(
                                        brush = Brush.horizontalGradient(
                                            colors = listOf(Color(0xFFEF5350), Color(0xFFE57373))
                                        ),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .shadow(4.dp, RoundedCornerShape(12.dp)),
                                contentPadding = PaddingValues(vertical = 12.dp)
                            ) {
                                Text(
                                    "🗑️ Xóa lịch",
                                    color = Color.White,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .padding(start = 12.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onToggleComplete
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (item.isCompleted) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF4CAF50),
                            modifier = Modifier
                                .size(40.dp)
                                .shadow(8.dp, CircleShape)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(
                                            brush = Brush.radialGradient(
                                                colors = listOf(
                                                    Color(0xFF81C784),
                                                    Color(0xFF4CAF50)
                                                )
                                            ),
                                            shape = CircleShape
                                        )
                                )
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = "Completed - Click to undo",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    } else {
                        Surface(
                            shape = CircleShape,
                            color = Color.Transparent,
                            border = BorderStroke(
                                3.dp,
                                Brush.sweepGradient(
                                    colors = listOf(
                                        Color(0xFF90CAF9),
                                        Color(0xFF64B5F6),
                                        Color(0xFF42A5F5),
                                        Color(0xFF90CAF9)
                                    )
                                )
                            ),
                            modifier = Modifier
                                .size(40.dp)
                                .shadow(4.dp, CircleShape)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .background(
                                            brush = Brush.radialGradient(
                                                colors = listOf(
                                                    Color(0xFF64B5F6).copy(alpha = 0.1f + cardShimmer * 0.1f),
                                                    Color.Transparent
                                                )
                                            ),
                                            shape = CircleShape
                                        )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}