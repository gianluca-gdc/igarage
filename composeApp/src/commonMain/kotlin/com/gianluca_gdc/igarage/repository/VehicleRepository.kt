package com.gianluca_gdc.igarage.repository

import com.gianluca_gdc.igarage.model.Vehicle
import com.gianluca_gdc.igarage.model.VehicleWithMaintenance

interface VehicleRepository {
    suspend fun getVehicles():List<Vehicle>
    suspend fun getVehicleById(id: String): Vehicle?
    suspend fun addVehicleByVin(vin: String)

    suspend fun getMarketValueForVehicle(vehicle:Vehicle):Vehicle?

    suspend fun fetchVehicleAndMaintenanceByVin(
        vin: String
    ): VehicleWithMaintenance
}