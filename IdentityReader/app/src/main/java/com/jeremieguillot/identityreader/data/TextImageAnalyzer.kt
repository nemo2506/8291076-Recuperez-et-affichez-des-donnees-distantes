package com.jeremieguillot.identityreader.data

import android.util.Log
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class TextImageAnalyzer : ImageAnalysis.Analyzer {

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    private var frameSkipCounter = 0

    @ExperimentalGetImage
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image =
                InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            val result = recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    val resultText = visionText.text
                    Log.i("resultText", resultText)
                    imageProxy.close()
                    /*for (block in visionText.textBlocks) {
                        val blockText = block.text
                        Log.i("blockText", blockText)
                        val blockCornerPoints = block.cornerPoints
                        Log.i("blockCornerPoints", blockCornerPoints.toString())
                        val blockFrame = block.boundingBox
                        Log.i("blockFrame", blockFrame.toString())
                        for (line in block.lines) {
                            val lineText = line.text
                            Log.i("lineText", lineText)
                            val lineCornerPoints = line.cornerPoints
                            Log.i("lineCornerPoints", lineCornerPoints.toString())
                            val lineFrame = line.boundingBox
                            Log.i("lineFrame", lineFrame.toString())
                            for (element in line.elements) {
                                val elementText = element.text
                                Log.i("elementText", elementText)
                                val elementCornerPoints = element.cornerPoints
                                Log.i("elementCornerPoints", elementCornerPoints.toString())
                                val elementFrame = element.boundingBox
                                Log.i("elementFrame", elementFrame.toString())
                            }
                        }
                    }*/
                }
                .addOnFailureListener { e ->
                    Log.e("error", e.message ?: "null")
                    imageProxy.close()
                }
        }
    }
}