package com.example.drawingapp

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import MenuWithOptions
import androidx.compose.ui.text.style.TextAlign

@Composable
fun DrawingApp() {
    val currentPath = remember { Path() }
    var lastPoint by remember { mutableStateOf<Offset?>(null) }
    val paths = remember { mutableStateListOf<Pair<Path, Color>>() }

    // Variables for stroke color and brush size
    var selectedColor by remember { mutableStateOf(Color.Black) }
    var selectedBrushSize by remember { mutableStateOf(10f) }

    var isPathDrawn by remember { mutableStateOf(false) }
    var canvasWidth by remember { mutableIntStateOf(0) }
    var canvasHeight by remember { mutableIntStateOf(0) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Keep the MenuWithOptions above the canvas, ensuring separation
        MenuWithOptions(
            onColorSelected = { color -> selectedColor = color },
            onBrushSizeSelected = { brushSize -> selectedBrushSize = brushSize }
        )

        // Drawing canvas
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
                    modifier = Modifier.align(Alignment.Center) // Center the text inside the Box
                )
            }

            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .onSizeChanged { size ->
                        canvasWidth = size.width
                        canvasHeight = size.height
                        bitmap = Bitmap.createBitmap(canvasWidth, canvasHeight, Bitmap.Config.ARGB_8888)
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
                                paths.add(Pair(Path().apply { addPath(currentPath) }, selectedColor))
                                currentPath.reset()
                                lastPoint = null
                            }
                        )
                    }
            ) {
                for ((path, color) in paths) {
                    drawPath(path, color, style = Stroke(width = selectedBrushSize))
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
                                saveDrawing(it, paths, context, canvasWidth, canvasHeight)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                    ) {
                        Text(text = "Download")
                    }
                }
            }
        }
    }
}