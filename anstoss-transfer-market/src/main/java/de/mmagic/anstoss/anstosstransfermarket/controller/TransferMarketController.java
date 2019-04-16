package de.mmagic.anstoss.anstosstransfermarket.controller;

import de.mmagic.anstoss.anstosstransfermarket.model.Player;
import de.mmagic.anstoss.anstosstransfermarket.service.TransferMarketService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class TransferMarketController {

    private final TransferMarketService transferMarketService;

    public TransferMarketController(TransferMarketService transferMarketService) {
        this.transferMarketService = transferMarketService;
    }

    @RequestMapping("/search")
    public List<Player> search() {
        return transferMarketService.search();
    }
}
