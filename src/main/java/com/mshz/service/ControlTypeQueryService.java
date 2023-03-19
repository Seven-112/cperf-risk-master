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

import com.mshz.domain.ControlType;
import com.mshz.domain.*; // for static metamodels
import com.mshz.repository.ControlTypeRepository;
import com.mshz.service.dto.ControlTypeCriteria;

/**
 * Service for executing complex queries for {@link ControlType} entities in the database.
 * The main input is a {@link ControlTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ControlType} or a {@link Page} of {@link ControlType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ControlTypeQueryService extends QueryService<ControlType> {

    private final Logger log = LoggerFactory.getLogger(ControlTypeQueryService.class);

    private final ControlTypeRepository controlTypeRepository;

    public ControlTypeQueryService(ControlTypeRepository controlTypeRepository) {
        this.controlTypeRepository = controlTypeRepository;
    }

    /**
     * Return a {@link List} of {@link ControlType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ControlType> findByCriteria(ControlTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ControlType> specification = createSpecification(criteria);
        return controlTypeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ControlType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ControlType> findByCriteria(ControlTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ControlType> specification = createSpecification(criteria);
        return controlTypeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ControlTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ControlType> specification = createSpecification(criteria);
        return controlTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link ControlTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ControlType> createSpecification(ControlTypeCriteria criteria) {
        Specification<ControlType> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ControlType_.id));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), ControlType_.type));
            }
        }
        return specification;
    }
}
