import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.sportradar.domain.model.Match
import org.sportradar.domain.model.Score

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UpdateMatchWindow(
    match: Match,
    onMatchUpdated: (updatedMatch: Match) -> Unit
) {
    var homeScoreState by remember { mutableStateOf(match.homeScore.value) }
    var awayScoreState by remember { mutableStateOf(match.awayScore.value) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .background(color = MaterialTheme.colors.surface),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Update Match Scores",
            style = MaterialTheme.typography.h6
        )
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = match.homeTeam.name, style = MaterialTheme.typography.body1)
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Button(onClick = { if (homeScoreState > 0) homeScoreState -= 1 }) {
                        Text("-")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "$homeScoreState", style = MaterialTheme.typography.h5)
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { homeScoreState += 1 }) {
                        Text("+")
                    }
                }
            }

            Spacer(Modifier.width(16.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = match.awayTeam.name, style = MaterialTheme.typography.body1)
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Button(onClick = { if (awayScoreState > 0) awayScoreState -= 1 }) {
                        Text("-")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "$awayScoreState", style = MaterialTheme.typography.h5)
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { awayScoreState += 1 }) {
                        Text("+")
                    }
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                val updatedMatch = Match(
                    match.id, match.homeTeam, match.awayTeam,
                    Score(homeScoreState),
                    Score(awayScoreState)
                )
                onMatchUpdated(updatedMatch)
            }
        ) {
            Text("Update Match")
        }
    }
}