package com.example.taskflow.presentation.ui.screens

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.taskflow.presentation.ui.components.FlowCard
import com.example.taskflow.presentation.viewmodel.AnalyticsEngine
import com.example.taskflow.presentation.viewmodel.ProfileViewModel
import com.example.taskflow.presentation.viewmodel.TaskViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfileScreen(
    taskViewModel: TaskViewModel,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val profile by profileViewModel.profile.collectAsState()
    val tasks by taskViewModel.tasks.collectAsState()
    val streak by taskViewModel.currentStreak.collectAsState()
    val totalCompleted = AnalyticsEngine.totalCompleted(tasks)

    var isEditing by remember { mutableStateOf(false) }
    var nameDraft by remember(profile.name, isEditing) { mutableStateOf(profile.name) }

    // Picking a new photo copies its bytes into this app's own private
    // storage (see copyImageToInternalStorage below) so the picture keeps
    // working after the picker's content:// URI permission expires.
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            scope.launch {
                val savedPath = copyImageToInternalStorage(context.filesDir, uri, context)
                if (savedPath != null) profileViewModel.updateImage(savedPath)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Profile", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            TextButton(onClick = {
                if (isEditing) profileViewModel.updateName(nameDraft)
                isEditing = !isEditing
            }) {
                Icon(
                    if (isEditing) Icons.Filled.Edit else Icons.Filled.Edit,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(if (isEditing) "Save" else "Edit")
            }
        }

        Spacer(Modifier.height(24.dp))

        Box(contentAlignment = Alignment.BottomEnd) {
            ProfileAvatar(
                imagePath = profile.imagePath,
                modifier = Modifier.size(96.dp)
            )
            if (isEditing) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable { imagePicker.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.CameraAlt,
                        contentDescription = "Change photo",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        if (isEditing) {
            OutlinedTextField(
                value = nameDraft,
                onValueChange = { nameDraft = it },
                singleLine = true,
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(0.8f)
            )
        } else {
            Text(profile.name, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
        }

        Spacer(Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FlowCard(modifier = Modifier.weight(1f)) {
                Text(
                    "$streak",
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Text(
                    "Day Streak",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
            FlowCard(modifier = Modifier.weight(1f)) {
                Text(
                    "$totalCompleted",
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Text(
                    "Tasks Completed",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun ProfileAvatar(imagePath: String?, modifier: Modifier = Modifier) {
    val bitmap = remember(imagePath) {
        imagePath?.let { path ->
            runCatching { BitmapFactory.decodeFile(path)?.asImageBitmap() }.getOrNull()
        }
    }
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        if (bitmap != null) {
            Image(
                bitmap = bitmap,
                contentDescription = "Profile photo",
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Icon(
                Icons.Filled.Person,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * Copies whatever the picker returned into a file this app owns
 * (filesDir/profile_image.jpg), overwriting any previous photo. Runs on
 * Dispatchers.IO since it's file/stream work - never block the main thread
 * with this even though it's "just" a profile picture.
 */
private suspend fun copyImageToInternalStorage(
    filesDir: File,
    uri: Uri,
    context: android.content.Context
): String? = withContext(Dispatchers.IO) {
    runCatching {
        val destination = File(filesDir, "profile_image.jpg")
        context.contentResolver.openInputStream(uri)?.use { input ->
            destination.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        destination.absolutePath
    }.getOrNull()
}
