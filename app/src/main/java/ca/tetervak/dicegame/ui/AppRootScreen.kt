package ca.tetervak.dicegame.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import ca.tetervak.dicegame.R
import ca.tetervak.dicegame.ui.roller.NotRolledBody
import ca.tetervak.dicegame.ui.roller.RolledBody
import ca.tetervak.dicegame.ui.roller.RollerTopAppBar
import ca.tetervak.dicegame.ui.roller.RollerUiState
import ca.tetervak.dicegame.ui.roller.RollerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppRootScreen(
    viewModel: RollerViewModel = viewModel()
) {
    val rollerUiState: RollerUiState = viewModel.uiState.value
    val numberOfDice: Int by viewModel.numberOfDice.collectAsState()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        topBar = {
            RollerTopAppBar(
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = viewModel::onRoll) {
                Icon(
                    imageVector = Icons.Outlined.Refresh,
                    contentDescription = stringResource(R.string.roll_dice)
                )
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        when (rollerUiState) {
            is RollerUiState.Rolled -> RolledBody(
                rollData = rollerUiState.rollData,
                date = rollerUiState.date,
                numberOfDice = numberOfDice,
                onRoll = viewModel::onRoll,
                onReset = viewModel::onReset,
                modifier = Modifier.padding(innerPadding)
            )

            is RollerUiState.NotRolled -> NotRolledBody(
                numberOfDice = numberOfDice,
                onRoll = viewModel::onRoll,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Preview
@Composable
fun RollerScreenPreviewNotRolled() {
    AppRootScreen(RollerViewModel())
}

@Preview
@Composable
fun RollerScreenPreviewRolled() {
    val viewModel = RollerViewModel()
    viewModel.onRoll()
    AppRootScreen(viewModel)
}
