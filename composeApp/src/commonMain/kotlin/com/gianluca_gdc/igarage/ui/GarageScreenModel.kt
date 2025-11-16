package com.gianluca_gdc.igarage.ui

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.gianluca_gdc.igarage.remote.VehicleDatabasesRemoteDataSourceImpl
import com.gianluca_gdc.igarage.repository.VehicleRepository
import com.gianluca_gdc.igarage.repository.VehicleRepositoryImpl
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GarageScreenModel(private val vehicleRepository: VehicleRepository) : StateScreenModel<GarageUiState>(GarageUiState()) {
    init{
        screenModelScope.launch {
            try {
                mutableState.update { it.copy(isLoading = true) }
                runCatching { vehicleRepository.getVehicles() }
                    .onSuccess { list ->
                        mutableState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = null,
                                vehicles = list
                            )
                        }
                    }
                    .onFailure { e ->
                        mutableState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = e.message ?: "Failed to Load"
                            )
                        }

                    }
            }
        }

    }
    fun onAddVin(vin:String){

    }
    fun onRefresh(){

    }
}