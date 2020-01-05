package de.mmagic.anstoss;

import de.mmagic.anstoss.model.Player;
import de.mmagic.anstoss.service.TransferMarketService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/search")
public class TransferMarketResource {

    @Inject
    TransferMarketService transferMarketService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Player> search() {
        return transferMarketService.search();
    }
}