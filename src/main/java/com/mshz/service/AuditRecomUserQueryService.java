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

import com.mshz.domain.AuditRecomUser;
import com.mshz.domain.*; // for static metamodels
import com.mshz.repository.AuditRecomUserRepository;
import com.mshz.service.dto.AuditRecomUserCriteria;

/**
 * Service for executing complex queries for {@link AuditRecomUser} entities in the database.
 * The main input is a {@link AuditRecomUserCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AuditRecomUser} or a {@link Page} of {@link AuditRecomUser} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AuditRecomUserQueryService extends QueryService<AuditRecomUser> {

    private final Logger log = LoggerFactory.getLogger(AuditRecomUserQueryService.class);

    private final AuditRecomUserRepository auditRecomUserRepository;

    public AuditRecomUserQueryService(AuditRecomUserRepository auditRecomUserRepository) {
        this.auditRecomUserRepository = auditRecomUserRepository;
    }

    /**
     * Return a {@link List} of {@link AuditRecomUser} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AuditRecomUser> findByCriteria(AuditRecomUserCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AuditRecomUser> specification = createSpecification(criteria);
        return auditRecomUserRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link AuditRecomUser} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AuditRecomUser> findByCriteria(AuditRecomUserCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AuditRecomUser> specification = createSpecification(criteria);
        return auditRecomUserRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AuditRecomUserCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AuditRecomUser> specification = createSpecification(criteria);
        return auditRecomUserRepository.count(specification);
    }

    /**
     * Function to convert {@link AuditRecomUserCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AuditRecomUser> createSpecification(AuditRecomUserCriteria criteria) {
        Specification<AuditRecomUser> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AuditRecomUser_.id));
            }
            if (criteria.getRecomId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRecomId(), AuditRecomUser_.recomId));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUserId(), AuditRecomUser_.userId));
            }
            if (criteria.getUserFullName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUserFullName(), AuditRecomUser_.userFullName));
            }
            if (criteria.getUserEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUserEmail(), AuditRecomUser_.userEmail));
            }
            if (criteria.getRole() != null) {
                specification = specification.and(buildSpecification(criteria.getRole(), AuditRecomUser_.role));
            }
        }
        return specification;
    }
}
