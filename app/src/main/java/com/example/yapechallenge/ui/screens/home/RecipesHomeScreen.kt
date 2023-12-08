package com.example.yapechallenge.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.BottomAppBar
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.yapechallenge.R
import com.example.yapechallenge.core.domain.Recipe
import com.example.yapechallenge.ui.components.shimmerBrush
import com.example.yapechallenge.ui.screens.detail.RecipeTitle
import kotlinx.coroutines.Dispatchers


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RecipesScreen(
    viewModel: RecipesViewModel,
    onRecipeClick: (Recipe) -> Unit,
    onSearchClick: () -> Unit
) {
    val recipes = viewModel.recipes.collectAsState(initial = emptyList())
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        bottomBar = {
            CustomBottomAppBar()
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
        floatingActionButton = {
            CustomFloatingActionButton()
        },
        content = {
            Column(
                Modifier
                    .fillMaxHeight()
                    .padding(it)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        keyboardController?.hide()
                    }
            ) {
                Searchbar(
                    modifier = Modifier
                        .padding(top = 32.dp, start = 16.dp, end = 16.dp)
                        .clickable {
                            onSearchClick.invoke()
                        },
                    onValueChange = {

                    }

                )
                CategorySelector(
                    modifier = Modifier.padding(top = 32.dp, start = 16.dp, end = 16.dp),
                    onCategorySelected = {

                    },
                    categories = listOf("All", "Food", "Drink"),
                    selectedCategory = "All"
                )
                DividerSection(Modifier.padding(top = 32.dp))
                RecipesGrid(
                    recipes = recipes.value,
                    onRecipeClick = onRecipeClick,
                    modifier = Modifier.padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 8.dp,
                        bottom = 8.dp
                    )
                )
            }
        }
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Searchbar(modifier: Modifier = Modifier, onValueChange: (String) -> Unit) {
    var mQuery by remember { mutableStateOf(TextFieldValue("")) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        modifier = modifier
            .background(colorResource(id = R.color.search_color), CircleShape)
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp)
    ) {
        Image(
            imageVector = Icons.Filled.Search,
            contentDescription = "search",
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 8.dp)
        )
        TextField(
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Black,
                disabledTextColor = Color.Transparent,
                backgroundColor = colorResource(id = R.color.search_color),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            value = mQuery.text,
            onValueChange = { newValue ->
                mQuery = TextFieldValue(newValue)
                onValueChange(newValue)
            },
            enabled = false,
            maxLines = 1,
            placeholder = {
                Text(text = "Search")
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                keyboardController?.hide()
            }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 48.dp, end = 16.dp)

        )
    }
}


@Composable
fun CategorySelector(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Category",
            style = TextStyle(
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.width(200.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            categories.forEach { category ->
                val isSelected = category == selectedCategory
                TextButton(
                    onClick = { onCategorySelected(category) },
                    modifier = Modifier
                        .background(
                            if (isSelected) colorResource(id = R.color.chip_color) else colorResource(
                                id = R.color.search_color
                            ),
                            CircleShape
                        )
                ) {
                    Text(
                        text = category,
                        color = if (isSelected) Color.White else colorResource(id = R.color.chip_text),
                        style = TextStyle(fontSize = 14.sp)
                    )
                }
            }
        }
    }
}

@Composable
fun DividerSection(modifier: Modifier = Modifier) {
    Spacer(
        modifier = modifier
            .background(colorResource(id = R.color.search_color))
            .fillMaxWidth()
            .height(1.dp)
    )
}

@Composable
fun RecipesGrid(
    recipes: List<Recipe>,
    modifier: Modifier = Modifier,
    onRecipeClick: (Recipe) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier
    ) {
        items(recipes.size) { index ->
            RecipeCard(recipe = recipes[index], modifier, onRecipeClick = onRecipeClick)
        }
    }
}

@Composable
fun RecipeCard(recipe: Recipe, modifier: Modifier = Modifier, onRecipeClick: (Recipe) -> Unit) {
    Column(
        modifier
            .clickable { onRecipeClick(recipe) }
    ) {
        RecipeImage(imageUrl = recipe.imageUrl, contentDescription = recipe.name)
        RecipeTitle(title = recipe.name, modifier = modifier)
        RecipeMeta(meta = "Food · > ${recipe.timePreparation} mins")
    }
}

@Composable
fun RecipeImage(imageUrl: String, contentDescription: String) {
    val showShimmer = remember { mutableStateOf(true) }
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .dispatcher(Dispatchers.IO)
            .memoryCacheKey(imageUrl)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCacheKey(imageUrl)
            .crossfade(false)
            .build(),
        error = painterResource(R.drawable.baseline_broken_image),
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
        onSuccess = { showShimmer.value = false },
        onError = { showShimmer.value = false },
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .fillMaxWidth()
            .background(shimmerBrush(targetValue = 1300f, showShimmer = showShimmer.value))
    )
}

@Composable
fun CustomBottomAppBar() {
    BottomAppBar(
        cutoutShape = CircleShape,
        backgroundColor = Color.White,
        contentColor = Color.Black,
        elevation = 8.dp,
        modifier = Modifier.height(68.dp),
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    IconButton(onClick = {

                    }) {
                        Icon(Icons.Filled.Home, contentDescription = "Home")
                    }
                    Text(text = "Home")
                }

                Spacer(Modifier.weight(1f, false))

                Column {
                    IconButton(onClick = {

                    }) {
                        Icon(Icons.Filled.Favorite, contentDescription = "Favorites")
                    }
                    Text(text = "Favorites")
                }
            }
        }
    )
}


@Composable
fun CustomFloatingActionButton() {
    FloatingActionButton(
        onClick = { },
        backgroundColor = colorResource(id = R.color.chip_color)
    ) {

        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.scan),
            contentDescription = "Scan",
            tint = Color.White
        )
    }
}

@Composable
fun RecipeMeta(meta: String) {
    Text(
        text = meta,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        style = TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.chip_text)
        )
    )
}

@Preview
@Composable
fun RecipesScreenPreview() {
    Scaffold(
        bottomBar = {
            CustomBottomAppBar()
        },
        floatingActionButton = {
            CustomFloatingActionButton()
        },
        floatingActionButtonPosition = FabPosition.Center,
        content = {
            Column(
                Modifier
                    .fillMaxHeight()
                    .padding(it)
            ) {
                Searchbar(
                    modifier = Modifier
                        .padding(top = 32.dp, start = 16.dp, end = 16.dp),
                    onValueChange = {

                    }
                )
                CategorySelector(
                    modifier = Modifier.padding(top = 32.dp, start = 16.dp, end = 16.dp),
                    onCategorySelected = {

                    },
                    categories = listOf("All", "Food", "Drink"),
                    selectedCategory = "All"
                )
                DividerSection(Modifier.padding(top = 32.dp))
                RecipesGrid(
                    recipes = (1..12).map { Recipe.DEFAULT },
                    onRecipeClick = {},
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    )
}


