package com.gianluca_gdc.igarage.repository

import com.gianluca_gdc.igarage.model.HealthStatus
import com.gianluca_gdc.igarage.model.MaintenanceTask
import com.gianluca_gdc.igarage.model.withStatus
import com.gianluca_gdc.igarage.remote.VehicleDatabasesRemoteDataSource
import com.gianluca_gdc.igarage.remote.toMaintenanceTasks
import com.gianluca_gdc.igarage.repository.VehicleRepositoryImpl

class MaintenanceRepositoryImpl(
    private val vehicleRepository: VehicleRepository,
    private val remote: VehicleDatabasesRemoteDataSource
) : MaintenanceRepository {
    override suspend fun getMaintenanceForVehicle(vehicleId: String): List<MaintenanceTask> {
        val vehicle = vehicleRepository.getVehicleById(vehicleId) ?: return emptyList()
        val vin = vehicle.vin
        val mileage = vehicle.mileage

        val remoteDtos = remote.getMaintenanceByVin(vin, mileage)
        val tasks = remoteDtos.toMaintenanceTasks(vehicleId)

        return tasks.map { it.withStatus(mileage) }
    }


    override suspend fun getHealthForVehicle(vehicleId: String): HealthStatus {
        TODO("Not yet implemented")
    }

}