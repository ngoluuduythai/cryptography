package com.example.crypto.crypt

interface PrivateKey : Key {
    fun decrypt(it: ByteArray): ByteArray
}