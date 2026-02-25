package org.delcom.pam_p4_ifs23013.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import org.delcom.pam_p4_ifs23013.helper.RouteHelper
import org.delcom.pam_p4_ifs23013.network.plants.data.ResponsePlantData
import org.delcom.pam_p4_ifs23013.ui.components.LoadingUI
import org.delcom.pam_p4_ifs23013.ui.components.TopAppBarComponent
import org.delcom.pam_p4_ifs23013.ui.viewmodels.PlantUIState
import org.delcom.pam_p4_ifs23013.ui.viewmodels.PlantViewModel

@Composable
fun PlantsDetailScreen(navController: NavHostController, snackbarHost: SnackbarHostState, plantViewModel: PlantViewModel, plantId: String) {
    val uiState by plantViewModel.uiState.collectAsState()

    LaunchedEffect(plantId) { plantViewModel.getPlantById(plantId) }

    Scaffold(
        topBar = { TopAppBarComponent(title = "Detail Tumbuhan", onBack = { RouteHelper.back(navController) }) },
        snackbarHost = { SnackbarHost(hostState = snackbarHost) }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            when (val state = uiState.plant) {
                is PlantUIState.Loading -> LoadingUI()
                is PlantUIState.Success -> PlantsDetailUI(state.data)
                is PlantUIState.Error -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = state.message, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
fun PlantsDetailUI(plant: ResponsePlantData) {
    Column(modifier = Modifier.fillMaxSize()) {
        AsyncImage(
            model = "https://pam-2026-p4-ifs23013-be.weslyambaritaa.fun:8080/plants/${plant.id}/image",
            contentDescription = plant.nama,
            modifier = Modifier.fillMaxWidth().height(250.dp),
            contentScale = ContentScale.Crop
        )
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = plant.nama, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = plant.deskripsi, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Manfaat:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Text(text = plant.manfaat, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Efek Samping:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Text(text = plant.efekSamping, style = MaterialTheme.typography.bodyMedium)
        }
    }
}