package service

import (
	"github.com/robfig/cron/v3"
	"log"
)

func StartScheduler(playerService *PlayerService) {
	c := cron.New()
	_, err := c.AddFunc("0 3 * * *", func() {
		playerService.ImportPlayers()
	})
	if err != nil {
		log.Fatal(err)
	}
	c.Start()
}
