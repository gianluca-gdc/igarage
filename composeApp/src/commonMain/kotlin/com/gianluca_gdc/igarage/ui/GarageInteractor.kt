package com.gianluca_gdc.igarage.ui

import com.gianluca_gdc.igarage.repository.MaintenanceRepository
import com.gianluca_gdc.igarage.repository.VehicleRepository

class GarageInteractor(
    private val vehicleRepository: VehicleRepository,
    private val maintenanceRepository: MaintenanceRepository
) {
    suspend fun loadGarage(): GarageUiState{
        return try{
            val vehicles = vehicleRepository.getVehicles()
            GarageUiState(
                isLoading = false,
                vehicles = vehicles,
                errorMessage = null
            )
        }catch(e: Throwable){
            GarageUiState(
                isLoading = false,
                vehicles = emptyList(),
                errorMessage = e.message ?: "Something went wrong"
            )
        }
    }
    suspend fun loadVehicleDetail(vehicleId: String): VehicleDetailUiState{
        return try{
            val vehicle = vehicleRepository.getVehicleById(vehicleId)
                ?: return VehicleDetailUiState(
                    isLoading = false,
                    errorMessage = "Vehicle not found"
                )
            val tasks = maintenanceRepository.getMaintenanceForVehicle(vehicleId)
            val health = maintenanceRepository.getHealthForVehicle(vehicleId)
            VehicleDetailUiState(
                isLoading = false,
                vehicle = vehicle,
                maintenanceTasks = tasks,
                healthStatus = health,
                errorMessage = null
            )
        }catch(e:Throwable){
            VehicleDetailUiState(
                isLoading = false,
                errorMessage = e.message ?: "Something went wrong"
            )
        }
    }
}