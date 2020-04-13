package de.mmagic.anstoss.resource;

import de.mmagic.anstoss.model.Player;
import de.mmagic.anstoss.service.PlayerService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

@Path("/players")
public class TransferMarketResource {

    @Inject
    PlayerService playerService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<PlayerRest> search() {
        return toRestModel(playerService.findAll());
    }

    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public List<PlayerRest> search(
            @QueryParam("position") List<String> positions,
            @QueryParam("strengthFrom") Integer strengthFrom,
            @QueryParam("strengthTo") Integer strengthTo,
            @QueryParam("ageFrom") Integer ageFrom,
            @QueryParam("ageTo") Integer ageTo,
            @QueryParam("maxPercent") Integer maxPercent,
            @QueryParam("maxAgePercent") Integer maxAgePercent) {
        return toRestModel(playerService.search(positions, strengthFrom, strengthTo, ageFrom, ageTo, maxPercent, maxAgePercent));
    }

    @GET
    @Path("/import")
    @Produces(MediaType.APPLICATION_JSON)
    public List<PlayerRest> importPlayers() {
        playerService.importPlayers();
        return search();
    }

    private List<PlayerRest> toRestModel(List<Player> all) {
        return all.stream().map(new PlayerConverter()).collect(Collectors.toList());
    }
}
