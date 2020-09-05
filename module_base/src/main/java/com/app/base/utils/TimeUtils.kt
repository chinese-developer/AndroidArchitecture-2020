package com.app.base.utils

import com.android.base.utils.android.ResourceUtils
import com.app.base.R
import com.blankj.utilcode.constant.TimeConstants
import java.util.*

/**一天的总秒数*/
const val TOTAL_SECONDS_OF_ONE_DAY = 24 * 60 * 60

/**根据出生年月日计算年龄*/
fun calcAgeFromBirthday(year: Int, month: Int, day: Int): Int {
    val calendar = Calendar.getInstance()
    val curYear = calendar.get(Calendar.YEAR)
    val curMonth = calendar.get(Calendar.MONDAY) + 1
    val curDay = calendar.get(Calendar.DAY_OF_MONTH)
    var age = curYear - year
    if (age == 0) {
        return 0
    }
    if (curMonth < month || curMonth == month && curDay < day) {
        age--
    }
    return age
}

/**组合日期，默认格式为 `yyyyMMdd` */
fun composeDate(year: Int, month: Int, day: Int, separator: String = ""): String {
    return arrayOf(year.toString(), month.to2BitText(), day.to2BitText()).joinToString(separator)
}

/**时间戳转为 `HH-mm` 格式*/
fun Long.millisecondsToShortTimeText(): String {
    val instance = Calendar.getInstance()
    instance.timeInMillis = this
    return "${instance.get(Calendar.HOUR_OF_DAY).to2BitText()}-${instance.get(Calendar.MINUTE).to2BitText()}"
}

/**时间戳转为 `MM-dd  HH:mm` 格式*/
fun Long.millisecondsToMediumTimeText(): String {
    val instance = Calendar.getInstance()
    instance.timeInMillis = this
    return "${(instance.get(Calendar.MONTH) + 1).to2BitText()}-${instance.get(Calendar.DAY_OF_MONTH).to2BitText()}  ${instance.get(Calendar.HOUR_OF_DAY).to2BitText()}:${instance.get(Calendar.MINUTE).to2BitText()}"
}

/** 参考[Int.secondsToTimeText] */
fun Long?.secondsToTimeText(adjustToFullMinutes: Boolean = false): String {
    return (this ?: 0).toInt().secondsToTimeText(adjustToFullMinutes)
}

/** 用于将秒转换为分钟时做修正，少于 1 分钟的秒值按照一分钟算，比如如果是 170 则会修正为 180 */
fun Int.adjustToFullMinutes(): Int {
    val mode = this % 60
    return if (mode != 0) {
        this + (60 - mode)
    } else {
        this
    }
}

/**
 * 根据秒值生成具体描述文本，格式为`{x小时x分钟}` 或者 `{x秒}`， [adjustToFullMinutes] 为 true 时表示整体时间大于等于 1 分钟时，转换为分钟表示后少于 1 分钟的剩余秒数按照 1 分钟算，否则剩余秒数将被忽略。
 *
 * 时间显示分两类：
 *
 * - （1）时间从0秒开始累计，包括限时可用列表的已用XX（时长）、用机时长排行榜、各类app使用时长排行榜；
 * - （2）时间从多开始倒计，包括首页设备卡片的剩余可用时长、临时可用
 *
 * 第一类时间显示规则：
 *
 *  - 已用时间：m秒（1=<m<=59），显示：m秒
 *  - 已用时间：x小时n分m秒（1=<n<=59；1=<m<=59），显示：x小时n分
 *  - 已用时间：x小时n分m秒（n=0；1=<m<=59），显示：x小时
 *
 * 简单来讲就是，当已用时间小于一分钟时，显示具体秒数；已用时间大于一分钟时，不显示不足一分钟的秒数而仅显示已使用的足额的分钟数。
 *
 * 第二类时间显示规则：
 *
 *  - 剩余可用m秒（1=<m<=59），显示：m秒；
 *  - 剩余可用n分钟m秒（1=<n<=59；1=<m<=59），显示 n+1分钟；
 */
fun Int?.secondsToTimeText(adjustToFullMinutes: Boolean = false): String {
    val seconds = this ?: 0

    if (seconds < 60) {
        return createTimeTextFromTimeValues(0, 0, seconds)
    }

    val adjustedSeconds = if (adjustToFullMinutes) {
        seconds.adjustToFullMinutes()
    } else {
        seconds
    }
    val totalMinute = adjustedSeconds / 60
    val hour = totalMinute / 60
    val minute = totalMinute % 60
    return createTimeTextFromTimeValues(hour, minute)
}

/**
 * 根据小时、分钟、秒生成具体描述文本，格式为`{x小时x分钟}` 或者 `{x秒}`
 */
fun createTimeTextFromTimeValues(hours: Int, minutes: Int, seconds: Int = 0): String {
    //0-59 只展示秒
    if (hours == 0 && minutes == 0 && seconds != 0) {
        return "$seconds${ResourceUtils.getString(R.string.second)}"
    }
    //大于一分钟后不再展示秒，全都是 0 则展示 0 分钟。
    if (hours == 0 || (hours == 0 && minutes == 0)) {
        return "$minutes${ResourceUtils.getString(R.string.minute)}"
    }
    if (minutes == 0) {
        return "$hours${ResourceUtils.getString(R.string.hour)}"
    }
    return "$hours${ResourceUtils.getString(R.string.hour)}$minutes${ResourceUtils.getString(R.string.minute)}"
}

/**转换为至少 2 位的文本，比如 `1` 应该转换为 `01`*/
fun Int.to2BitText(): String {
    if (this < 10 && this > -10) {
        return "0$this"
    }
    return toString()
}

/**获取今天凌晨的时间（生成的时间戳精确到天），用于[isToday], [isYesterday], [isDayBeforeYesterday] 等方法的传值*/
fun getWeeOfToday(): Long {
    val cal = Calendar.getInstance()
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.MILLISECOND, 0)
    return cal.timeInMillis
}

/**相对于今天，当前时间的秒值：`(hour * 60 * 60) + (minute * 60) + second`*/
fun secondsOfToday(): Int {
    val instance = Calendar.getInstance()
    val hour = instance.get(Calendar.HOUR_OF_DAY)
    val minute = instance.get(Calendar.MINUTE)
    val second = instance.get(Calendar.SECOND)
    return (hour * 60 * 60) + (minute * 60) + second
}

/**当前时间戳*/
fun timestampMillis() = System.currentTimeMillis()

/**判断是不是今天， [wee] 用作对比，应使用 [getWeeOfToday] 来创建，这个参数用于同时进行多个时间判断时减少创建 [Calendar] 对象*/
fun Long.isToday(wee: Long = getWeeOfToday()): Boolean {
    return this >= wee && this < wee + TimeConstants.DAY
}

/**判断是不是昨天， [wee] 用作对比，应使用 [getWeeOfToday] 来创建，这个参数用于同时进行多个时间判断时减少创建 [Calendar] 对象*/
fun Long.isYesterday(wee: Long = getWeeOfToday()) = (this + (24 * 60 * 60 * 1000)).isToday(wee)

/**判断是不是前天， [wee] 用作对比，应使用 [getWeeOfToday] 来创建，这个参数用于同时进行多个时间判断时减少创建 [Calendar] 对象*/
fun Long.isDayBeforeYesterday(wee: Long = getWeeOfToday()) = (this + (2 * 24 * 60 * 60 * 1000)).isToday(wee)

/**判断是不是一分钟以内*/
fun Long.isWithinOneMinute(cur: Long = timestampMillis()) = (cur - this) / 1000 < 60

/**判断是不是一小时以内*/
fun Long.isWithinOneHour(cur: Long = timestampMillis()) = (cur - this) / 1000 / 60 < 60

/**判断是不是一天以内*/
fun Long.isWithinHours(cur: Long = timestampMillis(), hours: Int) = (cur - this) / 1000 / 60 / 60 < hours

/**返回时间上的分钟差*/
fun Long.minutesDifference(cur: Long? = null) = ((cur ?: timestampMillis()) - this) / 1000 / 60

/**返回时间上的小时差*/
fun Long.hoursDifference(cur: Long? = null) = ((cur ?: timestampMillis()) - this) / 1000 / 60 / 60

/**
 * 格式化为具体描述：
 *
 *   - 刚刚：1分钟以内；
 *   - xx分钟前：1分钟-1个小时以内；
 *   - xx小时前：1小时-24小时以内；
 *   - 昨天09:35：24个小时-48个小时（可设置为显示出具体时间，也可不）；
 *   - 前天09:35：48个小时-72个小时（可设置为显示出具体时间，也可不）；
 *   - 09-23 09:35：当年的72小时后，当年的时间不显示年；
 *   - 2014-08-21 12:30：不在当年。
 */
fun Long.millisecondsToDetailDesc(): String {
    //对比的对象
    val thisCalendar = Calendar.getInstance()
    thisCalendar.timeInMillis = this

    //当前时间，精确到毫秒值
    val nowCalendar = Calendar.getInstance()
    val now = nowCalendar.timeInMillis

    //当前时间：用于对比今天、昨天、前天
    val wee = getWeeOfToday()

    return when {
        this.isWithinOneMinute(now) -> {
            ResourceUtils.getString(R.string.just)
        }
        this.isWithinOneHour(now) -> {
            ResourceUtils.getString(R.string.x_minutes_ago_mask, this.minutesDifference(now).toInt())
        }
        this.isToday(wee) -> {
            ResourceUtils.getString(R.string.x_hours_ago_mask, this.hoursDifference(now).toInt())
        }
        this.isYesterday(wee) -> {
            "${ResourceUtils.getString(R.string.yesterday)}${thisCalendar.get(Calendar.HOUR_OF_DAY).to2BitText()}:${thisCalendar.get(Calendar.MINUTE).to2BitText()}"
        }
        this.isDayBeforeYesterday(wee) -> {
            "${ResourceUtils.getString(R.string.the_day_before_yesterday)}${thisCalendar.get(Calendar.HOUR_OF_DAY).to2BitText()}:${thisCalendar.get(Calendar.MINUTE).to2BitText()}"
        }
        nowCalendar.get(Calendar.YEAR) == thisCalendar[Calendar.YEAR] -> {
            "${(thisCalendar.get(Calendar.MONTH) + 1).to2BitText()}-${thisCalendar.get(Calendar.DAY_OF_MONTH).to2BitText()} ${thisCalendar.get(Calendar.HOUR_OF_DAY).to2BitText()}:${thisCalendar.get(Calendar.MINUTE).to2BitText()}"
        }
        else -> {
            "${(thisCalendar.get(Calendar.YEAR)).to2BitText()}-${(thisCalendar.get(Calendar.MONTH) + 1).to2BitText()}-${thisCalendar.get(Calendar.DAY_OF_MONTH).to2BitText()} ${thisCalendar.get(Calendar.HOUR_OF_DAY).to2BitText()}:${thisCalendar.get(Calendar.MINUTE).to2BitText()}"
        }
    }
}