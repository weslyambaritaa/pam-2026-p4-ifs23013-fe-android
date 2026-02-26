package org.delcom.pam_p4_ifs23013.network.animals.data

import kotlinx.serialization.Serializable

@Serializable
data class ResponseAnimals (
    val animals: List<ResponseAnimalData>
)

@Serializable
data class ResponseAnimal (
    val animal: ResponseAnimalData
)

@Serializable
data class ResponseAnimalAdd (
    val animalId: String
)

@Serializable
data class ResponseAnimalData(
    val id: String,
    val nama: String,
    val deskripsi: String,
    val habitat: String,
    val makananFavorit: String,
    val createdAt: String,
    val updatedAt: String
)