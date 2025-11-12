package com.gianluca_gdc.igarage.remote

import com.gianluca_gdc.igarage.model.Vehicle

data class VehicleDbVinDecodeDto(
    val year:Int,
    val make:String,
    val model:String,
    val trim:String,
    val engineName:String,
    val engineCyl: Int?,
    val engineSize: Float?,
    val isElectric: Boolean,
    val isHybrid: Boolean
)
fun VehicleDbVinDecodeDto.toVehicle(vin:String): Vehicle {
    return Vehicle(
        year = year,
        id = vin,
        vin = vin,
        nickname = null,
        make = make,
        model = model,
        trim = trim,
        mileage = null,
        imageUrl = null,
        estimatedValue = null,
        valueChange6M = null,
        privatePartyValue = null,
        tradeInValue = null,
        dealerValue = null,
    )
}
