# TimeStats 耗时统计库

<div align="center">
  <img src="time-stats-logo.svg" width="500" alt="time-stats-logo">
</div>

![Version](https://img.shields.io/badge/version-1.0.0-blue)
![License](https://img.shields.io/badge/license-Apache%202.0-green)
![API](https://img.shields.io/badge/API-19%2B-brightgreen)

> Every millisecond tells a story.
>
> 每一次优化都始于测量。

## 一、简介

在性能优化领域，最容易被忽略的问题往往不是代码本身，而是缺少可靠的数据。

开发过程中，我们经常需要回答这样的问题：

 - 一个方法到底执行了多久？
 - 某段逻辑是否存在性能瓶颈？
 - 优化之后到底提升了多少？
 - 多次执行后的平均耗时是多少？
 - 极端情况下的耗时表现如何？

然而在实际项目中，这类统计往往通过大量重复代码完成：

```kotlin
val start = System.nanoTime()

task()

val cost = System.nanoTime() - start
```

随着统计场景增多，时间单位换算、异常处理、协程支持、统计分析等问题也会逐渐出现。

`TimeStats` 诞生的目的，就是把这些零散且重复的工作统一起来。

它专注于耗时统计这一件事情，从简单的代码执行时间测量，到平均值、中位数、P95、P99 等性能指标分析。

`TimeStats` 提供统一的时间模型与简洁的 API，帮助开发者专注于性能优化本身。

---

## 二、特性

- 基于 System.nanoTime() 实现高精度时间测量
- 支持同步代码与协程代码耗时统计
- 支持执行结果与耗时同时返回
- 支持异常安全统计
- 支持 Kotlin Duration 互操作
- 支持 Average（平均值）、Median（中位数）统计分析
- 支持 P50、P95、P99 百分位统计分析
- 零第三方依赖、零运行时依赖

## 三、SDK 适用范围

* Android SDK 版本：Min SDK 19（Android 4.4）及以上

---

## 四、集成方式

### 1. 添加仓库

```groovy
maven {
    url 'https://jitpack.io'
}
```

### 2. 添加依赖

Groovy：

```groovy
dependencies {
    implementation 'com.github.starseaway:time-stats:1.0.0'
}
```

Kotlin DSL：

```kotlin
dependencies {
    implementation("com.github.starseaway:time-stats:1.0.0")
}
```

## 五、快速开始

---

## 六、设计理念

时间本身无法被优化，能够被优化的，只有时间消耗背后的代码。

而在优化之前，首先需要知道时间究竟流向了哪里。

`TimeStats` 希望成为这个过程中的第一步。

## 七、版本记录

### V1.0.0 (2026-06-15)

- 首次发布，提供同步与协程耗时统计
- 提供 MeasureDuration 时间模型
- 支持 Average、Median、P95、P99 等统计分析