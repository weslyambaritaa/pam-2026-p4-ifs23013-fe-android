package org.delcom.pam_p4_ifs23013.network.animals.service

import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.delcom.pam_p4_ifs23013.helper.SuspendHelper
import org.delcom.pam_p4_ifs23013.network.data.ResponseMessage
import org.delcom.pam_p4_ifs23013.network.animals.data.ResponseAnimal
import org.delcom.pam_p4_ifs23013.network.animals.data.ResponseAnimalAdd
import org.delcom.pam_p4_ifs23013.network.animals.data.ResponseAnimals
import org.delcom.pam_p4_ifs23013.network.plants.data.ResponseProfile

class AnimalRepository (private val animalApiService: AnimalApiService): IAnimalRepository {
    override suspend fun getProfile(): ResponseMessage<ResponseProfile?> {
        return SuspendHelper.safeApiCall {
            animalApiService.getProfile()
        }
    }

    override suspend fun getAllAnimals(search: String?): ResponseMessage<ResponseAnimals?> {
        return SuspendHelper.safeApiCall {
            animalApiService.getAllAnimals(search)
        }
    }

    override suspend fun postAnimal(
        nama: RequestBody,
        deskripsi: RequestBody,
        habitat: RequestBody,
        makananFavorit: RequestBody,
        file: MultipartBody.Part
    ): ResponseMessage<ResponseAnimalAdd?> {
        return SuspendHelper.safeApiCall {
            animalApiService.postAnimal(
                nama = nama,
                deskripsi = deskripsi,
                habitat = habitat,
                makananFavorit = makananFavorit,
                file = file
            )
        }
    }

    override suspend fun getAnimalById(animalId: String): ResponseMessage<ResponseAnimal?> {
        return SuspendHelper.safeApiCall {
            animalApiService.getAnimalById(animalId)
        }
    }

    override suspend fun putAnimal(
        animalId: String,
        nama: RequestBody,
        deskripsi: RequestBody,
        habitat: RequestBody,
        makananFavorit: RequestBody,
        file: MultipartBody.Part?
    ): ResponseMessage<String?> {
        return SuspendHelper.safeApiCall {
            animalApiService.putAnimal(
                animalId = animalId,
                nama = nama,
                deskripsi = deskripsi,
                habitat = habitat,
                makananFavorit = makananFavorit,
                file = file
            )
        }
    }

    override suspend fun deleteAnimal(animalId: String): ResponseMessage<String?> {
        return SuspendHelper.safeApiCall {
            animalApiService.deleteAnimal(animalId)
        }
    }
}