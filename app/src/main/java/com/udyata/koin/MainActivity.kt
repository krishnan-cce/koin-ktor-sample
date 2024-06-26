package com.udyata.koin

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

import androidx.lifecycle.viewmodel.compose.viewModel
import com.udyata.koin.auth.OnTokenRequest
import org.koin.android.ext.android.inject
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {

    private val sessionManager: SessionManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KoinTheme (
                darkTheme = false
            ){
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    val viewModel: MainViewModel= koinViewModel()

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        UserScreen(viewModel)
                        LocationsScreenV2(viewModel)
                        Button(
                            shape = RectangleShape,
                            onClick = {
                                val request = OnTokenRequest(
                                    usernameOrEmail = "admin",
                                    password = "123456"
                                )
                                viewModel.onLogin(request)
                            }) {
                            Text(text = "Login")
                        }
                        OutlinedButton(
                            shape = RectangleShape,
                            onClick = {
                            viewModel.getUser()
                        }) {
                            Text(text = "Call API")
                        }
                        Button(
                            shape = RectangleShape,
                            onClick = {
                                viewModel.onAddStock()
                            }) {
                            Text(text = "Add Stock")
                        }
                        Text(text = sessionManager.jwtToken)
                    }
                }
            }
        }
    }
}



@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LocationsScreenV2(viewModel: MainViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column {
        TextField(
            value = state.searchQuery,
            onValueChange = { query -> viewModel.onEvent(LocationEvent.UpdateSearchQuery(query)) },
            label = { Text("Search") }
        )

        FlowRow {
            Button(onClick = { viewModel.onEvent(LocationEvent.ToggleSortType) }) {
                Text(if (state.sortType == Sort.ByName) "Remove Sort" else "Sort by Name")
            }
            Button(onClick = { viewModel.onEvent(LocationEvent.ToggleSortOrder) }) {
                Text(if (state.sortOrder == SortOrder.ASCENDING) "Sort Descending" else "Sort Ascending")
            }
            Button(onClick = { viewModel.onEvent(LocationEvent.ToggleGroupType) }) {
                Text(if (state.groupType == Group.ByFirstLetter) "Remove Grouping" else "Group by First Letter")
            }
        }

        when (val uiState = state.locationUiState) {
            is RequestState.Loading -> CircularProgressIndicator()
            is RequestState.Success -> {
                val response = uiState.data
                Log.d("LocationsScreenV2", "Success with data: ${response.data}")

                val filteredData = response.filter(
                    if (state.searchQuery.isNotEmpty()) Filter.ByQuery(state.searchQuery) else Filter.None
                )
                Log.d("LocationsScreenV2", "Filtered data: $filteredData")

                val sortedData = response.sort(filteredData, state.sortType, state.sortOrder)
                Log.d("LocationsScreenV2", "Sorted data: $sortedData")

                val groupedData = response.group(sortedData, state.groupType)
                Log.d("LocationsScreenV2", "Grouped data: $groupedData")

                if (state.groupType is Group.ByFirstLetter && groupedData != null) {
                    LazyColumn {
                        groupedData.forEach { (letter, locations) ->
                            item {
                                Text(text = letter.toString(), style = MaterialTheme.typography.bodyMedium)
                            }
                            items(locations) { location ->
                                Text(text = location.locationName ?: "Unknown Location")
                            }
                        }
                    }
                } else {
                    LazyColumn {
                        items(sortedData ?: listOf()) { location ->
                            Text(text = location.locationName ?: "Unknown Location")
                        }
                    }
                }
            }
            is RequestState.Error -> {
                Text(text = uiState.message ?: "An error occurred")
            }
            is RequestState.Idle -> {}
        }
    }
}








@Composable
fun UserScreen(viewModel: MainViewModel) {
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
