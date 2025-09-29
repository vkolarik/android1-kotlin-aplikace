package cz.mendelu.project.di

import cz.mendelu.project.database.ILocalItemRepository
import cz.mendelu.project.database.ItemDao
import cz.mendelu.project.database.LocalItemRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideItemsRepository(dao: ItemDao): ILocalItemRepository {
        return LocalItemRepositoryImpl(dao)
    }
}