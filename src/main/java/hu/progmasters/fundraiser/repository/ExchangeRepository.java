package hu.progmasters.fundraiser.repository;

import hu.progmasters.fundraiser.domain.Currency;
import hu.progmasters.fundraiser.domain.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeRepository extends JpaRepository<ExchangeRate, Long> {


    @Query("SELECT e.rate FROM ExchangeRate e WHERE e.currentTime = (SELECT MAX(e.currentTime) FROM ExchangeRate e) AND e.currency = :currency")
    Double findLatestRateByCurrency(Currency currency);
}
