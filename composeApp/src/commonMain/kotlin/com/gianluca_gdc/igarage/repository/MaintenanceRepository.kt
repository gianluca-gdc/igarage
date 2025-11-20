package com.gianluca_gdc.igarage.repository

import com.gianluca_gdc.igarage.model.HealthStatus
import com.gianluca_gdc.igarage.model.MaintenanceTask

interface MaintenanceRepository {
    suspend fun getMaintenanceForVehicle(vehicleId:String):List<MaintenanceTask>
    suspend fun getHealthForVehicle(vehicleId: String): HealthStatus
    suspend fun cacheMaintenanceForVehicle(
        vehicleId: String,
        tasks: List<MaintenanceTask>
    )
}