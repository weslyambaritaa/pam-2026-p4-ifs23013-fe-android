package org.delcom.pam_p4_ifs23013.ui.viewmodels

import androidx.annotation.Keep
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.delcom.pam_p4_ifs23013.network.plants.data.ResponsePlantData
import org.delcom.pam_p4_ifs23013.network.plants.data.ResponseProfile
import org.delcom.pam_p4_ifs23013.network.plants.service.IPlantRepository
import javax.inject.Inject

sealed interface ProfilePlantUIState {
    data class Success(val data: ResponseProfile) : ProfilePlantUIState
    data class Error(val message: String) : ProfilePlantUIState
    object Loading : ProfilePlantUIState
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

data class UIStatePlant(
    val profile: ProfilePlantUIState = ProfilePlantUIState.Loading,
    val plants: PlantsUIState = PlantsUIState.Loading,
    var plant: PlantUIState = PlantUIState.Loading
)

@HiltViewModel
@Keep
class PlantViewModel @Inject constructor(
    private val repository: IPlantRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(UIStatePlant())
    val uiState = _uiState.asStateFlow()

    fun getProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(profile = ProfilePlantUIState.Loading) }
            _uiState.update { it ->
                val tmpState = runCatching { repository.getProfile() }.fold(
                    onSuccess = {
                        if (it.status == "success") ProfilePlantUIState.Success(it.data!!)
                        else ProfilePlantUIState.Error(it.message)
                    },
                    onFailure = { ProfilePlantUIState.Error(it.message ?: "Unknown error") }
                )
                it.copy(profile = tmpState)
            }
        }
    }

    fun getAllPlants(search: String? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(plants = PlantsUIState.Loading) }
            _uiState.update { it ->
                val tmpState = runCatching { repository.getAllPlants(search) }.fold(
                    onSuccess = {
                        if (it.status == "success") PlantsUIState.Success(it.data!!.plants)
                        else PlantsUIState.Error(it.message)
                    },
                    onFailure = { PlantsUIState.Error(it.message ?: "Unknown error") }
                )
                it.copy(plants = tmpState)
            }
        }
    }

    fun getPlantById(plantId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(plant = PlantUIState.Loading) }
            _uiState.update { it ->
                val tmpState = runCatching { repository.getPlantById(plantId) }.fold(
                    onSuccess = {
                        if (it.status == "success") PlantUIState.Success(it.data!!.plant)
                        else PlantUIState.Error(it.message)
                    },
                    onFailure = { PlantUIState.Error(it.message ?: "Unknown error") }
                )
                it.copy(plant = tmpState)
            }
        }
    }
}