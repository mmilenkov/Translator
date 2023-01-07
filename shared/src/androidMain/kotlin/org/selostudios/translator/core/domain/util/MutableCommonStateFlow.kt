package org.selostudios.translator.core.domain.util

import kotlinx.coroutines.flow.MutableStateFlow

actual open class MutableCommonStateFlow<T> actual constructor(flow: MutableStateFlow<T>) : MutableStateFlow<T> by flow