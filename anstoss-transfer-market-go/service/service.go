package service

import (
	"anstoss-transfer-market-go/model"
	"anstoss-transfer-market-go/store"
	"slices"
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

func (service *PlayerService) FindPlayer(id int) (*model.Player, error) {
	return service.store.FindById(id)
}

func (service *PlayerService) FindAll() (*[]model.Player, error) {
	return service.store.FindAll()
}

func (service *PlayerService) ImportPlayers() (*[]model.Player, error) {
	players, err := service.importService.searchAnstossSite()
	if err != nil {
		return nil, err
	}
	if err = service.store.DeleteAll(); err != nil {
		return nil, err
	}
	if err = service.store.Save(players); err != nil {
		return nil, err
	}
	return players, nil
}

func (service *PlayerService) Search(positions []string, strengthFrom int, strengthTo int, ageFrom int, ageTo int, maxPercent int, categories []string) (*[]model.Player, error) {
	if positions == nil {
		positions = []string{}
	}
	if slices.Contains(positions, "LM") || slices.Contains(positions, "RM") {
		positions = append(positions, "LM RM")
	}
	if slices.Contains(positions, "LV") || slices.Contains(positions, "RV") {
		positions = append(positions, "LV RV")
	}
	if categories == nil {
		categories = []string{}
	}
	return service.store.Search(
		positions,
		strengthFrom,
		If(strengthTo == 0, 13, strengthTo),
		ageFrom,
		If(ageTo == 0, 50, ageTo),
		If(maxPercent == 0, -50, maxPercent),
		categories,
	)
}

func If[T any](condition bool, ifTrue, ifFalse T) T {
	if condition {
		return ifTrue
	}
	return ifFalse
}
