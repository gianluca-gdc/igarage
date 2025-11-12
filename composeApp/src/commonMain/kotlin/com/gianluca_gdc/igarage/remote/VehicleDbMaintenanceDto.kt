package com.gianluca_gdc.igarage.remote

import com.gianluca_gdc.igarage.model.MaintenanceTask
import com.gianluca_gdc.igarage.model.TaskCategory
import com.gianluca_gdc.igarage.model.TaskStatus
import kotlinx.serialization.Serializable

@Serializable
data class VehicleDbMaintenanceDto(
    val mileage: Int,
    val conditions: List<MaintenanceConditionDto>
)
@Serializable
data class MaintenanceConditionDto(
    val status: String,
    val description: List<String>
)

fun List<VehicleDbMaintenanceDto>.toMaintenanceTasks(vehicleId: String): List<MaintenanceTask> {
    return flatMap { dto ->
        val dueMileage = dto.mileage
        dto.conditions.flatMap { condition ->
            condition.description.map { serviceName ->
                MaintenanceTask(
                    id = "$dueMileage-${condition.status}-${serviceName.hashCode()}",
                    vehicleId = vehicleId,
                    name = serviceName,
                    description = "Recommended under ${condition.status} conditions",
                    category = when (condition.status.lowercase()) {
                        "severe" -> TaskCategory.MAJOR
                        else -> TaskCategory.MINOR
                    },
                    status = TaskStatus.OK,
                    dueMileage = dueMileage,
                    lastCompletedMileage = null,
                    dueDate = null,
                    lastCompletedDate = null
                )
            }
        }
    }
}

