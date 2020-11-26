package de.mmagic.anstoss.service;

import io.quarkus.scheduler.Scheduled;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class ImportScheduler {

    @Inject
    PlayerService playerService;

    @Scheduled(cron = "0 0 4 * * ?")
    public void importPlayers() {
        playerService.importPlayers();
    }
}
