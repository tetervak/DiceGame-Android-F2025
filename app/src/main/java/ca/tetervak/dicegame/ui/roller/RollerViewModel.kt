package ca.tetervak.dicegame.ui.roller

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.tetervak.dicegame.domain.RollData
import ca.tetervak.dicegame.domain.RollerService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import kotlin.random.Random


class RollerViewModel constructor(
) : ViewModel() {

    private val _uiState: MutableState<RollerUiState> =
        mutableStateOf(RollerUiState.NotRolled)
    val uiState: State<RollerUiState> = _uiState

    val numberOfDice: StateFlow<Int> = MutableStateFlow(3)

    private val rollerService: RollerService = RollerService(Random.Default)

    fun onRoll() {
        val rollData: RollData = rollerService.getRollData(numberOfDice.value)
        _uiState.value = RollerUiState.Rolled(
            rollData = rollData, date = Date()
        )
    }

    fun onReset() = viewModelScope.launch {
        _uiState.value = RollerUiState.NotRolled
    }

}