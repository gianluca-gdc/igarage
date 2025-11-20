package com.gianluca_gdc.igarage.ui.vehicledetail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import coil3.compose.AsyncImage
import com.gianluca_gdc.igarage.ServiceLocator
import com.gianluca_gdc.igarage.model.HealthLevel
import com.gianluca_gdc.igarage.model.HealthStatus
import com.gianluca_gdc.igarage.model.MaintenanceTask
import com.gianluca_gdc.igarage.model.TaskCategory
import com.gianluca_gdc.igarage.model.TaskStatus
import com.gianluca_gdc.igarage.model.Vehicle
import com.gianluca_gdc.igarage.ui.garage.formatCommas
import com.gianluca_gdc.igarage.ui.garage.formatOneDecimal
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.abs

@Preview(widthDp = 375, heightDp = 667)
@Composable
fun vehicleDetailScreenPreview(){
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
    val tasks = listOf(
        MaintenanceTask(
            id = "t1",
            vehicleId = "1",
            name = "Engine Oil & Filter",
            description = "Replace oil and filter with manufacturer spec oil.",
            category = TaskCategory.MINOR,
            status = TaskStatus.DUE_SOON,
            dueMileage = 80000,
            lastCompletedMileage = 70000,
            dueDate = null,
            lastCompletedDate = null
        ),
        MaintenanceTask(
            id = "t2",
            vehicleId = "1",
            name = "Brake Pads & Inspection",
            description = "Inspect brake pads, rotors, calipers, and brake fluid.",
            category = TaskCategory.CRITICAL,
            status = TaskStatus.OVERDUE,
            dueMileage = 76000,
            lastCompletedMileage = 62000,
            dueDate = null,
            lastCompletedDate = null
        ),
        MaintenanceTask(
            id = "t3",
            vehicleId = "1",
            name = "Cabin Air Filter",
            description = "Replace cabin air filter for HVAC system efficiency.",
            category = TaskCategory.MINOR,
            status = TaskStatus.OK,
            dueMileage = 90000,
            lastCompletedMileage = 75000,
            dueDate = null,
            lastCompletedDate = null
        ),
        MaintenanceTask(
            id = "t4",
            vehicleId = "1",
            name = "Transmission Fluid",
            description = "Drain and fill transmission with correct ATF.",
            category = TaskCategory.MAJOR,
            status = TaskStatus.DUE_SOON,
            dueMileage = 85000,
            lastCompletedMileage = 60000,
            dueDate = null,
            lastCompletedDate = null
        ),
        MaintenanceTask(
            id = "t5",
            vehicleId = "1",
            name = "Coolant Flush",
            description = "Flush cooling system and refill with OEM coolant.",
            category = TaskCategory.CRITICAL,
            status = TaskStatus.OVERDUE,
            dueMileage = 76000,
            lastCompletedMileage = 62000,
            dueDate = null,
            lastCompletedDate = null
        ),
        MaintenanceTask(
            id = "t6",
            vehicleId = "1",
            name = "Spark Plugs",
            description = "Replace spark plugs with OEM specification plugs.",
            category = TaskCategory.MINOR,
            status = TaskStatus.DUE_SOON,
            dueMileage = 90000,
            lastCompletedMileage = 65000,
            dueDate = null,
            lastCompletedDate = null
        )
    )
    val previewState = VehicleDetailUiState(isLoading = false,
        vehicle = sampleVehicle1,
        maintenanceTasks = tasks,
        healthStatus = HealthStatus(
            score = 65,
            level = HealthLevel.FAIR,
            criticalOverdue = 1,
            majorOverdue = 1,
            minorOverdue = 0,
            upcomingSoon = 3
        ),
        errorMessage = null
        )
    VehicleDetailScreenContent(state = previewState, navigator = LocalNavigator.current)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleDetailScreenContent(
    state: VehicleDetailUiState,
    navigator: Navigator?
){
    val vehicle: Vehicle? = state.vehicle
    var selectedTaskDescription by remember { mutableStateOf<MaintenanceTask?>(null) }
    Box(Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.swipeBack(onBack = { navigator?.pop() })

        ) { innerPadding ->
            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize().padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                state.errorMessage != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Error: ${state.errorMessage}")
                    }

                }

                else -> {
                    val tasks: List<MaintenanceTask> = state.maintenanceTasks
                    val health = state.healthStatus

                    Column {

                        // You can redesign all of this however you want:
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                                .padding(6.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            item {
                                VehicleDetailCard(vehicle!!, innerPadding)
                            }
                            item {
                                HealthCard(health)
                            }
                            item {
                                MaintenanceCard(tasks,{task -> selectedTaskDescription = task})
                            }
                        }
                    }
                }

            }

        }
        selectedTaskDescription?.let { MaintenanceDescriptionOverlay(it,onDismiss = {selectedTaskDescription = null}) }
    }

}
@Composable
fun MaintenanceCard(
    tasks: List<MaintenanceTask>, onTaskLongPress: (MaintenanceTask) -> Unit
){


    Card(Modifier.fillMaxWidth()){
        Column(Modifier.padding(10.dp).fillMaxWidth()){
            tasks.forEachIndexed{ index,  task->
                val statusColor = when(task.status){
                    TaskStatus.OVERDUE -> Color.Red
                    TaskStatus.DUE_SOON -> Color.Yellow
                    TaskStatus.OK -> Color.Green
                    TaskStatus.COMPLETED -> Color(0xFF4CAF50)
                }
                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.combinedClickable(
                        onClick = {},
                        onLongClick = {onTaskLongPress(task)})
                        .fillMaxWidth().padding(4.dp)){
                    task.status?.let {
                        Box(Modifier.weight(.5f).padding(0.dp),
                            contentAlignment = Alignment.Center) {
                            Surface(
                                Modifier.size(width = 80.dp,25.dp),
                                color = statusColor.copy(alpha = 0.55f),      // soft background
                                contentColor = Color.White,                  // text inherits this
                                shape = RoundedCornerShape(30),
                                tonalElevation = 0.dp,


                                ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = task.status.toString().replace('_', ' '),
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.ExtraBold),
                                        modifier = Modifier.padding(
                                            horizontal = 8.dp,
                                            vertical = 4.dp
                                        ),

                                        )
                                }
                            }
                        }
                    }
                    task.name?.let{
                        Text(modifier = Modifier.weight(1f),
                            text = task.name
                        )
                    }
                    task.dueMileage?.let{
                        Text(text = formatCommas(task.dueMileage))
                    }
                }
                if (index != tasks.lastIndex) {
                    Divider()
                }
            }


        }
    }
}
data class VehicleDetailScreen(val vehicleId: String) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val serviceLocator = remember { ServiceLocator }
        val model = rememberScreenModel {
            VehicleDetailScreenModel(
                vehicleRepository = serviceLocator.vehicleRepository,
                maintenanceRepository = serviceLocator.maintenanceRepository,
                vehicleId = vehicleId
            )
        }
        val state by model.state.collectAsState()
        VehicleDetailScreenContent(state = state,navigator = navigator)

    }



}
fun Modifier.swipeBack(
    threshold: Float = 80f,
    onBack: () -> Unit
): Modifier = pointerInput(Unit){
    var totalDragX = 0f
    detectHorizontalDragGestures(
        onDragEnd = {
            if(totalDragX > threshold){
                onBack()
            }
            totalDragX = 0f
        },
        onHorizontalDrag = { _ , dragAmount ->
            totalDragX += dragAmount
        }
    )
}





@Composable
fun HealthCard(
    health: HealthStatus?
) {
    val color = when (health?.level ?: HealthLevel.GOOD) {
        HealthLevel.EXCELLENT -> Color.Green
        HealthLevel.GOOD -> Color.Blue
        HealthLevel.FAIR -> Color.Yellow
        HealthLevel.POOR -> Color.Red
    }
    val gapSize = (health!!.score / 100.00).toFloat()

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation()
    ) {
            Row(
                Modifier.fillMaxWidth().padding(2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(horizontal = 10.dp)
                            .padding(vertical = 10.dp)
                            .size(70.dp),
                        color = color,
                        trackColor = color.copy(alpha = .35f),
                        strokeWidth = 8.dp,
                        progress = { gapSize },
                    )
                    Text(
                        text = health?.level!!.name,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Column(Modifier.padding(10.dp)
                    .weight(1f)) {
                    Text(
                        text = "Health",
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 20.sp
                    )
                    if(health.criticalOverdue > 0){
                        Text(text = "${health.criticalOverdue} Critical")
                    }
                    if(health.majorOverdue > 0 || health.minorOverdue > 0){
                        Text(text = "${health.majorOverdue + health.minorOverdue} Other")
                    }
                    if(health.upcomingSoon > 0){
                        Text(text = "${health.upcomingSoon} Upcoming")
                    }

                }
                Text(
                    modifier = Modifier.padding(end = 15.dp),
                    text = "${health?.score}",
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 40.sp
                )

            }

    }
}
@Composable
fun VehicleDetailCard(vehicle: Vehicle, padding: PaddingValues) {
    Card(
        modifier = Modifier
            .fillMaxWidth().padding(padding),
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
                text = "${vehicle.year} ${vehicle.make} ${vehicle.model}",
                style = MaterialTheme.typography.titleLarge
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(
                    Modifier.weight(1f),
                    verticalArrangement = Arrangement.SpaceBetween) {
                    vehicle.trim?.let{
                        Text(
                            text = "${vehicle.trim}"
                        )
                    }
                    vehicle.mileage?.let {
                        Text(
                            "${formatCommas(it)} mi",
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }

                }
                Column(horizontalAlignment = Alignment.End) {
                    vehicle.estimatedValue?.let { value ->
                        Text(
                            text = "$${formatCommas(value)}",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    vehicle.valueChange6M?.let { change ->
                        val arrow = if (change >= 0f) "▲" else "▼"
                        val arrowColor = if (change >= 0f) Color(0xFF4CAF50) else Color(0xFFF44336)
                        Text(
                            text = buildAnnotatedString {


                                // colored arrow
                                withStyle(SpanStyle(color = arrowColor)) {
                                    append(arrow)
                                }

                                append(" ")

                                // normal text
                                append("${formatOneDecimal(abs(change))}%")
                            },
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun MaintenanceDescriptionOverlay(
    task: MaintenanceTask,
    onDismiss: () -> Unit
){
    Box(Modifier.background(color = Color.DarkGray.copy(alpha = .6f)).fillMaxSize()
        .clickable(
            onClick = onDismiss,
            indication = null,
            interactionSource = remember{ MutableInteractionSource() }
        ),
        contentAlignment = Alignment.Center
        ){
        Card(Modifier.fillMaxWidth(.85f)
            .fillMaxHeight(.4f)
            .padding(10.dp)
            .clickable(
            onClick ={},
            indication = null,
            interactionSource = remember{MutableInteractionSource()}
        )){
            Column(Modifier.fillMaxSize()
                .padding(15.dp)) {
                Row(Modifier.padding(bottom = 10.dp)){
                    Text(text = "Maintenance Description",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp)
                }
                Row(){
                    Text(text = "${task.name}",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp)
                }
                Row() {
                    Text(task.description.toString())
                }
            }
        }
    }
}
