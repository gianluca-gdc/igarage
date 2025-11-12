package com.gianluca_gdc.igarage.model
import kotlinx.datetime.LocalDate
data class MaintenanceTask(
    val id: String,
    val vehicleId: String,

    val name: String,
    val description: String?,
    val category: TaskCategory,
    val status: TaskStatus,

    val dueMileage: Int?,
    val lastCompletedMileage: Int?,
    val dueDate: LocalDate?,
    val lastCompletedDate: LocalDate?

)
fun MaintenanceTask.withStatus(currentMileage: Int?): MaintenanceTask{
    if(currentMileage == null || dueMileage == null) return this
    val thresholdSoon = 1000 //miles
    val newStatus = when{
        lastCompletedMileage != null && lastCompletedMileage >= dueMileage -> TaskStatus.COMPLETED
        currentMileage > dueMileage -> TaskStatus.OVERDUE
        (dueMileage - currentMileage) <= thresholdSoon -> TaskStatus.DUE_SOON
        else -> TaskStatus.OK
    }
    return copy(status = newStatus)
}
enum class TaskCategory {
    CRITICAL,   // brakes, timing belt, etc.
    MAJOR,      // coolant, transmission fluid, etc.
    MINOR       // cabin filter, wipers, etc.
}

enum class TaskStatus {
    OVERDUE,
    DUE_SOON,
    OK,
    COMPLETED
}
