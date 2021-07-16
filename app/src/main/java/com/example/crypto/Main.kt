package com.example.crypto

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.io.IOException
import java.io.StringWriter
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.PrivateKey
import java.security.cert.X509Certificate
import java.security.interfaces.RSAPublicKey


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

//    val encrypt = encryptWithAndroidASymmetricKey("thai ngo", keyPair)
//    println("encrypt>>>  $encrypt")
//    val decrypt = decryptWithAndroidASymmetricKey(encrypt, keyPair)
//    println("decrypt>>>  $decrypt")





}


/**
 * Returns the Certificate as a PEM encoded String.
 *
 * @param certificate - X.509 Certificate.
 * @return PEM Encoded Certificate String.
 * @throws SCMSecurityException - On failure to create a PEM String.
 */
//@Throws(SCMSecurityException::class)
//fun getPEMEncodedString(certificate: X509Certificate): String? {
//    return try {
//        val stringWriter = StringWriter()
//        JcaPEMWriter(stringWriter).use { pemWriter -> pemWriter.writeObject(certificate) }
//        stringWriter.toString()
//    } catch (e: IOException) {
//        LOG.error(
//            "Error in encoding certificate." + certificate
//                .getSubjectDN().toString(), e
//        )
//        throw SCMSecurityException(
//            "PEM Encoding failed for certificate." +
//                    certificate.getSubjectDN().toString(), e
//        )
//    }
//}


//private suspend fun encryptWithAndroidASymmetricKey(data: String, keyPair: KeyPair): String = coroutineScope {
//    val encryptRs = async {
//         CipherWrapper(CipherWrapper.TRANSFORMATION_ASYMMETRIC).encrypt(
//            data,
//            keyPair.private,
//            true
//        )
//    }
//
//    return@coroutineScope encryptRs.await()
//}
//
//private suspend fun decryptWithAndroidASymmetricKey(data: String, keyPair: KeyPair): String = coroutineScope{
//    val decryptRs = async {
//        CipherWrapper(CipherWrapper.TRANSFORMATION_ASYMMETRIC).decrypt(
//            data,
//            keyPair.private,
//            true
//        )
//    }
//
//    return@coroutineScope decryptRs.await()
//}

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