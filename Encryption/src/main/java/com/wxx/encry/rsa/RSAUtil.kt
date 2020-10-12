package com.wxx.encry.rsa

/**
 * @author ：wuxinxi on 2020/5/22 .
 * @packages ：com.wxx.encry.rsa .
 * TODO:一句话描述
 */
class RSAUtil private constructor() {
    companion object {
        val instance: RSAUtil by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            RSAUtil()
        }
    }


}
