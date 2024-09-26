package com.example.drawingapp

import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Color

class DrawingHistory {
    private val undoStack = mutableListOf<Pair<Path, Color>>() // Stack for undo
    private val redoStack = mutableListOf<Pair<Path, Color>>() // Stack for redo

    // Push a path to the undo stack
    fun addPath(path: Pair<Path, Color>) {
        undoStack.add(path)
        redoStack.clear() // Clear the redo stack whenever a new path is added
    }

    // Undo the last path
    fun undo(): Pair<Path, Color>? {
        if (undoStack.isNotEmpty()) {
            val lastPath = undoStack.removeAt(undoStack.size - 1)
            redoStack.add(lastPath) // Add to redo stack
            return lastPath
        }
        return null
    }

    // Redo the last undone path
    fun redo(): Pair<Path, Color>? {
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
