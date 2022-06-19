package resource

import (
	"github.com/gorilla/mux"
	"log"
	"net/http"
)

type Server struct {
	router *mux.Router
}

func NewServer(resource *PlayerResource) *Server {
	router := mux.NewRouter()
	router.HandleFunc("/", resource.hello).Methods("GET")
	router.HandleFunc("/player", resource.getAll).Methods("GET")
	router.HandleFunc("/player/search", resource.search).Methods("GET")
	router.HandleFunc("/player/import", resource.importPlayers).Methods("GET")
	router.HandleFunc("/player/{id}", resource.getPlayer).Methods("GET")
	return &Server{router: router}
}

func (server *Server) ListenAndServe() {
	err := http.ListenAndServe(":9000", server.router)
	if err != nil {
		log.Fatal(err)
	}
}
