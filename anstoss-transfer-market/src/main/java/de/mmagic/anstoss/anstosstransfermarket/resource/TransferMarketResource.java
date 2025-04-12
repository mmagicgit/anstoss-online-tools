package de.mmagic.anstoss.anstosstransfermarket.resource;

import de.mmagic.anstoss.anstosstransfermarket.model.Player;
import de.mmagic.anstoss.anstosstransfermarket.service.PlayerService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/player")
public class TransferMarketResource {

    private final PlayerService playerService;

    public TransferMarketResource(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping
    public List<PlayerRest> search() {
        return toRestModel(playerService.findAll());
    }

    @GetMapping("/search")
    public List<PlayerRest> search(
            @RequestParam(value = "position", required = false) List<String> positions,
            @RequestParam(value = "strengthFrom", required = false) Integer strengthFrom,
            @RequestParam(value = "strengthTo", required = false) Integer strengthTo,
            @RequestParam(value = "ageFrom", required = false) Integer ageFrom,
            @RequestParam(value = "ageTo", required = false) Integer ageTo,
            @RequestParam(value = "maxPercent", required = false) Integer maxPercent,
            @RequestParam(value = "category", required = false) List<String> categories
    ) {
        return toRestModel(playerService.search(positions == null ? List.of() : positions, strengthFrom, strengthTo, ageFrom, ageTo, maxPercent, categories == null ? List.of() : categories));
    }

    @GetMapping(value = "/import", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PlayerRest> importPlayers() {
        playerService.importPlayers();
        return search();
    }

    private List<PlayerRest> toRestModel(List<Player> all) {
        return all.stream().map(new PlayerConverter()).collect(Collectors.toList());
    }
}
