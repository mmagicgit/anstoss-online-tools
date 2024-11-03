package resource

import (
	"anstoss-transfer-market-go/service"
	"encoding/json"
	"errors"
	"fmt"
	"log/slog"
	"net/http"
	"strconv"
)

type PlayerResource struct {
	service *service.PlayerService
}

func NewPlayerResource(service *service.PlayerService) *PlayerResource {
	return &PlayerResource{service: service}
}

func (resource *PlayerResource) hello(response http.ResponseWriter, request *http.Request) {
	slog.Debug(fmt.Sprintf("%s %s", request.Method, request.URL))
	_, err := fmt.Fprint(response, "Hello!")
	if err != nil {
		http.Error(response, err.Error(), http.StatusInternalServerError)
		return
	}
}

func (resource *PlayerResource) getPlayer(response http.ResponseWriter, request *http.Request) {
	slog.Debug(fmt.Sprintf("%s %s", request.Method, request.URL))
	response.Header().Set("Content-Type", "application/json")
	var idAsString = request.PathValue("id")
	id, err := strconv.Atoi(idAsString)
	if err != nil {
		http.Error(response, err.Error(), http.StatusBadRequest)
		return
	}
	player, err := resource.service.FindPlayer(id)
	if err != nil {
		http.Error(response, err.Error(), http.StatusNotFound)
		return
	}
	err = json.NewEncoder(response).Encode(player)
	if err != nil {
		http.Error(response, err.Error(), http.StatusInternalServerError)
		return
	}

}
func (resource *PlayerResource) getAll(response http.ResponseWriter, request *http.Request) {
	slog.Debug(fmt.Sprintf("%s %s", request.Method, request.URL))
	response.Header().Set("Content-Type", "application/json")
	players, err := resource.service.FindAll()
	if err != nil {
		http.Error(response, err.Error(), http.StatusInternalServerError)
		return
	}
	err = json.NewEncoder(response).Encode(players)
	if err != nil {
		http.Error(response, err.Error(), http.StatusInternalServerError)
		return
	}
}

func (resource *PlayerResource) search(response http.ResponseWriter, request *http.Request) {
	slog.Debug(fmt.Sprintf("%s %s", request.Method, request.URL))
	response.Header().Set("Content-Type", "application/json")
	errParse := request.ParseForm()
	positions := request.Form["position"]
	categories := request.Form["category"]
	strengthFrom, errStrengthFrom := strconv.Atoi(request.URL.Query().Get("strengthFrom"))
	strengthTo, errStrengthTo := strconv.Atoi(request.URL.Query().Get("strengthTo"))
	ageFrom, errAgeFrom := strconv.Atoi(request.URL.Query().Get("ageFrom"))
	ageTo, errAgeTo := strconv.Atoi(request.URL.Query().Get("ageTo"))
	maxPercent, errMaxPercent := strconv.Atoi(request.URL.Query().Get("maxPercent"))
	if err := errors.Join(errParse, errStrengthFrom, errStrengthTo, errAgeFrom, errAgeTo, errMaxPercent); err != nil {
		http.Error(response, err.Error(), http.StatusBadRequest)
		return
	}
	players, err := resource.service.Search(positions, strengthFrom, strengthTo, ageFrom, ageTo, maxPercent, categories)
	if err != nil {
		http.Error(response, err.Error(), http.StatusInternalServerError)
		return
	}
	err = json.NewEncoder(response).Encode(players)
	if err != nil {
		http.Error(response, err.Error(), http.StatusInternalServerError)
		return
	}
}

func (resource *PlayerResource) importPlayers(response http.ResponseWriter, request *http.Request) {
	slog.Debug(fmt.Sprintf("%s %s", request.Method, request.URL))
	response.Header().Set("Content-Type", "application/json")
	players, err := resource.service.ImportPlayers()
	if err != nil {
		http.Error(response, err.Error(), http.StatusInternalServerError)
		return
	}
	err = json.NewEncoder(response).Encode(players)
	if err != nil {
		http.Error(response, err.Error(), http.StatusInternalServerError)
		return
	}
}
