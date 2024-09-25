package com.example.drawingapp

import android.graphics.Bitmap
import android.os.Environment
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.toArgb
import android.content.Context
import android.graphics.Canvas
import java.io.File
import java.io.FileOutputStream

fun saveDrawing(
    bitmap: Bitmap,
    paths: List<Pair<Path, Color>>,
    context: Context,
    canvasWidth: Int,
    canvasHeight: Int
) {
    // Create an Android Canvas from the bitmap
    val canvas = Canvas(bitmap)

    // Draw each path onto the Android Canvas
    for ((path, color) in paths) {
        val paint = android.graphics.Paint().apply {
            this.color = color.toArgb()
            this.style = android.graphics.Paint.Style.STROKE
            this.strokeWidth = 10f
            this.isAntiAlias = true
        }
        canvas.drawPath(path.asAndroidPath(), paint)
    }

    // Define the file path and save the bitmap as PNG
    val filePath = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
        "drawing_${System.currentTimeMillis()}.png"
    )
    FileOutputStream(filePath).use { out ->
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        out.flush()
    }

    // Show success message to user
    Toast.makeText(context, "Drawing saved to gallery", Toast.LENGTH_LONG).show()
}
