package com.example.yapechallenge.ui.screens.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.yapechallenge.ui.components.shimmerBrush
import com.example.yapechallenge.ui.screens.detail.BackButton
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.delay

@Composable
fun MapScreen(onBackIconClick: () -> Unit) {

    var showShimmer by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = true) {
        delay(500) // this delay simulates the loading of the map
        showShimmer = false
    }

    if (showShimmer) {
        Column(
            modifier = Modifier.background(
                shimmerBrush(
                    targetValue = 1300f,
                    showShimmer = showShimmer
                )
            ).fillMaxSize()
        ) {}
    } else {
        GoogleMapsSection(onBackIconClick)
    }
}



@Composable
fun GoogleMapsSection(onBackIconClick: () -> Unit) {
    // Set properties using MapProperties which you can use to recompose the map

    val peru = LatLng(-12.046374, -77.042793)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(peru, 30f)
    }

    val mapProperties by remember {
        mutableStateOf(
            MapProperties(maxZoomPreference = 10f, minZoomPreference = 5f)
        )
    }
    val mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(mapToolbarEnabled = false)
        )
    }
    Box(Modifier.fillMaxSize()) {
        GoogleMap(
            properties = mapProperties,
            uiSettings = mapUiSettings,
            cameraPositionState = cameraPositionState
        ) {
            Marker(
                state = rememberMarkerState(position = peru),
                title = "Marker1",
                snippet = "Marker in Peru",
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)
            )
        }
        BackButton(onIconClick = onBackIconClick)
    }
}

@Preview
@Composable
fun GoogleMapsSectionPreview() {
    GoogleMapsSection {}
}
