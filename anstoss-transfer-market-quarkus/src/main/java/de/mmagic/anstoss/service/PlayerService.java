package de.mmagic.anstoss.service;

import de.mmagic.anstoss.model.Player;
import de.mmagic.anstoss.store.PlayerStore;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        List<String> positionsToSearch = new ArrayList<>(positions);
        if (positionsToSearch.contains("LM") || positionsToSearch.contains("RM")) {
            positionsToSearch.add("LM RM");
        }
        if (positionsToSearch.contains("LV") || positionsToSearch.contains("RV")) {
            positionsToSearch.add("LV RV");
        }
        return playerStore.search(
                positionsToSearch,
                strengthFrom == null ? 0 : strengthFrom,
                strengthTo == null ? 13 : strengthTo,
                ageFrom == null? 0 : ageFrom,
                ageTo == null ? 50 : ageTo,
                maxPercent == null ? -50 : maxPercent,
                maxAgePercent == null ? -50: maxAgePercent
        );
    }

    public void importPlayers() {
        playerStore.deleteAll();
        List<Player> players = transferMarketImportService.search();
        playerStore.save(players);
    }
}
