package com.gianluca_gdc.igarage.repository

import com.gianluca_gdc.igarage.model.HealthLevel
import com.gianluca_gdc.igarage.model.HealthStatus
import com.gianluca_gdc.igarage.model.MaintenanceTask
import com.gianluca_gdc.igarage.model.TaskCategory
import com.gianluca_gdc.igarage.model.TaskStatus
import com.gianluca_gdc.igarage.model.withStatus

class MaintenanceRepositoryImpl : MaintenanceRepository {

    private val tasksByVehicleId = mutableMapOf<String, List<MaintenanceTask>>()

    override suspend fun getMaintenanceForVehicle(vehicleId: String): List<MaintenanceTask> {
        return tasksByVehicleId[vehicleId] ?: emptyList()
    }


    override suspend fun getHealthForVehicle(vehicleId: String): HealthStatus {
        var score = 100
        val tasks = getMaintenanceForVehicle(vehicleId)
        val criticalOverdue = tasks.count { it.category == TaskCategory.CRITICAL && it.status == TaskStatus.OVERDUE }
        val majorOverdue = tasks.count{it.category == TaskCategory.MAJOR && it.status == TaskStatus.OVERDUE }
        val minorOverdue = tasks.count{it.category == TaskCategory.MINOR && it.status == TaskStatus.OVERDUE}
        val upcomingSoon = tasks.count{it.status == TaskStatus.DUE_SOON}
        if(criticalOverdue > 0){
            score -= 25 * criticalOverdue
        }
        if(majorOverdue > 0){
            score -= 15 * majorOverdue
        }
        if(minorOverdue > 0){
            score -= 10 * minorOverdue
        }
        if(upcomingSoon > 0){
            score -= upcomingSoon
        }
        val healthLevel = when {
            score >= 85 -> HealthLevel.EXCELLENT
            score >= 70 -> HealthLevel.GOOD
            score >= 50 -> HealthLevel.FAIR
            else -> HealthLevel.POOR
        }
        return HealthStatus(score,healthLevel,criticalOverdue,majorOverdue,minorOverdue,upcomingSoon)
    }

    override suspend fun cacheMaintenanceForVehicle(
        vehicleId: String,
        tasks: List<MaintenanceTask>
    ) {
        tasksByVehicleId[vehicleId] = tasks
    }

}