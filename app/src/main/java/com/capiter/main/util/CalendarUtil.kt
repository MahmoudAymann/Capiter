package com.capiter.main.util

import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

object CalendarUtil {

    //TODO USAGE:
//    val days = getDays(7, "28/04/2021")
//    Timber.e("$days")
//    val times = getTimes(15, "22:00", "23:00")
//    Timber.e("$times")

    fun getComingDays(numOfDays: Int, currentDate: String?): ArrayList<String> { // date format 15/02/1995
        return currentDate?.let {
            val daysList = arrayListOf<String>()
            if (it.contains("/")) {
                val currentDay = it.split("/")[0].toInt()
                val smp = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
                for (i in 0 until numOfDays) {
                    val calendar: Calendar = Calendar.getInstance()
                    calendar.set(Calendar.DATE, currentDay)
                    calendar.add(Calendar.DATE, i)
                    val day = smp.format(calendar.time)
                    daysList.add(day)
                }
                daysList
            } else
                throw IllegalArgumentException("wrong date format")
        } ?: throw NullPointerException("date is null")
    }


    private fun getHoursDiff(startTime: String, endTime: String, smp: SimpleDateFormat): Int {
        //get different between 2 times
        val startDate = smp.parse(startTime)
        val endDate = smp.parse(endTime)
        var difference = endDate!!.time - startDate!!.time
        if (difference < 0) {
            val dateMax = smp.parse("24:00")
            val dateMin = smp.parse("00:00")
            difference = dateMax!!.time - startDate.time + (endDate.time - dateMin!!.time)
        }
        val days = (difference / (1000 * 60 * 60 * 24)).toInt()
        val hours = ((difference - 1000 * 60 * 60 * 24 * days) / (1000 * 60 * 60)).toInt()
//        val min = (difference - 1000 * 60 * 60 * 24 * days - 1000 * 60 * 60 * hours).toInt() / (1000 * 60)
//        val sec = (difference - 1000 * 60 * 60 * 24 * days - 1000 * 60 * 60 * hours - 1000 * 60 * min).toInt() / 1000
        return hours
    }

     fun getTimes(minutesToCount: Int, startTime: String?, endTime: String?): ArrayList<String> {// time format must be 24 hr-> HH:mm
        return if (startTime.isNullOrBlank() || endTime.isNullOrBlank()) {
            Timber.e("start or end time is null or blank")
            arrayListOf()
        } else {
            val timesList = arrayListOf<String>()
            val smp = SimpleDateFormat("HH:mm", Locale.ENGLISH)
            val hrDiffs = getHoursDiff(startTime, endTime, smp)
            val countMin = (hrDiffs * 60) / minutesToCount
            for (i in 0..countMin) {
                val c = Calendar.getInstance()
                c.timeInMillis = smp.parse(startTime)!!.time
                c.set(Calendar.MINUTE, minutesToCount * i)
                timesList.add("${c.get(Calendar.HOUR_OF_DAY)}:${c.get(Calendar.MINUTE)}")
            }
            timesList
        }
    }


}