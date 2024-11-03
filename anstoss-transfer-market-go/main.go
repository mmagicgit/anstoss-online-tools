package main

import (
	"anstoss-transfer-market-go/resource"
	"anstoss-transfer-market-go/service"
	"anstoss-transfer-market-go/store"
	"fmt"
	"log/slog"
	"os"
)

func main() {
	logLevel := slog.LevelDebug
	slog.SetLogLoggerLevel(logLevel)
	logger := slog.New(slog.NewJSONHandler(os.Stdout, &slog.HandlerOptions{Level: logLevel}))
	//logger := slog.New(slog.NewTextHandler(os.Stdout, &slog.HandlerOptions{Level: logLevel}))
	slog.SetDefault(logger)

	//log.Println("Starting without Log level")
	slog.Debug(fmt.Sprintf("Starting with Log level %s", logLevel.String()))

	user := os.Getenv("ANSTOSS_USER")
	password := os.Getenv("ANSTOSS_PW")
	mongoConnect := os.Getenv("MONGO_CONNECT")

	client, err := service.NewAnstossHttpClient(user, password)
	if err != nil {
		slog.Error("Error creating http client", slog.String("error", err.Error()))
		return
	}
	importService := service.NewPlayerImportService(client)
	playerStore, err := store.NewPlayerStore(mongoConnect)
	if err != nil {
		slog.Error("Error connecting to database", slog.String("error", err.Error()))
		return
	}
	playerService := service.NewPlayerService(playerStore, importService)
	err = service.StartScheduler(playerService)
	if err != nil {
		slog.Error("Error starting scheduler", slog.String("error", err.Error()))
		return
	}
	playerResource := resource.NewPlayerResource(playerService)
	server := resource.NewServer(playerResource)
	err = server.ListenAndServe()
	if err != nil {
		slog.Error("Error starting server", slog.String("error", err.Error()))
		return
	}
}
