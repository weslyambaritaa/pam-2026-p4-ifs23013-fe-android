package org.delcom.pam_p4_ifs23013.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import org.delcom.pam_p4_ifs23013.R
import org.delcom.pam_p4_ifs23013.helper.RouteHelper
import org.delcom.pam_p4_ifs23013.network.plants.data.ResponsePlantData
import org.delcom.pam_p4_ifs23013.ui.components.LoadingUI
import org.delcom.pam_p4_ifs23013.ui.components.TopAppBarComponent
import org.delcom.pam_p4_ifs23013.ui.viewmodels.PlantUIState
import org.delcom.pam_p4_ifs23013.ui.viewmodels.PlantViewModel

@Composable
fun PlantsDetailScreen(
    navController: NavHostController,
    snackbarHost: SnackbarHostState,
    plantViewModel: PlantViewModel,
    plantId: String
) {
    val uiStatePlant by plantViewModel.uiState.collectAsState()
    var isLoading by remember { mutableStateOf(false) }
    var plant by remember { mutableStateOf<ResponsePlantData?>(null) }

    fun fetchPlantData() {
        isLoading = true
        plantViewModel.getPlantById(plantId)
    }

    LaunchedEffect(plantId) {
        fetchPlantData()
    }

    LaunchedEffect(uiStatePlant.plant) {
        if (uiStatePlant.plant !is PlantUIState.Loading) {
            isLoading = false
            if (uiStatePlant.plant is PlantUIState.Success) {
                plant = (uiStatePlant.plant as PlantUIState.Success).data
            }
        }
    }

    if (isLoading) {
        LoadingUI()
        return
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBarComponent(
            navController = navController,
            title = "Detail Tumbuhan",
            showBackButton = true,
            TopAppBarComponent(
                navController = navController,
                title = "Detail Tumbuhan",
                showBackButton = true
            )
        )

        Box(modifier = Modifier.weight(1f)) {
            if (plant != null) {
                PlantsDetailUI(plant = plant!!)
            } else {
                Text(
                    text = "Data tidak ditemukan",
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun PlantsDetailUI(plant: ResponsePlantData) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background)
    ) {
        AsyncImage(
            model = "https://pam-2026-p4-ifs23013-be.weslyambaritaa.fun:8080/plants/${plant.id}/image",
            contentDescription = plant.nama,
            placeholder = painterResource(R.drawable.img_placeholder),
            error = painterResource(R.drawable.img_placeholder),
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            contentScale = ContentScale.Crop
        )

        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = plant.nama,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Deskripsi",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = plant.deskripsi,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Manfaat",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = plant.manfaat,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Efek Samping",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.error
            )
            Text(
                text = plant.efekSamping,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPlantsDetailUI() {}