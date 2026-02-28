package org.delcom.pam_p4_ifs23013.ui.viewmodels

import androidx.annotation.Keep
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.delcom.pam_p4_ifs23013.network.plants.data.ResponsePlantData
import org.delcom.pam_p4_ifs23013.network.plants.data.ResponseProfile
import org.delcom.pam_p4_ifs23013.network.plants.service.IPlantRepository
import javax.inject.Inject

// Mengubah nama menjadi PlantProfileUIState untuk menghindari redeclaration
sealed interface PlantProfileUIState {
    data class Success(val data: ResponseProfile) : PlantProfileUIState
    data class Error(val message: String) : PlantProfileUIState
    object Loading : PlantProfileUIState
}

sealed interface PlantsUIState {
    data class Success(val data: List<ResponsePlantData>) : PlantsUIState
    data class Error(val message: String) : PlantsUIState
    object Loading : PlantsUIState
}

sealed interface PlantUIState {
    data class Success(val data: ResponsePlantData) : PlantUIState
    data class Error(val message: String) : PlantUIState
    object Loading : PlantUIState
}

sealed interface PlantActionUIState {
    data class Success(val message: String) : PlantActionUIState
    data class Error(val message: String) : PlantActionUIState
    object Loading : PlantActionUIState
    object Idle : PlantActionUIState // Menambahkan Idle state
}

data class UIStatePlant(
    val profile: PlantProfileUIState = PlantProfileUIState.Loading,
    val plants: PlantsUIState = PlantsUIState.Loading,
    var plant: PlantUIState = PlantUIState.Loading,
    var plantAction: PlantActionUIState = PlantActionUIState.Idle // Default ke Idle
)

@HiltViewModel
@Keep
class PlantViewModel @Inject constructor(
    private val repository: IPlantRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(UIStatePlant())
    val uiState = _uiState.asStateFlow()

    // Menambahkan fungsi clear state agar SnackBar tidak muncul berulang
    fun clearActionState() {
        _uiState.update { it.copy(plantAction = PlantActionUIState.Idle) }
    }

    fun getProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(profile = PlantProfileUIState.Loading) }
            val result = runCatching { repository.getProfile() }.fold(
                onSuccess = {
                    if (it.status == "success") PlantProfileUIState.Success(it.data!!)
                    else PlantProfileUIState.Error(it.message)
                },
                onFailure = { PlantProfileUIState.Error(it.message ?: "Gagal memuat profil") }
            )
            _uiState.update { it.copy(profile = result) }
        }
    }

    fun getAllPlants(search: String? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(plants = PlantsUIState.Loading) }
            val result = runCatching { repository.getAllPlants(search) }.fold(
                onSuccess = {
                    if (it.status == "success") PlantsUIState.Success(it.data!!.plants)
                    else PlantsUIState.Error(it.message)
                },
                onFailure = { PlantsUIState.Error(it.message ?: "Gagal memuat data") }
            )
            _uiState.update { it.copy(plants = result) }
        }
    }

    fun postPlant(
        nama: RequestBody,
        deskripsi: RequestBody,
        manfaat: RequestBody,
        efekSamping: RequestBody,
        file: MultipartBody.Part
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(plantAction = PlantActionUIState.Loading) }
            val result = runCatching {
                repository.postPlant(nama, deskripsi, manfaat, efekSamping, file)
            }.fold(
                onSuccess = {
                    if (it.status == "success") PlantActionUIState.Success("Berhasil menambah data")
                    else PlantActionUIState.Error(it.message)
                },
                onFailure = { PlantActionUIState.Error(it.message ?: "Terjadi kesalahan") }
            )
            _uiState.update { it.copy(plantAction = result) }
        }
    }

    fun getPlantById(plantId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(plant = PlantUIState.Loading) }
            val result = runCatching { repository.getPlantById(plantId) }.fold(
                onSuccess = {
                    if (it.status == "success") PlantUIState.Success(it.data!!.plant)
                    else PlantUIState.Error(it.message)
                },
                onFailure = { PlantUIState.Error(it.message ?: "Data tidak ditemukan") }
            )
            _uiState.update { it.copy(plant = result) }
        }
    }

    fun putPlant(
        plantId: String,
        nama: RequestBody,
        deskripsi: RequestBody,
        manfaat: RequestBody,
        efekSamping: RequestBody,
        file: MultipartBody.Part?
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(plantAction = PlantActionUIState.Loading) }
            val result = runCatching {
                repository.putPlant(plantId, nama, deskripsi, manfaat, efekSamping, file)
            }.fold(
                onSuccess = {
                    if (it.status == "success") PlantActionUIState.Success("Berhasil memperbarui data")
                    else PlantActionUIState.Error(it.message)
                },
                onFailure = { PlantActionUIState.Error(it.message ?: "Gagal memperbarui data") }
            )
            _uiState.update { it.copy(plantAction = result) }
        }
    }

    fun deletePlant(plantId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(plantAction = PlantActionUIState.Loading) }
            val result = runCatching { repository.deletePlant(plantId) }.fold(
                onSuccess = {
                    if (it.status == "success") PlantActionUIState.Success("Berhasil menghapus data")
                    else PlantActionUIState.Error(it.message)
                },
                onFailure = { PlantActionUIState.Error(it.message ?: "Gagal menghapus data") }
            )
            _uiState.update { it.copy(plantAction = result) }
        }
    }
}