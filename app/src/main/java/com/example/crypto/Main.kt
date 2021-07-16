package com.example.crypto

import android.graphics.Bitmap
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import com.example.crypto.crypt.rsa.RSAKeyPairGenerator
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.PrivateKey
import java.security.interfaces.RSAPublicKey
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope


suspend fun main() {
    val keyPair = createAsymmetricKeyPair()
//    println("skey:  ${keyPair.private}")
//    println("skey:  ${keyPair.public.encoded}")
    val rsaPublicKey = keyPair.public as RSAPublicKey
    val module = rsaPublicKey.modulus
    val exponent = rsaPublicKey.publicExponent

    println("module>>>  $module")
    println("exponent>>>  $exponent")
    println("module>>> toString ${module.toString()}")
    println("exponent>>> toString ${exponent.toString()}")
    println("module>>> toByteArray ${module.toByteArray()}")
    println("exponent>>> toByteArray ${exponent.toByteArray()}")
    println("module>>> toBigDecimal ${module.toBigDecimal()}")
    println("exponent>>> toBigDecimal ${exponent.toBigDecimal()}")

//    val rasKeyPair = RSAKeyPairGenerator(1024)
//    println("rasKeyPair>>>  ${rasKeyPair.invoke().public.value.asSequence()}")
//    println("rasKeyPair>>>  ${rasKeyPair.invoke().private.value}")

    val encrypt = encryptWithAndroidASymmetricKey("thai ngo", keyPair)
    println("encrypt>>>  $encrypt")
    val decrypt = decryptWithAndroidASymmetricKey(encrypt, keyPair)
    println("decrypt>>>  $decrypt")

}


private suspend fun encryptWithAndroidASymmetricKey(data: String, keyPair: KeyPair): String = coroutineScope {
    val encryptRs = async {
         CipherWrapper(CipherWrapper.TRANSFORMATION_ASYMMETRIC).encrypt(
            data,
            keyPair.private,
            true
        )
    }

    return@coroutineScope encryptRs.await()
}

private suspend fun decryptWithAndroidASymmetricKey(data: String, keyPair: KeyPair): String = coroutineScope{
    val decryptRs = async {
        CipherWrapper(CipherWrapper.TRANSFORMATION_ASYMMETRIC).decrypt(
            data,
            keyPair.private,
            true
        )
    }

    return@coroutineScope decryptRs.await()
}

fun createAsymmetricKeyPair(): KeyPair {
    val generator: KeyPairGenerator

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        generator =
            KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore")
        val builder =
            KeyGenParameterSpec.Builder(
                "KEY_ALIAS",
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_ECB)
                //.setUserAuthenticationRequired(true)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
        generator.initialize(builder.build())

    } else {
        generator = KeyPairGenerator.getInstance("RSA")
        generator.initialize(2048)
    }
    return generator.generateKeyPair()
}

fun getAsymmetricKeyPair(): KeyPair? {
    val keyStore = KeyStore.getInstance("AndroidKeyStore")
    keyStore.load(null)

    val privateKey = keyStore.getKey("KEY_ALIAS", null) as PrivateKey?
    val publicKey = keyStore.getCertificate("KEY_ALIAS")?.publicKey

    return if (privateKey != null && publicKey != null) {
        KeyPair(publicKey, privateKey)
    } else {
        null
    }
}