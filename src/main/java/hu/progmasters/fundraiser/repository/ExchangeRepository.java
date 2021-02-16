package hu.progmasters.fundraiser.repository;

import hu.progmasters.fundraiser.domain.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeRepository extends JpaRepository<ExchangeRate, Long> {

}
