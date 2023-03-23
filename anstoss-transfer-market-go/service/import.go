package service

import (
	"anstoss-transfer-market-go/model"
	"github.com/anaskhan96/soup"
	"log"
	"strconv"
	"strings"
)

type PlayerImportService struct {
	httpClient *AnstossHttpClient
}

func NewPlayerImportService(httpClient *AnstossHttpClient) *PlayerImportService {
	return &PlayerImportService{httpClient: httpClient}
}

func (service *PlayerImportService) searchAnstossSite() *[]model.Player {
	service.httpClient.Login()
	pageLinks := service.fetchPageLinks()

	var players []model.Player
	for _, pageLink := range pageLinks {
		pageResponse := service.httpClient.Get(pageLink)
		pageHtml := soup.HTMLParse(pageResponse)
		rows := pageHtml.FindStrict("table", "class", "daten_tabelle").FindAllStrict("tr")
		for index, row := range rows {
			if index == 0 {
				continue
			}
			player := service.fetchPlayer(row)
			players = append(players, player)
		}
	}
	return &players
}

func (service *PlayerImportService) fetchPageLinks() []string {
	//content/getContent.php?dyn=transfers/spielersuche&erg=1&&idealpos[]=MD&idealpos[]=RV&idealpos[]=LV&idealpos[]=LIB&idealpos[]=LM&idealpos[]=RM&idealpos[]=ZM&idealpos[]=ST&wettbewerb_id=&land_id=&nachname=&vorname=&genauigkeit=1&staerke_min=&staerke_max=&alter_min=&alter_max=&starkerfuss=&eigens_plus=&eigens_char_plus=&spielerboerse=1&max_abloese=
	path := "content/getContent.php?dyn=transfers/spielersuche&erg=1&&" +
		positionParameters() +
		"wettbewerb_id=&land_id=&genauigkeit=1&staerke_min=3&staerke_max=&alter_min=&alter_max=30&spielerboerse=1"

	result := service.httpClient.Post(path)
	html := soup.HTMLParse(result)
	pageNavigationDiv := html.FindStrict("div", "class", "navigation").FindNextSibling().FindNextSibling()
	anchorTags := pageNavigationDiv.FindAllStrict("a")
	var pageLinks []string
	for _, link := range anchorTags {
		pageLinks = append(pageLinks, link.Attrs()["href"])
	}
	return pageLinks
}

func (service *PlayerImportService) fetchPlayer(row soup.Root) model.Player {
	tableData := row.Children()
	position := strings.TrimSpace(tableData[0].Text())
	name := tableData[1].FindStrict("a").Text()
	strength, _ := strconv.ParseFloat(strings.ReplaceAll(tableData[2].Text(), ",", "."), 64)
	age, _ := strconv.Atoi(tableData[4].Text())
	country := tableData[5].FindStrict("img").Attrs()["title"]
	cash, _ := strconv.Atoi(strings.ReplaceAll(tableData[7].Text(), ".", ""))
	days, _ := strconv.Atoi(tableData[8].Text())

	playerLink := tableData[1].FindStrict("a").Attrs()["href"]
	playerIdString := strings.ReplaceAll(strings.ReplaceAll(playerLink, "?do=spieler&spieler_id=", ""), "#", "")
	playerId, _ := strconv.Atoi(playerIdString)
	aawLink := "content/getContent.php?dyn=transfers/aaw&spieler_id=" + playerIdString
	aawMultiMap := service.fetchAaw(aawLink, playerIdString)

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
	return player
}

func (service *PlayerImportService) fetchAaw(aawLink string, playerIdString string) map[model.AawCategory][]int {
	aawResponse := service.httpClient.Post(aawLink)
	aawHtml := soup.HTMLParse(aawResponse)

	var aawMultiMap = make(map[model.AawCategory][]int)
	aawRows := aawHtml.FindStrict("table", "id", "s"+playerIdString).FindAllStrict("tr", "class", "hide")

	for _, aawRow := range aawRows {
		aawTableData := aawRow.FindAllStrict("td")
		if len(aawTableData) == 2 {
			continue
		}
		aawMultiMap[model.Training] = append(aawMultiMap[model.Training], aawToInt(aawTableData[0].Text()))
		aawMultiMap[model.Fitness] = append(aawMultiMap[model.Fitness], aawToInt(aawTableData[1].Text()))
		aawMultiMap[model.Einsatz] = append(aawMultiMap[model.Einsatz], aawToInt(aawTableData[2].Text()))
		aawMultiMap[model.Alter] = append(aawMultiMap[model.Alter], aawToInt(aawTableData[3].Text()))
		aawMultiMap[model.Tor] = append(aawMultiMap[model.Tor], aawToInt(aawTableData[4].Text()))
	}
	return aawMultiMap
}

func positionParameters() string {
	var positionString string
	for _, position := range supportedPositions {
		positionString += "idealpos[]=" + position + "&"
	}
	return positionString
}

func aawToInt(text string) int {
	converted, err := strconv.Atoi(strings.ReplaceAll(text, " %", ""))
	if err != nil {
		log.Fatal(err)
	}
	return converted
}
