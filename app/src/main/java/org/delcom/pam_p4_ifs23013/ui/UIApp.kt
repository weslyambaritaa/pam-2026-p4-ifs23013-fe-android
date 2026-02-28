package org.delcom.pam_p4_ifs23013.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.delcom.pam_p4_ifs23013.helper.ConstHelper
import org.delcom.pam_p4_ifs23013.ui.components.CustomSnackbar
import org.delcom.pam_p4_ifs23013.ui.screens.HomeScreen
import org.delcom.pam_p4_ifs23013.ui.screens.AnimalsAddScreen
import org.delcom.pam_p4_ifs23013.ui.screens.AnimalsDetailScreen
import org.delcom.pam_p4_ifs23013.ui.screens.AnimalsEditScreen
import org.delcom.pam_p4_ifs23013.ui.screens.AnimalsScreen
import org.delcom.pam_p4_ifs23013.ui.screens.PlantsAddScreen
import org.delcom.pam_p4_ifs23013.ui.screens.PlantsDetailScreen
import org.delcom.pam_p4_ifs23013.ui.screens.PlantsEditScreen
import org.delcom.pam_p4_ifs23013.ui.screens.PlantsScreen
import org.delcom.pam_p4_ifs23013.ui.screens.ProfileScreen
import org.delcom.pam_p4_ifs23013.ui.viewmodels.AnimalViewModel
import org.delcom.pam_p4_ifs23013.ui.viewmodels.PlantViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun UIApp(
    navController: NavHostController = rememberNavController(),
    animalViewModel: AnimalViewModel,
    plantViewModel: PlantViewModel // Ditambahkan untuk Plants
) {
    // Inisialisasi SnackbarHostState
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState){ snackbarData ->
            CustomSnackbar(snackbarData, onDismiss = { snackbarHostState.currentSnackbarData?.dismiss() })
        } },
    ) { _ ->
        NavHost(
            navController = navController,
            startDestination = ConstHelper.RouteNames.Home.path,
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF7F8FA))

        ) {
            // Home
            composable(
                route = ConstHelper.RouteNames.Home.path,
            ) { _ ->
                HomeScreen(
                    navController = navController,
                )
            }

            // Profile
            composable(
                route = ConstHelper.RouteNames.Profile.path,
            ) { _ ->
                ProfileScreen(
                    navController = navController,
                    animalViewModel = animalViewModel
                )
            }

            // ==================== ANIMALS ROUTE ====================

            // Animals
            composable(
                route = ConstHelper.RouteNames.Animals.path,
            ) { _ ->
                AnimalsScreen(
                    navController = navController,
                    animalViewModel = animalViewModel
                )
            }

            // Animals Add
            composable(
                route = ConstHelper.RouteNames.AnimalsAdd.path,
            ) { _ ->
                AnimalsAddScreen(
                    navController = navController,
                    snackbarHost = snackbarHostState,
                    animalViewModel = animalViewModel
                )
            }

            // Animals Detail
            composable(
                route = ConstHelper.RouteNames.AnimalsDetail.path,
                arguments = listOf(
                    navArgument("animalId") { type = NavType.StringType },
                )
            ) { backStackEntry ->
                val animalId = backStackEntry.arguments?.getString("animalId") ?: ""

                AnimalsDetailScreen(
                    navController = navController,
                    snackbarHost = snackbarHostState,
                    animalViewModel = animalViewModel,
                    animalId = animalId
                )
            }

            // Animals Edit
            composable(
                route = ConstHelper.RouteNames.AnimalsEdit.path,
                arguments = listOf(
                    navArgument("animalId") { type = NavType.StringType },
                )
            ) { backStackEntry ->
                val animalId = backStackEntry.arguments?.getString("animalId") ?: ""

                AnimalsEditScreen(
                    navController = navController,
                    snackbarHost = snackbarHostState,
                    animalViewModel = animalViewModel,
                    animalId = animalId
                )
            }

            // ==================== PLANTS ROUTE ====================

            // Plants
            composable(
                route = ConstHelper.RouteNames.Plants.path,
            ) { _ ->
                PlantsScreen(
                    navController = navController,
                    plantViewModel = plantViewModel
                )
            }

            // Plants Add
            composable(
                route = ConstHelper.RouteNames.PlantsAdd.path,
            ) { _ ->
                PlantsAddScreen(
                    navController = navController,
                    snackbarHost = snackbarHostState,
                    plantViewModel = plantViewModel
                )
            }

            // Plants Detail
            composable(
                route = ConstHelper.RouteNames.PlantsDetail.path,
                arguments = listOf(
                    navArgument("plantId") { type = NavType.StringType },
                )
            ) { backStackEntry ->
                // Perhatikan argumen di sini adalah "plantId" (sesuai ConstHelper jika sudah diatur demikian)
                val plantId = backStackEntry.arguments?.getString("plantId") ?: ""

                PlantsDetailScreen(
                    navController = navController,
                    snackbarHost = snackbarHostState,
                    plantViewModel = plantViewModel,
                    plantId = plantId
                )
            }

            // Plants Edit
            composable(
                route = ConstHelper.RouteNames.PlantsEdit.path,
                arguments = listOf(
                    navArgument("plantId") { type = NavType.StringType },
                )
            ) { backStackEntry ->
                val plantId = backStackEntry.arguments?.getString("plantId") ?: ""

                PlantsEditScreen(
                    navController = navController,
                    snackbarHost = snackbarHostState,
                    plantViewModel = plantViewModel,
                    plantId = plantId
                )
            }
        }
    }
}