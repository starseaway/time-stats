package com.xinyi.timestats.model

import java.util.Locale
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

/**
 * 测量得到的耗时模型
 *
 * 以纳秒存储，支持转换为多种时间单位，并可与 Kotlin [Duration] 互转
 *
 * @property nanoTime 纳秒（ns）
 *
 * @author 新一
 * @date 2026/6/9 16:32
 */
@JvmInline
value class MeasureDuration(val nanoTime: Long) : Comparable<MeasureDuration> {

    companion object {

        @JvmStatic
        fun fromNanoseconds(ns: Long): MeasureDuration {
            return MeasureDuration(ns)
        }

        @JvmStatic
        fun fromMilliseconds(ms: Long): MeasureDuration {
            return MeasureDuration(
                ms * 1_000_000
            )
        }

        @JvmStatic
        fun fromSeconds(seconds: Long): MeasureDuration {
            return MeasureDuration(
                seconds * 1_000_000_000
            )
        }

        @JvmStatic
        fun fromDuration(duration: Duration): MeasureDuration {
            return MeasureDuration(
                duration.inWholeNanoseconds
            )
        }
    }

    /**
     * 纳秒（ns）
     */
    val ns: Long get() = nanoTime

    /**
     * 微秒（μs）
     */
    val us: Double get() = nanoTime / 1_000.0

    /**
     * 毫秒（ms）
     */
    val ms: Double get() = nanoTime / 1_000_000.0

    /**
     * 秒（s）
     */
    val seconds: Double get() = nanoTime / 1_000_000_000.0

    /**
     * 分钟（min）
     */
    val minutes: Double get() = seconds / 60

    /**
     * 小时（h）
     */
    val hours: Double get() = minutes / 60

    /**
     * 转为 Kotlin [Duration]
     */
    val duration: Duration get() = nanoTime.toDuration(DurationUnit.NANOSECONDS)

    override fun compareTo(other: MeasureDuration): Int {
        return nanoTime.compareTo(
            other.nanoTime
        )
    }

    operator fun plus(other: MeasureDuration): MeasureDuration {
        return MeasureDuration(
            nanoTime + other.nanoTime
        )
    }

    operator fun minus(other: MeasureDuration): MeasureDuration {
        return MeasureDuration(
            nanoTime - other.nanoTime
        )
    }

    /**
     * 格式化为纳秒
     */
    fun toNsString(): String = "${ns}ns"

    /**
     * 格式化为微秒
     */
    fun toUsString(): String = String.format(Locale.getDefault(), "%.3fμs", us)

    /**
     * 格式化为毫秒
     */
    fun toMsString(): String = String.format(Locale.getDefault(), "%.3fms", ms)

    /**
     * 格式化为秒
     */
    fun toSecondsString(): String = String.format(Locale.getDefault(), "%.3fs", seconds)

    /**
     * 格式化为分钟
     */
    fun toMinutesString(): String = String.format(Locale.getDefault(), "%.3fmin", minutes)

    /**
     * 格式化为小时
     */
    fun toHoursString(): String = String.format(Locale.getDefault(), "%.3fh", hours)

    /**
     * 智能格式化
     */
    fun toReadableString(): String {
        return when {
            nanoTime < 1_000L ->
                "${nanoTime}ns"

            nanoTime < 1_000_000L ->
                String.format(Locale.getDefault(), "%.3fμs", us)

            nanoTime < 1_000_000_000L ->
                String.format(Locale.getDefault(), "%.3fms", ms)

            nanoTime < 60_000_000_000L ->
                String.format(Locale.getDefault(), "%.3fs", seconds)

            nanoTime < 3_600_000_000_000L ->
                String.format(Locale.getDefault(), "%.3fmin", minutes)

            else ->
                String.format(Locale.getDefault(), "%.3fh", hours)
        }
    }

    override fun toString(): String {
        return toReadableString()
    }
}