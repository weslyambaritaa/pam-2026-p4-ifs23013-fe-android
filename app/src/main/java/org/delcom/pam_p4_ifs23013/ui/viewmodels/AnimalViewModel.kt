package org.delcom.pam_p4_ifs23013.ui.viewmodels

import android.util.Log
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
import org.delcom.pam_p4_ifs23013.network.animals.data.ResponseAnimalData
import org.delcom.pam_p4_ifs23013.network.animals.data.ResponseProfile
import org.delcom.pam_p4_ifs23013.network.animals.service.IAnimalRepository
import javax.inject.Inject

sealed interface ProfileUIState {
    data class Success(val data: ResponseProfile) : ProfileUIState
    data class Error(val message: String) : ProfileUIState
    object Loading : ProfileUIState
}

sealed interface AnimalsUIState {
    data class Success(val data: List<ResponseAnimalData>) : AnimalsUIState
    data class Error(val message: String) : AnimalsUIState
    object Loading : AnimalsUIState
}

sealed interface AnimalUIState {
    data class Success(val data: ResponseAnimalData) : AnimalUIState
    data class Error(val message: String) : AnimalUIState
    object Loading : AnimalUIState
}

sealed interface AnimalActionUIState {
    data class Success(val message: String) : AnimalActionUIState
    data class Error(val message: String) : AnimalActionUIState
    object Loading : AnimalActionUIState
    object Idle : AnimalActionUIState // Menambahkan state Idle agar SnackBar tidak muncul berulang
}

data class UIStateAnimal(
    val profile: ProfileUIState = ProfileUIState.Loading,
    val animals: AnimalsUIState = AnimalsUIState.Loading,
    val animal: AnimalUIState = AnimalUIState.Loading,
    val animalAction: AnimalActionUIState = AnimalActionUIState.Idle
)

@HiltViewModel
@Keep
class AnimalViewModel @Inject constructor(
    private val repository: IAnimalRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(UIStateAnimal())
    val uiState = _uiState.asStateFlow()

    // Menambahkan variabel akses cepat untuk actionUIState agar konsisten dengan AnimalsAddScreen
    private val _actionUIState = MutableStateFlow<AnimalActionUIState>(AnimalActionUIState.Idle)
    val actionUIState = _actionUIState.asStateFlow()

    fun clearActionState() {
        _actionUIState.value = AnimalActionUIState.Idle
    }

    fun getProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(profile = ProfileUIState.Loading) }
            val result = runCatching { repository.getProfile() }.fold(
                onSuccess = {
                    if (it.status == "success") ProfileUIState.Success(it.data!!)
                    else ProfileUIState.Error(it.message)
                },
                onFailure = { ProfileUIState.Error(it.localizedMessage ?: "Gagal terhubung ke server") }
            )
            _uiState.update { it.copy(profile = result) }
        }
    }

    fun getAllAnimals(search: String? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(animals = AnimalsUIState.Loading) }
            val result = runCatching { repository.getAllAnimals(search) }.fold(
                onSuccess = {
                    if (it.status == "success") AnimalsUIState.Success(it.data!!.animals)
                    else AnimalsUIState.Error(it.message)
                },
                onFailure = { AnimalsUIState.Error(it.localizedMessage ?: "Gagal memuat data hewan") }
            )
            _uiState.update { it.copy(animals = result) }
        }
    }

    fun postAnimal(
        nama: RequestBody,
        deskripsi: RequestBody,
        habitat: RequestBody,
        makananFavorit: RequestBody,
        file: MultipartBody.Part
    ) {
        viewModelScope.launch {
            _actionUIState.value = AnimalActionUIState.Loading

            runCatching {
                repository.postAnimal(nama, deskripsi, habitat, makananFavorit, file)
            }.fold(
                onSuccess = {
                    if (it.status == "success") {
                        _actionUIState.value = AnimalActionUIState.Success("Berhasil menambah data")
                    } else {
                        _actionUIState.value = AnimalActionUIState.Error(it.message)
                    }
                },
                onFailure = { e ->
                    Log.e("POST_ANIMAL", "Error: ${e.message}", e)
                    // Mengambil pesan error asli dari exception untuk mempermudah debug
                    _actionUIState.value = AnimalActionUIState.Error(e.localizedMessage ?: "Terjadi kesalahan koneksi")
                }
            )
        }
    }

    fun getAnimalById(animalId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(animal = AnimalUIState.Loading) }
            val result = runCatching { repository.getAnimalById(animalId) }.fold(
                onSuccess = {
                    if (it.status == "success") AnimalUIState.Success(it.data!!.animal)
                    else AnimalUIState.Error(it.message)
                },
                onFailure = { AnimalUIState.Error(it.localizedMessage ?: "Data tidak ditemukan") }
            )
            _uiState.update { it.copy(animal = result) }
        }
    }

    fun putAnimal(
        animalId: String,
        nama: RequestBody,
        deskripsi: RequestBody,
        habitat: RequestBody,
        makananFavorit: RequestBody,
        file: MultipartBody.Part?
    ) {
        viewModelScope.launch {
            _actionUIState.value = AnimalActionUIState.Loading
            runCatching {
                repository.putAnimal(animalId, nama, deskripsi, habitat, makananFavorit, file)
            }.fold(
                onSuccess = {
                    if (it.status == "success") _actionUIState.value = AnimalActionUIState.Success("Berhasil memperbarui data")
                    else _actionUIState.value = AnimalActionUIState.Error(it.message)
                },
                onFailure = { _actionUIState.value = AnimalActionUIState.Error(it.localizedMessage ?: "Gagal memperbarui data") }
            )
        }
    }

    fun deleteAnimal(animalId: String) {
        viewModelScope.launch {
            _actionUIState.value = AnimalActionUIState.Loading
            runCatching {
                repository.deleteAnimal(animalId)
            }.fold(
                onSuccess = {
                    if (it.status == "success") _actionUIState.value = AnimalActionUIState.Success("Berhasil menghapus data")
                    else _actionUIState.value = AnimalActionUIState.Error(it.message)
                },
                onFailure = { _actionUIState.value = AnimalActionUIState.Error(it.localizedMessage ?: "Gagal menghapus data") }
            )
        }
    }
}