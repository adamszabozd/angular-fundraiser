package hu.progmasters.fundraiser.controller;

import hu.progmasters.fundraiser.dto.FundListItem;
import hu.progmasters.fundraiser.service.FundService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/funds")
public class FundController {

    private static final Logger logger = LoggerFactory.getLogger(TransferController.class);

    private FundService fundService;

    @Autowired
    public FundController(FundService fundService) {
        this.fundService = fundService;
    }

    @GetMapping
    public ResponseEntity<List<FundListItem>> fetchAllFunds(){
        ResponseEntity<List<FundListItem>> response = new ResponseEntity<>(fundService.fetchAllForList(), HttpStatus.OK);
        logger.info("Fund list requested");
        return response;
    }
}
