package org.delcom.pam_p4_ifs23013.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.delcom.pam_p4_ifs23013.network.plants.data.ResponseProfile
import org.delcom.pam_p4_ifs23013.ui.components.BottomNavComponent
import org.delcom.pam_p4_ifs23013.ui.components.LoadingUI
import org.delcom.pam_p4_ifs23013.ui.components.TopAppBarComponent
import org.delcom.pam_p4_ifs23013.ui.viewmodels.PlantViewModel
import org.delcom.pam_p4_ifs23013.ui.viewmodels.ProfilePlantUIState

@Composable
fun ProfileScreen(
    navController: NavHostController,
    plantViewModel: PlantViewModel
) {
    val uiState by plantViewModel.uiState.collectAsState()
    var profile by remember { mutableStateOf<ResponseProfile?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isLoading = true
        plantViewModel.getProfile()
    }

    LaunchedEffect(uiState.profile) {
        when (val state = uiState.profile) {
            is ProfilePlantUIState.Loading -> isLoading = true
            is ProfilePlantUIState.Success -> {
                isLoading = false
                profile = state.data
            }
            is ProfilePlantUIState.Error -> {
                isLoading = false
            }
        }
    }

    if (isLoading) {
        LoadingUI()
        return
    }

    Scaffold(
        topBar = {
            TopAppBarComponent(navController = navController, title = "Profil", showBackButton = false)
        },
        bottomBar = { BottomNavComponent(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (profile != null) {
                Text(text = "Nama Profil", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                Text(text = profile!!.nama, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Username", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                Text(text = profile!!.username, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Tentang", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                Text(text = profile!!.tentang, style = MaterialTheme.typography.bodyLarge)
            } else {
                Text("Gagal memuat profil.")
            }
        }
    }
}