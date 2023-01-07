package org.selostudios.translator.core.domain.util

import kotlinx.coroutines.flow.Flow

// Section from https://github.com/icerockdev/moko-mvvm
expect class CommonFlow<T>(flow: Flow<T>): Flow<T>

fun <T> Flow<T>.toCommonFlow() = CommonFlow(this)