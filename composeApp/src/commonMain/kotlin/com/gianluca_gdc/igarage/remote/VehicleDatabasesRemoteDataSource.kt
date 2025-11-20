package com.gianluca_gdc.igarage.remote


interface VehicleDatabasesRemoteDataSource {
    suspend fun getMaintenanceByVin(
        vin: String,
        currentMileage: Int?
    ): List<VehicleDbMaintenanceDto>

    suspend fun getMarketValue(
        vin: String,
        currentMileage: Int?
    ): VehicleDbMarketValueDto

    suspend fun decodeVinWithMaintenance(
        vin: String
    ): VehicleDbVinWithMaintenanceDto
}