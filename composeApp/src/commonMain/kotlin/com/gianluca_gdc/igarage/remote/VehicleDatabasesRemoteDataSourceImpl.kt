package com.gianluca_gdc.igarage.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter

class VehicleDatabasesRemoteDataSourceImpl(
    private val client: HttpClient,
    private val apiKeyProvider: () -> String,
    private val useSandbox: Boolean
) : VehicleDatabasesRemoteDataSource {

    private val baseUrl: String
        get() = if (useSandbox) {
            "https://api.vehicledatabases.com/sandbox"
        } else {
            "https://api.vehicledatabases.com"
        }

    override suspend fun getMaintenanceByVin(
        vin: String,
        currentMileage: Int?
    ): List<VehicleDbMaintenanceDto> {
        return client.get("$baseUrl/vehicle-maintenance/v4/$vin") {
            header("x-AuthKey", apiKeyProvider())
            currentMileage?.let { parameter("mileage", it) }
        }.body()
    }

    override suspend fun getMarketValue(
        vin:String,
        currentMileage: Int?
    ): VehicleDbMarketValueDto {
        return client.get(
            "$baseUrl/advanced-vin-decode/v2/$vin"
        ) {
            header("x-AuthKey", apiKeyProvider())
        }.body()
    }
    // same idea for , decodeVin
    override suspend fun decodeVinWithMaintenance(vin: String): VehicleDbVinWithMaintenanceDto {
        return client.get("$baseUrl/vin-decode/$vin"){
            header("x-AuthKey", apiKeyProvider())
        }.body()
    }
}