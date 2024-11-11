package com.example.drawingapp.functionalities

import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Color

class DrawingHistory {
    private val undoStack = mutableListOf<Triple<Path, Color, Float>>() // Stack for undo, now using Triple
    private val redoStack = mutableListOf<Triple<Path, Color, Float>>() // Stack for redo, now using Triple

    // Push a path (as Triple) to the undo stack
    fun addPath(path: Triple<Path, Color, Float>) {
        undoStack.add(path)
        redoStack.clear() // Clear the redo stack whenever a new path is added
    }

    // Undo the last path and return a Triple<Path, Color, Float>
    fun undo(): Triple<Path, Color, Float>? {
        if (undoStack.isNotEmpty()) {
            val lastPath = undoStack.removeAt(undoStack.size - 1)
            redoStack.add(lastPath) // Add to redo stack
            return lastPath
        }
        return null
    }

    // Redo the last undone path and return a Triple<Path, Color, Float>
    fun redo(): Triple<Path, Color, Float>? {
        if (redoStack.isNotEmpty()) {
            val pathToRedo = redoStack.removeAt(redoStack.size - 1)
            undoStack.add(pathToRedo) // Add back to undo stack
            return pathToRedo
        }
        return null
    }

    // Clear all history
    fun clearHistory() {
        undoStack.clear()
        redoStack.clear()
    }
}
