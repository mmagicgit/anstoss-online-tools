package resource

import (
	"github.com/gorilla/mux"
	"github.com/rs/cors"
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
	handler := cors.AllowAll().Handler(server.router)
	err := http.ListenAndServe(":9000", handler)
	if err != nil {
		log.Fatal(err)
	}
}
