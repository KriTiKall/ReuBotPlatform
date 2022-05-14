package com.example.model.entity

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class EmptyLessonTest { // 1987375157

    @Test
    fun saveOrUpdateTest1() {

        assertEquals(1987375157, SingleLesson(EmptyLesson()).hashCode())
    }
}