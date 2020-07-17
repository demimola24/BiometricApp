package com.example.biometricapp.encryption

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.nio.charset.StandardCharsets
import java.security.Key
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec
import android.security.keystore.KeyProperties.KEY_ALGORITHM_AES
import javax.crypto.spec.IvParameterSpec
import android.os.Build
import java.security.spec.AlgorithmParameterSpec
import javax.crypto.spec.GCMParameterSpec


object AESCrypt {
    private const val ALGORITHM = "AES/GCM/NoPadding"
    private const val PARAM_ALIAS = "test_alias"
    private const val key_ALIAS = "ssEQ44F74b3Srrn35p5sV3Jb95"

    @Throws(Exception::class)
    fun encrypt(value: String): String {
        val key = getSecretKey() ?: return ""
        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, key, getParams(PARAM_ALIAS.toByteArray()))
        val encryptedByteValue = cipher.doFinal(value.toByteArray(charset("utf-8")))

        return Base64.encodeToString(encryptedByteValue, Base64.DEFAULT)
    }

    @Throws(Exception::class)
    fun decrypt(value: String): String {
        val key = getSecretKey() ?: return ""
        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE, key, getParams(PARAM_ALIAS.toByteArray()))
        //cipher.init(Cipher.DECRYPT_MODE, key)
        val decryptedValue64 = Base64.decode(value, Base64.DEFAULT)
        val decryptedByteValue = cipher.doFinal(decryptedValue64)
        return String(decryptedByteValue, StandardCharsets.UTF_8)

    }

    @Throws(Exception::class)
    private fun generateKey(): Key {
        return SecretKeySpec(key_ALIAS.toByteArray(), ALGORITHM)
    }


    @Throws(Exception::class)
    fun generateSecretKey(): SecretKey? {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            val keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM_AES, "AndroidKeyStore")
            val spec = KeyGenParameterSpec
                    .Builder(key_ALIAS, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setKeySize(128)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .setRandomizedEncryptionRequired(false)
                    .build()
            keyGenerator.init(spec)
            return keyGenerator.generateKey()
        }else{
            return null
        }

    }

    @Throws(Exception::class)
    fun getSecretKey(): SecretKey? {
        val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
        val secretKeyEntry = keyStore.getEntry(key_ALIAS, null) as KeyStore.SecretKeyEntry?
        return secretKeyEntry?.secretKey ?: generateSecretKey()
    }

    private fun getParams(iv: ByteArray): AlgorithmParameterSpec {
        return getParams(iv, 0, iv.size)
    }

    private fun getParams(buf: ByteArray, offset: Int, len: Int): AlgorithmParameterSpec {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            // GCMParameterSpec should always be present in Java 7 or newer, but it's missing on
            // some Android devices with API level <= 19. Fortunately, we can initialize the cipher
            // with just an IvParameterSpec. It will use a tag size of 128 bits.
            IvParameterSpec(buf, offset, len)
        } else GCMParameterSpec(16 * 8, buf, offset, len)
    }
}