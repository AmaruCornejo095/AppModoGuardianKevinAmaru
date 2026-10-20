package com.example.appmodoguardiankevinamaru.viewmodels

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File

class ProfileViewModel : ViewModel() {

    private val _imageUri = MutableStateFlow<Uri?>(null)
    val imageUri: StateFlow<Uri?> = _imageUri.asStateFlow()

    fun updateImageUri(uri: Uri?) {
        _imageUri.value = uri
    }

    fun createTempPictureUri(context: Context): Uri {
        val tempFile = File.createTempFile(
            "picture_${System.currentTimeMillis()}",
            ".jpg",
            context.externalCacheDir
        ).apply {
            createNewFile()
            deleteOnExit()
        }

        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            tempFile
        )
    }
}