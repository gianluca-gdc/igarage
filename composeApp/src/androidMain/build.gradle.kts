android {
    buildFeatures { buildConfig = true }
    defaultConfig {
        // Load from local.properties
        val props = java.util.Properties().apply {
            val f = rootProject.file("local.properties")
            if (f.exists()) f.inputStream().use { load(it) }
        }
        val vehicleDbKey = props.getProperty("VEHICLE_DB_KEY") ?: ""

        buildConfigField("String", "VEHICLE_DATABASES_KEY", "\"$vehicleDbKey\"")
    }
}