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

import com.mshz.domain.AuditUser;
import com.mshz.domain.*; // for static metamodels
import com.mshz.repository.AuditUserRepository;
import com.mshz.service.dto.AuditUserCriteria;

/**
 * Service for executing complex queries for {@link AuditUser} entities in the database.
 * The main input is a {@link AuditUserCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AuditUser} or a {@link Page} of {@link AuditUser} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AuditUserQueryService extends QueryService<AuditUser> {

    private final Logger log = LoggerFactory.getLogger(AuditUserQueryService.class);

    private final AuditUserRepository auditUserRepository;

    public AuditUserQueryService(AuditUserRepository auditUserRepository) {
        this.auditUserRepository = auditUserRepository;
    }

    /**
     * Return a {@link List} of {@link AuditUser} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AuditUser> findByCriteria(AuditUserCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AuditUser> specification = createSpecification(criteria);
        return auditUserRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link AuditUser} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AuditUser> findByCriteria(AuditUserCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AuditUser> specification = createSpecification(criteria);
        return auditUserRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AuditUserCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AuditUser> specification = createSpecification(criteria);
        return auditUserRepository.count(specification);
    }

    /**
     * Function to convert {@link AuditUserCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AuditUser> createSpecification(AuditUserCriteria criteria) {
        Specification<AuditUser> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AuditUser_.id));
            }
            if (criteria.getAuditId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAuditId(), AuditUser_.auditId));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUserId(), AuditUser_.userId));
            }
            if (criteria.getUserFullName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUserFullName(), AuditUser_.userFullName));
            }
            if (criteria.getUserEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUserEmail(), AuditUser_.userEmail));
            }
            if (criteria.getRole() != null) {
                specification = specification.and(buildSpecification(criteria.getRole(), AuditUser_.role));
            }
        }
        return specification;
    }
}
