package com.example.yapechallenge.ui.screens.search

import android.app.appsearch.SearchResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yapechallenge.R
import com.example.yapechallenge.core.domain.SearchRecipe
import com.example.yapechallenge.ui.screens.detail.RecipeTitle
import com.example.yapechallenge.ui.screens.home.RecipeImage
import com.example.yapechallenge.ui.screens.home.RecipeMeta

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel,
    onIconBackClick: () -> Unit,
    onSearchResultClick: (Int) -> Unit
) {

    var query = remember {
        mutableStateOf("")
    }

    val searchHistory = searchViewModel.searchHistory.collectAsState(initial = emptyList())

    val searchResults = searchViewModel.searchResults.collectAsState(emptyList())

    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        Modifier
            .fillMaxHeight()
    ) {
        SearchView(modifier = Modifier.padding(top = 32.dp, start = 16.dp, end = 16.dp),
            onValueChange = {
                query.value = it
            },
            onIconBackClick = onIconBackClick,
            onSearchKeyboardClick = {
                keyboardController?.hide()
                searchViewModel.onSearchClick(query.value)
            }
        )
        Spacer(modifier = Modifier.padding(top = 16.dp))
        if (searchResults.value.isEmpty()) {
            InitialState(previousSearch = searchHistory.value, onSearchClick = {
                keyboardController?.hide()
            }) {
            }
        } else {
            SearchResult(recipeResult = searchResults.value, onRecipeClick = {
                onSearchResultClick.invoke(it.id)
            })
        }
    }
}

@Composable
fun InitialState(
    previousSearch: List<String>,
    onSearchClick: () -> Unit,
    onSearchSuggestionsClick: () -> Unit,
) {
    Column {
        RecentSearch(previousSearch, onSearchClick = onSearchClick)
        Spacer(modifier = Modifier.padding(top = 16.dp))
        com.example.yapechallenge.ui.screens.detail.DividerSection()
        Spacer(modifier = Modifier.padding(top = 16.dp))
        SearchSuggestions(modifier = Modifier.padding(start = 16.dp)) {
            onSearchSuggestionsClick.invoke()
        }
    }
}

@Composable
fun SearchView(
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    onIconBackClick: () -> Unit,
    onSearchKeyboardClick: () -> Unit
) {
    var mQuery by remember { mutableStateOf(TextFieldValue("")) }
    Row {
        Image(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = "back",
            modifier = modifier
                .align(Alignment.CenterVertically)
                .clickable {
                    onIconBackClick.invoke()
                }
        )
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
                maxLines = 1,
                placeholder = {
                    Text(text = "Search")
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    onSearchKeyboardClick.invoke()
                }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 48.dp, end = 16.dp)

            )
        }
    }
}

@Composable
fun RecentSearch(
    previousSearch: List<String>,
    modifier: Modifier = Modifier,
    onSearchClick: () -> Unit
) {
    SearchList(previousSearch, modifier, onSearchClick)
}

@Composable
fun SearchList(
    previousSearch: List<String>,
    modifier: Modifier = Modifier,
    onSearchClick: () -> Unit
) {
    LazyColumn(modifier = modifier) {
        items(previousSearch.size) { index ->
            SearchCard(previousSearch = previousSearch[index], onSearchClick = onSearchClick)
        }
    }
}

@Composable
fun SearchCard(previousSearch: String, onSearchClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSearchClick.invoke() }
            .padding(start = 16.dp, end = 16.dp, top = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            imageVector = Icons.Filled.Refresh, contentDescription = null,
            colorFilter = ColorFilter.tint(Color(0xff9fa5c0))
        )
        Text(
            text = previousSearch,
            textAlign = TextAlign.Left,
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xff3e5481)
            ),
            modifier = Modifier
                .width(250.dp)
                .alpha(.5f)
        )
        Image(
            imageVector = Icons.Filled.Info, contentDescription = null,
            colorFilter = ColorFilter.tint(Color(0xff9fa5c0))
        )
    }
}

@Composable
fun SearchSuggestions(modifier: Modifier, onSuggestionClick: () -> Unit) {
    Column(modifier = modifier
        .fillMaxWidth()
        .clickable { onSuggestionClick.invoke() }) {
        Text(
            text = "Search suggestions",
            textAlign = TextAlign.Left,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xff3e5481)
            ),
            modifier = Modifier.width(250.dp)
        )
        Spacer(modifier = Modifier.padding(8.dp))
        SuggestedRow("sushi", "sandwich")
        Spacer(modifier = Modifier.padding(8.dp))
        SuggestedRow("seafood", "fried rice")
    }
}

@Composable
fun SuggestedRow(text: String, text2: String) {
    Row(horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.width(150.dp)) {
        TextButton(
            onClick = {},
            enabled = false,
            modifier = Modifier.background(
                color = colorResource(id = R.color.search_color),
                shape = CircleShape
            )
        ) {
            Text(
                text = text,
                color = colorResource(id = R.color.chip_text),
                style = TextStyle(fontSize = 14.sp)
            )
        }
        TextButton(
            onClick = {},
            enabled = false,
            modifier = Modifier.background(
                color = colorResource(id = R.color.search_color),
                shape = CircleShape
            )
        ) {
            Text(
                text = text2,
                color = colorResource(id = R.color.chip_text),
                style = TextStyle(fontSize = 14.sp)
            )
        }
    }
}

@Composable
fun SearchResult(
    recipeResult: List<SearchRecipe>,
    modifier: Modifier = Modifier,
    onRecipeClick: (SearchRecipe) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier
    ) {
        items(recipeResult.size) { index ->
            SearchRecipeCard(searchRecipe = recipeResult[index], onRecipeClick = onRecipeClick)
        }
    }
}

@Composable
fun SearchRecipeCard(searchRecipe: SearchRecipe, onRecipeClick: (SearchRecipe) -> Unit) {
    Column(
        Modifier
            .clickable { onRecipeClick(searchRecipe) }
            .padding(16.dp)
    ) {
        RecipeImage(imageUrl = searchRecipe.imageUrl, contentDescription = searchRecipe.title)
        RecipeTitle(title = searchRecipe.title, modifier = Modifier)
        RecipeMeta(meta = "")
    }
}

@Preview
@Composable
fun InitialStatePreview() {
    InitialState(listOf("a", "a", "a"), {}, {})
}

@Preview
@Composable
fun SearchResultScreenPreview() {
    SearchResult(recipeResult = (1..4).map { SearchRecipe.DEFAULT }, onRecipeClick = {})
}

@Preview
@Composable
fun SearchViewPreview() {
    SearchView(modifier = Modifier.padding(top = 32.dp, start = 16.dp, end = 16.dp),
        onValueChange = {
            ""
        },
        onIconBackClick = {},
        onSearchKeyboardClick = {

        }
    )
}

@Preview
@Composable
fun SearchScreenPreview() {
    Column(
        Modifier
            .fillMaxHeight()
    ) {
        SearchView(modifier = Modifier.padding(top = 32.dp, start = 16.dp, end = 16.dp),
            onValueChange = {
                ""
            },
            onIconBackClick = {},
            onSearchKeyboardClick = {

            }
        )
        Spacer(modifier = Modifier.padding(top = 16.dp))
        //val searchResults = listOf<SearchRecipe>(SearchRecipe.DEFAULT)
        val searchResults = (1..8).map { SearchRecipe.DEFAULT }
        val searchHistory = listOf<String>()
        if (searchResults.isEmpty()) {
            InitialState(previousSearch = searchHistory, onSearchClick = {}) {}
        } else {
            SearchResult(recipeResult = searchResults, onRecipeClick = {

            })
        }
    }
}
