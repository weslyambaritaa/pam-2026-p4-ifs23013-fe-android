package org.delcom.pam_p4_ifs23013.ui.screens

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import okhttp3.MultipartBody
import org.delcom.pam_p4_ifs23013.R
import org.delcom.pam_p4_ifs23013.helper.*
import org.delcom.pam_p4_ifs23013.helper.SuspendHelper.SnackBarType
import org.delcom.pam_p4_ifs23013.helper.ToolsHelper.toRequestBodyText
import org.delcom.pam_p4_ifs23013.helper.ToolsHelper.uriToMultipart
import org.delcom.pam_p4_ifs23013.network.animals.data.ResponseAnimalData
import org.delcom.pam_p4_ifs23013.ui.components.BottomNavComponent
import org.delcom.pam_p4_ifs23013.ui.components.LoadingUI
import org.delcom.pam_p4_ifs23013.ui.components.TopAppBarComponent
import org.delcom.pam_p4_ifs23013.ui.viewmodels.AnimalActionUIState
import org.delcom.pam_p4_ifs23013.ui.viewmodels.AnimalUIState
import org.delcom.pam_p4_ifs23013.ui.viewmodels.AnimalViewModel

@Composable
fun AnimalsEditScreen(
    navController: NavHostController,
    snackbarHost: SnackbarHostState,
    animalViewModel: AnimalViewModel,
    animalId: String
) {
    val uiStateAnimal by animalViewModel.uiState.collectAsState()
    val actionState by animalViewModel.actionUIState.collectAsState() // Tambahan untuk deteksi success/error

    var isLoading by remember { mutableStateOf(false) }
    var animal by remember { mutableStateOf<ResponseAnimalData?>(null) }

    LaunchedEffect(Unit) {
        isLoading = true
        animalViewModel.getAnimalById(animalId)
        animalViewModel.clearActionState()
    }

    LaunchedEffect(uiStateAnimal.animal) {
        val state = uiStateAnimal.animal
        if (state is AnimalUIState.Success) {
            animal = state.data
            isLoading = false
        } else if (state is AnimalUIState.Error) {
            RouteHelper.back(navController)
            isLoading = false
        }
    }

    fun onSave(context: Context, nama: String, deskripsi: String, habitat: String, makananFavorit: String, file: Uri? = null) {
        isLoading = true
        val namaBody = nama.toRequestBodyText()
        val deskripsiBody = deskripsi.toRequestBodyText()
        val habitatBody = habitat.toRequestBodyText()
        val favoritBody = makananFavorit.toRequestBodyText()

        var filePart: MultipartBody.Part? = null
        if (file != null) {
            filePart = uriToMultipart(context, file, "file")
        }

        animalViewModel.putAnimal(
            animalId = animalId,
            nama = namaBody,
            deskripsi = deskripsiBody,
            habitat = habitatBody,
            makananFavorit = favoritBody,
            file = filePart,
        )
    }

    // Pantau hasil dari putAnimal
    LaunchedEffect(actionState) {
        when (actionState) {
            is AnimalActionUIState.Success -> {
                isLoading = false
                SuspendHelper.showSnackBar(snackbarHost, SnackBarType.SUCCESS, (actionState as AnimalActionUIState.Success).message)
                navController.popBackStack()
            }
            is AnimalActionUIState.Error -> {
                isLoading = false
                SuspendHelper.showSnackBar(snackbarHost, SnackBarType.ERROR, (actionState as AnimalActionUIState.Error).message)
            }
            else -> {}
        }
    }

    if (isLoading || animal == null) {
        LoadingUI()
        return
    }

    Column(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background)) {
        TopAppBarComponent(navController = navController, title = "Ubah Data", showBackButton = true)
        Box(modifier = Modifier.weight(1f)) {
            AnimalsEditUI(animal = animal!!, onSave = ::onSave)
        }
        BottomNavComponent(navController = navController)
    }
}

@Composable
fun AnimalsEditUI(animal: ResponseAnimalData, onSave: (Context, String, String, String, String, Uri?) -> Unit) {
    val alertState = remember { mutableStateOf(AlertState()) }

    // PERBAIKAN: Menggunakan 'var' untuk state agar bisa diperbarui (Fix error compile)
    var dataFile by remember { mutableStateOf<Uri?>(null) }
    var dataNama by remember { mutableStateOf(animal.nama) }
    var dataDeskripsi by remember { mutableStateOf(animal.deskripsi) }
    var dataManfaat by remember { mutableStateOf(animal.habitat) }
    var dataEfekSamping by remember { mutableStateOf(animal.makananFavorit) }

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val deskripsiFocus = remember { FocusRequester() }

    val imagePicker = rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) { uri ->
        dataFile = uri
    }

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Form Gambar
        Box(
            modifier = Modifier.size(150.dp).align(Alignment.CenterHorizontally).clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.primaryContainer).clickable {
                    imagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                },
            contentAlignment = Alignment.Center
        ) {
            val imageSource = dataFile ?: ToolsHelper.getAnimalImageUrl(animal.id)
            AsyncImage(
                model = imageSource,
                placeholder = painterResource(R.drawable.img_placeholder),
                error = painterResource(R.drawable.img_placeholder),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        // Form Inputs
        OutlinedTextField(value = dataNama, onValueChange = { dataNama = it }, label = { Text("Nama") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = dataDeskripsi, onValueChange = { dataDeskripsi = it }, label = { Text("Deskripsi") }, modifier = Modifier.fillMaxWidth().height(120.dp))
        OutlinedTextField(value = dataManfaat, onValueChange = { dataManfaat = it }, label = { Text("Habitat") }, modifier = Modifier.fillMaxWidth().height(120.dp))
        OutlinedTextField(value = dataEfekSamping, onValueChange = { dataEfekSamping = it }, label = { Text("Makanan Favorit") }, modifier = Modifier.fillMaxWidth().height(120.dp))

        Spacer(modifier = Modifier.height(64.dp))
    }

    Box(modifier = Modifier.fillMaxSize()) {
        FloatingActionButton(
            onClick = {
                if (dataNama.isEmpty()) AlertHelper.show(alertState, AlertType.ERROR, "Nama wajib diisi!")
                else onSave(context, dataNama, dataDeskripsi, dataManfaat, dataEfekSamping, dataFile)
            },
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)
        ) {
            Icon(Icons.Default.Save, contentDescription = "Simpan")
        }
    }

    if (alertState.value.isVisible) {
        AlertDialog(
            onDismissRequest = { AlertHelper.dismiss(alertState) },
            title = { Text(alertState.value.type.title) },
            text = { Text(alertState.value.message) },
            confirmButton = { TextButton(onClick = { AlertHelper.dismiss(alertState) }) { Text("OK") } }
        )
    }
}