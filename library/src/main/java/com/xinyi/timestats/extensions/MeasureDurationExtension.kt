@file:JvmName("MeasureDurationExtension")

package com.xinyi.timestats.extensions

import com.xinyi.timestats.model.MeasureDuration

/**
 * [MeasureDuration] 集合的统计分析扩展
 *
 * 适用于多次采样后的性能指标计算，例如压测、基准测试或 A/B 对比场景。
 *
 * 提供平均值、中位数以及 P50、P95、P99 等比例统计能力。
 *
 * @author 新一
 * @date 2026/6/15 19:04
 */

/**
 * 计算采样数据的算术平均耗时
 *
 * 注意：平均值容易受极端值影响；若存在偶发慢请求，建议结合 [median] 或 [p95] 一并观察。
 *
 * @return 所有采样耗时的算术平均值
 * @throws IllegalArgumentException 当集合为空时
 */
fun Collection<MeasureDuration>.average(): MeasureDuration {
    require(isNotEmpty()) { "Cannot compute average of empty collection" }
    val sum = sumOf { it.nanoTime }
    return MeasureDuration(sum / size)
}

/**
 * 计算采样数据的中位耗时
 *
 * 相比 [average]，中位数对极端值不敏感，更能反映「大多数请求」的实际体验。
 *
 * @return 中位耗时；偶数个样本时取中间两项的算术平均
 * @throws IllegalArgumentException 当集合为空时
 */
fun Collection<MeasureDuration>.median(): MeasureDuration {
    val sorted = map { it.nanoTime }.sorted()
    require(sorted.isNotEmpty()) { "Cannot compute median of empty collection" }
    val mid = sorted.size / 2
    return if (sorted.size % 2 == 0) {
        MeasureDuration((sorted[mid - 1] + sorted[mid]) / 2)
    } else {
        MeasureDuration(sorted[mid])
    }
}

/**
 * 计算指定比例的耗时阈值（内部采用线性插值法计算）
 *
 * 将所有采样按耗时从快到慢排列，找出「大约有多少比例的采样比某个值更快」对应的耗时。
 *
 * 参数 [percentile] 表示这个比例（百分比），例如 95 表示 P95：约 95% 的采样比结果更快。
 *
 * @param percentile 目标比例（百分比），取值范围 0..100；50 表示约 50% 的采样比结果更快
 * @return 对应比例的耗时阈值
 * @throws IllegalArgumentException 当集合为空，或 [percentile] 不在 0..100 范围内时
 */
fun Collection<MeasureDuration>.percentile(percentile: Double): MeasureDuration {
    require(percentile in 0.0..100.0) { "Percentile must be in 0..100, but was $percentile" }
    val sorted = map { it.nanoTime }.sorted()
    require(sorted.isNotEmpty()) { "Cannot compute percentile of empty collection" }
    if (sorted.size == 1) {
        return MeasureDuration(sorted[0])
    }

    val rank = percentile / 100.0 * (sorted.size - 1)
    val lower = rank.toInt()
    val upper = (lower + 1).coerceAtMost(sorted.size - 1)
    val weight = rank - lower
    val value = sorted[lower] + ((sorted[upper] - sorted[lower]) * weight).toLong()
    return MeasureDuration(value)
}

/**
 * 计算 P50 耗时
 *
 * P 表示 Percent（百分比），50 即 50%：约一半采样比该值更快，一半更慢。
 *
 * 结果等价于 [median] 的百分位表示。
 *
 * @return P50 耗时
 * @throws IllegalArgumentException 当集合为空时
 */
fun Collection<MeasureDuration>.p50(): MeasureDuration = percentile(50.0)

/**
 * 计算 P95 耗时
 *
 * 即 95%：约 95% 的采样比该值更快，约 5% 更慢。
 *
 * 常用于指标评估与性能告警，比平均值更能反映用户感知的 "慢请求" 上限。
 *
 * @return P95 耗时
 * @throws IllegalArgumentException 当集合为空时
 */
fun Collection<MeasureDuration>.p95(): MeasureDuration = percentile(95.0)

/**
 * 计算 P99 耗时
 *
 * 即 99%：约 99% 的采样比该值更快，约 1% 更慢。
 *
 * 用于观察最慢那一小撮请求的耗时，适合排查偶发卡顿或 GC、锁竞争等长尾问题。
 *
 * @return P99 耗时
 * @throws IllegalArgumentException 当集合为空时
 */
fun Collection<MeasureDuration>.p99(): MeasureDuration = percentile(99.0)