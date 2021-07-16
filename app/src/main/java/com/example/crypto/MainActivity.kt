package com.example.crypto

import android.os.Build
import android.os.Bundle
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64.encode
import androidx.appcompat.app.AppCompatActivity
import com.example.crypto.crypt.rsa.RSAKeyPairGenerator

import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.PrivateKey
import java.util.Base64;

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val keyPair = createAsymmetricKeyPair()
        println("skey:  ${keyPair.private}")


//        val key = ECKeys()
//        key.genRSAKeyPair(object : ECRSAKeyPairListener{
//            override fun onFailure(message: String, e: Exception) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onGenerated(keyPair: KeyPair) {
//                println("skey:  ${keyPair.private}")
//                println("skey:  ${keyPair.public}")            }
//
//        })
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

