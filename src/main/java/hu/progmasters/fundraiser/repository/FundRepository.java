package hu.progmasters.fundraiser.repository;

import hu.progmasters.fundraiser.domain.Account;
import hu.progmasters.fundraiser.domain.Fund;
import hu.progmasters.fundraiser.domain.FundCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FundRepository extends JpaRepository<Fund, Long> {

    Optional<Fund> findByFundTitle(String title);

    @Query("SELECT f FROM Fund f WHERE f.fundCategory = ?1 AND f.status = 'ACTIVE'")
    List<Fund> findAllByCategory(FundCategory category);

    @Query("SELECT f FROM Fund f WHERE f.creator = :account")
    List<Fund> findAllByCreator(Account account);

    @Query("SELECT f FROM Fund f WHERE f.status = 'ACTIVE'")
    List<Fund> findAllActiveFunds();

    @Query("SELECT f FROM Fund f WHERE f.creator = :account AND f.id = :id")
    Optional<Fund> findByCreatorAndId(Account account, Long id);

}
