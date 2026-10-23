package com.ccsw.tutorial.prestamo;

import com.ccsw.tutorial.prestamo.model.Prestamo;
import org.springframework.data.jpa.domain.Specification;
import com.ccsw.tutorial.common.criteria.SearchCriteria;
import jakarta.persistence.criteria.*;

import java.time.LocalDate;

public class PrestamoSpecification implements Specification<Prestamo> {
    private static final long serialVersionUID = 1L;

    private final SearchCriteria criteria;

    public PrestamoSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    private Path<?> getPath(Root<Prestamo> root){
        String key = criteria.getKey();
        String[] split = key.split("[.]", 0);

        Path<?> expression = root.get(split[0]);
        for (int i = 1; i < split.length; i++) {
            expression = expression.get(split[i]);
        }

        return expression;
    }

    @Override
    public Predicate toPredicate(Root<Prestamo> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (criteria.getOperation().equalsIgnoreCase(":") && criteria.getValue() != null) {
            Path<?> path = getPath(root);
            if (path.getJavaType() == String.class) {
                return builder.like(path.as(String.class), "%" + criteria.getValue() + "%");
            } else {
                return builder.equal(path, criteria.getValue());
            }
        }else if (criteria.getOperation().equalsIgnoreCase(">=")&& criteria.getValue() != null) {
            return builder.greaterThanOrEqualTo(getPath(root).as(LocalDate.class),(LocalDate) criteria.getValue());
        }else if (criteria.getOperation().equalsIgnoreCase("<=")&& criteria.getValue() != null) {
            return builder.lessThanOrEqualTo(getPath(root).as(LocalDate.class),(LocalDate) criteria.getValue());
        }
        return null;
    }
}
