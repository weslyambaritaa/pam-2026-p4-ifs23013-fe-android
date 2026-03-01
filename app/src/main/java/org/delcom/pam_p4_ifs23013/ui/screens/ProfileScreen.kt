package org.delcom.pam_p4_ifs23013.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import org.delcom.pam_p4_ifs23013.R
import org.delcom.pam_p4_ifs23013.ui.components.BottomNavComponent
import org.delcom.pam_p4_ifs23013.ui.components.TopAppBarComponent
import org.delcom.pam_p4_ifs23013.ui.theme.DelcomTheme
import org.delcom.pam_p4_ifs23013.ui.viewmodels.AnimalViewModel

@Composable
fun ProfileScreen(
    navController: NavHostController,
    animalViewModel: AnimalViewModel // Tetap dipertahankan sebagai parameter agar tidak error di file UIApp.kt
) {
    // Karena datanya di-hardcode, kita tidak perlu lagi memanggil API dan LoadingUI.
    // Layar akan langsung menampilkan data secara instan.

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top App Bar
        TopAppBarComponent(navController = navController, title = "Profile", showBackButton = false)

        // Content
        Box(
            modifier = Modifier.weight(1f)
        ) {
            ProfileUI()
        }

        // Bottom Nav
        BottomNavComponent(navController = navController)
    }
}

@Composable
fun ProfileUI(){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {

        // Header Profile
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, bottom = 16.dp),
            contentAlignment = Alignment.Center
        ) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                // Foto Profil (Menggunakan file foto_profile.jpg di drawable)
                Image(
                    painter = painterResource(id = R.drawable.foto_profile),
                    contentDescription = "Photo Profil",
                    contentScale = ContentScale.Crop, // Memastikan gambar terpotong rapi menjadi lingkaran
                    modifier = Modifier
                        .size(110.dp)
                        .clip(CircleShape)
                        .border(3.dp, Color.White, CircleShape)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Nama Anda
                Text(
                    text = "Wesly Fery Wanda Ambarita",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                // NIM Anda
                Text(
                    text = "11S23013",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Bio Section
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Tentang Saya",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Deskripsi / Bio Anda
                Text(
                    text = "Mahasiswa S1 Informatics. Memiliki minat yang besar di bidang pengembangan perangkat lunak, termasuk Web Development dan Android Development.",
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun PreviewProfileUI(){
    DelcomTheme {
        ProfileUI()
    }
}