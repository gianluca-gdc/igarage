package com.gianluca_gdc.igarage.model

data class HealthStatus(
    val score: Int,
    val level: HealthLevel,

    val criticalOverdue: Int,
    val majorOverdue: Int,
    val minorOverdue: Int,
    val upcomingSoon: Int
)
enum class HealthLevel {
    EXCELLENT,
    GOOD,
    FAIR,
    POOR
}