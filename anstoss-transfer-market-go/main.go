package main

import (
	"anstoss-transfer-market-go/resource"
	"anstoss-transfer-market-go/service"
	"anstoss-transfer-market-go/store"
	"os"
)

func main() {
	importService := service.NewPlayerImportService(service.NewAnstossHttpClient(os.Args[1], os.Args[2]))
	playerService := service.NewPlayerService(store.NewPlayerStore(), importService)
	playerResource := resource.NewPlayerResource(playerService)
	server := resource.NewServer(playerResource)
	server.ListenAndServe()
}
