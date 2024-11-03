package resource

import (
	"fmt"
	"github.com/rs/cors"
	"log/slog"
	"net/http"
)

type Server struct {
	router *http.ServeMux
}

func NewServer(resource *PlayerResource) *Server {
	//Before Go 1.22: wildcards and http method not supported
	//router := http.NewServeMux()
	//router.HandleFunc("/", resource.hello)
	//router.HandleFunc("/player", resource.getAllOrOne)
	//router.HandleFunc("/player/search", resource.search)
	//router.HandleFunc("/player/import", resource.importPlayers)

	//Gorilla router
	//router := mux.NewRouter()
	//router.HandleFunc("/", resource.hello).Methods("GET")
	//router.HandleFunc("/player", resource.getAll).Methods("GET")
	//router.HandleFunc("/player/{id}", resource.getPlayer).Methods("GET")
	//router.HandleFunc("/player/search", resource.search).Methods("GET")
	//router.HandleFunc("/player/import", resource.importPlayers).Methods("POST")

	//Go 1.22 router
	router := http.NewServeMux()
	router.HandleFunc("GET /", resource.hello)
	router.HandleFunc("GET /player", resource.getAll)
	router.HandleFunc("GET /player/{id}", resource.getPlayer)
	router.HandleFunc("GET /player/search", resource.search)
	router.HandleFunc("POST /player/import", resource.importPlayers)
	return &Server{router: router}
}

func (server *Server) ListenAndServe() error {
	handler := cors.AllowAll().Handler(server.router)
	port := 1111
	slog.Info(fmt.Sprintf("Server ready on port %d", port))
	if err := http.ListenAndServe(fmt.Sprintf(":%d", port), handler); err != nil {
		return err
	}
	return nil
}
