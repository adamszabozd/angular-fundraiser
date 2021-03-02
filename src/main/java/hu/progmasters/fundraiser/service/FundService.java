package hu.progmasters.fundraiser.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.istack.Nullable;
import com.itextpdf.html2pdf.HtmlConverter;
import hu.progmasters.fundraiser.domain.*;
import hu.progmasters.fundraiser.dto.fund.*;
import hu.progmasters.fundraiser.repository.FundRepository;
import hu.progmasters.fundraiser.repository.TransferRepository;
import hu.progmasters.fundraiser.service.cloudinary.CloudinaryUploadException;
import hu.progmasters.fundraiser.service.cloudinary.UploadResponse;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.persistence.EntityNotFoundException;
import java.io.*;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static hu.progmasters.fundraiser.service.FundSpecifications.fundBelongsToCategory;
import static hu.progmasters.fundraiser.service.FundSpecifications.fundIsActive;

@Service
@Transactional
public class FundService {

    private final FundRepository fundRepository;
    private final AccountService accountService;
    private final MessageSource messageSource;
    private final ExchangeService exchangeService;
    private final Cloudinary cloudinary;
    private final TransferRepository transferRepository;
    private final SpringTemplateEngine thymeleafTemplateEngine;

    @Autowired
    public FundService(FundRepository fundRepository,
                       AccountService accountService,
                       TransferRepository transferRepository,
                       ExchangeService exchangeService,
                       MessageSource messageSource,
                       Cloudinary cloudinary,
                       SpringTemplateEngine thymeleafTemplateEngine
    ) {
        this.fundRepository = fundRepository;
        this.accountService = accountService;
        this.exchangeService = exchangeService;
        this.transferRepository = transferRepository;
        this.messageSource = messageSource;
        this.cloudinary = cloudinary;
        this.thymeleafTemplateEngine = thymeleafTemplateEngine;
    }


    private List<FundListItem> fetchFundsToList(List<Fund> fundList, Locale locale) {
        return fundList.stream()
                       .map(fund -> {
                           String categoryDisplayName = messageSource.getMessage(fund.getFundCategory().getCode(), null, locale);
                           return new FundListItem(fund, categoryDisplayName);
                       })
                       .collect(Collectors.toList());
    }

    public FundDetailsItem fetchFundDetails(Long id, Locale locale) {
        Optional<Fund> optionalFund = fundRepository.findById(id);
        return getFundDetailsItem(locale, id, optionalFund);
    }

    private FundDetailsItem getFundDetailsItem(Locale locale, Long id, Optional<Fund> optionalFund) {
        if (optionalFund.isPresent()) {
            Long backers = transferRepository.numberOfBackers(id);
            CategoryOption category = new CategoryOption(optionalFund.get().getFundCategory().toString(),
                                                         messageSource.getMessage(optionalFund.get().getFundCategory().getCode(),
                                                                                  null, locale));
            return new FundDetailsItem(optionalFund.get(), backers, category, getDailyDonations(optionalFund.get().getTransferList(),
                                                                                                optionalFund.get().getTimeStamp()));
        } else {
            throw new EntityNotFoundException();
        }
    }

    private List<DailyDonation> getDailyDonations(List<Transfer> transferList, LocalDateTime startDateTime) {
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
        return fetchFundsToList(fundRepository.findAllByCreator(accountService.findByEmail(email)), locale);
    }

    public FundDetailsItem fetchMyFundDetails(String email, Long id, Locale locale) {
        Optional<Fund> optionalFund = fundRepository.findByCreatorAndId(accountService.findByEmail(email), id);
        return getFundDetailsItem(locale, id, optionalFund);
    }

    public ModifyFundFormInit fillModifyFundForm(String email, Long id, Locale locale) {
        Optional<Fund> optionalFund = fundRepository.findByCreatorAndId(accountService.findByEmail(email), id);
        if (optionalFund.isPresent()) {
            List<StatusOption> statusOptions = getStatusOptions(locale);
            return new ModifyFundFormInit(optionalFund.get(), statusOptions);
        } else {
            throw new EntityNotFoundException();
        }
    }

    private List<StatusOption> getStatusOptions(Locale locale) {
        List<StatusOption> statusOptions = new ArrayList<>();
        for (Status status : Status.values()) {
            statusOptions.add(new StatusOption(status.toString(), messageSource.getMessage(status.getCode(), null, locale)));
        }
        return statusOptions;
    }

    public void modifyFund(String email, ModifyFundFormCommand modifyFundFormCommand) {
        Optional<Fund> optionalFund = fundRepository.findByCreatorAndId(accountService.findByEmail(email), modifyFundFormCommand.getId());
        if (optionalFund.isPresent()) {
            Fund fund = optionalFund.get();
            fund.setShortDescription(modifyFundFormCommand.getShortDescription());
            PolicyFactory policy = Sanitizers.BLOCKS
                    .and(Sanitizers.FORMATTING)
                    .and(Sanitizers.LINKS)
                    .and(Sanitizers.STYLES)
                    .and(Sanitizers.TABLES);
            String safeLongDescription = policy.sanitize(modifyFundFormCommand.getLongDescription());
            fund.setLongDescription(safeLongDescription);
            fund.setTargetAmount(modifyFundFormCommand.getTargetAmount());
            fund.setEndDate(modifyFundFormCommand.getEndDate());
            fund.setStatus(Status.valueOf(modifyFundFormCommand.getStatus()));
            fundRepository.save(fund);
        } else {
            throw new EntityNotFoundException();
        }
    }

    private String storeFile(CommonsMultipartFile commonsMultipartFile) {

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

    public void saveNewFund(String email, FundFormCommand fundFormCommand) {
        Account myAccount = accountService.findByEmail(email);
        String uploadedImageUrl;
        if (fundFormCommand.getImageFile() != null) {
            uploadedImageUrl = storeFile(fundFormCommand.getImageFile());
        } else {
            uploadedImageUrl = "https://cdn.iconscout.com/icon/free/png-256/k-characters-character-alphabet-letter-36028.png";
        }
        Fund fund = new Fund(fundFormCommand, myAccount, uploadedImageUrl);
        fundRepository.save(fund);
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

    public List<CategoryOption> getCategoryOptions(Locale locale) {
        List<CategoryOption> categoryOptions = new ArrayList<>();
        for (FundCategory category : FundCategory.values()) {
            categoryOptions.add(new CategoryOption(category.toString(), messageSource.getMessage(category.getCode(), null, locale)));
        }
        return categoryOptions;
    }

    public List<Fund> fetchActiveTargetFunds() {
        return fundRepository.findAllActiveFunds();
    }

    public List<Fund> findTargetFundById(Long id) {
        List<Fund> targetFund = new ArrayList<>();
        targetFund.add(findById(id));
        return targetFund;
    }

    public Fund findById(Long id) {
        Optional<Fund> optionalFund = fundRepository.findById(id);
        if (optionalFund.isPresent()) {
            return optionalFund.get();
        } else {
            throw new EntityNotFoundException();
        }
    }

    public FundPageData fetchPageableList(Pageable pageInformation, @Nullable String category, Locale locale) {
        Page<Fund> page;
        Long count;

        if (category != null) {
            page = fundRepository.findAll(Specification.where(fundIsActive()).and(fundBelongsToCategory(category)), pageInformation);
            count = fundRepository.countAllByStatusByCategory(FundCategory.valueOf(category));
        } else {
            page = fundRepository.findAll(fundIsActive(), pageInformation);
            count = fundRepository.countAllByStatus();
        }

        List<FundListItem> funds = page.stream()
                .map(fund -> {
                    String categoryDisplayName = messageSource.getMessage(fund.getFundCategory().getCode(), null, locale);
                    return new FundListItem(fund, categoryDisplayName);
                })
                .collect(Collectors.toList());

        return new FundPageData(count, funds);
    }
    public byte[] generatePdf(Long id, Locale locale) {
        Optional<Fund> fundOptional = fundRepository.findById(id);
        if (fundOptional.isEmpty()) {
            throw new EntityNotFoundException();
        }
        Fund fund = fundOptional.get();
        Context ctx = new Context(locale);
        ctx.setVariable("title", fund.getFundTitle());
        ctx.setVariable("category", messageSource.getMessage(fund.getFundCategory().getCode(), null, locale));
        NumberFormat nf = NumberFormat.getInstance(locale);
        ctx.setVariable("goalAmount", nf.format((long)(double)(fund.getTargetAmount())) + " " + fund.getCurrency().toString());
        ctx.setVariable("endDate", fund.getEndDate());
        ctx.setVariable("imageUrl", fund.getImageUrl());
        ctx.setVariable("longDescription", fund.getLongDescription());
        String htmlContent = thymeleafTemplateEngine.process("fund-details", ctx);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        HtmlConverter.convertToPdf(htmlContent, outputStream);
        return outputStream.toByteArray();
    }

}
