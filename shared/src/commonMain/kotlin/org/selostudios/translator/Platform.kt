package org.selostudios.translator

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform