package com.gianluca_gdc.igarage.remote

import com.gianluca_gdc.igarage.model.Vehicle
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class VehicleDbVinWithMaintenanceDataDto(
    val vin: String,
    val year: Int,
    val make: String,
    val model: String,
    val trim: String,
    val style: String,
    @SerialName("trim_and_style") val trimAndStyle: String,
    val summary: String,
    val maintenance: List<VehicleDbMaintenanceDto>
)
fun VehicleDbVinWithMaintenanceDataDto.toVehicle(vin:String): Vehicle {
    return Vehicle(
        year = year,
        id = vin,
        vin = vin,
        nickname = null,
        make = make,
        model = model,
        trim = trimAndStyle,
        mileage = null,
        imageUrl = null,
        estimatedValue = null,
        valueChange6M = null,
        privatePartyValue = null,
        tradeInValue = null,
        dealerValue = null,
    )
}
