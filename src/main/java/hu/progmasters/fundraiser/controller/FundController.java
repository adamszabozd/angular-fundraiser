package hu.progmasters.fundraiser.controller;

import hu.progmasters.fundraiser.domain.FundCategory;
import hu.progmasters.fundraiser.dto.CategoryOption;
import hu.progmasters.fundraiser.dto.FundFormCommand;
import hu.progmasters.fundraiser.dto.FundListItem;
import hu.progmasters.fundraiser.service.FundService;
import hu.progmasters.fundraiser.validation.FundFormCommandValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/funds")
public class FundController {

    private static final Logger logger = LoggerFactory.getLogger(FundController.class);

    private final FundService fundService;
    private final FundFormCommandValidator validator;

    @Autowired
    public FundController(FundService fundService, FundFormCommandValidator fundFormCommandValidator) {
        this.fundService = fundService;
        this.validator = fundFormCommandValidator;
    }

    @InitBinder(value = "fundFormCommand")
    protected void init (WebDataBinder webDataBinder){
        webDataBinder.addValidators(validator);
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
        //TODO - Review: Logikát ne rakjunk a controllerbe!!!!
        for (FundCategory category : FundCategory.values()) {
            categoryOptions.add(new CategoryOption(category));
        }
        ResponseEntity<List<CategoryOption>> response = new ResponseEntity<>(categoryOptions, HttpStatus.OK);
        logger.info("Category list requested");
        return response;
    }

    @GetMapping("/{id}")
    public ResponseEntity<FundListItem> getFundDetails(@PathVariable Long id){
        return new ResponseEntity<>(fundService.fetchFundDetails(id), HttpStatus.OK);
    }

    //TODO - Review: Response Entity-nek adjunk generic típust! Sír a szegény IDEA :(
    @PostMapping
    public ResponseEntity<Void> saveNewFund(@RequestBody @Valid FundFormCommand fundFormCommand, Principal principal){
        String emailAddress = principal.getName();
        fundService.savenewFund(fundFormCommand, emailAddress);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
