package com.gianluca_gdc.igarage

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform