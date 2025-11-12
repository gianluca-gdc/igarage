package com.gianluca_gdc.igarage.ui

import com.gianluca_gdc.igarage.model.Vehicle

data class GarageUiState(
    val isLoading: Boolean = true,
    val vehicles: List<Vehicle> = emptyList(),
    val errorMessage: String? = null
)
