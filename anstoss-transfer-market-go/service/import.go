package service

import (
	"anstoss-transfer-market-go/model"
	"errors"
	"fmt"
	"github.com/anaskhan96/soup"
	"log/slog"
	"strconv"
	"strings"
)

type PlayerImportService struct {
	httpClient *AnstossHttpClient
}

func NewPlayerImportService(httpClient *AnstossHttpClient) *PlayerImportService {
	return &PlayerImportService{httpClient: httpClient}
}

func (service *PlayerImportService) searchAnstossSite() (*[]model.Player, error) {
	err := service.httpClient.Login()
	if err != nil {
		return nil, err
	}
	pageLinks, err := service.fetchPageLinks()
	if err != nil {
		return nil, err
	}

	var players []model.Player
	for _, pageLink := range pageLinks {
		pageResponse, err := service.httpClient.Get(pageLink)
		if err != nil {
			return nil, err
		}
		pageHtml := soup.HTMLParse(pageResponse)
		rows := pageHtml.FindStrict("table", "class", "daten_tabelle").FindAllStrict("tr")
		for index, row := range rows {
			if index == 0 {
				continue
			}
			player, err := service.fetchPlayer(row)
			if err != nil {
				return nil, err
			}
			players = append(players, player)
		}
	}
	slog.Info(fmt.Sprintf("%d player(s) found", len(players)))
	return &players, nil
}

func (service *PlayerImportService) fetchPageLinks() ([]string, error) {
	//content/getContent.php?dyn=transfers/spielersuche&erg=1&&idealpos[]=MD&idealpos[]=RV&idealpos[]=LV&idealpos[]=LIB&idealpos[]=LM&idealpos[]=RM&idealpos[]=ZM&idealpos[]=ST&wettbewerb_id=&land_id=&nachname=&vorname=&genauigkeit=1&staerke_min=&staerke_max=&alter_min=&alter_max=&starkerfuss=&eigens_plus=&eigens_char_plus=&spielerboerse=1&max_abloese=
	path := "content/getContent.php?dyn=transfers/spielersuche&erg=1&&" +
		positionParameters() +
		"wettbewerb_id=&land_id=&genauigkeit=1&staerke_min=3&staerke_max=&alter_min=&alter_max=30&spielerboerse=1"

	result, err := service.httpClient.Post(path)
	if err != nil {
		return nil, err
	}
	html := soup.HTMLParse(result)
	pageNavigationDiv := html.FindStrict("div", "class", "navigation").FindNextSibling().FindNextSibling()
	anchorTags := pageNavigationDiv.FindAllStrict("a")
	var pageLinks []string
	for _, link := range anchorTags {
		pageLinks = append(pageLinks, link.Attrs()["href"])
	}
	return pageLinks, nil
}

func (service *PlayerImportService) fetchPlayer(row soup.Root) (model.Player, error) {
	tableData := row.Children()
	position := strings.TrimSpace(tableData[0].Text())
	name := tableData[1].FindStrict("a").Text()
	strength, errStrength := strconv.ParseFloat(strings.ReplaceAll(tableData[2].Text(), ",", "."), 64)
	age, errAge := strconv.Atoi(tableData[4].Text())
	country := tableData[5].FindStrict("img").Attrs()["title"]
	cash, errCash := strconv.Atoi(strings.ReplaceAll(tableData[7].Text(), ".", ""))
	days, errDays := strconv.Atoi(tableData[8].Text())

	playerLink := tableData[1].FindStrict("a").Attrs()["href"]
	playerIdString := strings.ReplaceAll(strings.ReplaceAll(playerLink, "?do=spieler&spieler_id=", ""), "#", "")
	playerId, errPlayerId := strconv.Atoi(playerIdString)
	aawLink := "content/getContent.php?dyn=transfers/aaw&spieler_id=" + playerIdString
	aawMultiMap, errFetch := service.fetchAaw(aawLink, playerIdString)

	if err := errors.Join(errStrength, errAge, errCash, errDays, errPlayerId, errFetch); err != nil {
		return model.Player{}, err
	}
	player := model.Player{
		Id:       playerId,
		Age:      age,
		Name:     name,
		Position: position,
		Country:  country,
		Price:    cash,
		Days:     days,
		Strength: strength,
		Aaw:      aawMultiMap,
	}
	return player, nil
}

func (service *PlayerImportService) fetchAaw(aawLink string, playerIdString string) (map[model.AawCategory][]int, error) {
	aawResponse, err := service.httpClient.Post(aawLink)
	if err != nil {
		return nil, err
	}
	aawHtml := soup.HTMLParse(aawResponse)

	var aawMultiMap = make(map[model.AawCategory][]int)
	aawRows := aawHtml.FindStrict("table", "id", "s"+playerIdString).FindAllStrict("tr", "class", "hide")

	for _, aawRow := range aawRows {
		aawTableData := aawRow.FindAllStrict("td")
		if len(aawTableData) == 2 {
			continue
		}
		var percentValues [5]int
		for index := range 5 {
			toInt, err := aawToInt(aawTableData[index].Text())
			if err != nil {
				return nil, err
			}
			percentValues[index] = toInt
		}
		aawMultiMap[model.Training] = append(aawMultiMap[model.Training], percentValues[0])
		aawMultiMap[model.Fitness] = append(aawMultiMap[model.Fitness], percentValues[1])
		aawMultiMap[model.Einsatz] = append(aawMultiMap[model.Einsatz], percentValues[2])
		aawMultiMap[model.Alter] = append(aawMultiMap[model.Alter], percentValues[3])
		aawMultiMap[model.Tor] = append(aawMultiMap[model.Tor], percentValues[4])
	}
	return aawMultiMap, nil
}

func positionParameters() string {
	var positionString string
	for _, position := range supportedPositions {
		positionString += "idealpos[]=" + position + "&"
	}
	return positionString
}

func aawToInt(text string) (int, error) {
	replaced := strings.ReplaceAll(text, " %", "")
	converted, err := strconv.Atoi(replaced)
	return converted, err
}
