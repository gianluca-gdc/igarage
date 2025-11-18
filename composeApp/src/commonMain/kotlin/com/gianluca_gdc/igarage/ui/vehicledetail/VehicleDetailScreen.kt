package com.gianluca_gdc.igarage.ui.vehicledetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.gianluca_gdc.igarage.ServiceLocator
import com.gianluca_gdc.igarage.model.HealthLevel
import com.gianluca_gdc.igarage.model.HealthStatus
import com.gianluca_gdc.igarage.model.MaintenanceTask
import com.gianluca_gdc.igarage.model.Vehicle

data class VehicleDetailScreen(val vehicleId: String) : Screen {
    @Composable
    override fun Content() {
            val navigator = LocalNavigator.current
            val serviceLocator = remember {
                ServiceLocator.init("1234", useSandbox = true)
                ServiceLocator
            }
            """val model by rememberScreenModel { VehicleDetailScreenModel() }
            LaunchedEffect(vehicleId) {
                uiState = interactor.loadVehicleDetail(vehicleId)
            }

            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                uiState.errorMessage != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Error: ${uiState.errorMessage}")
                    }
                }

                else -> {
                    val vehicle: Vehicle? = uiState.vehicle
                    val tasks: List<MaintenanceTask> = uiState.maintenanceTasks
                    val health = uiState.healthStatus

                    // You can redesign all of this however you want:
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = {
                                    Text(
                                        vehicle?.nickname
                                            ?: "${vehicle?.year ?: ""} ${vehicle?.make ?: ""} ${vehicle?.model ?: ""}"
                                    )
                                },
                                navigationIcon = {
                                    IconButton(onClick = onBack) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                            contentDescription = "Back"
                                        )
                                    }
                                }
                            )
                        }
                    ) { innerPadding ->
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            item {
                                HealthCard(uiState.healthStatus)
                            }
                            items(tasks) { task ->
                                Text("${task.name} - ${task.status.toString().replace('_', ' ')}")
                            }
                        }
                    }
                }
                """
            }
        }

        @Composable
        fun HealthCard(
            health: HealthStatus?
        ){
            val containerColor = when (health?.level ?: HealthLevel.GOOD) {
                HealthLevel.EXCELLENT -> MaterialTheme.colorScheme.tertiaryContainer
                HealthLevel.GOOD -> MaterialTheme.colorScheme.primaryContainer
                HealthLevel.FAIR -> MaterialTheme.colorScheme.secondaryContainer
                HealthLevel.POOR -> MaterialTheme.colorScheme.errorContainer
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = containerColor),
                elevation = CardDefaults.cardElevation()
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        text = "Health: ${health?.score}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = health?.level!!.name,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}