package org.selostudios.translator.translation.data.networking

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import org.selostudios.translator.db.TranslationDB

actual class DatabaseDriverFactory constructor(private val context: Context){
    actual fun create(): SqlDriver {
        return AndroidSqliteDriver(
            TranslationDB.Schema,
            context,
            "translation.db"
        )
    }
}