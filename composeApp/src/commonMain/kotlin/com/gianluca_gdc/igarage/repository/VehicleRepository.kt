package com.gianluca_gdc.igarage.repository

import com.gianluca_gdc.igarage.model.Vehicle

interface VehicleRepository {
    suspend fun getVehicles():List<Vehicle>
    suspend fun getVehicleById(id: String): Vehicle?
    suspend fun addVehicleByVin(vin: String)
}