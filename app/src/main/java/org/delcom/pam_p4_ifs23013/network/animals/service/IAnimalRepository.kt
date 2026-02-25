package org.delcom.pam_p4_ifs23013.network.animals.service

import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.delcom.pam_p4_ifs23013.network.data.ResponseMessage
import org.delcom.pam_p4_ifs23013.network.animals.data.ResponseAnimal
import org.delcom.pam_p4_ifs23013.network.animals.data.ResponseAnimalAdd
import org.delcom.pam_p4_ifs23013.network.animals.data.ResponseAnimals

interface IAnimalRepository {
    // Ambil profile developer
    suspend fun getProfile(): ResponseMessage<ResponseProfile?>

    // Ambil semua data hewan
    suspend fun getAllAnimals(
        search: String? = null
    ): ResponseMessage<ResponseAnimals?>

    // Tambah data hewan
    suspend fun postAnimal(
        nama: RequestBody,
        deskripsi: RequestBody,
        habitat: RequestBody,
        makananFavorit: RequestBody,
        file: MultipartBody.Part
    ): ResponseMessage<ResponseAnimalAdd?>

    // Ambil data hewan berdasarkan ID
    suspend fun getAnimalById(
        animalId: String
    ): ResponseMessage<ResponseAnimal?>


    // Ubah data hewan
    suspend fun putAnimal(
        animalId: String,
        nama: RequestBody,
        deskripsi: RequestBody,
        habitat: RequestBody,
        makananFavorit: RequestBody,
        file: MultipartBody.Part? = null
    ): ResponseMessage<String?>

    // Hapus data tumbuhan
    suspend fun deleteAnimal(
        animalId: String
    ): ResponseMessage<String?>
}