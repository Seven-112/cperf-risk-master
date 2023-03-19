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

import com.mshz.domain.AuditStatusTraking;
import com.mshz.domain.*; // for static metamodels
import com.mshz.repository.AuditStatusTrakingRepository;
import com.mshz.service.dto.AuditStatusTrakingCriteria;

/**
 * Service for executing complex queries for {@link AuditStatusTraking} entities in the database.
 * The main input is a {@link AuditStatusTrakingCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AuditStatusTraking} or a {@link Page} of {@link AuditStatusTraking} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AuditStatusTrakingQueryService extends QueryService<AuditStatusTraking> {

    private final Logger log = LoggerFactory.getLogger(AuditStatusTrakingQueryService.class);

    private final AuditStatusTrakingRepository auditStatusTrakingRepository;

    public AuditStatusTrakingQueryService(AuditStatusTrakingRepository auditStatusTrakingRepository) {
        this.auditStatusTrakingRepository = auditStatusTrakingRepository;
    }

    /**
     * Return a {@link List} of {@link AuditStatusTraking} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AuditStatusTraking> findByCriteria(AuditStatusTrakingCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AuditStatusTraking> specification = createSpecification(criteria);
        return auditStatusTrakingRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link AuditStatusTraking} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AuditStatusTraking> findByCriteria(AuditStatusTrakingCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AuditStatusTraking> specification = createSpecification(criteria);
        return auditStatusTrakingRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AuditStatusTrakingCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AuditStatusTraking> specification = createSpecification(criteria);
        return auditStatusTrakingRepository.count(specification);
    }

    /**
     * Function to convert {@link AuditStatusTrakingCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AuditStatusTraking> createSpecification(AuditStatusTrakingCriteria criteria) {
        Specification<AuditStatusTraking> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AuditStatusTraking_.id));
            }
            if (criteria.getAuditId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAuditId(), AuditStatusTraking_.auditId));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), AuditStatusTraking_.status));
            }
            if (criteria.getTracingAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTracingAt(), AuditStatusTraking_.tracingAt));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUserId(), AuditStatusTraking_.userId));
            }
            if (criteria.getEditable() != null) {
                specification = specification.and(buildSpecification(criteria.getEditable(), AuditStatusTraking_.editable));
            }
            if (criteria.getRecom() != null) {
                specification = specification.and(buildSpecification(criteria.getRecom(), AuditStatusTraking_.recom));
            }
        }
        return specification;
    }
}
