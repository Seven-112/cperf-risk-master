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

import com.mshz.domain.AuditCycle;
import com.mshz.domain.*; // for static metamodels
import com.mshz.repository.AuditCycleRepository;
import com.mshz.service.dto.AuditCycleCriteria;

/**
 * Service for executing complex queries for {@link AuditCycle} entities in the database.
 * The main input is a {@link AuditCycleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AuditCycle} or a {@link Page} of {@link AuditCycle} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AuditCycleQueryService extends QueryService<AuditCycle> {

    private final Logger log = LoggerFactory.getLogger(AuditCycleQueryService.class);

    private final AuditCycleRepository auditCycleRepository;

    public AuditCycleQueryService(AuditCycleRepository auditCycleRepository) {
        this.auditCycleRepository = auditCycleRepository;
    }

    /**
     * Return a {@link List} of {@link AuditCycle} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AuditCycle> findByCriteria(AuditCycleCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AuditCycle> specification = createSpecification(criteria);
        return auditCycleRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link AuditCycle} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AuditCycle> findByCriteria(AuditCycleCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AuditCycle> specification = createSpecification(criteria);
        return auditCycleRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AuditCycleCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AuditCycle> specification = createSpecification(criteria);
        return auditCycleRepository.count(specification);
    }

    /**
     * Function to convert {@link AuditCycleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AuditCycle> createSpecification(AuditCycleCriteria criteria) {
        Specification<AuditCycle> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AuditCycle_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), AuditCycle_.name));
            }
        }
        return specification;
    }
}
