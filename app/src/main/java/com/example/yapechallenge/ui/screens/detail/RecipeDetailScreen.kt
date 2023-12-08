package com.example.yapechallenge.ui.screens.detail

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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.yapechallenge.R
import com.example.yapechallenge.core.domain.Recipe
import com.example.yapechallenge.ui.components.shimmerBrush
import kotlinx.coroutines.Dispatchers

@Composable
fun RecipeDetailScreen(
    recipeDetailViewModel: RecipeDetailViewModel,
    id: Int?,
    onIconClick: () -> Unit,
    onIconMapClick: () -> Unit
) {

    val recipeEntry = recipeDetailViewModel.recipe.collectAsState(initial = Recipe.DEFAULT)
    val isLoading = recipeDetailViewModel.isLoading.collectAsState(initial = true)

    recipeDetailViewModel.getRecipeDetail(id ?: 0)
    RecipeDetail(
        recipeEntry.value,
        isLoading.value,
        onIconClick = onIconClick,
        onIconMapClick = onIconMapClick
    )
}

@Composable
fun RecipeDetail(
    recipe: Recipe,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    onIconClick: () -> Unit,
    onIconMapClick: () -> Unit
) {
    Column {
        RecipeImageHeader(isLoading, recipe.imageUrl, recipe.name, onIconClick)
        RecipeInfoCard(modifier, recipe, onIconMapClick)
    }
}

@Composable
fun RecipeImageHeader(
    isLoading: Boolean,
    imageUrl: String,
    description: String,
    onIconClick: () -> Unit
) {
    Box {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .dispatcher(Dispatchers.IO)
                .memoryCacheKey(imageUrl)
                .diskCacheKey(imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = description,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .height(250.dp)
                .fillMaxWidth()
                .background(shimmerBrush(targetValue = 1300f, showShimmer = isLoading))
        )
        BackButton(onIconClick = onIconClick)
    }
}

@Composable
fun BackButton(onIconClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .padding(start = 24.dp, top = 32.dp)
            .clickable { onIconClick.invoke() }) {
        Box(
            modifier = modifier
                .requiredSize(size = 56.dp)
                .clip(shape = RoundedCornerShape(28.dp))
                .background(color = Color.White.copy(alpha = 0.5f))
        ) {
            Image(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .padding(16.dp),
                colorFilter = ColorFilter.tint(Color.White)
            )
        }
    }
}

@Composable
fun RecipeInfoCard(modifier: Modifier = Modifier, recipe: Recipe, onIconClick: () -> Unit) {
    val scrollState = rememberScrollState()
    Box(
        modifier = modifier
            .offset(y = -40.dp)
            .clip(RoundedCornerShape(topEnd = 32.dp, topStart = 32.dp))
            .background(Color.White)
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Column(Modifier.padding(horizontal = 24.dp)) {
            RecipeTitle(title = recipe.name, modifier)
            RecipeDetails(detailText = "Food · > ${recipe.timePreparation} mins", modifier)
            Spacer(modifier = Modifier.height(8.dp))
            UserLikesSection(likes = 273, modifier)
            Spacer(modifier = Modifier.height(8.dp))
            UserMapsSection(modifier, onIconClick = onIconClick)
            DividerSection()
            RecipeSectionTitle(title = "Description", modifier)
            Spacer(modifier = Modifier.height(6.dp))
            RecipeDescription(description = recipe.description, modifier)
            DividerSection()
            RecipeSectionTitle(title = "Ingredients", modifier)
            Spacer(modifier = Modifier.height(8.dp))
            recipe.ingredients.forEach { ingredient ->
                IngredientItem(ingredient, modifier)
            }
        }
    }
}

@Composable
fun RecipeTitle(title: String, modifier: Modifier) {
    Text(
        text = title,
        color = Color(0xff3e5481),
        lineHeight = 1.59.em,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        style = TextStyle(
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        ),
        modifier = modifier.padding(top = 24.dp)
    )
}

@Composable
fun RecipeDetails(detailText: String, modifier: Modifier) {
    Text(
        text = detailText,
        style = TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.chip_text)
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    )
}

@Composable
fun UserLikesSection(likes: Int, modifier: Modifier) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        BoxedIcon(Icons.Filled.Favorite)
        Text(
            text = "$likes likes",
            color = Color(0xff3e5481),
            lineHeight = 1.59.em,
            style = TextStyle(
                fontSize = 17.sp,
                fontWeight = FontWeight.Normal,
                letterSpacing = 0.5.sp
            ),
            modifier = modifier.padding(start = 8.dp)
        )
    }
}

@Composable
fun UserMapsSection(modifier: Modifier, onIconClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { onIconClick.invoke() }) {
        BoxedIcon(Icons.Filled.LocationOn)
        Text(
            text = "See location",
            color = Color(0xff3e5481),
            lineHeight = 1.59.em,
            style = TextStyle(
                fontSize = 17.sp,
                fontWeight = FontWeight.Normal,
                letterSpacing = 0.5.sp
            ),
            modifier = modifier.padding(start = 8.dp)
        )
    }
}

@Composable
fun DividerSection() {
    Spacer(modifier = Modifier.height(6.dp))
    Divider(
        color = colorResource(id = R.color.search_color),
        thickness = 1.dp
    )
    Spacer(modifier = Modifier.height(6.dp))
}

@Composable
fun RecipeSectionTitle(title: String, modifier: Modifier) {
    Text(
        text = title,
        color = Color(0xff3e5481),
        lineHeight = 1.59.em,
        style = TextStyle(
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        ),
        modifier = modifier
    )
}

@Composable
fun RecipeDescription(description: String, modifier: Modifier) {
    val spanned = HtmlCompat.fromHtml(description, HtmlCompat.FROM_HTML_MODE_COMPACT)
    Text(
        text = spanned.toString(),
        color = Color(0xff9fa5c0),
        lineHeight = 1.67.em,
        style = TextStyle(
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.5.sp
        ),
        textAlign = TextAlign.Justify,
        modifier = modifier
    )
}

@Composable
fun IngredientItem(ingredient: String, modifier: Modifier) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        BoxedIcon(Icons.Filled.Check)
        Text(
            text = ingredient,
            color = Color(0xff3e5481),
            lineHeight = 1.59.em,
            style = TextStyle(
                fontSize = 17.sp,
                fontWeight = FontWeight.Normal,
                letterSpacing = 0.5.sp
            ),
            modifier = modifier.padding(start = 8.dp, top = 6.dp, bottom = 6.dp)
        )
    }
}

@Composable
fun BoxedIcon(icon: ImageVector, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .requiredSize(size = 32.dp)
            .clip(shape = RoundedCornerShape(100.dp))
            .background(color = Color(0xffe3fff8))
    ) {
        Image(
            imageVector = icon,
            colorFilter = ColorFilter.tint(Color(0xff1fcc79)),
            contentDescription = null,
            modifier = Modifier
                .align(alignment = Alignment.Center)

                .requiredSize(size = 18.dp)
        )
    }
}


@Preview
@Composable
fun RecipeImageHeaderPreview() {
    RecipeImageHeader(false, "", Recipe.DEFAULT.name, {})
}

@Preview
@Composable
fun RecipeInfoCardPreview() {
    RecipeInfoCard(Modifier, Recipe.DEFAULT, {})
}

@Preview
@Composable
fun RecipeDetailPreview() {
    RecipeDetail(Recipe.DEFAULT, false, onIconClick = {}, onIconMapClick = {})
}

