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

import com.mshz.domain.AuditEventTrigger;
import com.mshz.domain.*; // for static metamodels
import com.mshz.repository.AuditEventTriggerRepository;
import com.mshz.service.dto.AuditEventTriggerCriteria;

/**
 * Service for executing complex queries for {@link AuditEventTrigger} entities in the database.
 * The main input is a {@link AuditEventTriggerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AuditEventTrigger} or a {@link Page} of {@link AuditEventTrigger} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AuditEventTriggerQueryService extends QueryService<AuditEventTrigger> {

    private final Logger log = LoggerFactory.getLogger(AuditEventTriggerQueryService.class);

    private final AuditEventTriggerRepository auditEventTriggerRepository;

    public AuditEventTriggerQueryService(AuditEventTriggerRepository auditEventTriggerRepository) {
        this.auditEventTriggerRepository = auditEventTriggerRepository;
    }

    /**
     * Return a {@link List} of {@link AuditEventTrigger} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AuditEventTrigger> findByCriteria(AuditEventTriggerCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AuditEventTrigger> specification = createSpecification(criteria);
        return auditEventTriggerRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link AuditEventTrigger} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AuditEventTrigger> findByCriteria(AuditEventTriggerCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AuditEventTrigger> specification = createSpecification(criteria);
        return auditEventTriggerRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AuditEventTriggerCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AuditEventTrigger> specification = createSpecification(criteria);
        return auditEventTriggerRepository.count(specification);
    }

    /**
     * Function to convert {@link AuditEventTriggerCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AuditEventTrigger> createSpecification(AuditEventTriggerCriteria criteria) {
        Specification<AuditEventTrigger> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AuditEventTrigger_.id));
            }
            if (criteria.getEditorId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEditorId(), AuditEventTrigger_.editorId));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), AuditEventTrigger_.createdAt));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), AuditEventTrigger_.name));
            }
            if (criteria.getRecurrence() != null) {
                specification = specification.and(buildSpecification(criteria.getRecurrence(), AuditEventTrigger_.recurrence));
            }
            if (criteria.getDisabled() != null) {
                specification = specification.and(buildSpecification(criteria.getDisabled(), AuditEventTrigger_.disabled));
            }
            if (criteria.getEditorName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEditorName(), AuditEventTrigger_.editorName));
            }
            if (criteria.getFirstStartedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFirstStartedAt(), AuditEventTrigger_.firstStartedAt));
            }
            if (criteria.getNextStartAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNextStartAt(), AuditEventTrigger_.nextStartAt));
            }
            if (criteria.getStartCount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartCount(), AuditEventTrigger_.startCount));
            }
            if (criteria.getAuditId() != null) {
                specification = specification.and(buildSpecification(criteria.getAuditId(),
                    root -> root.join(AuditEventTrigger_.audit, JoinType.LEFT).get(Audit_.id)));
            }
        }
        return specification;
    }
}
