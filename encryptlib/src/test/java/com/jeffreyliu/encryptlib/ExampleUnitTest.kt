package com.jeffreyliu.encryptlib

import android.util.Base64
import android.util.Log
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
        val alias = "my alias"
        val pin = "text to be encrypted"
        val se = SymmetricEncryptor()
        assert(se.generateKey(alias))

        val encryptedByteArray = se.encrypt(alias, pin.toByteArray())
        assertNotNull(encryptedByteArray)
        val encryptedString = Base64.encodeToString(encryptedByteArray, Base64.DEFAULT)
        val iv = se.getIV()

        val se2 = SymmetricEncryptor()
        val decryptedByteArray =
            se2.decrypt(alias, encryptedByteArray!!, iv)
        assertNotNull(decryptedByteArray)
        val decryptedString = decryptedByteArray?.decodeToString()
        Log.d("jeff", "encryptedString is $encryptedString")
        Log.d("jeff", "decryptedString is $decryptedString")
        assertEquals(pin, decryptedString)
    }
}