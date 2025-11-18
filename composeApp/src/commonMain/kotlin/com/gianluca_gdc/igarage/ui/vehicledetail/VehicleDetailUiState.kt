package com.gianluca_gdc.igarage.ui.vehicledetail

import com.gianluca_gdc.igarage.model.HealthStatus
import com.gianluca_gdc.igarage.model.MaintenanceTask
import com.gianluca_gdc.igarage.model.Vehicle

data class VehicleDetailUiState(
    val isLoading: Boolean = true,
    val vehicle: Vehicle? = null,
    val maintenanceTasks: List<MaintenanceTask> = emptyList(),
    val healthStatus: HealthStatus? = null,
    val errorMessage: String? = null

    )