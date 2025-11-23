package ui.componentes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.ui.text.TextStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopBar(
    titulo: String,
    colorFondo: Color = Color.Gray,
    colorTexto: Color = Color.Black
) {
    Surface(
        color = colorFondo,
        tonalElevation = 4.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = titulo,
                color = colorTexto,
                style = TextStyle(fontSize = 24.sp),
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}
