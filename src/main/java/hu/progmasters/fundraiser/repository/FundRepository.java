package hu.progmasters.fundraiser.repository;

import hu.progmasters.fundraiser.domain.Fund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FundRepository extends JpaRepository<Fund, Long> {

    Fund findByFundTitle(String title);

}
