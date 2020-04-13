package de.mmagic.anstoss.service;

import de.mmagic.anstoss.model.Player;
import de.mmagic.anstoss.store.PlayerStore;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class PlayerService {

    @Inject
    TransferMarketImportService transferMarketImportService;

    @Inject
    PlayerStore playerStore;

    public List<Player> findAll() {
        return playerStore.findAll();
    }

    public List<Player> search(List<String> positions, Integer strengthFrom, Integer strengthTo, Integer ageFrom, Integer ageTo, Integer maxPercent, Integer maxAgePercent) {
        return playerStore.search(
                positions,
                strengthFrom == null ? 0 : strengthFrom,
                strengthTo == null ? 13 : strengthTo,
                ageFrom == null? 0 : ageFrom,
                ageTo == null ? 50 : ageTo,
                maxPercent == null ? -50 : maxPercent,
                maxAgePercent == null ? -50: maxAgePercent
        );
    }

    public void importPlayers() {
        List<Player> players = transferMarketImportService.search();
        playerStore.addAll(players);
    }
}
