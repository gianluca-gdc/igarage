package com.gianluca_gdc.igarage.repository

import com.gianluca_gdc.igarage.model.Vehicle
import com.gianluca_gdc.igarage.remote.VehicleDatabasesRemoteDataSource
import com.gianluca_gdc.igarage.remote.toVehicle

class VehicleRepositoryImpl(private val remote: VehicleDatabasesRemoteDataSource
): VehicleRepository {
    private val vehicles = mutableListOf<Vehicle>()

    override suspend fun getVehicles(): List<Vehicle> = vehicles

    override suspend fun getVehicleById(id: String): Vehicle? =
        vehicles.find { it.id == id }

    override suspend fun addVehicleByVin(vin: String) {
        val dto = remote.decodeVin(vin)
        vehicles.add(dto.toVehicle(vin))
    }
}