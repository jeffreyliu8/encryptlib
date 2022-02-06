package com.jeffreyliu.encryptlibtestapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.jeffreyliu.encryptlib.AndroidKeyStoreSymmetricEncryptor

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // NOTE: it's your responsibility to determine what your code do when api level is
        // lower than 23 (M)

        val alias = "my alias"
        val pin = "text to be encrypted"
        val se = AndroidKeyStoreSymmetricEncryptor()
        val isKeyGenerated = se.generateKey(alias)
        if (isKeyGenerated) {
            val encryptedByteArray = se.encrypt(alias, pin.toByteArray())

            val encryptedString = Base64.encodeToString(encryptedByteArray, Base64.DEFAULT)
            val iv = se.getIV()


            val decryptedByteArray =
                se.decrypt(alias, encryptedByteArray!!, iv)

            val decryptedString = decryptedByteArray?.decodeToString()
            Log.d("jeff", "encryptedString is $encryptedString")
            Log.d("jeff", "decryptedString is $decryptedString")

            val msg = if (pin == decryptedString) {
                "success"
            } else {
                "failed"
            }
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "no key generated", Toast.LENGTH_LONG).show()
        }
    }
}