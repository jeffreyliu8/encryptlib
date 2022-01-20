package com.jeffreyliu.encryptlib

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testEnc() {
        val password = "jeffrey"
        val pin = "text to be encrypted"
        val se = SymmetricEncryptor()
        val secretKey = se.generateSecretKeySpec(password)
        assertNotNull(secretKey)

        val encryptedByteArray = se.encrypt(pin.toByteArray(), secretKey!!)
        assertNotNull(encryptedByteArray)

        val iv = se.getIV()

        val se2 = SymmetricEncryptor()
        val decryptedByteArray =
            se2.decrypt(encryptedByteArray!!, iv, secretKey)
        assertNotNull(decryptedByteArray)

        println("encryptedByteArray is $encryptedByteArray")
        println("decryptedByteArray is $decryptedByteArray")
        assert(pin.toByteArray().contentEquals(decryptedByteArray))
    }
}