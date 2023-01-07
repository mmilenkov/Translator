package org.selostudios.translator.core.domain.util

import kotlinx.coroutines.flow.MutableStateFlow

//Allows the creation of MutableStateFlows from the iOS side
class IOSMutableStateFlow<T>(initialValue: T): MutableCommonStateFlow<T>(MutableStateFlow(initialValue)) {
}