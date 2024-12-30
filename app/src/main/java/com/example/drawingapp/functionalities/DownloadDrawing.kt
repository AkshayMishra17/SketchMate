package com.example.drawingapp.functionalities

import android.graphics.Bitmap
import android.os.Environment
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.toArgb
import android.content.Context
import android.graphics.Canvas
import androidx.compose.runtime.snapshots.SnapshotStateList
import java.io.File
import java.io.FileOutputStream

fun saveDrawing(
    bitmap: Bitmap, //drawing will save on it,its like an image file
    paths: SnapshotStateList<Triple<Path, Color, Float>>, //will contain the path and color used
    context: Context
    //canvas width
) {
    val canvas = Canvas(bitmap) //canvas created so that our drawing can be drawn on the bitmap

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
    Toast.makeText(context, "Drawing saved to gallery", Toast.LENGTH_LONG).show()
}
