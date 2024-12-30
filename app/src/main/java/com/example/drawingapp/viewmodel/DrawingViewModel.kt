package com.example.drawingapp.viewmodel


import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import com.example.drawingapp.functionalities.DrawingHistory

data class PathData(val path: Path, val color: Color, val brushSize: Float)

class DrawingViewModel : ViewModel() {

    val paths = mutableStateListOf<PathData>()
    val currentPath = Path()
    var lastPoint = mutableStateOf<Offset?>(null)
    var selectedColor = mutableStateOf(Color.Black)
    var selectedBrushSize = mutableStateOf(10f)
    var canvasBitmap = mutableStateOf<Bitmap?>(null)
    var isPathDrawn = mutableStateOf(false)

    private val drawingHistory = DrawingHistory()

    fun startPath(offset: Offset) {
        lastPoint.value = offset
        currentPath.moveTo(offset.x, offset.y)
        isPathDrawn.value = true
    }

    fun addPointToPath(offset: Offset) {
        currentPath.lineTo(offset.x, offset.y)
        lastPoint.value = offset
    }

    fun finishPath() {
        val newPath = PathData(Path().apply { addPath(currentPath) }, selectedColor.value, selectedBrushSize.value)
        paths.add(newPath)
        drawingHistory.addPath(newPath)
        currentPath.reset()
        lastPoint.value = null
    }

    fun clearDrawing(canvasWidth: Int, canvasHeight: Int) {
        paths.clear()
        drawingHistory.clearHistory()
        isPathDrawn.value = false
        canvasBitmap.value = Bitmap.createBitmap(canvasWidth, canvasHeight, Bitmap.Config.ARGB_8888)
    }

    @SuppressLint("NewApi")
    fun undo() {
        drawingHistory.undo()?.let { paths.removeLast() }
    }

    fun redo() {
        drawingHistory.redo()?.let { paths.add(it) }
    }
}
