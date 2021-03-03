package hu.progmasters.fundraiser.repository;

import hu.progmasters.fundraiser.domain.Account;
import hu.progmasters.fundraiser.domain.Fund;
import hu.progmasters.fundraiser.domain.FundCategory;
import org.hibernate.annotations.NamedNativeQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FundRepository extends JpaRepository<Fund, Long>, JpaSpecificationExecutor<Fund> {

    Optional<Fund> findByFundTitle(String title);

    @Query("SELECT COUNT (f) FROM Fund f WHERE f.status = 'ACTIVE' ")
    Long countAllByStatus();

    @Query("SELECT  COUNT (f) FROM Fund f WHERE f.fundCategory = ?1 AND f.status = 'ACTIVE'")
    Long countAllByStatusByCategory(FundCategory category);


//    @Query("SELECT f FROM Fund f WHERE f.fundCategory = ?1 AND f.status = 'ACTIVE'")
//    List<Fund> findAllByCategory(FundCategory category);

    @Query("SELECT f FROM Fund f WHERE f.creator = :account")
    List<Fund> findAllByCreator(Account account);

    @Query("SELECT f FROM Fund f WHERE f.status = 'ACTIVE'")
    List<Fund> findAllActiveFunds();

    @Query("SELECT f FROM Fund f WHERE f.creator = :account AND f.id = :id")
    Optional<Fund> findByCreatorAndId(Account account, Long id);

    @Query("SELECT f FROM Fund f LEFT JOIN ExchangeRate e ON f.currency = e.currency WHERE e.currentTime = (SELECT MAX(e.currentTime) FROM ExchangeRate e) AND f.status = 'ACTIVE' AND f.fundCategory = :category ORDER BY f.raisedAmount / e.rate DESC")
    Page<Fund> findByCategoryOrderByRaised(Pageable pageable, FundCategory category);

    @Query("SELECT f FROM Fund f LEFT JOIN ExchangeRate e ON f.currency = e.currency WHERE e.currentTime = (SELECT MAX(e.currentTime) FROM ExchangeRate e) AND f.status = 'ACTIVE' ORDER BY f.raisedAmount / e.rate DESC")
    Page<Fund> findAllOrderByRaised(Pageable pageable);
}
