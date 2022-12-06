package com.top1shvetsvadim1.milestoneebs5


import org.junit.Test

class HoursValidateUnitTest {

    private val timeInterval = TimeInterval(10 to 23, 15 to 41)
    private val timeInvers = TimeInterval(22 to 1, 4 to 3)

    @Test
    fun test_1() {
        assert(timeInterval.checkIfInRange(14 to 21))
    }

    @Test
    fun test_2() {
        assert(!timeInterval.checkIfInRange(10 to 0))
    }

    @Test
    fun test_3() {
        assert(timeInterval.checkIfInRange(10 to 24))
    }

    @Test
    fun test_4() {
        assert(timeInterval.checkIfInRange(15 to 41))
    }

    @Test
    fun test_5() {
        assert(!timeInterval.checkIfInRange(15 to 43))
    }

    @Test
    fun test_6() {
        assert(timeInterval.checkIfInRange(11 to 1))
    }

    @Test
    fun test_7() {
        assert(timeInvers.checkIfInRange(2 to 3))
    }

    @Test
    fun test_8() {
        assert(!timeInvers.checkIfInRange(20 to 3))
    }

    @Test
    fun test_9() {
        assert(timeInvers.checkIfInRange(3 to 40))
    }

    @Test
    fun test_10() {
        assert(timeInvers.checkIfInRange(23 to 1))
    }

    @Test
    fun test_11() {
        assert(!timeInvers.checkIfInRange(5 to 10))
    }

    @Test
    fun test_12() {
        assert(timeInvers.checkIfInRange(1 to 34))
    }
}