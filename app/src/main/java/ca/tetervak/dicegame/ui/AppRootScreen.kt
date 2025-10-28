package ca.tetervak.dicegame.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import ca.tetervak.dicegame.R
import ca.tetervak.dicegame.domain.RollData
import ca.tetervak.dicegame.ui.roller.NotRolledBody
import ca.tetervak.dicegame.ui.roller.RolledBody
import ca.tetervak.dicegame.ui.roller.RollerTopAppBar
import ca.tetervak.dicegame.ui.roller.RollerUiState
import ca.tetervak.dicegame.ui.roller.RollerViewModel
import ca.tetervak.dicegame.ui.theme.DiceGameTheme
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppRootScreen() {

    val viewModel: RollerViewModel = hiltViewModel()
    val state: State<RollerUiState> = viewModel.uiState.collectAsState()
    val uiState: RollerUiState = state.value

    var showAboutDialog: Boolean by rememberSaveable {
        mutableStateOf(false)
    }

    AppRootScreenBody(
        uiState = uiState,
        onChangeOfNumberOfDice = viewModel::onChangeOfNumberOfDice,
        onRoll = viewModel::onRoll,
        onReset = viewModel::onReset,
        onHelp = { showAboutDialog = true}
    )

    if (showAboutDialog) {
        DiceGameAbout(onDismissRequest = { showAboutDialog = false })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppRootScreenBody(
    uiState: RollerUiState,
    onChangeOfNumberOfDice: (Int) -> Unit,
    onRoll: () -> Unit,
    onReset: () -> Unit,
    onHelp: () -> Unit
){
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        topBar = {
            RollerTopAppBar(
                onHelpButtonClick = onHelp,
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onRoll) {
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

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = 32.dp)
        ) {

            NumberOfDiceInput(
                numberOfDice = uiState.numberOfDice,
                onChange = onChangeOfNumberOfDice
            )

            when (uiState) {
                is RollerUiState.Rolled -> RolledBody(
                    rollData = uiState.rollData,
                    date = uiState.date
                )

                is RollerUiState.NotRolled -> NotRolledBody(
                    numberOfDice = uiState.numberOfDice
                )
            }

            Button(
                onClick = onRoll, modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(text = stringResource(R.string.roll_button_label, uiState.numberOfDice))
            }
            Button(
                onClick = onReset, modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(text = stringResource(R.string.reset_button_label))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NumberOfDiceInput(
    numberOfDice: Int,
    onChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectExpanded: Boolean by remember { mutableStateOf(false) }
    val selectOptions = stringArrayResource(id = R.array.choices_of_numbers_of_dice)
    val selectedText = selectOptions[numberOfDice - 1]

    ExposedDropdownMenuBox(
        expanded = selectExpanded,
        onExpandedChange = { selectExpanded = it },
        modifier = modifier
    ) {
        OutlinedTextField(
            readOnly = true,
            value = selectedText,
            onValueChange = { },
            label = {
                Text(
                    text = stringResource(R.string.number_of_dice),
                    fontSize = 14.sp
                )
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = selectExpanded
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            ),
            textStyle = TextStyle.Default.copy(
                fontSize = 20.sp,
                color = colorResource(id = R.color.purple_500)
            ),
            modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
        )
        ExposedDropdownMenu(
            expanded = selectExpanded,
            onDismissRequest = {
                selectExpanded = false
            }
        ) {
            selectOptions.forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        selectExpanded = false
                        onChange(selectOptions.indexOf(option) + 1)
                    },
                    text = {
                        Text(text = option, fontSize = 20.sp)
                    }
                )
            }
        }
    }
}

@Composable
fun DiceGameAbout(onDismissRequest: () -> Unit) =
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(stringResource(R.string.about_dice_game)) },
        text = {
            Text(
                text = stringResource(R.string.programming_example),
                fontSize = 18.sp
            )
        },
        confirmButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text(stringResource(R.string.ok))
            }
        }
    )

@Preview
@Composable
fun RollerScreenPreviewNotRolled() {
    DiceGameTheme {
        AppRootScreenBody(
            uiState = RollerUiState.NotRolled(numberOfDice = 3),
            onChangeOfNumberOfDice = {},
            onRoll = {},
            onReset = {},
            onHelp = {}
        )
    }
}

@Preview
@Composable
fun RollerScreenPreviewRolled() {
    DiceGameTheme {
        AppRootScreenBody(
            uiState = RollerUiState.Rolled(rollData = RollData(listOf(1, 2, 3)), date = Date()),
            onChangeOfNumberOfDice = {},
            onRoll = {},
            onReset = {},
            onHelp = {}
        )
    }
}
