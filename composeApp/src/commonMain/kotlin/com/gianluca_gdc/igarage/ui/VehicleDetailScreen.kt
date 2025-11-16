package com.gianluca_gdc.igarage.ui

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen

data class VehicleDetailScreen(val vehicleId: String) : Screen {
    @Composable
    override fun Content() {
        // TODO: show vehicle + HealthStatus + maintenance
    }
}