package com.gianluca_gdc.igarage.repository

import com.gianluca_gdc.igarage.model.HealthLevel
import com.gianluca_gdc.igarage.model.HealthStatus
import com.gianluca_gdc.igarage.model.MaintenanceTask
import com.gianluca_gdc.igarage.model.TaskCategory
import com.gianluca_gdc.igarage.model.TaskStatus
import kotlinx.datetime.LocalDate

class FakeMaintenanceRepository: MaintenanceRepository {
    override suspend fun getMaintenanceForVehicle(vehicleId: String): List<MaintenanceTask> {
        return listOf(
            MaintenanceTask(
                id = "task_oil",
                vehicleId = vehicleId,
                name = "Engine oil & filter",
                description = "Use 5W-30 synthetic, replace crush washer.",
                category = TaskCategory.CRITICAL,
                status = TaskStatus.OVERDUE,
                dueMileage = 81000,
                lastCompletedMileage = 76000,
                dueDate = null,
                lastCompletedDate = LocalDate(2024, 6, 1)
            ),
            MaintenanceTask(
                id = "task_brake_fluid",
                vehicleId = vehicleId,
                name = "Brake fluid flush",
                description = "Flush with DOT 4, bleed corners in sequence.",
                category = TaskCategory.MAJOR,
                status = TaskStatus.DUE_SOON,
                dueMileage = 85000,
                lastCompletedMileage = 55000,
                dueDate = null,
                lastCompletedDate = LocalDate(2022, 5, 10)
            ),
            MaintenanceTask(
                id = "task_cabin_filter",
                vehicleId = vehicleId,
                name = "Cabin air filter",
                description = null,
                category = TaskCategory.MINOR,
                status = TaskStatus.OK,
                dueMileage = 90000,
                lastCompletedMileage = 78000,
                dueDate = null,
                lastCompletedDate = LocalDate(2024, 3, 20)
            )
        )
    }

    override suspend fun getHealthForVehicle(vehicleId: String): HealthStatus {
        return HealthStatus(
            score = 25,
            level = HealthLevel.POOR,
            criticalOverdue = 1,
            majorOverdue = 0,
            minorOverdue = 0,
            upcomingSoon = 1
        )
    }
}