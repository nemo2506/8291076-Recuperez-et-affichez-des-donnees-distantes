package com.jeremieguillot.identityreader.scan.presentation

import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.jeremieguillot.identityreader.core.domain.MRZ
import com.jeremieguillot.identityreader.scan.data.MRZRecognitionOCR
import com.jeremieguillot.identityreader.scan.data.MRZResult.Failure
import com.jeremieguillot.identityreader.scan.data.MRZResult.Success
import com.jeremieguillot.identityreader.scan.data.TextImageAnalyzer

@Composable
fun ScanScreen(onSuccess: (MRZ) -> Unit) {
    val recognizer = remember { MRZRecognitionOCR() }
    val context = LocalContext.current
    val analyzer = remember {
        TextImageAnalyzer(onSuccess = {

            when (val result = recognizer.recognize(it)) {
                Failure -> {/*will retry automatically*/
                }

                is Success -> {
                    onSuccess(result.mrz)
                }
            }
        }
        )
    }

    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(CameraController.IMAGE_ANALYSIS)
            setImageAnalysisAnalyzer(
                ContextCompat.getMainExecutor(context), analyzer
            )
        }
    }

    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            OverlayScreen {
                CameraPreview(
                    controller = controller, modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}