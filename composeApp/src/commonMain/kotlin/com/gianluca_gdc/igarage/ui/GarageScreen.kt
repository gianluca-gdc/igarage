package com.gianluca_gdc.igarage.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import coil3.compose.AsyncImagePainter
import com.gianluca_gdc.igarage.ServiceLocator
import com.gianluca_gdc.igarage.model.Vehicle

private fun formatCurrency(value: Int): String {
    val s = value.toString()
    val withCommas = s.reversed().chunked(3).joinToString(",").reversed()
    return "$" + withCommas
}

private fun formatOneDecimal(value: Float): String {
    // round to 1 decimal without JVM String.format
    val rounded = kotlin.math.round(value * 10f) / 10f
    val text = rounded.toString()
    return if (text.contains('.')) {
        val parts = text.split('.')
        val frac = parts.getOrNull(1) ?: "0"
        parts[0] + "." + frac.padEnd(1, '0').take(1)
    } else text + ".0"
}

object GarageScreen : Screen {
    @Composable
    override fun Content() {
        // show a simple list placeholder + click → push detail
        val navigator = LocalNavigator.current
        // TODO: render vehicles; on click: navigator?.push(VehicleDetailScreen(id))
        val model = rememberScreenModel { GarageScreenModel(ServiceLocator.vehicleRepository) }
        val state by model.state.collectAsState()
        if (state.isLoading) {
            CircularProgressIndicator(Modifier)
        } else if (state.errorMessage != null) {
            Column {
                Text(state.errorMessage!!)
                Button(onClick = {TODO()}) {
                    Text("Retry")
                }
            }

        } else if (state.vehicles.isEmpty()) {
            var textInput by remember { mutableStateOf("") }

            Column {
                Text("Your Garage is Empty\n Please Add a Vehicle to your Garage")
                Row {
                    OutlinedTextField(
                        value = textInput,
                        onValueChange = {newText -> textInput = newText},
                        placeholder = {Text("Enter your Vin")},
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(onClick = { TODO() }) {
                        Text("+")
                    }
                }
            }
        } else {
            var textInput by remember { mutableStateOf("") }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)

            ){
                items(state.vehicles) { vehicle ->
                    VehicleCard(
                        vehicle = vehicle,
                        onClick = { navigator?.push(VehicleDetailScreen(vehicle.id)) }
                    )
                }


            }
            Row {
                OutlinedTextField(
                    value = textInput,
                    onValueChange = {newText -> textInput = newText},
                    placeholder = {Text("Enter your Vin")},
                    modifier = Modifier.fillMaxWidth()
                )
                Button(onClick = { TODO() }) {
                    Text("+")
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
        Column(Modifier.padding(16.dp)) {
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
                    text = "Est. Value: ${formatCurrency(value)}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            vehicle.valueChange6M?.let { change ->
                val arrow = if (change >= 0f) "▲" else "▼"
                Text(
                    text = "6 mo: $arrow ${formatOneDecimal(change)}%",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
