package main

import (
	"anstoss-transfer-market-go/resource"
	"anstoss-transfer-market-go/service"
	"anstoss-transfer-market-go/store"
	"os"
)

func main() {
	user := os.Getenv("ANSTOSS_USER")
	password := os.Getenv("ANSTOSS_PW")
	mongoConnect := os.Getenv("MONGO_CONNECT")

	importService := service.NewPlayerImportService(service.NewAnstossHttpClient(user, password))
	playerService := service.NewPlayerService(store.NewPlayerStore(mongoConnect), importService)
	service.StartScheduler(playerService)
	playerResource := resource.NewPlayerResource(playerService)
	server := resource.NewServer(playerResource)
	server.ListenAndServe()
}
