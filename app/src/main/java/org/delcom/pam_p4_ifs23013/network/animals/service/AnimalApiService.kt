package org.delcom.pam_p4_ifs23013.network.animals.service

import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.delcom.pam_p4_ifs23013.network.data.ResponseMessage
import org.delcom.pam_p4_ifs23013.network.animals.data.ResponseAnimal
import org.delcom.pam_p4_ifs23013.network.animals.data.ResponseAnimalAdd
import org.delcom.pam_p4_ifs23013.network.animals.data.ResponseAnimals
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface AnimalApiService {
    // Ambil profile developer
    @GET("profile")
    suspend fun getProfile(): ResponseMessage<ResponseProfile?>

    // Ambil semua data hewan
    @GET("animals")
    suspend fun getAllAnimals(
        @Query("search") search: String? = null
    ): ResponseMessage<ResponseAnimals?>

    // Tambah data hewan
    @Multipart
    @POST("/animals")
    suspend fun postAnimal(
        @Part("nama") nama: RequestBody,
        @Part("deskripsi") deskripsi: RequestBody,
        @Part("habitat") habitat: RequestBody,
        @Part("makananFavorit") makananFavorit: RequestBody,
        @Part file: MultipartBody.Part
    ): ResponseMessage<ResponseAnimalAdd?>

    // Ambil data hewan berdasarkan ID
    @GET("animals/{animalId}")
    suspend fun getAnimalById(
        @Path("animalId") animalId: String
    ): ResponseMessage<ResponseAnimal?>


    // Ubah data hewan
    @Multipart
    @PUT("/animals/{animalId}")
    suspend fun putAnimal(
        @Path("animalId") animalId: String,
        @Part("nama") nama: RequestBody,
        @Part("deskripsi") deskripsi: RequestBody,
        @Part("habitat") habitat: RequestBody,
        @Part("makananFavorit") makananFavorit: RequestBody,
        @Part file: MultipartBody.Part? = null
    ): ResponseMessage<String?>

    // Hapus data hewan
    @DELETE("animals/{animalId}")
    suspend fun deleteAnimal(
        @Path("animalId") animalId: String
    ): ResponseMessage<String?>
}