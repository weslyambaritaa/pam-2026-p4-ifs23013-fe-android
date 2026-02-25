package org.delcom.pam_p4_ifs23013.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.delcom.pam_p4_ifs23013.network.animals.service.IAnimalAppContainer
import org.delcom.pam_p4_ifs23013.network.animals.service.IAnimalRepository
import org.delcom.pam_p4_ifs23013.network.animals.service.AnimalAppContainer

@Module
@InstallIn(SingletonComponent::class)
object AnimalModule {
    @Provides
    fun provideAnimalContainer(): IAnimalAppContainer {
        return AnimalAppContainer()
    }

    @Provides
    fun provideAnimalRepository(container: IAnimalAppContainer): IAnimalRepository {
        return container.animalRepository
    }
}