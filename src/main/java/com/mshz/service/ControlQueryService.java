package com.mshz.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.mshz.domain.Control;
import com.mshz.domain.*; // for static metamodels
import com.mshz.repository.ControlRepository;
import com.mshz.service.dto.ControlCriteria;

/**
 * Service for executing complex queries for {@link Control} entities in the database.
 * The main input is a {@link ControlCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Control} or a {@link Page} of {@link Control} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ControlQueryService extends QueryService<Control> {

    private final Logger log = LoggerFactory.getLogger(ControlQueryService.class);

    private final ControlRepository controlRepository;

    public ControlQueryService(ControlRepository controlRepository) {
        this.controlRepository = controlRepository;
    }

    /**
     * Return a {@link List} of {@link Control} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Control> findByCriteria(ControlCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Control> specification = createSpecification(criteria);
        return controlRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Control} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Control> findByCriteria(ControlCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Control> specification = createSpecification(criteria);
        return controlRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ControlCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Control> specification = createSpecification(criteria);
        return controlRepository.count(specification);
    }

    /**
     * Function to convert {@link ControlCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Control> createSpecification(ControlCriteria criteria) {
        Specification<Control> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Control_.id));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Control_.description));
            }
            if (criteria.getValidationRequired() != null) {
                specification = specification.and(buildSpecification(criteria.getValidationRequired(), Control_.validationRequired));
            }
            if (criteria.getTypeId() != null) {
                specification = specification.and(buildSpecification(criteria.getTypeId(),
                    root -> root.join(Control_.type, JoinType.LEFT).get(ControlType_.id)));
            }
            if (criteria.getMaturityId() != null) {
                specification = specification.and(buildSpecification(criteria.getMaturityId(),
                    root -> root.join(Control_.maturity, JoinType.LEFT).get(ControlMaturity_.id)));
            }
            if (criteria.getRiskId() != null) {
                specification = specification.and(buildSpecification(criteria.getRiskId(),
                    root -> root.join(Control_.risk, JoinType.LEFT).get(Risk_.id)));
            }
        }
        return specification;
    }
}
