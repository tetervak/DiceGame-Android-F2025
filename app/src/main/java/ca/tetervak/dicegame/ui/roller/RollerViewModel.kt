package ca.tetervak.dicegame.ui.roller

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import ca.tetervak.dicegame.domain.RollData
import ca.tetervak.dicegame.domain.RollerService
import java.util.Date
import kotlin.random.Random

class RollerViewModel : ViewModel() {

    private val _uiState: MutableState<RollerUiState> =
        mutableStateOf(RollerUiState.NotRolled(numberOfDice = 3))
    val uiState: State<RollerUiState> = _uiState

    private val rollerService: RollerService = RollerService(Random.Default)

    fun onRoll() {
        val rollData: RollData = rollerService.getRollData(uiState.value.numberOfDice)
        _uiState.value = RollerUiState.Rolled(
            rollData = rollData, date = Date()
        )
    }

    fun onReset() {
        _uiState.value = RollerUiState.NotRolled(uiState.value.numberOfDice)
    }

    fun onChangeOfNumberOfDice(newNumberOfDice: Int) {
        _uiState.value = RollerUiState.NotRolled(newNumberOfDice)
    }

}