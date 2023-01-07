package org.selostudios.translator.core.domain.util

// This hides the kotlin.coroutines implementation from ios
// Section from https://github.com/icerockdev/moko-mvvm
fun interface DisposableHandle: kotlinx.coroutines.DisposableHandle