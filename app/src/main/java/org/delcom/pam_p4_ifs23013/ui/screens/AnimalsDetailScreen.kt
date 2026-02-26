package org.delcom.pam_p4_ifs23013.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import org.delcom.pam_p4_ifs23013.R
import org.delcom.pam_p4_ifs23013.helper.ConstHelper
import org.delcom.pam_p4_ifs23013.helper.RouteHelper
import org.delcom.pam_p4_ifs23013.helper.SuspendHelper
import org.delcom.pam_p4_ifs23013.helper.SuspendHelper.SnackBarType
import org.delcom.pam_p4_ifs23013.helper.ToolsHelper
import org.delcom.pam_p4_ifs23013.network.animals.data.ResponseAnimalData
import org.delcom.pam_p4_ifs23013.ui.components.BottomDialog
import org.delcom.pam_p4_ifs23013.ui.components.BottomDialogType
import org.delcom.pam_p4_ifs23013.ui.components.BottomNavComponent
import org.delcom.pam_p4_ifs23013.ui.components.LoadingUI
import org.delcom.pam_p4_ifs23013.ui.components.TopAppBarComponent
import org.delcom.pam_p4_ifs23013.ui.components.TopAppBarMenuItem
import org.delcom.pam_p4_ifs23013.ui.theme.DelcomTheme
import org.delcom.pam_p4_ifs23013.ui.viewmodels.AnimalActionUIState
import org.delcom.pam_p4_ifs23013.ui.viewmodels.AnimalUIState
import org.delcom.pam_p4_ifs23013.ui.viewmodels.AnimalViewModel

@Composable
fun AnimalsDetailScreen(
    navController: NavHostController,
    snackbarHost: SnackbarHostState,
    animalViewModel: AnimalViewModel,
    animalId: String
) {
    val uiStateAnimal by animalViewModel.uiState.collectAsState()
    // Perbaikan State Tambahan (ViewModel Baru)
    val actionState by animalViewModel.actionUIState.collectAsState()

    var isLoading by remember { mutableStateOf(false) }
    var isConfirmDelete by remember { mutableStateOf(false) }
    var animal by remember { mutableStateOf<ResponseAnimalData?>(null) }

    LaunchedEffect(Unit) {
        isLoading = true
        animalViewModel.getAnimalById(animalId)
        animalViewModel.clearActionState()
    }

    LaunchedEffect(uiStateAnimal.animal) {
        when (val state = uiStateAnimal.animal) {
            is AnimalUIState.Success -> {
                animal = state.data
                isLoading = false
            }
            is AnimalUIState.Error -> {
                isLoading = false
                RouteHelper.back(navController)
            }
            else -> {}
        }
    }

    fun onDelete() {
        isLoading = true
        animalViewModel.deleteAnimal(animalId)
    }

    // Menggunakan actionState yang dipisah (sesuai saran perbaikan ViewModel sebelumnya)
    LaunchedEffect(actionState) {
        when (actionState) {
            is AnimalActionUIState.Success -> {
                isLoading = false
                SuspendHelper.showSnackBar(
                    snackbarHost = snackbarHost,
                    type = SnackBarType.SUCCESS,
                    message = (actionState as AnimalActionUIState.Success).message
                )
                RouteHelper.to(navController, ConstHelper.RouteNames.Animals.path, true)
            }
            is AnimalActionUIState.Error -> {
                isLoading = false
                SuspendHelper.showSnackBar(
                    snackbarHost = snackbarHost,
                    type = SnackBarType.ERROR,
                    message = (actionState as AnimalActionUIState.Error).message
                )
            }
            else -> {}
        }
    }

    if (isLoading || animal == null) {
        LoadingUI()
        return
    }

    val detailMenuItems = listOf(
        TopAppBarMenuItem(
            text = "Ubah Data",
            icon = Icons.Filled.Edit,
            onClick = {
                RouteHelper.to(
                    navController,
                    ConstHelper.RouteNames.AnimalsEdit.path.replace("{animalId}", animal!!.id),
                )
            }
        ),
        TopAppBarMenuItem(
            text = "Hapus Data",
            icon = Icons.Filled.Delete,
            onClick = { isConfirmDelete = true }
        ),
    )

    Column(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background)) {
        TopAppBarComponent(
            navController = navController,
            title = animal?.nama ?: "Detail",
            showBackButton = true,
            customMenuItems = detailMenuItems
        )
        Box(modifier = Modifier.weight(1f)) {
            AnimalsDetailUI(animal = animal!!)
            BottomDialog(
                type = BottomDialogType.ERROR,
                show = isConfirmDelete,
                onDismiss = { isConfirmDelete = false },
                title = "Konfirmasi Hapus Data",
                message = "Apakah Anda yakin ingin menghapus data ini?",
                confirmText = "Ya, Hapus",
                onConfirm = { onDelete() },
                cancelText = "Batal",
                destructiveAction = true
            )
        }
        BottomNavComponent(navController = navController)
    }
}

@Composable
fun AnimalsDetailUI(animal: ResponseAnimalData) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(vertical = 16.dp)) {
            AsyncImage(
                model = ToolsHelper.getAnimalImageUrl(animal.id),
                contentDescription = animal.nama,
                placeholder = painterResource(R.drawable.img_placeholder),
                error = painterResource(R.drawable.img_placeholder),
                modifier = Modifier.fillMaxWidth().height(250.dp),
                contentScale = ContentScale.Fit
            )
            Text(
                text = animal.nama,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        DetailCard("Deskripsi", animal.deskripsi)
        DetailCard("Habitat", animal.habitat)
        DetailCard("Makanan Favorit", animal.makananFavorit)
    }
}

@Composable
fun DetailCard(title: String, content: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            HorizontalDivider(modifier = Modifier.fillMaxWidth().padding(top = 4.dp))
            Text(text = content, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 4.dp))
        }
    }
}