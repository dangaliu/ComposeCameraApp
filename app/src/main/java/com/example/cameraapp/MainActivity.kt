package com.example.cameraapp

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.cameraapp.ui.theme.CameraAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CameraAppTheme {
                ImagePicker(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
fun ImagePicker(
    modifier: Modifier = Modifier
) {

    var hasImage by remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            hasImage = uri != null
            imageUri = uri
        }
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            hasImage = success
        }
    )

    Box(
        modifier = modifier
    ) {
        if (hasImage && imageUri != null) {
            AsyncImage(
                model = imageUri,
                contentDescription = "SelectedImage",
                modifier = Modifier.fillMaxWidth()
            )
        }
        Column(
            modifier = Modifier.align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    imagePicker.launch("image/*")
                }
            ) {
                Text(
                    text = "Select Image"
                )
            }
            Spacer(Modifier.height(16.dp))
            Button(onClick = {
                val uri = ComposeFileProvider.getImageUri(context = context)
                imageUri = uri
                cameraLauncher.launch(uri)
            }) {
                Text(text = "Take photo")
            }
        }
    }
}

