package service

import (
	"fmt"
	"github.com/anaskhan96/soup"
	"strconv"
	"strings"
)

type OneOnOneService struct {
	httpClient *AnstossHttpClient
}

func NewAOneOnOneService(http *AnstossHttpClient) *OneOnOneService {
	return &OneOnOneService{httpClient: http}
}

func (service OneOnOneService) FetchData(currentTeam string) {
	service.httpClient.Login()
	gameIds := service.fetchGameIds(currentTeam)
	players := service.fetchPlayers(currentTeam, gameIds)
	printResult(players, currentTeam)
}

func (service OneOnOneService) fetchGameIds(currentTeam string) []string {
	var gameIds []string
	for playDay := 1; playDay < 35; playDay++ {
		playDayContent := service.httpClient.Get("content/getFixed.php?do=land;land_id=168;wettbewerb_st_id=240;spieltag_nr=" + strconv.Itoa(playDay) + ";start_jahr=2021")
		playDayHtml := soup.HTMLParse(playDayContent)
		find := playDayHtml.FindStrict("table", "class", "daten_tabelle").FindStrict("a", "title", currentTeam)
		gameId := find.Pointer.Parent.Parent.LastChild.FirstChild.Attr[0].Val
		gameId = strings.ReplaceAll(gameId, "../content/getContent.php?dyn=spielbericht;spiel_id=", "")
		gameId = strings.ReplaceAll(gameId, "#", "")
		gameIds = append(gameIds, gameId)
	}
	return gameIds
}

func (service OneOnOneService) fetchPlayers(currentTeam string, gameIds []string) []*Player {
	var players []*Player
	for _, gameId := range gameIds {
		content := service.httpClient.Get("content/getContent.php?dyn=statistiksystem;spiel_id=" + gameId + ";statistik=spiele_zweikaempfe")
		html := soup.HTMLParse(content)
		tableRows := html.FindStrict("table", "class", "daten_tabelle").FindAllStrict("tr")

		var attackingPlayer *Player
		var defendingPlayer *Player

		for index, tableRow := range tableRows {
			if index == 0 {
				continue
			}
			tableData := tableRow.Children()
			minute, _ := strconv.Atoi(tableData[1].Text())
			oneOnOne := OneOnOne{
				Minute:        minute,
				AttackingTeam: tableData[0].FindStrict("a").Text(),
				Attacker:      tableData[2].Text(),
				Defender:      tableData[3].Text(),
				AttackerWins:  tableData[4].Text() == "X",
			}

			var defendingTeam string
			if oneOnOne.AttackingTeam == currentTeam {
				defendingTeam = "opponent"
			} else {
				defendingTeam = currentTeam
			}

			attackingPlayer, players = createOrFindPlayer(players, oneOnOne.Attacker, oneOnOne.AttackingTeam)
			defendingPlayer, players = createOrFindPlayer(players, oneOnOne.Defender, defendingTeam)

			if oneOnOne.AttackerWins {
				attackingPlayer.OffensiveWon = attackingPlayer.OffensiveWon + 1
				defendingPlayer.DefensiveLost++
			} else {
				attackingPlayer.OffensiveLost++
				defendingPlayer.DefensiveWon++
			}
		}
	}
	return players
}

func createOrFindPlayer(players []*Player, name string, team string) (*Player, []*Player) {
	for _, player := range players {
		if player.Name == name {
			return player, players
		}
	}
	player := Player{Name: name, Team: team}
	players = append(players, &player)
	return &player, players
}

func printResult(players []*Player, currentTeam string) {
	for _, player := range players {
		if player.Team == currentTeam {
			percentOffensive := 0.0
			if player.OffensiveWon+player.OffensiveLost > 0 {
				percentOffensive = float64(player.OffensiveWon*100) / float64(player.OffensiveWon+player.OffensiveLost)
			}
			percentDefensive := 0.0
			if player.DefensiveWon+player.DefensiveLost > 0 {
				percentDefensive = float64(player.DefensiveWon*100) / float64(player.DefensiveWon+player.DefensiveLost)
			}
			fmt.Printf("%s: Offensive %d/%d (%.0f%%), Defensive %d/%d (%.0f%%)\n", player.Name, player.OffensiveWon, player.OffensiveWon+player.OffensiveLost, percentOffensive, player.DefensiveWon, player.DefensiveWon+player.DefensiveLost, percentDefensive)
		}
	}
}
