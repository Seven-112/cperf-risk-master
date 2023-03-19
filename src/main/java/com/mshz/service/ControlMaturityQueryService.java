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

import com.mshz.domain.ControlMaturity;
import com.mshz.domain.*; // for static metamodels
import com.mshz.repository.ControlMaturityRepository;
import com.mshz.service.dto.ControlMaturityCriteria;

/**
 * Service for executing complex queries for {@link ControlMaturity} entities in the database.
 * The main input is a {@link ControlMaturityCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ControlMaturity} or a {@link Page} of {@link ControlMaturity} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ControlMaturityQueryService extends QueryService<ControlMaturity> {

    private final Logger log = LoggerFactory.getLogger(ControlMaturityQueryService.class);

    private final ControlMaturityRepository controlMaturityRepository;

    public ControlMaturityQueryService(ControlMaturityRepository controlMaturityRepository) {
        this.controlMaturityRepository = controlMaturityRepository;
    }

    /**
     * Return a {@link List} of {@link ControlMaturity} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ControlMaturity> findByCriteria(ControlMaturityCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ControlMaturity> specification = createSpecification(criteria);
        return controlMaturityRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ControlMaturity} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ControlMaturity> findByCriteria(ControlMaturityCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ControlMaturity> specification = createSpecification(criteria);
        return controlMaturityRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ControlMaturityCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ControlMaturity> specification = createSpecification(criteria);
        return controlMaturityRepository.count(specification);
    }

    /**
     * Function to convert {@link ControlMaturityCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ControlMaturity> createSpecification(ControlMaturityCriteria criteria) {
        Specification<ControlMaturity> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ControlMaturity_.id));
            }
            if (criteria.getLabel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLabel(), ControlMaturity_.label));
            }
        }
        return specification;
    }
}
