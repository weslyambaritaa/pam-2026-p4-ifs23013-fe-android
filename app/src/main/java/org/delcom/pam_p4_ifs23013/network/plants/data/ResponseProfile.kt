package org.delcom.pam_p4_ifs23013.network.animals.data

import kotlinx.serialization.Serializable

@Serializable
data class ResponseProfile(
    val username: String,
    val nama: String,
    val tentang: String,
)