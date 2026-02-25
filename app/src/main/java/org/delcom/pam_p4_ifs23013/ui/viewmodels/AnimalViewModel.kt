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
import org.delcom.pam_p4_ifs23013.network.animals.data.ResponseAnimalData
import org.delcom.pam_p4_ifs23013.network.plants.data.ResponseProfile
import org.delcom.pam_p4_ifs23013.network.animals.service.IAnimalRepository
import javax.inject.Inject

sealed interface ProfileAnimalUIState {
    data class Success(val data: ResponseProfile) : ProfileAnimalUIState
    data class Error(val message: String) : ProfileAnimalUIState
    object Loading : ProfileAnimalUIState
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
}

data class UIStateAnimal(
    val profile: ProfileAnimalUIState = ProfileAnimalUIState.Loading,
    val animals: AnimalsUIState = AnimalsUIState.Loading,
    var animal: AnimalUIState = AnimalUIState.Loading,
    var animalAction: AnimalActionUIState = AnimalActionUIState.Loading
)

@HiltViewModel
@Keep
class AnimalViewModel @Inject constructor(
    private val repository: IAnimalRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(UIStateAnimal())
    val uiState = _uiState.asStateFlow()

    fun getProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(profile = ProfileAnimalUIState.Loading) }
            _uiState.update { it ->
                val tmpState = runCatching { repository.getProfile() }.fold(
                    onSuccess = {
                        if (it.status == "success") ProfileAnimalUIState.Success(it.data!!)
                        else ProfileAnimalUIState.Error(it.message)
                    },
                    onFailure = { ProfileAnimalUIState.Error(it.message ?: "Unknown error") }
                )
                it.copy(profile = tmpState)
            }
        }
    }

    fun getAllAnimals(search: String? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(animals = AnimalsUIState.Loading) }
            _uiState.update { it ->
                val tmpState = runCatching { repository.getAllAnimals(search) }.fold(
                    onSuccess = {
                        if (it.status == "success") AnimalsUIState.Success(it.data!!.animals)
                        else AnimalsUIState.Error(it.message)
                    },
                    onFailure = { AnimalsUIState.Error(it.message ?: "Unknown error") }
                )
                it.copy(animals = tmpState)
            }
        }
    }

    fun postAnimal(nama: RequestBody, deskripsi: RequestBody, habitat: RequestBody, makananFavorit: RequestBody, file: MultipartBody.Part) {
        viewModelScope.launch {
            _uiState.update { it.copy(animalAction = AnimalActionUIState.Loading) }
            _uiState.update { it ->
                val tmpState = runCatching { repository.postAnimal(nama, deskripsi, habitat, makananFavorit, file) }.fold(
                    onSuccess = {
                        if (it.status == "success") AnimalActionUIState.Success(it.data!!.animalId)
                        else AnimalActionUIState.Error(it.message)
                    },
                    onFailure = { AnimalActionUIState.Error(it.message ?: "Unknown error") }
                )
                it.copy(animalAction = tmpState)
            }
        }
    }

    fun getAnimalById(animalId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(animal = AnimalUIState.Loading) }
            _uiState.update { it ->
                val tmpState = runCatching { repository.getAnimalById(animalId) }.fold(
                    onSuccess = {
                        if (it.status == "success") AnimalUIState.Success(it.data!!.animal)
                        else AnimalUIState.Error(it.message)
                    },
                    onFailure = { AnimalUIState.Error(it.message ?: "Unknown error") }
                )
                it.copy(animal = tmpState)
            }
        }
    }

    fun putAnimal(animalId: String, nama: RequestBody, deskripsi: RequestBody, habitat: RequestBody, makananFavorit: RequestBody, file: MultipartBody.Part?) {
        viewModelScope.launch {
            _uiState.update { it.copy(animalAction = AnimalActionUIState.Loading) }
            _uiState.update { it ->
                val tmpState = runCatching { repository.putAnimal(animalId, nama, deskripsi, habitat, makananFavorit, file) }.fold(
                    onSuccess = {
                        if (it.status == "success") AnimalActionUIState.Success(it.message)
                        else AnimalActionUIState.Error(it.message)
                    },
                    onFailure = { AnimalActionUIState.Error(it.message ?: "Unknown error") }
                )
                it.copy(animalAction = tmpState)
            }
        }
    }

    fun deleteAnimal(animalId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(animalAction = AnimalActionUIState.Loading) }
            _uiState.update { it ->
                val tmpState = runCatching { repository.deleteAnimal(animalId) }.fold(
                    onSuccess = {
                        if (it.status == "success") AnimalActionUIState.Success(it.message)
                        else AnimalActionUIState.Error(it.message)
                    },
                    onFailure = { AnimalActionUIState.Error(it.message ?: "Unknown error") }
                )
                it.copy(animalAction = tmpState)
            }
        }
    }
}