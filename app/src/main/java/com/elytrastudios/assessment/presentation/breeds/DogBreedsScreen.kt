package com.elytrastudios.assessment.presentation.breeds

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.material3.DropdownMenu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.elytrastudios.assessment.MainActivity
import com.elytrastudios.assessment.data.model.DogBreed
import kotlinx.coroutines.launch
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import com.elytrastudios.assessment.presentation.common.shimmerEffect
import com.elytrastudios.assessment.util.PollingStatusManager
import kotlinx.coroutines.awaitCancellation


@Composable
fun DogBreedsScreen(viewModel: DogBreedsViewModel = hiltViewModel()) {

    val lifecycleOwner = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var expandedIndex by remember { mutableStateOf<Int?>(null) }

    val state by viewModel.state.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val lastUpdated by PollingStatusManager.lastFetchTime.collectAsState()

    var showFilterMenu by remember { mutableStateOf(false) }
    var isRefreshing by remember { mutableStateOf(false) }
    val pullToRefreshState = rememberPullToRefreshState()


// Lifecycle-aware polling: starts when lifecycle enters STARTED, stops when cancelled
    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.startPolling()
            try {
                awaitCancellation() // Keeps coroutine alive until lifecycle stops
            } finally {
                viewModel.stopPolling() // Ensures cleanup when lifecycle ends
            }
        }
    }

// Handle back press to exit app
    BackHandler {
        (context as? MainActivity)?.finish()
    }

    Scaffold(
        topBar =
            {
            Row(
                modifier = Modifier .fillMaxWidth().padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Search field bound to ViewModel query
                TextField(
                    value = searchQuery,
                    onValueChange = { viewModel.updateSearchQuery(it) },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Search breeds...")
                    }
                )
                IconButton(onClick = { showFilterMenu = true }) {
                    Icon( imageVector = Icons.Default.FilterList,
                        contentDescription = "Filter" )
                }

                // Dropdown filter menu for sorting
                DropdownMenu(
                    expanded = showFilterMenu,
                    onDismissRequest = { showFilterMenu = false },
                    modifier = Modifier .fillMaxWidth().padding(8.dp),
                ) {
                    DropdownMenuItem(
                        text = { Text("Ascending") },
                        onClick = {
                            viewModel.updateSortOrder(SortOrder.ASCENDING)
                            showFilterMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Descending") },
                        onClick = {
                            viewModel.updateSortOrder(SortOrder.DESCENDING)
                            showFilterMenu = false
                        }
                    )
                }
            }


        }
    ) { paddingValues ->
        PullToRefreshBox(
            state = pullToRefreshState,
            isRefreshing = isRefreshing,
            onRefresh = {
                isRefreshing = true
                scope.launch {
                    viewModel.refreshBreeds() // Manual refresh triggers fetch
                    isRefreshing = false
                }
            },
            modifier = Modifier
                .padding( PaddingValues(
                    start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
                    top = paddingValues.calculateTopPadding(),
                    end = paddingValues.calculateEndPadding(LayoutDirection.Ltr)
                    , bottom = 8.dp )
                )
        ) {
            when (state)
            {
                is DogBreedsState.Loading -> {
                    // Shimmer placeholders while loading
                    LazyColumn(
                        modifier = Modifier .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        items(10) {
                            Card(
                                modifier = Modifier .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .shimmerEffect(),
                                shape = RoundedCornerShape(12.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(
                                    modifier = Modifier .fillMaxWidth()
                                        .height(80.dp)
                                        .padding(12.dp)
                                ) { }
                            }
                        }
                    }
                }
                is DogBreedsState.Success -> {
                    val breeds = (state as DogBreedsState.Success).breeds
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        // Show last updated timestamp from PollingStatusManager
                        item {
                            Text(
                                text = "Last updated: $lastUpdated",
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                        // Expandable list of breeds
                        itemsIndexed(breeds) { index, breed ->
                            ExpandableBreedItem(
                                breed = breed,
                                isExpanded = expandedIndex == index,
                                onExpandToggle = {
                                    expandedIndex = if (expandedIndex == index) null else index
                                }
                            )
                        }

                    }
                }
                is DogBreedsState.Error -> {
                    // Error UI with retry option
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Error: ${(state as DogBreedsState.Error).message}",
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.refreshBreeds() }) {
                            Text("Retry")
                        }
                    }
                }
            }

        }
    }
}

@Composable
fun ExpandableBreedItem(
    breed: DogBreed,
    isExpanded: Boolean,
    onExpandToggle: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onExpandToggle() }
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = breed.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isExpanded) "Collapse" else "Expand"
                )
            }

            if (isExpanded && breed.subBreeds.isNotEmpty()) {
                Column(modifier = Modifier.padding(start = 16.dp)) {
                    breed.subBreeds.forEach { sub ->
                        Text(
                            text = sub,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}



