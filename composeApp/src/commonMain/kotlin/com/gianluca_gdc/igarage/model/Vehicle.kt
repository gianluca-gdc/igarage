package com.gianluca_gdc.igarage.model

data class Vehicle(
    val id: String,
    val vin: String,
    val nickname: String?,
    val year: Int?,
    val make: String?,
    val model: String?,
    val trim: String?,
    val mileage: Int?,
    val imageUrl: String?,

    val estimatedValue: Int?,
    val privatePartyValue: Int?,
    val tradeInValue: Int?,
    val dealerValue:Int?,
    val valueChange6M: Float? // percent change over 6 months

)

