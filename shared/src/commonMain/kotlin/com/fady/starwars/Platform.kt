package com.fady.starwars

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform