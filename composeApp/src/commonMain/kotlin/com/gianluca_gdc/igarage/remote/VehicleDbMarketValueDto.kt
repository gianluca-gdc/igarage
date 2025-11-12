package com.gianluca_gdc.igarage.remote

import com.gianluca_gdc.igarage.model.Vehicle
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VehicleDbMarketValueDto(
    @SerialName("market value") val marketValueRows: List<MarketValueConditionRowDto>
)
@Serializable
data class MarketValueConditionRowDto(
    @SerialName("Condition") val condition: String,
    @SerialName("Private Party") val privateParty: String,
    @SerialName("Trade-In") val tradeIn: String,
    @SerialName("Dealer Retail") val dealerRetail: String
)
fun VehicleDbMarketValueDto.toAverageMarketValue(vehicle: Vehicle): Vehicle {
    val avg = marketValueRows.find { it.condition == "Average" } ?: marketValueRows.first()
    val privatePartyValue = avg.privateParty.filter { it.isDigit() }.toInt()
    val tradeInValue = avg.tradeIn.filter { it.isDigit() }.toInt()
    val dealerValue = avg.dealerRetail.filter{it.isDigit()}.toInt()
    return vehicle.copy(
        privatePartyValue = privatePartyValue,
        tradeInValue = tradeInValue,
        dealerValue = dealerValue,
        estimatedValue = (privatePartyValue + tradeInValue + dealerValue)/3
    )
}