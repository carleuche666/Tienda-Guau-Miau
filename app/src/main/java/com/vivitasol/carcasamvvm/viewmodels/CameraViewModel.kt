package com.vivitasol.carcasamvvm.viewmodels

import android.content.Context
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.LifecycleCameraController
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import java.io.File

class CameraViewModel : ViewModel() {

    fun takePhoto(
        cameraController: LifecycleCameraController,
        context: Context,
        onPhotoTaken: (String) -> Unit,
        onError: (ImageCaptureException) -> Unit
    ) {
        val file = File.createTempFile("pet_photo", ".jpg", context.cacheDir)
        val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()

        cameraController.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    outputFileResults.savedUri?.let {
                        onPhotoTaken(it.toString())
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    onError(exception)
                }
            }
        )
    }
}