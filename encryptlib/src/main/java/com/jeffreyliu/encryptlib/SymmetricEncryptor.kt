package com.jeffreyliu.encryptlib

import java.lang.Exception
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


class SymmetricEncryptor(
    val defaultIV: Boolean = false
) {
    private lateinit var iV: ByteArray

    companion object {
        private const val AES_ALGORITHM = "AES"
        private const val CIPHER_TYPE = "$AES_ALGORITHM/CBC/PKCS5PADDING"
        private const val AES_KEY_SIZE = 256
        private const val HASH_ALGORITHM = "SHA-256"

        private val ivBytes = byteArrayOf(
            0x00,
            0x00,
            0x00,
            0x00,
            0x00,
            0x00,
            0x00,
            0x00,
            0x00,
            0x00,
            0x00,
            0x00,
            0x00,
            0x00,
            0x00,
            0x00
        )
    }


    fun getIV(): ByteArray {
        return iV
    }

    /**
     * Generates a secret key for encrypt and decrypt
     *
     * I can't think of a reason to use this, when the key is just stored in memory only
     */
    fun generateKey(): SecretKey? {
        try {
            val keygen = KeyGenerator.getInstance(AES_ALGORITHM)
            keygen.init(AES_KEY_SIZE)
            return keygen.generateKey()
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) e.printStackTrace()
        }
        return null
    }

    /**
     * Generates SHA256 hash of the password which is used as key
     *
     * @param password used to generated key
     * @return SHA256 of the password
     */
    fun generateSecretKeySpec(password: String): SecretKeySpec? {
        try {
            val digest = MessageDigest.getInstance(HASH_ALGORITHM)
            val bytes = password.toByteArray()
            digest.update(bytes, 0, bytes.size)
            val key = digest.digest()
            return SecretKeySpec(key, AES_ALGORITHM)
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) e.printStackTrace()
        }
        return null
    }

    fun encrypt(plainText: ByteArray, secretKey: SecretKey): ByteArray? {
        try {
            val cipher = Cipher.getInstance(CIPHER_TYPE)
            if (defaultIV) {
                val ivSpec = IvParameterSpec(ivBytes)
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec)
            } else {
                cipher.init(Cipher.ENCRYPT_MODE, secretKey)
                iV = cipher.iv
            }
            return cipher.doFinal(plainText)
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) e.printStackTrace()
        }
        return null
    }

    fun decrypt(cipherText: ByteArray, secretKey: SecretKey, IV: ByteArray? = ivBytes): ByteArray? {
        try {
            val cipher = Cipher.getInstance(CIPHER_TYPE)
            val ivSpec = if (defaultIV) {
                IvParameterSpec(ivBytes)
            } else {
                IvParameterSpec(IV)
            }
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec)
            return cipher.doFinal(cipherText)
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) e.printStackTrace()
        }
        return null
    }
}