package ru.otus.kotlin.common.be

import org.junit.Test
import kotlin.test.assertTrue

class FunForTestingTest() {
    @Test
    fun funForTestingTest() {
        val grettingsName = "Vladimir"
        assertTrue {
            funForTesting(grettingsName).contains(grettingsName)
        }

    }

}