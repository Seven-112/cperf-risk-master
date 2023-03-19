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

import com.mshz.domain.RiskType;
import com.mshz.domain.*; // for static metamodels
import com.mshz.repository.RiskTypeRepository;
import com.mshz.service.dto.RiskTypeCriteria;

/**
 * Service for executing complex queries for {@link RiskType} entities in the database.
 * The main input is a {@link RiskTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RiskType} or a {@link Page} of {@link RiskType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RiskTypeQueryService extends QueryService<RiskType> {

    private final Logger log = LoggerFactory.getLogger(RiskTypeQueryService.class);

    private final RiskTypeRepository riskTypeRepository;

    public RiskTypeQueryService(RiskTypeRepository riskTypeRepository) {
        this.riskTypeRepository = riskTypeRepository;
    }

    /**
     * Return a {@link List} of {@link RiskType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RiskType> findByCriteria(RiskTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<RiskType> specification = createSpecification(criteria);
        return riskTypeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link RiskType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RiskType> findByCriteria(RiskTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<RiskType> specification = createSpecification(criteria);
        return riskTypeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RiskTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<RiskType> specification = createSpecification(criteria);
        return riskTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link RiskTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<RiskType> createSpecification(RiskTypeCriteria criteria) {
        Specification<RiskType> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), RiskType_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), RiskType_.name));
            }
        }
        return specification;
    }
}
