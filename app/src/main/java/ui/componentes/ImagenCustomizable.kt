package ui.componentes

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.appaaron_ampuero.R

@Composable
fun ImagenCostumizable(uri: Uri?) {
    val context = LocalContext.current

    val painter = rememberAsyncImagePainter(
        model = uri ?: R.drawable.dueno,
        error = painterResource(id = R.drawable.dueno),
        placeholder = painterResource(id = R.drawable.dueno)
    )

    Image(
        painter = painter,
        contentDescription = "Foto de perfil",
        modifier = Modifier
            .size(120.dp)
            .clip(CircleShape)
    )
}




