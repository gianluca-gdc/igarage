package com.gianluca_gdc.igarage.remote


interface VehicleDatabasesRemoteDataSource {
    suspend fun getMaintenanceByVin(
        vin: String,
        currentMileage: Int?
    ): List<VehicleDbMaintenanceDto>

    suspend fun getMarketValue(
        vin: String,
        year: Int,
        make: String,
        model: String,
        trim: String?,
        stateCode: String,
        currentMileage: Int?
    ): VehicleDbMarketValueDto

    suspend fun decodeVin(
        vin: String
    ): VehicleDbVinDecodeDto
}