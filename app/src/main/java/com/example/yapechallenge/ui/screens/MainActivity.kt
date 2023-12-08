package com.example.yapechallenge.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.yapechallenge.ui.screens.home.RecipesViewModel
import com.example.yapechallenge.ui.Routes
import com.example.yapechallenge.ui.screens.map.MapScreen
import com.example.yapechallenge.ui.screens.detail.RecipeDetailScreen
import com.example.yapechallenge.ui.screens.detail.RecipeDetailViewModel
import com.example.yapechallenge.ui.screens.home.RecipesScreen
import com.example.yapechallenge.ui.screens.search.SearchViewModel
import com.example.yapechallenge.ui.screens.search.SearchScreen
import com.example.yapechallenge.ui.theme.YapeChallengeTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    var showSplash = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition{
            showSplash
        }

        setContent {
            YapeChallengeTheme {

                val snackbarHostState = remember { SnackbarHostState() }
                val scope = rememberCoroutineScope()
                val scaffoldState = rememberScaffoldState()
                val navController = rememberNavController()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    scaffoldState = scaffoldState,
                    content = { _ ->
                        NavHost(
                            navController = navController,
                            startDestination = Routes.MAIN.screen
                        ) {
                            composable(Routes.MAIN.screen) {

                                val recipesViewModel: RecipesViewModel = hiltViewModel()

                                RecipesScreen(recipesViewModel, {
                                    scope.launch {
                                        navController.navigate(
                                            Routes.DETAIL.screen.replace(
                                                "{id}",
                                                "${it.id}"
                                            )
                                        )
                                    }
                                }, {
                                    navController.navigate(Routes.SEARCH.screen)
                                },{
                                    showSplash = it
                                })
                            }
                            composable(
                                Routes.DETAIL.screen,
                                arguments = listOf(navArgument("id") { type = NavType.IntType })
                            ) { backStackEntry ->
                                val recipeDetailViewModel: RecipeDetailViewModel = hiltViewModel()
                                val id = backStackEntry.arguments?.getInt("id")
                                RecipeDetailScreen(
                                    recipeDetailViewModel = recipeDetailViewModel,
                                    id = id,
                                    onIconClick = {
                                        navController.popBackStack()
                                    },
                                    onIconMapClick = {
                                        navController.navigate(Routes.MAP.screen)
                                    })
                            }
                            composable(Routes.MAP.screen) {
                                MapScreen {
                                    navController.popBackStack()
                                }
                            }
                            composable(
                                Routes.SEARCH.screen
                            ) {
                                val searchViewModel: SearchViewModel = hiltViewModel()

                                SearchScreen(
                                    searchViewModel = searchViewModel,
                                    onIconBackClick = {
                                        navController.popBackStack()
                                    },
                                    onSearchResultClick = { id ->
                                        navController.navigate(
                                            route = Routes.DETAIL.screen.replace(
                                                "{id}",
                                                "$id"
                                            )
                                        )
                                    })
                            }
                        }
                    },
                    snackbarHost = {
                        SnackbarHost(hostState = snackbarHostState)
                    }
                )
            }
        }
    }
}