package org.delcom.pam_p4_ifs23013.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import org.delcom.pam_p4_ifs23013.helper.ConstHelper
import org.delcom.pam_p4_ifs23013.helper.RouteHelper
import org.delcom.pam_p4_ifs23013.network.plants.data.ResponsePlantData
import org.delcom.pam_p4_ifs23013.ui.components.BottomNavComponent
import org.delcom.pam_p4_ifs23013.ui.components.LoadingUI
import org.delcom.pam_p4_ifs23013.ui.components.TopAppBarComponent
import org.delcom.pam_p4_ifs23013.ui.viewmodels.PlantViewModel
import org.delcom.pam_p4_ifs23013.ui.viewmodels.PlantsUIState

@Composable
fun PlantsScreen(navController: NavHostController, plantViewModel: PlantViewModel) {
    val uiState by plantViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) { plantViewModel.getAllPlants() }

    Scaffold(
        topBar = { TopAppBarComponent("Daftar Tumbuhan") },
        bottomBar = { BottomNavComponent(navController) }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            when (val state = uiState.plants) {
                is PlantsUIState.Loading -> LoadingUI()
                is PlantsUIState.Success -> PlantsUI(state.data) { plantId ->
                    // RouteHelper.to mengharapkan (navController, route) atau (navController, route, navOptions)
                    navController.navigate(ConstHelper.RouteNames.PlantsDetail.path.replace("{plantId}", plantId))
                }
                is PlantsUIState.Error -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = state.message, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
fun PlantsUI(plants: List<ResponsePlantData>, onOpen: (String) -> Unit) {
    LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        items(plants) { plant ->
            Card(
                modifier = Modifier.fillMaxWidth().clickable { onOpen(plant.id) },
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = "https://pam-2026-p4-ifs23013-be.weslyambaritaa.fun:8080/plants/${plant.id}/image",
                        contentDescription = plant.nama,
                        modifier = Modifier.size(80.dp).clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(text = plant.nama, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text(text = plant.deskripsi, style = MaterialTheme.typography.bodyMedium, maxLines = 2)
                    }
                }
            }
        }
    }
}