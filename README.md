[![License](https://img.shields.io/badge/license-Apache%202-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![](https://jitpack.io/v/jeffreyliu8/encryptlib.svg)](https://jitpack.io/#jeffreyliu8/encryptlib)

[![](https://www.buymeacoffee.com/assets/img/custom_images/orange_img.png)](https://www.buymeacoffee.com/jeffliu)

# encryptlib
encryption lib for android api level 23 or above


----------------

### Setup
```groovy
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```


##### Dependencies
```groovy
	dependencies {
		implementation 'com.github.jeffreyliu8.encryptlib:release:0.0.5'
	}
```

A quick example is shown below also in MainActivity, using android keystore to perform AES encryption, requires api level 23 or above(see tests):

```kotlin
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
```

A quick example is shown below just for aes with your own password, doesn't require api level 23(see tests):

```kotlin
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
            se2.decrypt(encryptedByteArray!!, secretKey, iv)
        assertNotNull(decryptedByteArray)

        println("encryptedByteArray is $encryptedByteArray")
        println("decryptedByteArray is $decryptedByteArray")
        assert(pin.toByteArray().contentEquals(decryptedByteArray))
    }

    @Test
    fun testEncDefaultIV() {
        val password = "jeffrey"
        val pin = "text to be encrypted"
        val se = SymmetricEncryptor(defaultIV = true)
        val secretKey = se.generateSecretKeySpec(password)
        assertNotNull(secretKey)

        val encryptedByteArray = se.encrypt(pin.toByteArray(), secretKey!!)
        assertNotNull(encryptedByteArray)


        val se2 = SymmetricEncryptor()
        val decryptedByteArray =
            se2.decrypt(encryptedByteArray!!, secretKey)
        assertNotNull(decryptedByteArray)

        println("encryptedByteArray is $encryptedByteArray")
        println("decryptedByteArray is $decryptedByteArray")
        assert(pin.toByteArray().contentEquals(decryptedByteArray))
    }
```


Requirements
--------------
Requires a minimum SDK version of 21 to compile, AndroidKeyStoreSymmetricEncryptor requires 23, SymmetricEncryptor requires 21.
