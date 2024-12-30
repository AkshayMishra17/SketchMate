package com.example.drawingapp.ui_layer

import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.drawingapp.functionalities.DrawingHistory
import com.example.drawingapp.functionalities.MenuWithOptions
import com.example.drawingapp.functionalities.saveDrawing

@Composable
fun DrawingApp() {
    val currentPath = remember { Path() }
    var lastPoint by remember { mutableStateOf<Offset?>(null) }
    val paths = remember { mutableStateListOf<Triple<Path, Color, Float>>() }
    val drawingHistory = remember { DrawingHistory() }

    var selectedColor by remember { mutableStateOf(Color.Black) }
    var selectedBrushSize by remember { mutableFloatStateOf(10f) }

    var isPathDrawn by remember { mutableStateOf(false) }
    var canvasWidth by remember { mutableIntStateOf(0) }
    var canvasHeight by remember { mutableIntStateOf(0) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize()) {
        MenuWithOptions(
            onColorSelected = { color -> selectedColor = color },
            onBrushSizeSelected = { brushSize -> selectedBrushSize = brushSize }
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp)
        ) {
            if (!isPathDrawn) {
                Text(
                    text = "Start Drawing",
                    style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .onSizeChanged { size ->
                        canvasWidth = size.width
                        canvasHeight = size.height
                        bitmap =
                            Bitmap.createBitmap(canvasWidth, canvasHeight, Bitmap.Config.ARGB_8888)
                    }
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { offset ->
                                lastPoint = offset
                                currentPath.moveTo(offset.x, offset.y)
                                isPathDrawn = true
                            },
                            onDrag = { change, _ ->
                                val offset = change.position
                                currentPath.lineTo(offset.x, offset.y)
                                lastPoint = offset
                            },
                            onDragEnd = {
                                val newPath = Triple(Path().apply { addPath(currentPath) }, selectedColor, selectedBrushSize)
                                paths.add(newPath)
                                drawingHistory.addPath(newPath)
                                currentPath.reset()
                                lastPoint = null
                            }
                        )
                    }
            ) {
                for ((path, color, brushSize) in paths) {
                    drawPath(path, color, style = Stroke(width = brushSize))
                }
            }

            if (isPathDrawn) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 25.dp),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Button(
                        onClick = {
                            paths.clear()
                            drawingHistory.clearHistory()
                            isPathDrawn = false
                            bitmap = Bitmap.createBitmap(canvasWidth, canvasHeight, Bitmap.Config.ARGB_8888)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                    ) {
                        Text(text = "Clear Screen")
                    }

                    Button(
                        onClick = {
                            bitmap?.let {
                                saveDrawing(it, paths, context)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                    ) {
                        Text(text = "Download")
                    }

                    Button(
                        onClick = {
                            drawingHistory.undo()?.let {
                                paths.removeAt(paths.size - 1)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                    ) {
                        Text(text = "Undo")
                    }

                    Button(
                        onClick = {
                            drawingHistory.redo()?.let { pathToRedo ->
                                paths.add(pathToRedo)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                    ) {
                        Text(text = "Redo")
                    }
                }
            }
        }
    }
}
