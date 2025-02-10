import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.sportradar.ScoreboardApi
import org.sportradar.domain.model.Match


@Composable
@Preview
fun ScoreboardApp(scoreboard: ScoreboardApi) {
    val matches = remember { mutableStateListOf<Match>().apply { addAll(scoreboard.getAllOngoingMatches()) } }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    var updateMatch by remember { mutableStateOf<Match?>(null) }
    var showStartNewMatchWindow by remember { mutableStateOf(false) }
    var showUpdateMatchWindow by remember { mutableStateOf(false) }

    val scaffoldState = rememberScaffoldState()


    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            scaffoldState.snackbarHostState.showSnackbar(it)
            errorMessage = null
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = { SnackbarHost(scaffoldState.snackbarHostState) }
    ) { padding ->

        Column(modifier = Modifier.padding(padding).padding(16.dp).fillMaxSize()) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Button(onClick = {
                    showStartNewMatchWindow = true
                }) {
                    Text("Start Match")
                }
                Button(onClick = {
                    matches.clear()
                    matches.addAll(scoreboard.getAllOngoingMatches())
                    println(matches)
                }) {
                    Text("Refresh")
                }
            }

            Spacer(Modifier.height(16.dp))

            Text("Ongoing Matches", style = MaterialTheme.typography.h6)
            LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f)) {
                items(matches.size) { index ->
                    val match = matches[index]
                    Row(
                        Modifier.padding(8.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("${match.homeTeam.name} vs ${match.awayTeam.name}")
                            Text("${match.homeScore.value} vs ${match.awayScore.value}")
                        }
                        Button(onClick = {
                            updateMatch = match
                            showUpdateMatchWindow = true
                        }) {
                            Text("Update Score")
                        }
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
        }
        if (showStartNewMatchWindow) {
            StartNewMatchWindow(
                onMatchStarted = { homeTeam, awayTeam ->
                    showStartNewMatchWindow = false
                    try {
                        scoreboard.startMatch(homeTeam, awayTeam)
                        matches.clear()
                        matches.addAll(scoreboard.getAllOngoingMatches())
                    } catch (e: Exception) {
                        errorMessage = "Error: ${e.message}"
                    }

                }
            )
        }
        if (showUpdateMatchWindow) {
            updateMatch?.let {
                UpdateMatchWindow(
                    it,
                    onMatchUpdated = { updatedMatch ->
                        showUpdateMatchWindow = false
                        try {
                            scoreboard.updateScore(
                                updatedMatch.homeTeam,
                                updatedMatch.awayTeam,
                                updatedMatch.homeScore,
                                updatedMatch.awayScore
                            )
                            matches.clear()
                            matches.addAll(scoreboard.getAllOngoingMatches())
                        } catch (e: Exception) {
                            errorMessage = "Error: ${e.message}"
                        }
                    }
                )
            }
        }
    }
}

fun main() = application {
    val scoreboard = ScoreboardApi.create()
    Window(onCloseRequest = ::exitApplication) {
        MaterialTheme {
            RootApp(scoreboard = scoreboard)
        }
    }
}

@Composable
@Preview
fun DemoPage(onContinue: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome the Live Football World Cup Score Board!", style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(16.dp).padding(16.dp))
        Text(
            "This app allows you to manage matches, update scores, and track ongoing games.",
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(8.dp),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onContinue) {
            Text("Start Using App")
        }
    }
}

@Composable
@Preview
fun RootApp(scoreboard: ScoreboardApi) {
    var isDemoShown by remember { mutableStateOf(true) }

    if (isDemoShown) {
        DemoPage(onContinue = { isDemoShown = false })
    } else {
        ScoreboardApp(scoreboard = scoreboard)
    }
}
