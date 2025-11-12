package com.gianluca_gdc.igarage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.gianluca_gdc.igarage.repository.FakeMaintenanceRepository
import com.gianluca_gdc.igarage.repository.VehicleRepositoryImpl
import com.gianluca_gdc.igarage.ui.GarageInteractor
import com.gianluca_gdc.igarage.ui.GarageScreen
import com.gianluca_gdc.igarage.ui.VehicleDetailScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            val interactor = remember {
                GarageInteractor(
                    vehicleRepository = VehicleRepositoryImpl(),
                    maintenanceRepository = FakeMaintenanceRepository()
                )
            }
            var selectedVehicleId by remember{mutableStateOf<String?>(null)}
            if(selectedVehicleId == null){
                GarageScreen(onVehicleClick = {id -> selectedVehicleId = id},
                    interactor = interactor)
            }else{
                VehicleDetailScreen(vehicleId = selectedVehicleId!!,
                    interactor = interactor,
                    onBack = {selectedVehicleId = null})
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}