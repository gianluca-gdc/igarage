package com.gianluca_gdc.igarage.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gianluca_gdc.igarage.model.Vehicle
import androidx.compose.foundation.lazy.items

@Composable
fun GarageScreen(
    interactor: GarageInteractor,
    onVehicleClick: (String) -> Unit = {}
){
    var uiState by remember { mutableStateOf(GarageUiState()) }
    LaunchedEffect(Unit){
        uiState = interactor.loadGarage()
    }
    when{
        uiState.isLoading ->{
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center){
                CircularProgressIndicator()
            }
        }
        uiState.errorMessage != null ->{
            Box(Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center){
                Text("Error: ${uiState.errorMessage}")
            }
        }
        else ->{
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)

            ){
                items(uiState.vehicles) { vehicle ->
                    VehicleCard(
                        vehicle = vehicle,
                        onClick = { onVehicleClick(vehicle.id) }
                    )
                }
            }
        }
    }

}

@Composable
fun VehicleCard(vehicle: Vehicle, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation()
    ) {
        Column(Modifier.padding(16.dp)){
                Text(
                    text = vehicle.nickname
                        ?: "${vehicle.year ?: ""} ${vehicle.make ?: ""} ${vehicle.model ?: ""}",
                    style = MaterialTheme.typography.titleMedium
                )
                vehicle.mileage?.let {
                    Text(
                        "Mileage $it mi",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                vehicle.estimatedValue?.let { value ->
                    Text(
                        text = "Est. Value: $ ${"%,d".format(value)}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                vehicle.valueChange6M?.let { change ->
                    val arrow = if (change >= 0) "▲" else "▼"
                    Text(
                        text = "6 mo: $arrow ${"%.1f".format(change)}%",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
    }
}
