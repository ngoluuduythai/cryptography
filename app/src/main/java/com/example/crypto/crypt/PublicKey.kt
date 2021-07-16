package com.example.crypto.crypt

interface PublicKey : Key {
    fun encrypt(it: ByteArray): ByteArray
}