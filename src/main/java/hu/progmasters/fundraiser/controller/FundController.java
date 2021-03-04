package hu.progmasters.fundraiser.controller;

import hu.progmasters.fundraiser.dto.fund.*;
import hu.progmasters.fundraiser.service.FundService;
import hu.progmasters.fundraiser.validation.FundFormCommandValidator;
import hu.progmasters.fundraiser.validation.ModifyFundFormCommandValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    protected void initModifyFundBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(modifyFundFormCommandValidator);
    }


    @GetMapping(value = {"/list", "/list/{category}"})
    public ResponseEntity<FundPageData> fetchFundsForList(
            Pageable pageInformation,
            @PathVariable(required = false) String category,
            Locale locale) {
        FundPageData fetchedData = fundService.fetchPageableList(pageInformation, category, locale);
        return new ResponseEntity<>(fetchedData, HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<FundDetailsItem> getFundDetails(@PathVariable Long id, Locale locale) {
        return new ResponseEntity<>(fundService.fetchFundDetails(id, locale), HttpStatus.OK);
    }

    @GetMapping("/myFunds")
    public ResponseEntity<MyFundsData> fetchMyFunds(Principal principal, Locale locale) {
        return new ResponseEntity<>(fundService.fetchMyFunds(principal.getName(), locale), HttpStatus.OK);
    }

    @GetMapping("/myFunds/{id}")
    public ResponseEntity<FundDetailsItem> fetchMyFundDetails(Principal principal, @PathVariable Long id, Locale locale) {
        return new ResponseEntity<>(fundService.fetchMyFundDetails(principal.getName(), id, locale), HttpStatus.OK);
    }

    @GetMapping("/modify/{id}")
    public ResponseEntity<ModifyFundFormInit> fillModifyFundForm(Principal principal, @PathVariable Long id, Locale locale) {
        return new ResponseEntity<>(fundService.fillModifyFundForm(principal.getName(), id, locale), HttpStatus.OK);
    }

    @PutMapping("/modify")
    public ResponseEntity<Void> modifyFund(@ModelAttribute @Valid ModifyFundFormCommand modifyFundFormCommand, Principal principal) {
        fundService.modifyFund(principal.getName(), modifyFundFormCommand);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> saveNewFund(@ModelAttribute @Valid FundFormCommand fundFormCommand, Principal principal) {
        String emailAddress = principal.getName();
        fundService.saveNewFund(emailAddress, fundFormCommand);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/initData")
    public ResponseEntity<FundFormInitData> fetchFundFormInitData(Locale locale) {
        logger.info("Category list, status list and currency list requested");
        return new ResponseEntity<>(fundService.fetchFundFormInitData(locale), HttpStatus.OK);
    }

    @GetMapping("/enumData")
    public ResponseEntity<EnumData> fetchEnumData(Locale locale) {
        logger.info("Enum data requested");
        return new ResponseEntity<>(fundService.getEnumData(locale), HttpStatus.OK);
    }

    @GetMapping(
            value = "pdf/{id}",
            produces = MediaType.APPLICATION_PDF_VALUE
    )
    public @ResponseBody byte[] generatePdf(@PathVariable Long id, Locale locale) {
        return fundService.generatePdf(id, locale);
    }

}
