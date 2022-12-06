package com.top1shvetsvadim1.milestoneebs5

class TimeInterval(private val from: Pair<Int, Int>, private val to: Pair<Int, Int>) {

    private var result = false
    private val maxTime = 86400

    fun checkIfInRange(time: Pair<Int, Int>): Boolean {
        val resultFinal = getSeconds(time.first, time.second)
        return checkTime(resultFinal)
    }

    private fun checkTime(time:Int): Boolean{
        val resultFrom = getSeconds(from.first, from.second)
        val resultTo = getSeconds(to.first, to.second)
        if(from.first > to.first){
            for(i in resultFrom..maxTime){
                if(time == i){
                    result = true
                }
            }
            for(i in 0..resultTo){
                if(time == i){
                    result = true
                }
            }
        }else {
            for (i in resultFrom..resultTo) {
                if (time == i) {
                    result = true
                }
            }
        }
        return result
    }

    private fun getSeconds(hour: Int, minute: Int): Int {
        return hourInSecond(hour) + minuteInSeconds(minute)
    }

    private fun hourInSecond(hour: Int): Int {
        return hour * 60 * 60
    }

    private fun minuteInSeconds(minute: Int): Int {
        return minute * 60
    }
}