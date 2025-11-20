package com.gianluca_gdc.igarage.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VehicleDbVinWithMaintenanceDto(
    val data: VehicleDbVinWithMaintenanceDataDto
)
