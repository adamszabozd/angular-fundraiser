package hu.progmasters.fundraiser.service;

import hu.progmasters.fundraiser.domain.Fund;
import hu.progmasters.fundraiser.dto.FundListItem;
import hu.progmasters.fundraiser.repository.FundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FundService {

    private final FundRepository fundRepository;

    @Autowired
    public FundService(FundRepository fundRepository) {
        this.fundRepository = fundRepository;
    }

    public List<Fund> findAll() {
        return fundRepository.findAll();
    }

    public List<FundListItem> fetchAllForList(){
        return fundRepository.findAll().stream().map(FundListItem::new).collect(Collectors.toList());
    }



}
