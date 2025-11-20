package com.gianluca_gdc.igarage.model

data class VehicleWithMaintenance(
    val vehicle: Vehicle,
    val maintenanceTasks: List<MaintenanceTask>
)
