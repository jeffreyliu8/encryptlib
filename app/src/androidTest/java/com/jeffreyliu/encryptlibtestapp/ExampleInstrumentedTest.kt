package com.jeffreyliu.encryptlibtestapp

import android.util.Base64
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.jeffreyliu.encryptlib.AndroidKeyStoreSymmetricEncryptor

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.jeffreyliu.encryptlibtestapp", appContext.packageName)
    }

    @Test
    fun testEnc() {
        val alias = "my alias"
        val pin = "text to be encrypted"
        val se = AndroidKeyStoreSymmetricEncryptor()
        assert(se.generateKey(alias))

        val encryptedByteArray = se.encrypt(alias, pin.toByteArray())
        assertNotNull(encryptedByteArray)
        val encryptedString = Base64.encodeToString(encryptedByteArray, Base64.DEFAULT)
        val iv = se.getIV()

        val se2 = AndroidKeyStoreSymmetricEncryptor()
        val decryptedByteArray =
            se2.decrypt(alias, encryptedByteArray!!, iv)
        assertNotNull(decryptedByteArray)
        val decryptedString = decryptedByteArray?.decodeToString()
        Log.d("jeff", "encryptedString is $encryptedString")
        Log.d("jeff", "decryptedString is $decryptedString")
        assertEquals(pin, decryptedString)
    }
}