package resource

import (
	"fmt"
	"github.com/gorilla/mux"
	"github.com/rs/cors"
	"log/slog"
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

func (server *Server) ListenAndServe() error {
	handler := cors.AllowAll().Handler(server.router)
	port := 1111
	slog.Info(fmt.Sprintf("Starting server an port %d", port))
	if err := http.ListenAndServe(fmt.Sprintf(":%d", port), handler); err != nil {
		return err
	}
	return nil
}
