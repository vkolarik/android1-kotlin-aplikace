package cz.mendelu.project.di

import cz.mendelu.project.database.ItemDao
import cz.mendelu.project.database.ItemDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    @Singleton
    fun provideItemsDao(database: ItemDatabase): ItemDao {
        return database.itemDao()
    }
}