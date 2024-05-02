package ca.tetervak.dicegame.ui.roller

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RollerViewModelTest {

    private lateinit var viewModel: RollerViewModel

    @Before
    fun setUp() {
        println("--- testing case ---")
        viewModel = RollerViewModel()
    }

    @After
    fun tearDown() {
        println("--- ------- ---- ---")
    }

    @Test
    fun init() {
        println("initial state = ${viewModel.uiState.value}")
        assert(viewModel.uiState.value is RollerUiState.NotRolled)
        assertEquals(3, viewModel.uiState.value.numberOfDice)
    }

    @Test
    fun onRoll() {
        viewModel.onRoll()
        println("state after roll = ${viewModel.uiState.value}")
        assert(viewModel.uiState.value is RollerUiState.Rolled)
    }

    @Test
    fun onReset() {
        viewModel.onChangeOfNumberOfDice(2)
        viewModel.onRoll()
        viewModel.onReset()
        println("state after reset = ${viewModel.uiState.value}")
        assert(viewModel.uiState.value is RollerUiState.NotRolled)
        assertEquals(3, viewModel.uiState.value.numberOfDice)
    }

    @Test
    fun onChangeOfNumberOfDice() {
        viewModel.onRoll()
        println("setting the number of dice to 2")
        viewModel.onChangeOfNumberOfDice(2)
        println("state = ${viewModel.uiState.value}")
        assert(viewModel.uiState.value is RollerUiState.NotRolled)
        assertEquals(2, viewModel.uiState.value.numberOfDice)
    }

}