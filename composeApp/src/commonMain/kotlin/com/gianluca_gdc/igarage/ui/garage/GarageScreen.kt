package com.gianluca_gdc.igarage.ui.garage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardBackspace
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import coil3.compose.AsyncImage
import com.gianluca_gdc.igarage.ServiceLocator
import com.gianluca_gdc.igarage.model.Vehicle
import com.gianluca_gdc.igarage.ui.garage.GarageScreenModel
import com.gianluca_gdc.igarage.ui.garage.GarageUiState
import com.gianluca_gdc.igarage.ui.vehicledetail.VehicleDetailScreen
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.abs
import kotlin.math.round

private fun formatCommas(value: Int): String {
    val s = value.toString()
    val withCommas = s.reversed().chunked(3).joinToString(",").reversed()
    return withCommas
}

private fun formatOneDecimal(value: Float): String {
    // round to 1 decimal without JVM String.format
    val rounded = round(value * 10f) / 10f
    val text = rounded.toString()
    return if (text.contains('.')) {
        val parts = text.split('.')
        val frac = parts.getOrNull(1) ?: "0"
        parts[0] + "." + frac.padEnd(1, '0').take(1)
    } else text + ".0"
}
@Preview(widthDp = 375, heightDp = 667)
@Composable
fun GarageScreenPreview() {
    val sampleVehicle1 = Vehicle(
        id = "1",
        vin = "WBAPH7G50BNM56522",
        nickname = "E90",
        year = 2011,
        make = "BMW",
        model = "328i",
        trim = "i Sedan Manual",
        mileage = 78000,
        imageUrl = "https://www.pngmart.com/files/22/BMW-E90-PNG-File.png",
        estimatedValue = 12000,
        valueChange6M = 3.5f,
        privatePartyValue = null,
        tradeInValue = null,
        dealerValue = null
    )
    val sampleVehicle2 = Vehicle(
        id = "2",
        vin = "WBAPH7G50BNM56532",
        nickname = "Drift 240",
        year = 1997,
        make = "Nissan",
        model = "240sx",
        trim = "base",
        mileage = 165000,
        imageUrl = null,
        estimatedValue = 19000,
        valueChange6M = -10.0f,
        privatePartyValue = null,
        tradeInValue = null,
        dealerValue = null
    )
    val sampleVehicle3 = Vehicle(
        id = "3",
        vin = "WBAPH7G50BNM56422",
        nickname = "Work Truck",
        year = 2011,
        make = "Ford",
        model = "F150",
        trim = "base",
        mileage = 43500,
        imageUrl = "https://p7.hiclipart.com/preview/233/5/126/5bbc277c87b62.jpg",
        estimatedValue = 35000,
        valueChange6M = 8.4f,
        privatePartyValue = null,
        tradeInValue = null,
        dealerValue = null
    )

    val previewState = GarageUiState(
        isLoading = false,
        errorMessage = null,
        vehicles = listOf(sampleVehicle1, sampleVehicle2, sampleVehicle3)
    )

    GarageScreenContent(
        state = previewState,
        onRefresh = {},
        onAddVin = {},
        onVehicleClick = {}
    )
}
object GarageScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current

        val serviceLocator = remember {
            ServiceLocator.init("1234", useSandbox = true)
            ServiceLocator
        }
        val model = rememberScreenModel { GarageScreenModel(ServiceLocator.vehicleRepository) }
        val state by model.state.collectAsState()

        GarageScreenContent(
            state = state,
            onRefresh = { model.onRefresh() },
            onAddVin = { vin -> model.onAddVin(vin) },
            onVehicleClick = { vehicleId ->
                navigator?.push(VehicleDetailScreen(vehicleId))
            }
        )
    }
}

@Composable
fun GarageScreenContent(
    state: GarageUiState,
    onRefresh: () -> Unit,
    onAddVin: (String) -> Unit,
    onVehicleClick: (String) -> Unit
) {
    var isVinBoxVisible by remember { mutableStateOf(false) }
    var textInput by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    text = "iGarage"
                )
                if (!state.isLoading) {
                    IconButton(onClick = { onRefresh() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh"
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            if (!state.isLoading) {
                Row(
                    Modifier
                        .padding(start = 30.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = if (isVinBoxVisible) {
                        Arrangement.Start
                    } else {
                        Arrangement.End
                    }
                ) {
                    FloatingActionButton(
                        containerColor = Black,
                        contentColor = White,
                        onClick = { isVinBoxVisible = !isVinBoxVisible }
                    ) {
                        if (!isVinBoxVisible) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "add"
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.KeyboardBackspace,
                                contentDescription = "back"
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        if (state.isLoading) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        } else if (state.errorMessage != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(state.errorMessage)
                Button(onClick = { onRefresh() }) {
                    Text("Retry")
                }
            }
        } else if (state.vehicles.isEmpty()) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Center
            ) {
                if (!isVinBoxVisible) {
                    Text(
                        "Your Garage is Empty\n Please Add a Vehicle to your Garage",
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(vertical = 10.dp),
                        textAlign = TextAlign.Center
                    )
                }
                if (isVinBoxVisible) {
                    Row {
                        OutlinedTextField(
                            value = textInput,
                            onValueChange = { newText -> textInput = newText },
                            placeholder = { Text("Enter Your Vin...") },
                            modifier = Modifier
                                .padding(10.dp)
                                .fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(onDone = {
                                if (textInput.length == 17) {
                                    onAddVin(textInput)
                                }
                            })
                        )
                    }
                }
            }
        } else {
            if (!isVinBoxVisible) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.vehicles) { vehicle ->
                        VehicleCard(
                            vehicle = vehicle,
                            onClick = { onVehicleClick(vehicle.id) }
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    OutlinedTextField(
                        value = textInput,
                        onValueChange = { newText -> textInput = newText },
                        placeholder = { Text("Enter your Vin") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            if (textInput.length == 17) {
                                onAddVin(textInput)
                            }
                        })
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
        Column {
            // IMAGE SECTION
            if (vehicle.imageUrl != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp) // tweak as you like
                ) {
                    AsyncImage(
                        model = vehicle.imageUrl,
                        contentDescription = "${vehicle.make} ${vehicle.model}",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
        Column(Modifier.padding(16.dp)) {
            Text(
                text = vehicle.nickname
                    ?: "${vehicle.year ?: ""} ${vehicle.make ?: ""} ${vehicle.model ?: ""}",
                style = MaterialTheme.typography.titleMedium
            )
            if(vehicle.nickname != null){
                Text(
                    text = "${vehicle.year} ${vehicle.make} ${vehicle.model}",
                    style = MaterialTheme.typography.titleSmall
                )
            }
            vehicle.mileage?.let {
                Text(
                    "Mileage: ${formatCommas(it)} mi",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            vehicle.estimatedValue?.let { value ->
                Text(
                    text = "Est. Value: $${formatCommas(value)}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            vehicle.valueChange6M?.let { change ->
                val arrow = if (change >= 0f) "▲" else "▼"
                val arrowColor = if (change >= 0f) Color(0xFF4CAF50) else Color(0xFFF44336)
                Text(
                    text = buildAnnotatedString {
                        append("6 mo: ")

                        // colored arrow
                        withStyle(SpanStyle(color = arrowColor)) {
                            append(arrow)
                        }

                        append(" ")

                        // normal text
                        append("${formatOneDecimal(abs(change))}%")
                    },
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
