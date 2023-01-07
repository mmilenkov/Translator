package org.selostudios.translator.core.domain.util

import kotlinx.coroutines.flow.MutableStateFlow

// Section from https://github.com/icerockdev/moko-mvvm
expect open class MutableCommonStateFlow<T>(flow: MutableStateFlow<T>): MutableStateFlow<T>

fun <T> MutableStateFlow<T>.toMutableCommonStateFlow() = MutableCommonStateFlow(this)