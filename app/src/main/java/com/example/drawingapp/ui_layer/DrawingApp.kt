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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.drawingapp.functionalities.MenuWithOptions
import com.example.drawingapp.functionalities.saveDrawing
import com.example.drawingapp.viewmodel.DrawingViewModel

@Composable
fun DrawingApp() {
    val viewModel: DrawingViewModel = viewModel()
    val paths = viewModel.paths
    val isPathDrawn by viewModel.isPathDrawn
    var canvasWidth by remember { mutableIntStateOf(0) }
    var canvasHeight by remember { mutableIntStateOf(0) }
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize()) {
        MenuWithOptions(
            onColorSelected = { color -> viewModel.selectedColor.value = color },
            onBrushSizeSelected = { brushSize -> viewModel.selectedBrushSize.value = brushSize }
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
                        if (viewModel.canvasBitmap.value == null) {
                            viewModel.canvasBitmap.value = Bitmap.createBitmap(canvasWidth, canvasHeight, Bitmap.Config.ARGB_8888)
                        }
                    }
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { offset -> viewModel.startPath(offset) },
                            onDrag = { change, _ -> viewModel.addPointToPath(change.position) },
                            onDragEnd = { viewModel.finishPath() }
                        )
                    }
            ) {
                for (pathData in paths) {
                    drawPath(pathData.path, pathData.color, style = Stroke(width = pathData.brushSize))
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
                        onClick = { viewModel.clearDrawing(canvasWidth, canvasHeight) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                    ) {
                        Text(text = "Clear Screen")
                    }

                    Button(
                        onClick = {
                            viewModel.canvasBitmap.value?.let { bitmap ->
                                saveDrawing(bitmap, paths, context)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                    ) {
                        Text(text = "Download")
                    }

                    Button(
                        onClick = { viewModel.undo() },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                    ) {
                        Text(text = "Undo")
                    }

                    Button(
                        onClick = { viewModel.redo() },
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
