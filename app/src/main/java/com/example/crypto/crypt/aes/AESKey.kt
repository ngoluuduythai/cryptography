package com.example.crypto.crypt.aes

import com.example.crypto.crypt.Key

interface AESKey : Key {
    val value: ByteArray
}