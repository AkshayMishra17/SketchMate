import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Slider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MenuWithOptions(
    onColorSelected: (Color) -> Unit,
    onBrushSizeSelected: (Float) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedColor by remember { mutableStateOf(Color.Black) }
    var selectedBrushSize by remember { mutableStateOf(10f) }

    // Menu with dropdown near button
    Box(modifier = Modifier.fillMaxWidth().padding(top = 50.dp), contentAlignment = Alignment.TopStart) {
        // Menu icon inside a Box
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu",
                modifier = Modifier.size(42.dp)
            )
        }

        // Dropdown menu with color and brush size options
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color.Unspecified) // Ensure the background color is visible
        ) {
            // Color options
            Text("Choose Color", fontSize = 16.sp, modifier = Modifier.padding(8.dp))
            Row(
                modifier = Modifier.padding(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                val colors = listOf(Color.Red, Color.Green, Color.Blue, Color.Yellow, Color.Black, Color.White)
                colors.forEach { color ->
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(color, shape = CircleShape)
                            .clickable {
                                selectedColor = color
                                onColorSelected(color)
                                expanded = false
                            }
                    )
                }
            }

            // Brush size options
            Text("Brush Size", fontSize = 16.sp, modifier = Modifier.padding(8.dp))
            Slider(
                value = selectedBrushSize,
                onValueChange = { selectedBrushSize = it },
                valueRange = 1f..50f,
                modifier = Modifier.padding(8.dp)
            )

            Text("Selected Brush Size: ${selectedBrushSize.toInt()}", fontSize = 14.sp, modifier = Modifier.padding(8.dp))

            // Update the selected brush size
            onBrushSizeSelected(selectedBrushSize)
        }
    }
}
