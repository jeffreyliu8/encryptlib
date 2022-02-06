package com.jeffreyliu.encryptlib

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.annotation.RequiresApi
import java.lang.Exception
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.IvParameterSpec


class AndroidKeyStoreSymmetricEncryptor {
    private lateinit var keyStore: KeyStore
    private lateinit var iV: ByteArray

    companion object {
        @RequiresApi(Build.VERSION_CODES.M)
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

    /**
     * Returns true if key is generated successfully, api level lower than 23 will return false
     */
    @RequiresApi(Build.VERSION_CODES.M)
    fun generateKey(alias: String): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false
        }
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

    /**
     * api level lower than 23 will return null
     */
    @RequiresApi(Build.VERSION_CODES.M)
    fun encrypt(alias: String, plainText: ByteArray): ByteArray? {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return null
        }
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

    /**
     * api level lower than 23 will return null
     */
    @RequiresApi(Build.VERSION_CODES.M)
    fun decrypt(alias: String, cipherText: ByteArray, IV: ByteArray): ByteArray? {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return null
        }
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