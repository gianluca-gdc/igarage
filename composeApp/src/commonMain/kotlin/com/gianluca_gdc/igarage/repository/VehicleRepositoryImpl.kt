package com.gianluca_gdc.igarage.repository

import com.gianluca_gdc.igarage.model.Vehicle
import com.gianluca_gdc.igarage.model.VehicleWithMaintenance
import com.gianluca_gdc.igarage.remote.VehicleDatabasesRemoteDataSource
import com.gianluca_gdc.igarage.remote.toAverageMarketValue
import com.gianluca_gdc.igarage.remote.toMaintenanceTasks
import com.gianluca_gdc.igarage.remote.toVehicle

class VehicleRepositoryImpl(private val remote: VehicleDatabasesRemoteDataSource,
                            private val maintenanceRepository: MaintenanceRepository

): VehicleRepository {
    private val vehicles = mutableListOf<Vehicle>()

    override suspend fun getVehicles(): List<Vehicle> = vehicles

    override suspend fun getVehicleById(id: String): Vehicle? =
        vehicles.find { it.id == id }

    override suspend fun addVehicleByVin(vin: String) {
        val combined = fetchVehicleAndMaintenanceByVin(vin)

        vehicles.add(combined.vehicle)

        maintenanceRepository.cacheMaintenanceForVehicle(
            combined.vehicle.id,
            combined.maintenanceTasks
        )

    }

    override suspend fun getMarketValueForVehicle(vehicle:Vehicle): Vehicle {
        val dto = remote.getMarketValue(
            vin = vehicle.vin,
            currentMileage = vehicle.mileage
        )
        val newVehicle = dto.toAverageMarketValue(vehicle)
        vehicles.set(vehicles.indexOf(vehicle),newVehicle)
        return newVehicle
    }
    override suspend fun fetchVehicleAndMaintenanceByVin(
        vin: String
    ): VehicleWithMaintenance {
        val dto = remote.decodeVinWithMaintenance(vin)

        val vehicle = dto.data.toVehicle(vin)                    // your existing VIN mapper
        val tasks = dto.data.maintenance.toMaintenanceTasks(vehicle.id)  // your existing helper

        // Optionally: save vehicle + tasks into your in-memory list / DB here

        return VehicleWithMaintenance(vehicle, tasks)
    }

}