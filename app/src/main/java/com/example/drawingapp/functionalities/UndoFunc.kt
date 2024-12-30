package com.example.drawingapp.functionalities


import com.example.drawingapp.viewmodel.PathData

class DrawingHistory {
    private val undoStack = mutableListOf<PathData>()
    private val redoStack = mutableListOf<PathData>()

    fun addPath(pathData: PathData) {
        undoStack.add(pathData)
        redoStack.clear() // Clear redo stack on new action
    }

    fun undo(): PathData? {
        if (undoStack.isNotEmpty()) {
            val pathData = undoStack.removeLast()
            redoStack.add(pathData)
            return pathData
        }
        return null
    }

    fun redo(): PathData? {
        if (redoStack.isNotEmpty()) {
            val pathData = redoStack.removeLast()
            undoStack.add(pathData)
            return pathData
        }
        return null
    }

    fun clearHistory() {
        undoStack.clear()
        redoStack.clear()
    }
}
