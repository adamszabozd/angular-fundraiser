package hu.progmasters.fundraiser.service;

import hu.progmasters.fundraiser.domain.ExchangeRate;
import hu.progmasters.fundraiser.domain.Fund;
import hu.progmasters.fundraiser.domain.FundCategory;
import hu.progmasters.fundraiser.domain.Status;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

public class FundSpecifications {

    public static Specification<Fund> fundIsActive() {
        return new Specification<Fund>() {
            @Override
            public Predicate toPredicate(Root<Fund> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("status"), Status.ACTIVE);
            }
        };
    }

    public static Specification<Fund> fundBelongsToCategory(String category) {
        return new Specification<Fund>() {
            @Override
            public Predicate toPredicate(Root<Fund> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("fundCategory"), FundCategory.valueOf(category));
            }
        };
    }

}
