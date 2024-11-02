package service

import (
	"github.com/robfig/cron/v3"
	"log/slog"
)

func StartScheduler(playerService *PlayerService) error {
	c := cron.New()
	_, err := c.AddFunc("0 3 * * *", func() {
		slog.Info("Starting player import")
		_, err := playerService.ImportPlayers()
		if err != nil {
			slog.Error("Error importing players", slog.String("error", err.Error()))
		}
	})
	if err != nil {
		return err
	}
	c.Start()
	return nil
}
