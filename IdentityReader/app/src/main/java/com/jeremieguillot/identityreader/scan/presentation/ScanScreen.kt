package com.jeremieguillot.identityreader.scan.presentation

import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.jeremieguillot.identityreader.core.presentation.Destination
import com.jeremieguillot.identityreader.scan.data.MRZRecognitionOCR
import com.jeremieguillot.identityreader.scan.data.MRZResult.Failure
import com.jeremieguillot.identityreader.scan.data.MRZResult.Success
import com.jeremieguillot.identityreader.scan.data.TextImageAnalyzer

@Composable
fun ScanScreen(navController: NavHostController) {

//    var fabVisible by remember { mutableStateOf(false) }
//    var showDialog by remember { mutableStateOf(false) }
//
//    if (showDialog) {
//        ModifyMRZDialog(
//            mrz = MRZ.EMPTY,
//            onDismiss = { showDialog = false },
//            onSave = { documentNumber, dateOfBirth, dateOfExpiry ->
//                val dateOfBirth1 = dateOfBirth.toMRZFormat()
//                val mrz = MRZ(documentNumber, dateOfBirth1, dateOfExpiry.toMRZFormat())
//                navController.navigate(Destination.ReaderScreen(mrz))
//            })
//    }

//    LaunchedEffect(Unit) {
//        delay(5000)
//        fabVisible = true
//    }


    val recognizer = remember { MRZRecognitionOCR() }
    val context = LocalContext.current
    val analyzer = remember {
        TextImageAnalyzer(onSuccess = {

            when (val result = recognizer.recognize(it)) {
                Failure -> {/*will retry automatically*/
                }

                is Success -> {
                    navController.navigate(Destination.ReaderScreen(result.mrz))
                }
            }
        }
        )
    }

    val controller = remember {
        LifecycleCameraController(context).apply {
            isPinchToZoomEnabled = false
            setEnabledUseCases(CameraController.IMAGE_ANALYSIS)
            setImageAnalysisAnalyzer(
                ContextCompat.getMainExecutor(context), analyzer
            )
        }
    }

    Scaffold(floatingActionButton = {
//        if (fabVisible) {
//            FloatingActionButton(
//                onClick = { showDialog = true },
//            ) {
//                Icon(Icons.Filled.Edit, null)
//            }
//        }
    }) { padding ->
        OverlayScreen(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            CameraPreview(
                controller = controller, modifier = Modifier.fillMaxSize()
            )
        }
    }
}