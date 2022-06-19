package service

import (
	"anstoss-transfer-market-go/model"
	"anstoss-transfer-market-go/store"
	"golang.org/x/exp/slices"
)

var (
	supportedPositions = []string{"LIB", "MD", "LV", "RV", "ZM", "RM", "LM", "ST"}
)

type PlayerService struct {
	store         *store.PlayerStore
	importService *PlayerImportService
}

func NewPlayerService(store *store.PlayerStore, importService *PlayerImportService) *PlayerService {
	return &PlayerService{store: store, importService: importService}
}

func (service *PlayerService) FindPlayer(id int) *model.Player {
	return service.store.FindById(id)
}

func (service *PlayerService) FindAll() *[]model.Player {
	return service.store.FindAll()
}

func (service *PlayerService) ImportPlayers() *[]model.Player {
	players := service.importService.searchAnstossSite()
	service.store.DeleteAll()
	service.store.Save(players)
	return players
}

func (service *PlayerService) Search(positions []string, strengthFrom int, strengthTo int, ageFrom int, ageTo int, maxPercent int, maxAgePercent int) *[]model.Player {
	if positions == nil || len(positions) == 0 {
		positions = supportedPositions
	}
	if slices.Contains(positions, "LM") || slices.Contains(positions, "RM") {
		positions = append(positions, "LM RM")
	}
	if slices.Contains(positions, "LV") || slices.Contains(positions, "RV") {
		positions = append(positions, "LV RV")
	}
	return service.store.Search(
		positions,
		strengthFrom,
		If(strengthTo == 0, 13, strengthTo),
		ageFrom,
		If(ageTo == 0, 50, ageTo),
		If(maxPercent == 0, -50, maxPercent),
		If(maxAgePercent == 0, -50, maxAgePercent),
	)
}

func If[T any](condition bool, ifTrue, ifFalse T) T {
	if condition {
		return ifTrue
	}
	return ifFalse
}
