package com.example.crypto
import android.util.Base64
import java.nio.charset.StandardCharsets

object Base64Encoder {

    fun base64(targetBytes: ByteArray): String {
        return Base64.encodeToString(targetBytes, Base64.NO_WRAP)
    }

    fun debase64(targetString: String): ByteArray {
        return Base64.decode(targetString.toByteArray(StandardCharsets.UTF_8), Base64.NO_WRAP)
    }

    fun debase64Url(targetString: String): ByteArray {
        return Base64.decode(targetString.toByteArray(StandardCharsets.UTF_8), Base64.URL_SAFE)
    }
}