import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.sportradar.domain.model.Team

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StartNewMatchWindow(
    onMatchStarted: (homeTeam: Team, awayTeam: Team) -> Unit
) {
    val worldCup2022Countries = listOf(
        "Qatar", "Ecuador", "Senegal", "Netherlands",
        "England", "Iran", "USA", "Wales",
        "Argentina", "Saudi Arabia", "Mexico", "Poland",
        "France", "Australia", "Denmark", "Tunisia",
        "Spain", "Costa Rica", "Germany", "Japan",
        "Belgium", "Canada", "Morocco", "Croatia",
        "Brazil", "Serbia", "Switzerland", "Cameroon",
        "Portugal", "Ghana", "Uruguay", "South Korea"
    )

    var homeTeam by remember { mutableStateOf("") }
    var awayTeam by remember { mutableStateOf("") }
    var isHomeDropdownExpanded by remember { mutableStateOf(false) }
    var isAwayDropdownExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .background(
                color = MaterialTheme.colors.surface,
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Start a New Match", style = MaterialTheme.typography.h6)
        Spacer(Modifier.height(16.dp))

        // Home Team Dropdown
        ExposedDropdownMenuBox(
            expanded = isHomeDropdownExpanded,
            onExpandedChange = { isHomeDropdownExpanded = it }
        ) {
            TextField(
                value = homeTeam,
                onValueChange = {},
                readOnly = true,
                label = { Text("Select Home Team") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isHomeDropdownExpanded)
                },
                modifier = Modifier.fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = isHomeDropdownExpanded,
                onDismissRequest = { isHomeDropdownExpanded = false }
            ) {
                worldCup2022Countries.forEach { country ->
                    DropdownMenuItem(onClick = {
                        homeTeam = country
                        isHomeDropdownExpanded = false
                    }) {
                        Text(text = country)
                    }
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        // Away Team Dropdown
        ExposedDropdownMenuBox(
            expanded = isAwayDropdownExpanded,
            onExpandedChange = { isAwayDropdownExpanded = it }
        ) {
            TextField(
                value = awayTeam,
                onValueChange = {},
                readOnly = true,
                label = { Text("Select Away Team") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isAwayDropdownExpanded)
                },
                modifier = Modifier.fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = isAwayDropdownExpanded,
                onDismissRequest = { isAwayDropdownExpanded = false }
            ) {
                worldCup2022Countries.forEach { country ->
                    DropdownMenuItem(onClick = {
                        awayTeam = country
                        isAwayDropdownExpanded = false
                    }) {
                        Text(text = country)
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                try {
                    if (homeTeam.isNotBlank() && awayTeam.isNotBlank()) {
                        onMatchStarted(Team(homeTeam), Team(awayTeam))
                    }
                } catch (e: Exception) {
                    println("Error: ${e.message}")
                }
            }
        ) {
            Text("Start Match")
        }
    }
}