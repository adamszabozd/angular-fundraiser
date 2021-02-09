package hu.progmasters.fundraiser.controller;

import hu.progmasters.fundraiser.domain.FundCategory;
import hu.progmasters.fundraiser.dto.CategoryOption;
import hu.progmasters.fundraiser.dto.FundFormCommand;
import hu.progmasters.fundraiser.dto.FundListItem;
import hu.progmasters.fundraiser.service.FundService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
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

    @GetMapping("/initData")
    public ResponseEntity<List<CategoryOption>> fetchCategoryList(){
        List<CategoryOption> categoryOptions = new ArrayList<>();
        for (FundCategory category : FundCategory.values()) {
            categoryOptions.add(new CategoryOption(category));
        }
        ResponseEntity<List<CategoryOption>> response = new ResponseEntity<>(categoryOptions, HttpStatus.OK);
        logger.info("Category list requested");
        return response;
    }

    @PostMapping
    public ResponseEntity saveNewFund(@RequestBody FundFormCommand fundFormCommand, Principal principal){
        String emailAddress = principal.getName();
        fundService.savenewFund(fundFormCommand, emailAddress);
        return new ResponseEntity(HttpStatus.CREATED);
    }
}
