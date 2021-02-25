package hu.progmasters.fundraiser.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.progmasters.fundraiser.domain.*;
import hu.progmasters.fundraiser.dto.fund.*;
import hu.progmasters.fundraiser.repository.FundRepository;
import hu.progmasters.fundraiser.repository.TransferRepository;
import hu.progmasters.fundraiser.service.cloudinary.CloudinaryUploadException;
import hu.progmasters.fundraiser.service.cloudinary.UploadResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class FundService {

    private final FundRepository fundRepository;
    //TODO - Review: Ne hívjuk "keresztbe" a rétegeket, ha elkerülhető, FundService a Fundokért felel,
    // ergo FundRepositoryba nyúljon csak. Amennyiben Account v Transfer kell neki, azt a dedikált Serviceiken keresztül kérjük le inkább
    private final AccountService accountService;
    private final TransferRepository transferRepository;
    private final MessageSource messageSource;
    private final ExchangeService exchangeService;
    private final Cloudinary cloudinary;

    @Autowired
    public FundService(FundRepository fundRepository,
                       AccountService accountService,
                       TransferRepository transferRepository,
                       ExchangeService exchangeService,
                       MessageSource messageSource,
                       Cloudinary cloudinary
    ) {
        this.fundRepository = fundRepository;
        this.accountService = accountService;
        this.exchangeService = exchangeService;
        this.transferRepository = transferRepository;
        this.messageSource = messageSource;
        this.cloudinary = cloudinary;
    }

    public List<Fund> findAll() {
        return fundRepository.findAll();
    }

    public List<FundListItem> fetchAllForList(Locale locale) {
        return fundRepository.findAllActiveFunds().stream()
                .map(fund -> {
                    String categoryDisplayName = messageSource.getMessage(fund.getFundCategory().getCode(), null, locale);
                    return new FundListItem(fund, categoryDisplayName);
                })
                .collect(Collectors.toList());
    }

    public void saveNewFund(FundFormCommand fundFormCommand, String emailAddress) {
        Account myAccount = accountService.findByEmail(emailAddress);
        String uploadedImageUrl = storeFile(fundFormCommand.getImageFile());
        Fund fund = new Fund(fundFormCommand, myAccount, uploadedImageUrl);
        fundRepository.save(fund);
    }

    public Fund findById(Long id) {
        Optional<Fund> optionalFund = fundRepository.findById(id);
        if (optionalFund.isPresent()) {
            return optionalFund.get();
        } else {
            throw new EntityNotFoundException();
        }
    }

    public FundModifyItem fetchModifyFund(Long id, Locale locale) {
        Fund fund = findById(id);
        List<StatusOption> statusOptions = getStatusOptions(locale);
        return new FundModifyItem(fund, statusOptions);
    }

    public String storeFile(CommonsMultipartFile commonsMultipartFile) {

        Map params = ObjectUtils.asMap(
                "access_mode", "authenticated",
//                "access_type", "token",
                "overwrite", false,
                "type", "authenticated",
                "resource_type", "auto",
                "use_filename", true);
        UploadResponse uploadResponse;
        File fileToUpload = new File(System.getProperty("java.io.tmpdir") + '/' + commonsMultipartFile.getOriginalFilename());
        try {
            commonsMultipartFile.transferTo(fileToUpload);
            uploadResponse = new ObjectMapper()
                    .convertValue(cloudinary.uploader().upload(fileToUpload, params), UploadResponse.class);
        } catch (IOException e) {
            throw new CloudinaryUploadException();
        }
        return uploadResponse.getSecureUrl();
    }

    public FundDetailsItem fetchFundDetails(Long id, Locale locale) {
        //TODO - Review: A következő két sor kb 3x ismétlődik, csak ebben az osztályban...
        // Sok kicsi sokra megy...
        Optional<Fund> fund = fundRepository.findById(id);
        if (fund.isPresent()) {
            Long backers = transferRepository.numberOfBackers(id);
            String categoryDisplayName = messageSource.getMessage(fund.get().getFundCategory().getCode(), null, locale);
            return new FundDetailsItem(fund.get(), backers, categoryDisplayName, getDailyDonations(fund.get().getTransferList(), fund.get().getTimeStamp()));
        } else {
            throw new EntityNotFoundException();
        }
    }

    public List<DailyDonation> getDailyDonations(List<Transfer> transferList, LocalDateTime startDateTime) {
        LocalDate date = startDateTime.toLocalDate();
        Map<String, Double> dailyDonationMap = new HashMap<>();
        while (!date.isAfter(LocalDate.now())) {
            String dateString = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            dailyDonationMap.put(dateString, 0.0);
            date = date.plusDays(1);
        }
        for (Transfer transfer : transferList) {
            if (transfer.getConfirmed() && !transfer.getTimeStamp().isBefore(startDateTime)) {
                String dateString = transfer.getTimeStamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                dailyDonationMap.put(dateString, dailyDonationMap.getOrDefault(dateString, 0.0) + transfer.getTargetAmount());
            }
        }
        List<DailyDonation> result = new ArrayList<>();
        for (Map.Entry<String, Double> entry : dailyDonationMap.entrySet()) {
            result.add(new DailyDonation(entry.getKey(), entry.getValue()));
        }
        result.sort(Comparator.comparing(DailyDonation::getDate));
        return result;
    }

    public List<FundListItem> fetchMyFunds(String email, Locale locale) {

        Account myAccount = accountService.findByEmail(email);
        //TODO - Review: Itt miért nem a FundRepository van használva??
        return myAccount.getFunds().stream()
                .map(fund -> {
                    String categoryDisplayName = messageSource.getMessage(fund.getFundCategory().getCode(), null, locale);
                    return new FundListItem(fund, categoryDisplayName);
                })
                .collect(Collectors.toList());
    }

    public void modifyFund(ModifyFundFormCommand modifyFundFormCommand) {

        Fund fund = findById(modifyFundFormCommand.getId());
        fund.setShortDescription(modifyFundFormCommand.getShortDescription());
        fund.setLongDescription(modifyFundFormCommand.getLongDescription());
        fund.setImageUrl(modifyFundFormCommand.getImageUrl());
        fund.setTargetAmount(modifyFundFormCommand.getTargetAmount());
        fund.setEndDate(modifyFundFormCommand.getEndDate());
        fund.setStatus(Status.valueOf(modifyFundFormCommand.getStatus()));
        fundRepository.save(fund);
    }

    public List<FundListItem> fetchFundsByCategory(String categoryName, Locale locale) {
        if (contains(categoryName)) {
            FundCategory category = FundCategory.valueOf(categoryName);
            List<Fund> funds = fundRepository.findAllByCategory(category);
            return funds.stream()
                    .map(fund -> {
                        String categoryDisplayName = messageSource.getMessage(fund.getFundCategory().getCode(), null, locale);
                        return new FundListItem(fund, categoryDisplayName);
                    })
                    .collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException();
        }
    }

    public FundFormInitData fetchFundFormInitData(Locale locale) {
        FundFormInitData fundFormInitData = new FundFormInitData();
        List<CategoryOption> categoryOptions = getCategoryOptions(locale);
        List<StatusOption> statusOptions = getStatusOptions(locale);
        fundFormInitData.setCategoryOptions(categoryOptions);
        fundFormInitData.setCurrencyOptions(exchangeService.fetchRates());
        fundFormInitData.setStatusOptions(statusOptions);
        return fundFormInitData;
    }

    private boolean contains(String categoryName) {

        for (FundCategory c : FundCategory.values()) {
            if (c.name().equals(categoryName)) {
                return true;
            }
        }
        return false;
    }

    public List<Fund> findAllById(Long id) {
        List<Fund> fundList = new ArrayList<>();
        fundList.add(findById(id));
        return fundList;
    }

    public List<CategoryOption> getCategoryOptions(Locale locale) {
        List<CategoryOption> categoryOptions = new ArrayList<>();
        for (FundCategory category : FundCategory.values()) {
            categoryOptions.add(new CategoryOption(category.toString(), messageSource.getMessage(category.getCode(), null, locale)));
        }
        return categoryOptions;
    }

    private List<StatusOption> getStatusOptions(Locale locale) {
        List<StatusOption> statusOptions = new ArrayList<>();
        for (Status status : Status.values()) {
            statusOptions.add(new StatusOption(status.toString(), messageSource.getMessage(status.getCode(), null, locale)));
        }
        return statusOptions;
    }

}
