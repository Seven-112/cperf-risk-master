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

import com.mshz.domain.AuditRecommendationFile;
import com.mshz.domain.*; // for static metamodels
import com.mshz.repository.AuditRecommendationFileRepository;
import com.mshz.service.dto.AuditRecommendationFileCriteria;

/**
 * Service for executing complex queries for {@link AuditRecommendationFile} entities in the database.
 * The main input is a {@link AuditRecommendationFileCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AuditRecommendationFile} or a {@link Page} of {@link AuditRecommendationFile} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AuditRecommendationFileQueryService extends QueryService<AuditRecommendationFile> {

    private final Logger log = LoggerFactory.getLogger(AuditRecommendationFileQueryService.class);

    private final AuditRecommendationFileRepository auditRecommendationFileRepository;

    public AuditRecommendationFileQueryService(AuditRecommendationFileRepository auditRecommendationFileRepository) {
        this.auditRecommendationFileRepository = auditRecommendationFileRepository;
    }

    /**
     * Return a {@link List} of {@link AuditRecommendationFile} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AuditRecommendationFile> findByCriteria(AuditRecommendationFileCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AuditRecommendationFile> specification = createSpecification(criteria);
        return auditRecommendationFileRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link AuditRecommendationFile} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AuditRecommendationFile> findByCriteria(AuditRecommendationFileCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AuditRecommendationFile> specification = createSpecification(criteria);
        return auditRecommendationFileRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AuditRecommendationFileCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AuditRecommendationFile> specification = createSpecification(criteria);
        return auditRecommendationFileRepository.count(specification);
    }

    /**
     * Function to convert {@link AuditRecommendationFileCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AuditRecommendationFile> createSpecification(AuditRecommendationFileCriteria criteria) {
        Specification<AuditRecommendationFile> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AuditRecommendationFile_.id));
            }
            if (criteria.getRecommendationId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRecommendationId(), AuditRecommendationFile_.recommendationId));
            }
            if (criteria.getFileId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFileId(), AuditRecommendationFile_.fileId));
            }
            if (criteria.getFileName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFileName(), AuditRecommendationFile_.fileName));
            }
        }
        return specification;
    }
}
