package ru.ok.common.general

import kotlin.test.Test
import kotlin.test.assertEquals

internal class SomeCommonModelTest() {
    @Test
    fun someCommonModelTest() {
        val st = SomeCommonModel("1", "data")
        assertEquals("1", st.id)
        assertEquals("data", st.data)
    }
}