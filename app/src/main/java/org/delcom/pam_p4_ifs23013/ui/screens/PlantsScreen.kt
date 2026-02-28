package org.delcom.pam_p4_ifs23013.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.delcom.pam_p4_ifs23013.data.DummyData
import org.delcom.pam_p4_ifs23013.data.PlantData
import org.delcom.pam_p4_ifs23013.helper.ConstHelper
import org.delcom.pam_p4_ifs23013.helper.RouteHelper
import org.delcom.pam_p4_ifs23013.ui.components.BottomNavComponent
import org.delcom.pam_p4_ifs23013.ui.components.TopAppBarComponent
import org.delcom.pam_p4_ifs23013.ui.viewmodels.PlantViewModel

@Composable
fun PlantsScreen(
    navController: NavHostController,
    plantViewModel: PlantViewModel // Tetap dibiarkan jika sewaktu-waktu dipakai lagi
) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

    // Ubah tipe data state ke PlantData (dari DummyData)
    var plants by remember { mutableStateOf<List<PlantData>>(emptyList()) }

    // Fungsi Query/Pencarian manual dari DummyData
    fun fetchPlantsData() {
        val queryText = searchQuery.text.trim()
        val allPlants = DummyData.getPlantsData()

        plants = if (queryText.isEmpty()) {
            allPlants // Jika pencarian kosong, tampilkan semua
        } else {
            // Filter data yang namanya mengandung huruf yang dicari (abaikan huruf besar/kecil)
            allPlants.filter { it.nama.contains(queryText, ignoreCase = true) }
        }
    }

    // Picu pengambilan data saat halaman pertama kali dibuka
    LaunchedEffect(Unit) {
        fetchPlantsData()
    }

    // Fungsi klik jika ingin membuka detail
    fun onOpen(plantId: String) {
        // Karena PlantData tidak punya ID, kita pakai nama sebagai pengganti sementara
        RouteHelper.to(
            navController = navController,
            destination = "plants/${plantId}"
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top App Bar
        TopAppBarComponent(
            navController = navController,
            title = "Plants", showBackButton = false,
            withSearch = true,
            searchQuery = searchQuery,
            onSearchQueryChange = { query ->
                searchQuery = query
                // Jika ingin pencarian berjalan realtime (live search) saat diketik, uncomment baris bawah:
                // fetchPlantsData()
            },
            onSearchAction = {
                fetchPlantsData() // Pencarian dijalankan saat tombol enter/search di keyboard ditekan
            }
        )
        // Content
        Box(
            modifier = Modifier.weight(1f)
        ) {
            PlantsUI(
                plants = plants,
                onOpen = ::onOpen
            )

            // Floating Action Button
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                FloatingActionButton(
                    onClick = {
                        RouteHelper.to(
                            navController,
                            ConstHelper.RouteNames.PlantsAdd.path
                        )
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Tambah Tumbuhan"
                    )
                }
            }
        }
        // Bottom Nav
        BottomNavComponent(navController = navController)
    }
}

@Composable
fun PlantsUI(
    plants: List<PlantData>, // Ubah parameter menjadi List<PlantData>
    onOpen: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(plants) { plant ->
            PlantItemUI(
                plant = plant,
                onOpen = onOpen
            )
        }
    }

    if (plants.isEmpty()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Text(
                text = "Tidak ada data!",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}

@Composable
fun PlantItemUI(
    plant: PlantData, // Ubah parameter menjadi PlantData
    onOpen: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                // Kita mengirimkan nama sebagai parameter "ID" untuk saat ini
                onOpen(plant.nama)
            },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Kita ganti AsyncImage menjadi Image standar bawaan Compose untuk meload file drawable (Resource)
            Image(
                painter = painterResource(id = plant.gambar),
                contentDescription = plant.nama,
                modifier = Modifier
                    .size(70.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = plant.nama,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = plant.deskripsi,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}