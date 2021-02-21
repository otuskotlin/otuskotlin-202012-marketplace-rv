package ru.ok.common.general

import kotlin.test.Test
import kotlin.test.assertTrue

internal class SomeCommonFunTest {
    @Test
    fun someCommonFunTest() {
        val str = "Some Common Fun"
        assertTrue {
            someCommonFun(str).contains(str)
        }
    }
}