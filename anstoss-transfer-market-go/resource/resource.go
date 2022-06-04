package resource

import (
	"anstoss-transfer-market-go/service"
	"encoding/json"
	"fmt"
	"github.com/gorilla/mux"
	"log"
	"net/http"
	"strconv"
)

type PlayerResource struct {
	service *service.PlayerService
}

func NewPlayerResource(service *service.PlayerService) *PlayerResource {
	return &PlayerResource{service: service}
}

func (resource PlayerResource) hello(response http.ResponseWriter, request *http.Request) {
	_, err := fmt.Fprint(response, "Hello!")
	if err != nil {
		log.Fatal(err)
	}
}

func (resource PlayerResource) getPlayer(response http.ResponseWriter, request *http.Request) {
	response.Header().Set("Content-Type", "application/json")
	var params = mux.Vars(request)
	id, _ := strconv.Atoi(params["id"])
	player := resource.service.FindPlayer(id)
	err := json.NewEncoder(response).Encode(player)
	if err != nil {
		log.Fatal(err)
	}
}
func (resource PlayerResource) getAll(response http.ResponseWriter, request *http.Request) {
	response.Header().Set("Content-Type", "application/json")
	players := resource.service.FindAll()
	err := json.NewEncoder(response).Encode(players)
	if err != nil {
		log.Fatal(err)
	}
}

func (resource PlayerResource) search(response http.ResponseWriter, request *http.Request) {
	response.Header().Set("Content-Type", "application/json")
	err := request.ParseForm()
	if err != nil {
		log.Fatal(err)
	}
	positions := request.Form["position"]
	strengthFrom, _ := strconv.Atoi(request.URL.Query().Get("strengthFrom"))
	strengthTo, _ := strconv.Atoi(request.URL.Query().Get("strengthTo"))
	ageFrom, _ := strconv.Atoi(request.URL.Query().Get("ageFrom"))
	ageTo, _ := strconv.Atoi(request.URL.Query().Get("ageTo"))
	maxPercent, _ := strconv.Atoi(request.URL.Query().Get("maxPercent"))
	maxAgePercent, _ := strconv.Atoi(request.URL.Query().Get("maxAgePercent"))
	players := resource.service.Search(positions, strengthFrom, strengthTo, ageFrom, ageTo, maxPercent, maxAgePercent)
	err = json.NewEncoder(response).Encode(players)
	if err != nil {
		log.Fatal(err)
	}
}

func (resource PlayerResource) importPlayers(response http.ResponseWriter, request *http.Request) {
	response.Header().Set("Content-Type", "application/json")
	players := resource.service.ImportPlayers()
	err := json.NewEncoder(response).Encode(players)
	if err != nil {
		log.Fatal(err)
	}
}
