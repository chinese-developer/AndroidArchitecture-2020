package com.android.cache

const val TAG = "===CACHE==="

// 2 * TIME_HOUR 保存一小时
const val TIME_HOUR = 60 * 60

// 2 * TIME_DAY 保存两天
const val TIME_DAY = TIME_HOUR * 24

const val MAX_SIZE = 1000 * 1000 * 50 // 50 mb
const val MAX_COUNT = Int.MAX_VALUE // 不限制存放数据的数量
