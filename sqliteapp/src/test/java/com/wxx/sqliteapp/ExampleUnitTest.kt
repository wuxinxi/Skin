package com.wxx.sqliteapp

import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {

        val i=100L
        val i1=java.lang.Long(100L)

        print(i.equals(i1))
    }
}
