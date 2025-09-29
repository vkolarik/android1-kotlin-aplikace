package cz.mendelu.project.di

import android.content.Context
import cz.mendelu.project.database.ItemDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideItemsDatabase(@ApplicationContext context: Context): ItemDatabase {
        return ItemDatabase.getDatabase(context)
    }
}