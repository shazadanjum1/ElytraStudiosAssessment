package com.elytrastudios.assessment.presentation.breeds


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elytrastudios.assessment.data.model.DogBreed
import com.elytrastudios.assessment.domain.usecase.GetDogBreedsUseCase
import com.elytrastudios.assessment.util.AppLogger
import com.elytrastudios.assessment.util.PollingStatusManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.*
import java.text.SimpleDateFormat
import java.util.*

@HiltViewModel
class DogBreedsViewModel @Inject constructor(
    private val getDogBreedsUseCase: GetDogBreedsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<DogBreedsState>(DogBreedsState.Loading)
    val state: StateFlow<DogBreedsState> = _state.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private var allBreeds: List<DogBreed> = emptyList()


    private var pollingJob: Job? = null

    // Starts polling loop if not already active
    fun startPolling() {
        if (pollingJob?.isActive == true) return

        pollingJob = viewModelScope.launch {
            while (isActive) {
                AppLogger.i("DogBreedsViewModel", "Polling cycle started at ${System.currentTimeMillis()}")
                fetchBreeds() // Fetch data each cycle
                delay(35_000) // Wait 35 seconds before next cycle
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopPolling() // Ensure polling stops when ViewModel is destroyed
    }

    fun stopPolling() {
        pollingJob?.cancel()
        pollingJob = null
    }

    // Manual refresh triggered by pull-to-refresh or retry
    fun refreshBreeds() {
        viewModelScope.launch {
            fetchBreeds()
        }
    }

    // Core fetch logic: calls use case, updates state, logs, updates PollingStatusManager
    private suspend fun fetchBreeds() {

        _state.value = DogBreedsState.Loading
        val result = getDogBreedsUseCase()
        _state.value = result.fold(
            onSuccess = { breeds ->
                AppLogger.d("DogBreedsViewModel", "Polling success: ${breeds.size} breeds")
                allBreeds = breeds
                applyFilter() // Apply search + sort filters

                PollingStatusManager.updatePollingStatus(true)
                PollingStatusManager.updateLastFetchTime(getCurrentTimestamp())

                DogBreedsState.Success(allBreeds)
            },
            onFailure = { error ->
                AppLogger.e("DogBreedsViewModel", "Polling failed", error)
                PollingStatusManager.updatePollingStatus(false)
                PollingStatusManager.updateLastFetchTime(getCurrentTimestamp())

                DogBreedsState.Error(error.message ?: "Unknown error")
            }
        )
    }

    // Updates search query and reapplies filter
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        applyFilter()
    }

    // Formats timestamp for last fetch
    private fun getCurrentTimestamp(): String {
        val formatter = SimpleDateFormat("HH:mm:ss dd/MM/yyyy", Locale.getDefault())
        return formatter.format(Date())
    }


    private val _sortOrder = MutableStateFlow(SortOrder.ASCENDING)

    // Updates sort order and reapplies filter
    fun updateSortOrder(order: SortOrder) {
        _sortOrder.value = order
        applyFilter()
    }

    // Applies search + sort filters to allBreeds and updates state
    private fun applyFilter() {

        if (allBreeds.isEmpty()) {
            return
        }

        val query = _searchQuery.value.lowercase(Locale.getDefault())
        val filtered = if (query.isBlank()) {
            allBreeds
        } else {
            allBreeds.filter { breed ->
                breed.name.lowercase(Locale.getDefault()).contains(query) ||
                        breed.subBreeds.any { it.lowercase(Locale.getDefault()).contains(query) }
            }
        }

        val sorted = when (_sortOrder.value) {
            SortOrder.ASCENDING -> filtered.sortedBy { it.name }
            SortOrder.DESCENDING -> filtered.sortedByDescending { it.name }
        }

        _state.value = DogBreedsState.Success(sorted)
    }

}

enum class SortOrder { ASCENDING, DESCENDING }

