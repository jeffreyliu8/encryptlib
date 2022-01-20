package com.jeffreyliu.encryptlib

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.lang.Exception
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.IvParameterSpec


class SymmetricEncryptor {
    private lateinit var keyStore: KeyStore
    private lateinit var iV: ByteArray

    companion object {
        private const val CIPHER_TYPE =
            "${KeyProperties.KEY_ALGORITHM_AES}/${KeyProperties.BLOCK_MODE_CBC}/${KeyProperties.ENCRYPTION_PADDING_PKCS7}"
        private const val ANDROID_KEY_PROVIDER = "AndroidKeyStore"
    }

    init {
        try {
            keyStore = KeyStore.getInstance(ANDROID_KEY_PROVIDER)
            keyStore.load(null)
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) e.printStackTrace()
        }
    }

    fun getIV(): ByteArray {
        return iV
    }

    fun generateKey(alias: String): Boolean {
        try {
            if (keyStore.containsAlias(alias)) {
                return true
            } else {
                val keygen =
                    KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_PROVIDER)
                keygen.init(
                    KeyGenParameterSpec.Builder(
                        alias,
                        KeyProperties.PURPOSE_ENCRYPT
                                or KeyProperties.PURPOSE_DECRYPT
                    )
                        .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                        .setKeySize(256)
                        .setUserAuthenticationRequired(false)
                        .build()
                )
                keygen.generateKey()
                return true
            }
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) e.printStackTrace()
        }
        return false
    }

    fun encrypt(alias: String, plainText: ByteArray): ByteArray? {
        try {
            val secretKey = keyStore.getKey(alias, null)
            val cipher = Cipher.getInstance(CIPHER_TYPE)
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
            iV = cipher.iv
            return cipher.doFinal(plainText)
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) e.printStackTrace()
        }
        return null
    }

    fun decrypt(alias: String, cipherText: ByteArray, IV: ByteArray): ByteArray? {
        try {
            val secretKey = keyStore.getKey(alias, null)
            val cipher = Cipher.getInstance(CIPHER_TYPE)
            val ivSpec = IvParameterSpec(IV)
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec)
            return cipher.doFinal(cipherText)
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) e.printStackTrace()
        }
        return null
    }
}