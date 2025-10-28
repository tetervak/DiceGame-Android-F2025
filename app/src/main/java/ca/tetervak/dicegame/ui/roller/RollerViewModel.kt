package ca.tetervak.dicegame.ui.roller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.tetervak.dicegame.data.PreferencesRepository
import ca.tetervak.dicegame.domain.RollData
import ca.tetervak.dicegame.data.RollDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class RollerViewModel @Inject constructor(
    private val rollDataRepository: RollDataRepository,
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<RollerUiState> = MutableStateFlow(INIT_STATE)
    val uiState: StateFlow<RollerUiState> = _uiState

    init {
        viewModelScope.launch(Dispatchers.IO) {
            preferencesRepository.getNumberOfDiceFlow().collect { numberOfDice ->
                _uiState.value = RollerUiState.NotRolled(numberOfDice)
            }
        }
    }

    fun onRoll() {
        val rollData: RollData = rollDataRepository.getRandomRollData(numberOfDice = uiState.value.numberOfDice)
        _uiState.value = RollerUiState.Rolled(
            rollData = rollData, date = Date()
        )
    }

    fun onReset() {
        _uiState.value = INIT_STATE
    }

    fun onChangeOfNumberOfDice(newNumberOfDice: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            preferencesRepository.saveNumberOfDice(newNumberOfDice)
        }
    }

    companion object {
        private val INIT_STATE = RollerUiState.NotRolled(numberOfDice = PreferencesRepository.DEFAULT_NUMBER_OF_DICE)
    }

}