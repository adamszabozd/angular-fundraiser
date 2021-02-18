package hu.progmasters.fundraiser.repository;

import hu.progmasters.fundraiser.domain.Fund;
import hu.progmasters.fundraiser.domain.FundCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FundRepository extends JpaRepository<Fund, Long> {

    //TODO - Review: Itt egyáltalán nem garantált, hogy Fund-ot kapsz vissza, és akkor hibára fogsz futni,
    // érdemesebb egy Optional-ba csomagolni. Ezután rá leszünk kényszerítve, hogy kezeljük, amennyiben nincs ott érték
    // Optional<Fund> findByFundTitle(String title);
    Fund findByFundTitle(String title);

    @Query("SELECT f FROM Fund f WHERE f.fundCategory = ?1")
    List<Fund> findAllByCategory(FundCategory category);


}
