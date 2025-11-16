package com.gianluca_gdc.igarage

import com.gianluca_gdc.igarage.network.HttpClientFactory
import com.gianluca_gdc.igarage.remote.VehicleDatabasesRemoteDataSourceImpl
import com.gianluca_gdc.igarage.repository.MaintenanceRepository
import com.gianluca_gdc.igarage.repository.MaintenanceRepositoryImpl
import com.gianluca_gdc.igarage.repository.VehicleRepository
import com.gianluca_gdc.igarage.repository.VehicleRepositoryImpl

object ServiceLocator {
    private var initialized = false

    lateinit var vehicleRepository: VehicleRepository
        private set

    lateinit var maintenanceRepository: MaintenanceRepository
        private set

    /**
     * Must be called from the platform layer (e.g., Android) because BuildConfig is not
     * available in commonMain. Pass your API key here and we wire everything up.
     */
    fun init(apiKey: String, useSandbox: Boolean) {
        if (initialized) return

        val httpClient = HttpClientFactory().create()
        val remote = VehicleDatabasesRemoteDataSourceImpl(
            client = httpClient,
            apiKeyProvider = { apiKey },
            useSandbox = useSandbox
        )

        vehicleRepository = VehicleRepositoryImpl(remote)
        maintenanceRepository = MaintenanceRepositoryImpl(vehicleRepository, remote)

        initialized = true
    }
}