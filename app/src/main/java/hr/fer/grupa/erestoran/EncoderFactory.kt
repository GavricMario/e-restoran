package hr.fer.grupa.erestoran

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream

fun encodeToBase64(bitmap: Bitmap):String{
    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
    val str = android.util.Base64.encodeToString(byteArray, android.util.Base64.DEFAULT)
    return str
}