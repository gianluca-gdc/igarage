package com.gianluca_gdc.igarage.ui.vehicledetail

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.gianluca_gdc.igarage.model.MaintenanceTask
import com.gianluca_gdc.igarage.model.Vehicle
import com.gianluca_gdc.igarage.model.withStatus
import com.gianluca_gdc.igarage.repository.MaintenanceRepository
import com.gianluca_gdc.igarage.repository.VehicleRepository
import com.gianluca_gdc.igarage.ui.garage.GarageUiState
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VehicleDetailScreenModel(
    private val vehicleRepository: VehicleRepository,
    private val maintenanceRepository: MaintenanceRepository,
    private val vehicleId: String
): StateScreenModel<VehicleDetailUiState>(VehicleDetailUiState()) {
    init {

        screenModelScope.launch {
            mutableState.update { it.copy(isLoading = true) }

            val vehicle = try {
                vehicleRepository.getVehicleById(vehicleId)
                    ?: throw IllegalStateException("Vehicle not found")
            } catch (e: Exception) {
                mutableState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Failed to grab vehicle"
                    )
                }
                return@launch
            }

            val maintenanceTasks = maintenanceRepository.getMaintenanceForVehicle(vehicleId)
            val health = maintenanceRepository.getHealthForVehicle(vehicleId)

            mutableState.update {
                it.copy(
                    isLoading = false,
                    vehicle = vehicle,
                    maintenanceTasks = maintenanceTasks,
                    healthStatus = health,
                    errorMessage = null
                )
            }
        }
    }
}