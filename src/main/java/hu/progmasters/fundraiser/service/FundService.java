package hu.progmasters.fundraiser.service;

import hu.progmasters.fundraiser.domain.Fund;
import hu.progmasters.fundraiser.repository.FundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FundService {

    //TODO - REVIEW: Aláhúzza az IDEA, nem szúrja a szemeteket? :) Mehet minden final-ra (mindenhol)
    private FundRepository fundRepository;

    @Autowired
    public FundService(FundRepository fundRepository) {
        this.fundRepository = fundRepository;
    }

    public List<Fund> findAll() {
        return fundRepository.findAll();
    }

}
