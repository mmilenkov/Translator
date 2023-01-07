package org.selostudios.translator.android.di

import android.app.Application
import com.squareup.sqldelight.db.SqlDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import org.selostudios.translator.db.TranslationDB
import org.selostudios.translator.translation.data.history.HistoryDatasource
import org.selostudios.translator.translation.data.history.SQLDelightHistoryDatasource
import org.selostudios.translator.translation.data.networking.DatabaseDriverFactory
import org.selostudios.translator.translation.data.networking.HttpClientFactory
import org.selostudios.translator.translation.data.networking.TranslationClientKtor
import org.selostudios.translator.translation.domain.TranslationClient
import org.selostudios.translator.translation.domain.usecases.TranslationUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient{
        return HttpClientFactory().create()
    }

    @Provides
    @Singleton
    fun provideTranslationClient(httpClient: HttpClient): TranslationClient{
        return TranslationClientKtor(httpClient)
    }

    @Provides
    @Singleton
    fun provideDatabaseDriver(context: Application): SqlDriver{
        return DatabaseDriverFactory(context).create()
    }

    @Provides
    @Singleton
    fun provideHistoryDatasource(driver: SqlDriver): HistoryDatasource{
        return SQLDelightHistoryDatasource(TranslationDB(driver))
    }

    @Provides
    @Singleton
    fun provideTranslationUseCase(client: TranslationClient, datasource: HistoryDatasource): TranslationUseCase{
        return TranslationUseCase(client, datasource)
    }
}