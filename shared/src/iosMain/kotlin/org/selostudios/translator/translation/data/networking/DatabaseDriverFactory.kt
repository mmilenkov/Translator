package org.selostudios.translator.translation.data.networking

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import org.selostudios.translator.db.TranslationDB

actual class DatabaseDriverFactory {
    actual fun create(): SqlDriver {
        return NativeSqliteDriver(
            TranslationDB.Schema,
            "translation.db"
        )
    }

}