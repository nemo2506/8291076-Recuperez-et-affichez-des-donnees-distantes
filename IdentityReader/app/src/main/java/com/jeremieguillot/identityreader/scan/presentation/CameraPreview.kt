package com.jeremieguillot.identityreader.scan.presentation

import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun CameraPreview(
    controller: LifecycleCameraController, modifier: Modifier = Modifier,
    overlayColor: Color = Color.Black.copy(alpha = 0.5f),
    transparentCircleRadius: Dp = 100.dp
) {

    val lifecycleOwner = LocalLifecycleOwner.current

    Box(modifier = modifier) {
        AndroidView(
            factory = {
                PreviewView(it).apply {
                    this.controller = controller
                    controller.bindToLifecycle(lifecycleOwner)
                }
            },
            modifier = modifier
        )

        // Overlay
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            val circleRadiusPx = transparentCircleRadius.toPx()
            val circleCenter = Offset(canvasWidth / 2, canvasHeight / 2)

            // Draw the semi-transparent overlay
            drawRect(
                color = overlayColor,
                size = size
            )

            // Draw the transparent circle
            drawCircle(
                color = Color.Transparent,
                radius = circleRadiusPx,
                center = circleCenter,
                blendMode = BlendMode.Clear
            )
        }
    }
}