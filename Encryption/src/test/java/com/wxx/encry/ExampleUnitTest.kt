package com.wxx.encry

import com.wxx.encry.aes.AESUtil
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun testAES() {
        val content = "wuxinxi吴新喜"
        val password = "123456789"
        val encryptData = AESUtil.instance.encrypt(content.toByteArray(), password.toByteArray())
        val decrypt = AESUtil.instance.decrypt(encryptData!!, password.toByteArray())
        print("解密后的内容：${String(decrypt!!)}")
    }
}
