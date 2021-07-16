package com.example.crypto

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64.encode
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.crypto.crypt.rsa.RSAKeyPairGenerator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.PrivateKey
import java.util.Base64;

class MainActivity : AppCompatActivity() {
    companion object {
        val TAG = MainActivity::class.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val encryptionService = EncryptionServices(applicationContext)
        encryptionService.createMasterKeyAsymmetric(null)


        val encrypt = encryptSecret(applicationContext, "thai ngo")
        println("encrypt>>>  $encrypt")
        val decrypt = decryptSecret(applicationContext, encrypt)
        println("decrypt>>>  $decrypt")
    }

    /**
     * Encrypt secret before saving it.
     */
    fun encryptSecret(applicationContext: Context, secret: String?): String {
        secret?.let {
            return try {
                Log.d(TAG, "encrypt data: $secret")
                EncryptionServices(applicationContext).encryptAsymmetric(it, null)
            } catch (e: Exception) {
                Log.d(TAG, "encrypt is error: ${e.message}")
                ""
            }
        } ?: return ""
    }

    /**
     * Decrypt secret before showing it.
     */
    fun decryptSecret(applicationContext: Context, secret: String?): String {
        secret?.let {
            return try {
                Log.d(TAG, "decrypt data is: $secret");
                EncryptionServices(applicationContext).decryptAsymmetric(it, null)
            } catch (e: Exception) {
                Log.d(TAG, "decrypt is error: ${e.message}");
                ""
            }

        } ?: return ""
    }


    private fun encryptWithAndroidASymmetricKey(data: String, keyPair: KeyPair): String {
        return CipherWrapper(CipherWrapper.TRANSFORMATION_ASYMMETRIC).encrypt(
            data,
            keyPair.private,
            true
        )
    }

    private fun decryptWithAndroidASymmetricKey(data: String, keyPair: KeyPair): String {

        return CipherWrapper(CipherWrapper.TRANSFORMATION_ASYMMETRIC).decrypt(
            data,
            keyPair.private,
            true
        )
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
}

