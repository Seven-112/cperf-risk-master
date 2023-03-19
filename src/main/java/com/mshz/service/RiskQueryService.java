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

import com.mshz.domain.Risk;
import com.mshz.domain.*; // for static metamodels
import com.mshz.repository.RiskRepository;
import com.mshz.service.dto.RiskCriteria;

/**
 * Service for executing complex queries for {@link Risk} entities in the database.
 * The main input is a {@link RiskCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Risk} or a {@link Page} of {@link Risk} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RiskQueryService extends QueryService<Risk> {

    private final Logger log = LoggerFactory.getLogger(RiskQueryService.class);

    private final RiskRepository riskRepository;

    public RiskQueryService(RiskRepository riskRepository) {
        this.riskRepository = riskRepository;
    }

    /**
     * Return a {@link List} of {@link Risk} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Risk> findByCriteria(RiskCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Risk> specification = createSpecification(criteria);
        return riskRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Risk} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Risk> findByCriteria(RiskCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Risk> specification = createSpecification(criteria);
        return riskRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RiskCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Risk> specification = createSpecification(criteria);
        return riskRepository.count(specification);
    }

    /**
     * Function to convert {@link RiskCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Risk> createSpecification(RiskCriteria criteria) {
        Specification<Risk> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Risk_.id));
            }
            if (criteria.getLabel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLabel(), Risk_.label));
            }
            if (criteria.getProbability() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getProbability(), Risk_.probability));
            }
            if (criteria.getGravity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGravity(), Risk_.gravity));
            }
            if (criteria.getCause() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCause(), Risk_.cause));
            }
            if (criteria.getControlsId() != null) {
                specification = specification.and(buildSpecification(criteria.getControlsId(),
                    root -> root.join(Risk_.controls, JoinType.LEFT).get(Control_.id)));
            }
            if (criteria.getTypeId() != null) {
                specification = specification.and(buildSpecification(criteria.getTypeId(),
                    root -> root.join(Risk_.type, JoinType.LEFT).get(RiskType_.id)));
            }
        }
        return specification;
    }
}
