package com.xinyi.timestats.model

/**
 * 带执行结果的时间统计结果
 *
 * @param value 执行结果
 * @param duration 耗时信息
 *
 * @author 新一
 * @date 2026/6/9 16:46
 */
data class MeasureResult<T>(val value: T, val duration: MeasureDuration)