package com.example.crypto.crypt.rsa

import com.example.crypto.crypt.Key

interface RSAKey : Key {
    val value: ByteArray
}