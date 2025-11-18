package com.gianluca_gdc.igarage.ui.garage

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.gianluca_gdc.igarage.repository.VehicleRepository
import com.gianluca_gdc.igarage.ui.garage.GarageUiState
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GarageScreenModel(private val vehicleRepository: VehicleRepository)
    : StateScreenModel<GarageUiState>(GarageUiState()) {
    init{

        screenModelScope.launch {
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
    fun onAddVin(vin:String){
        if(vin.length != 17 || vin.lowercase().contains('o') || !vin.all{it.isLetterOrDigit()}){
            mutableState.update { it.copy(
                isLoading = false,
                errorMessage = "Please Make Sure VIN is correct"
            ) }
            return
        }
        screenModelScope.launch {
            mutableState.update { it.copy(isLoading = true) }

            runCatching { vehicleRepository.addVehicleByVin(vin) }
                .onSuccess { mutableState.update { it.copy(
                    isLoading = false,
                    errorMessage = null,
                    vehicles = vehicleRepository.getVehicles()
                )
                    }
                }
                .onFailure{e ->
                    mutableState.update{it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Failed to Fetch VIN"
                    )}
                }
        }

    }
    fun onRefresh(){
        screenModelScope.launch {
            mutableState.update { it.copy(isLoading = true) }
            var updatedList = vehicleRepository.getVehicles()
            updatedList.forEach{vehicle ->
                vehicleRepository.getMarketValueForVehicle(vehicle)
            }
            mutableState.update { it.copy(
                isLoading = false,
                errorMessage = null,
                vehicles = updatedList
                )
            }
        }
    }


}