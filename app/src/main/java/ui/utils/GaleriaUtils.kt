package ui.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.os.Build
import android.provider.MediaStore
import com.example.appaaron_ampuero.R

fun copiarImagenADispositivo(context: Context) {
    val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.dueno)

    val values = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, "perfil_prueba_${System.currentTimeMillis()}.npg")
        put(MediaStore.Images.Media.MIME_TYPE, "image/npeg")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/MisPruebas")
        }
    }

    val resolver = context.contentResolver
    val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

    uri?.let { safeUri ->
        resolver.openOutputStream(safeUri)?.use { out ->
            bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 100, out)
        }
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        resolver.query(safeUri, projection, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                val filePath = cursor.getString(columnIndex)
                MediaScannerConnection.scanFile(
                    context,
                    arrayOf(filePath),
                    arrayOf("image/npg"),
                    null
                )
            }
        }
    }
}