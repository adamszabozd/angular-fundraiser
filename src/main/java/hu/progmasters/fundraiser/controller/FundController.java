package hu.progmasters.fundraiser.controller;

import hu.progmasters.fundraiser.domain.FundCategory;
import hu.progmasters.fundraiser.dto.fund.*;
import hu.progmasters.fundraiser.service.FundService;
import hu.progmasters.fundraiser.validation.FundFormCommandValidator;
import hu.progmasters.fundraiser.validation.ModifyFundFormCommandValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final ModifyFundFormCommandValidator modifyFundFormCommandValidator;

    @Autowired
    public FundController(FundService fundService, FundFormCommandValidator fundFormCommandValidator, ModifyFundFormCommandValidator modifyFundFormCommandValidator) {
        this.fundService = fundService;
        this.validator = fundFormCommandValidator;
        this.modifyFundFormCommandValidator = modifyFundFormCommandValidator;

    }

    @InitBinder(value = "fundFormCommand")
    protected void init(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(validator);
    }

    //TODO - Review: Nagyon kreatív a metódusnév...
    @InitBinder(value = "modifyFundFormCommand")
    protected void init2(WebDataBinder webDataBinder){
        webDataBinder.addValidators(modifyFundFormCommandValidator);
    }

    @GetMapping
    public ResponseEntity<List<FundListItem>> fetchAllFunds() {
        ResponseEntity<List<FundListItem>> response = new ResponseEntity<>(fundService.fetchAllForList(), HttpStatus.OK);
        logger.info("Fund list requested");
        return response;
    }

    @GetMapping("/myFunds")
    public ResponseEntity<List<FundListItem>> fetchMyFunds(Principal principal) {
        return new ResponseEntity<>(fundService.fetchMyFunds(principal.getName()), HttpStatus.OK);
    }

    @GetMapping("/initData")
    public ResponseEntity<List<CategoryOption>> fetchCategoryList() {
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
    public ResponseEntity<FundDetailsItem> getFundDetails(@PathVariable Long id) {
        return new ResponseEntity<>(fundService.fetchFundDetails(id), HttpStatus.OK);
    }

    @GetMapping("/modify/{id}")
    public ResponseEntity<FundDetailsItem> modifyFund(@PathVariable Long id) {
        return new ResponseEntity<>(fundService.fetchFundDetails(id), HttpStatus.OK);
    }

    @GetMapping("/categories/{category}")
    public ResponseEntity<List<FundListItem>> getFundsByCategory(@PathVariable String category) {
        return new ResponseEntity<>(fundService.fetchFundsByCategory(category), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> saveNewFund(@RequestBody @Valid FundFormCommand fundFormCommand, Principal principal) {
        String emailAddress = principal.getName();
        fundService.saveNewFund(fundFormCommand, emailAddress);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Void> modifyFund(@RequestBody @Valid ModifyFundFormCommand modifyFundFormCommand){
        fundService.modifyFund(modifyFundFormCommand);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
