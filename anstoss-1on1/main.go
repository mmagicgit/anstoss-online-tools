package main

import (
	"anstoss-1on1/service"
	"os"
)

func main() {
	httpClient := service.NewAnstossHttpClient(os.Args[1], os.Args[2])
	var currentTeam = os.Args[3]
	oneOnOneService := service.NewAOneOnOneService(httpClient)
	oneOnOneService.FetchData(currentTeam)
}
