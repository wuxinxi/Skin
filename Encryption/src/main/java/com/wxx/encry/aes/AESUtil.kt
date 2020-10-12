package com.wxx.encry.aes

import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.SecretKeySpec

/**
 * @author ：wuxinxi on 2020/5/21 .
 * @packages ：com.wxx.encry.aes .
 * TODO:AES单向加密
 */
class AESUtil private constructor() {
    companion object {
        val instance: AESUtil by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            AESUtil()
        }

        private const val ALGORITHM = "AES"
        private const val KEY_SIZE = 128
    }


    /**
     * @param content 需要加密的内容
     * @param password 秘钥
     */
    fun encrypt(content: ByteArray, password: ByteArray): ByteArray? {
        try {
            val keyGenerator = KeyGenerator.getInstance(ALGORITHM)
            //用户秘钥作为随机数初始化
            keyGenerator.init(KEY_SIZE, SecureRandom(password))

            //生成秘钥
            val secretKey = keyGenerator.generateKey()
            //对秘钥进行基本的编码
            val encodedFormat = secretKey.encoded
            //转换成功AES规范的秘钥
            val key = SecretKeySpec(encodedFormat, ALGORITHM)
            val cipher = Cipher.getInstance(ALGORITHM)
            //开始加密
            cipher.init(Cipher.ENCRYPT_MODE, key)
            return cipher.doFinal(content)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    /**
     * @param content 需要解密的内容
     * @param password 秘钥
     */
    fun decrypt(content: ByteArray, password: ByteArray): ByteArray? {
        try {
            val keyGenerator = KeyGenerator.getInstance(ALGORITHM)
            //用户秘钥作为随机数初始化
            keyGenerator.init(KEY_SIZE, SecureRandom(password))

            //生成秘钥
            val secretKey = keyGenerator.generateKey()
            //对秘钥进行基本的编码
            val encodedFormat = secretKey.encoded
            //转换成功AES规范的秘钥
            val key = SecretKeySpec(encodedFormat, ALGORITHM)
            val cipher = Cipher.getInstance(ALGORITHM)
            //开始加密
            cipher.init(Cipher.DECRYPT_MODE, key)
            return cipher.doFinal(content)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

}