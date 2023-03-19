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

import com.mshz.domain.AuditRecommendation;
import com.mshz.domain.*; // for static metamodels
import com.mshz.repository.AuditRecommendationRepository;
import com.mshz.service.dto.AuditRecommendationCriteria;

/**
 * Service for executing complex queries for {@link AuditRecommendation} entities in the database.
 * The main input is a {@link AuditRecommendationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AuditRecommendation} or a {@link Page} of {@link AuditRecommendation} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AuditRecommendationQueryService extends QueryService<AuditRecommendation> {

    private final Logger log = LoggerFactory.getLogger(AuditRecommendationQueryService.class);

    private final AuditRecommendationRepository auditRecommendationRepository;

    public AuditRecommendationQueryService(AuditRecommendationRepository auditRecommendationRepository) {
        this.auditRecommendationRepository = auditRecommendationRepository;
    }

    /**
     * Return a {@link List} of {@link AuditRecommendation} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AuditRecommendation> findByCriteria(AuditRecommendationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AuditRecommendation> specification = createSpecification(criteria);
        return auditRecommendationRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link AuditRecommendation} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AuditRecommendation> findByCriteria(AuditRecommendationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AuditRecommendation> specification = createSpecification(criteria);
        return auditRecommendationRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AuditRecommendationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AuditRecommendation> specification = createSpecification(criteria);
        return auditRecommendationRepository.count(specification);
    }

    /**
     * Function to convert {@link AuditRecommendationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AuditRecommendation> createSpecification(AuditRecommendationCriteria criteria) {
        Specification<AuditRecommendation> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AuditRecommendation_.id));
            }
            if (criteria.getAuditorId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAuditorId(), AuditRecommendation_.auditorId));
            }
            if (criteria.getAuditorName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAuditorName(), AuditRecommendation_.auditorName));
            }
            if (criteria.getAuditorEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAuditorEmail(), AuditRecommendation_.auditorEmail));
            }
            if (criteria.getAuditId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAuditId(), AuditRecommendation_.auditId));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), AuditRecommendation_.status));
            }
            if (criteria.getResponsableId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getResponsableId(), AuditRecommendation_.responsableId));
            }
            if (criteria.getResponsableName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getResponsableName(), AuditRecommendation_.responsableName));
            }
            if (criteria.getResponsableEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getResponsableEmail(), AuditRecommendation_.responsableEmail));
            }
            if (criteria.getDateLimit() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateLimit(), AuditRecommendation_.dateLimit));
            }
            if (criteria.getEditAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEditAt(), AuditRecommendation_.editAt));
            }
            if (criteria.getExecutedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getExecutedAt(), AuditRecommendation_.executedAt));
            }
            if (criteria.getEntityId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEntityId(), AuditRecommendation_.entityId));
            }
            if (criteria.getEntiyName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEntiyName(), AuditRecommendation_.entiyName));
            }
        }
        return specification;
    }
}
