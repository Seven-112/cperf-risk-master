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

import com.mshz.domain.Audit;
import com.mshz.domain.*; // for static metamodels
import com.mshz.repository.AuditRepository;
import com.mshz.service.dto.AuditCriteria;

/**
 * Service for executing complex queries for {@link Audit} entities in the database.
 * The main input is a {@link AuditCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Audit} or a {@link Page} of {@link Audit} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AuditQueryService extends QueryService<Audit> {

    private final Logger log = LoggerFactory.getLogger(AuditQueryService.class);

    private final AuditRepository auditRepository;

    public AuditQueryService(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    /**
     * Return a {@link List} of {@link Audit} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Audit> findByCriteria(AuditCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Audit> specification = createSpecification(criteria);
        return auditRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Audit} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Audit> findByCriteria(AuditCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Audit> specification = createSpecification(criteria);
        return auditRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AuditCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Audit> specification = createSpecification(criteria);
        return auditRepository.count(specification);
    }

    /**
     * Function to convert {@link AuditCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Audit> createSpecification(AuditCriteria criteria) {
        Specification<Audit> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Audit_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Audit_.title));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), Audit_.startDate));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), Audit_.endDate));
            }
            if (criteria.getExecutedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getExecutedAt(), Audit_.executedAt));
            }
            if (criteria.getProcessId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getProcessId(), Audit_.processId));
            }
            if (criteria.getProcessName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProcessName(), Audit_.processName));
            }
            if (criteria.getProcessCategoryId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getProcessCategoryId(), Audit_.processCategoryId));
            }
            if (criteria.getProcessCategoryName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProcessCategoryName(), Audit_.processCategoryName));
            }
            if (criteria.getRiskLevel() != null) {
                specification = specification.and(buildSpecification(criteria.getRiskLevel(), Audit_.riskLevel));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), Audit_.type));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Audit_.status));
            }
            if (criteria.getRiskId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRiskId(), Audit_.riskId));
            }
            if (criteria.getRiskName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRiskName(), Audit_.riskName));
            }
            if (criteria.getCycleId() != null) {
                specification = specification.and(buildSpecification(criteria.getCycleId(),
                    root -> root.join(Audit_.cycle, JoinType.LEFT).get(AuditCycle_.id)));
            }
        }
        return specification;
    }
}
