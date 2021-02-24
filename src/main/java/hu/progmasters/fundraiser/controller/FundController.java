package hu.progmasters.fundraiser.controller;

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
import java.util.List;
import java.util.Locale;

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
    protected void initFundBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(validator);
    }

    @InitBinder(value = "modifyFundFormCommand")
    protected void initModifyFundBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(modifyFundFormCommandValidator);
    }

    @GetMapping
    public ResponseEntity<List<FundListItem>> fetchAllFunds(Locale locale) {
        ResponseEntity<List<FundListItem>> response = new ResponseEntity<>(fundService.fetchAllForList(locale), HttpStatus.OK);
        logger.info("Fund list requested");
        return response;
    }

    @GetMapping("/myFunds")
    public ResponseEntity<List<FundListItem>> fetchMyFunds(Principal principal, Locale locale) {
        return new ResponseEntity<>(fundService.fetchMyFunds(principal.getName(), locale), HttpStatus.OK);
    }

    @GetMapping("/initData")
    public ResponseEntity<FundFormInitData> fetchFundFormInitData(Locale locale) {
        logger.info("Category list, status list and currency list requested");
        return new ResponseEntity<>(fundService.fetchFundFormInitData(locale), HttpStatus.OK);
    }
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryOption>> fetchCategoryOptions(Locale locale) {
        logger.info("Category list requested");
        return new ResponseEntity<>(fundService.getCategoryOptions(locale), HttpStatus.OK); //getCategoryOptions
    }

    @GetMapping("/{id}")
    public ResponseEntity<FundDetailsItem> getFundDetails(@PathVariable Long id, Locale locale) {
        return new ResponseEntity<>(fundService.fetchFundDetails(id, locale), HttpStatus.OK);
    }

    @GetMapping("/modify/{id}")
    public ResponseEntity<FundModifyItem> modifyFund(@PathVariable Long id, Locale locale) {
        return new ResponseEntity<>(fundService.fetchModifyFund(id, locale), HttpStatus.OK);
    }

    @GetMapping("/categories/{category}")
    public ResponseEntity<List<FundListItem>> getFundsByCategory(@PathVariable String category, Locale locale) {
        return new ResponseEntity<>(fundService.fetchFundsByCategory(category, locale), HttpStatus.OK);
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
