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
import org.delcom.pam_p4_ifs23013.ui.viewmodels.AnimalViewModel

@Composable
fun AnimalsAddScreen(
    navController: NavHostController,
    snackbarHost: SnackbarHostState,
    animalViewModel: AnimalViewModel
) {
    // Memantau state aksi (Success/Error) dari ViewModel
    val actionState by animalViewModel.actionUIState.collectAsState()

    var isLoading by remember { mutableStateOf(false) }
    // PERBAIKAN: Gunakan var agar bisa diupdate
    var tmpAnimal by remember { mutableStateOf<ResponseAnimalData?>(null) }

    LaunchedEffect(Unit) {
        animalViewModel.clearActionState()
    }

    fun onSave(
        context: Context,
        nama: String,
        deskripsi: String,
        habitat: String,
        makananFavorit: String,
        file: Uri
    ) {
        isLoading = true
        val namaBody = nama.toRequestBodyText()
        val deskripsiBody = deskripsi.toRequestBodyText()
        val habitatBody = habitat.toRequestBodyText()
        val favoritBody = makananFavorit.toRequestBodyText()
        val filePart = uriToMultipart(context, file, "file")

        animalViewModel.postAnimal(
            nama = namaBody,
            deskripsi = deskripsiBody,
            habitat = habitatBody,
            makananFavorit = favoritBody,
            file = filePart,
        )
    }

    // Menangani perpindahan halaman jika berhasil, atau munculkan snackbar jika gagal
    LaunchedEffect(actionState) {
        when (actionState) {
            is AnimalActionUIState.Success -> {
                isLoading = false
                SuspendHelper.showSnackBar(
                    snackbarHost,
                    SnackBarType.SUCCESS,
                    (actionState as AnimalActionUIState.Success).message
                )
                navController.popBackStack()
            }
            is AnimalActionUIState.Error -> {
                isLoading = false
                SuspendHelper.showSnackBar(
                    snackbarHost,
                    SnackBarType.ERROR,
                    (actionState as AnimalActionUIState.Error).message
                )
            }
            else -> {}
        }
    }

    if (isLoading) {
        LoadingUI()
    } else {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            TopAppBarComponent(
                navController = navController,
                title = "Tambah Data",
                showBackButton = true,
            )
            Box(modifier = Modifier.weight(1f)) {
                AnimalsAddUI(
                    tmpAnimal = tmpAnimal,
                    onSave = ::onSave
                )
            }
            BottomNavComponent(navController = navController)
        }
    }
}

@Composable
fun AnimalsAddUI(
    tmpAnimal: ResponseAnimalData?,
    onSave: (Context, String, String, String, String, Uri) -> Unit
) {
    val alertState = remember { mutableStateOf(AlertState()) }
    // State untuk input form
    var dataFile by remember { mutableStateOf<Uri?>(null) }
    var dataNama by remember { mutableStateOf(tmpAnimal?.nama ?: "") }
    var dataDeskripsi by remember { mutableStateOf(tmpAnimal?.deskripsi ?: "") }
    var dataManfaat by remember { mutableStateOf(tmpAnimal?.habitat ?: "") }
    var dataEfekSamping by remember { mutableStateOf(tmpAnimal?.makananFavorit ?: "") }

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val deskripsiFocus = remember { FocusRequester() }
    val habitatFocus = remember { FocusRequester() }
    val efekFocus = remember { FocusRequester() }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri -> dataFile = uri }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Bagian Upload Gambar
        Box(
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.CenterHorizontally)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
                .clickable {
                    imagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                },
            contentAlignment = Alignment.Center
        ) {
            if (dataFile != null) {
                AsyncImage(
                    model = dataFile,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Text("Pilih Gambar", color = MaterialTheme.colorScheme.onPrimaryContainer)
            }
        }

        // Field Input
        OutlinedTextField(
            value = dataNama,
            onValueChange = { dataNama = it },
            label = { Text("Nama Hewan") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { deskripsiFocus.requestFocus() })
        )

        OutlinedTextField(
            value = dataDeskripsi,
            onValueChange = { dataDeskripsi = it },
            label = { Text("Deskripsi") },
            modifier = Modifier.fillMaxWidth().height(120.dp).focusRequester(deskripsiFocus),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { habitatFocus.requestFocus() })
        )

        OutlinedTextField(
            value = dataManfaat,
            onValueChange = { dataManfaat = it },
            label = { Text("Habitat") },
            modifier = Modifier.fillMaxWidth().height(120.dp).focusRequester(habitatFocus),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { efekFocus.requestFocus() })
        )

        OutlinedTextField(
            value = dataEfekSamping,
            onValueChange = { dataEfekSamping = it },
            label = { Text("Makanan Favorit") },
            modifier = Modifier.fillMaxWidth().height(120.dp).focusRequester(efekFocus),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
        )

        Spacer(modifier = Modifier.height(80.dp))
    }

    // Tombol Simpan
    Box(modifier = Modifier.fillMaxSize()) {
        FloatingActionButton(
            onClick = {
                if (dataFile == null) {
                    AlertHelper.show(alertState, AlertType.ERROR, "Gambar wajib dipilih!")
                } else if (dataNama.isEmpty()) {
                    AlertHelper.show(alertState, AlertType.ERROR, "Nama tidak boleh kosong!")
                } else {
                    onSave(context, dataNama, dataDeskripsi, dataManfaat, dataEfekSamping, dataFile!!)
                }
            },
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(Icons.Default.Save, contentDescription = "Simpan")
        }
    }

    // Alert Dialog untuk validasi lokal
    if (alertState.value.isVisible) {
        AlertDialog(
            onDismissRequest = { AlertHelper.dismiss(alertState) },
            title = { Text(alertState.value.type.title) },
            text = { Text(alertState.value.message) },
            confirmButton = {
                TextButton(onClick = { AlertHelper.dismiss(alertState) }) { Text("OK") }
            }
        )
    }
}