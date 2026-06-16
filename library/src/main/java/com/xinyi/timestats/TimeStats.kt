package com.xinyi.timestats

import com.xinyi.timestats.model.MeasureDuration
import com.xinyi.timestats.model.MeasureResult

/**
 * TimeStats 核心测量入口
 *
 * 提供同步代码、协程代码以及对象相关操作的耗时统计能力。
 *
 * 所有耗时均基于 [System.nanoTime] 计算，统计结果统一使用 [MeasureDuration] 或 [MeasureResult] 表示。
 *
 * @author 新一
 * @date 2026/6/9 11:19
 */
object TimeStats {

    /**
     * 测量代码执行耗时
     *
     * @param block 执行逻辑
     * @return 耗时信息
     */
    @JvmStatic
    inline fun measureTime(block: () -> Unit): MeasureDuration {
        val start = System.nanoTime()
        block()
        return MeasureDuration(System.nanoTime() - start)
    }

    /**
     * 测量代码执行耗时，并返回执行结果
     *
     * @param block 执行逻辑
     */
    @JvmStatic
    inline fun <R> measureTimeWithResult(block: () -> R): MeasureResult<R> {
        val start = System.nanoTime()
        val result = block()
        return MeasureResult(
            value = result,
            duration = MeasureDuration(System.nanoTime() - start)
        )
    }

    /**
     * 测量对象相关操作执行耗时
     *
     * @param block 执行逻辑
     *
     * 示例代码：
     * ```kotlin
     * val list = mutableListOf<Int>()
     *
     * val duration = list.measureTime {
     *     repeat(100_000) { index ->
     *         it.add(index)
     *     }
     * }
     *
     * println("耗时：${duration.toMsString()}")
     * ```
     */
    @JvmStatic
    inline fun <T> T.measureTime(block: (T) -> Unit): MeasureDuration {
        val start = System.nanoTime()
        block(this)
        return MeasureDuration(System.nanoTime() - start)
    }

    /**
     * 测量对象相关操作执行耗时，并返回执行结果
     *
     * @param block 执行逻辑
     *
     * 示例代码：
     * ```kotlin
     * val numbers = (1..100_000).toList()
     *
     * val result = numbers.measureTimeWithResult {
     *     it.sum()
     * }
     *
     * println("sum = ${result.value}")
     * println("duration = ${result.duration}")
     * ```
     */
    @JvmStatic
    inline fun <T, R> T.measureTimeWithResult(block: (T) -> R): MeasureResult<R> {
        val start = System.nanoTime()
        val result = block(this)
        return MeasureResult(
            value = result,
            duration = MeasureDuration(System.nanoTime() - start)
        )
    }

    /**
     * 测量协程代码执行耗时
     *
     * @param block 执行逻辑
     */
    @JvmStatic
    suspend inline fun measureSuspendTime(block: suspend () -> Unit): MeasureDuration {
        val start = System.nanoTime()
        block()
        return MeasureDuration(System.nanoTime() - start)
    }

    /**
     * 测量对象相关协程操作执行耗时
     *
     * @param block 执行逻辑
     */
    @JvmStatic
    suspend inline fun <T> T.measureSuspendTime(block: suspend (T) -> Unit): MeasureDuration {
        val start = System.nanoTime()
        block(this)
        return MeasureDuration(System.nanoTime() - start)
    }

    /**
     * 测量协程代码执行耗时，并返回执行结果
     *
     * @param block 执行逻辑
     */
    @JvmStatic
    suspend inline fun <R> measureSuspendTimeWithResult(block: suspend () -> R): MeasureResult<R> {
        val start = System.nanoTime()
        val result = block()
        return MeasureResult(
            value = result,
            duration = MeasureDuration(System.nanoTime() - start)
        )
    }

    /**
     * 测量对象相关协程操作执行耗时，并返回执行结果
     *
     * @param block 执行逻辑
     */
    @JvmStatic
    suspend inline fun <T, R> T.measureSuspendTimeWithResult(block: suspend (T) -> R): MeasureResult<R> {
        val start = System.nanoTime()
        val result = block(this)
        return MeasureResult(
            value = result,
            duration = MeasureDuration(System.nanoTime() - start)
        )
    }
}