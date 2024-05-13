package com.udyata.koin

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.udyata.koin.ui.theme.KoinTheme

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

import androidx.lifecycle.viewmodel.compose.viewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {

//    private val sessionManager: SessionManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KoinTheme (
                darkTheme = false
            ){
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        UserScreen()
                        LocationsScreenV2()

                    }
                }
            }
        }
    }
}



@Composable
fun LocationsScreenV2(viewModel: MainViewModel= koinViewModel()) {
    val uiState by viewModel.locationUiState.collectAsStateWithLifecycle()

    if (uiState.isLoading()) {
        CircularProgressIndicator()
    } else if (uiState.isSuccess()) {
        val data = uiState.getSuccessData()
        LazyColumn {
            items(data.data ?: listOf()) { location ->
                Text(text = location.locationName ?: "Unknown Location")
            }
        }
    } else if (uiState.isError()) {
        Text(text = uiState.getErrorMessage() ?: "An error occurred")
    }

}


@Composable
fun UserScreen(viewModel: MainViewModel = koinViewModel()) {
    val uiState by viewModel.userState.collectAsStateWithLifecycle()

    if (uiState.isLoading()) {
        CircularProgressIndicator()
    } else if (uiState.isSuccess()) {
        val userData = uiState.getSuccessData()
        LazyColumn {
            item {
                Text(text = userData.data?.username ?: "No Username")
                Text(text = userData.data?.name ?: "No Name")
                Text(text = userData.data?.email ?: "No Email")
            }
        }
    } else if (uiState.isError()) {
        Text(text = uiState.getErrorMessage() ?: "An error occurred")
    }
}
