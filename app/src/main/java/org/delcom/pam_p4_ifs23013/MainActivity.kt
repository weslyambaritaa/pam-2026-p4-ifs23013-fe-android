package org.delcom.pam_p4_ifs23013

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.delcom.pam_p4_ifs23013.ui.UIApp
import org.delcom.pam_p4_ifs23013.ui.theme.DelcomTheme
import org.delcom.pam_p4_ifs23013.ui.viewmodels.AnimalViewModel
import org.delcom.pam_p4_ifs23013.ui.viewmodels.PlantViewModel // 1. Tambahkan import PlantViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val animalViewModel: AnimalViewModel by viewModels()
    private val plantViewModel: PlantViewModel by viewModels() // 2. Inisialisasi PlantViewModel menggunakan delegasi by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DelcomTheme {
                UIApp(
                    animalViewModel = animalViewModel,
                    plantViewModel = plantViewModel // 3. Masukkan plantViewModel ke dalam UIApp
                )
            }
        }
    }
}