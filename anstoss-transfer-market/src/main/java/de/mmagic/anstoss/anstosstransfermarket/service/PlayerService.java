package de.mmagic.anstoss.anstosstransfermarket.service;

import de.mmagic.anstoss.anstosstransfermarket.model.Player;
import de.mmagic.anstoss.anstosstransfermarket.store.PlayerStore;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {

    private final TransferMarketImportService transferMarketImportService;
    private final PlayerStore playerStore;

    public PlayerService(TransferMarketImportService transferMarketImportService, PlayerStore playerStore) {
        this.transferMarketImportService = transferMarketImportService;
        this.playerStore = playerStore;
    }

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
