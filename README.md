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
		implementation 'com.github.jeffreyliu8.encryptlib:release:0.0.4'
	}
```


A quick example is shown below(see tests):

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
Requires a minimum SDK version of 23
