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

import com.mshz.domain.AuditStatusTrakingFile;
import com.mshz.domain.*; // for static metamodels
import com.mshz.repository.AuditStatusTrakingFileRepository;
import com.mshz.service.dto.AuditStatusTrakingFileCriteria;

/**
 * Service for executing complex queries for {@link AuditStatusTrakingFile} entities in the database.
 * The main input is a {@link AuditStatusTrakingFileCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AuditStatusTrakingFile} or a {@link Page} of {@link AuditStatusTrakingFile} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AuditStatusTrakingFileQueryService extends QueryService<AuditStatusTrakingFile> {

    private final Logger log = LoggerFactory.getLogger(AuditStatusTrakingFileQueryService.class);

    private final AuditStatusTrakingFileRepository auditStatusTrakingFileRepository;

    public AuditStatusTrakingFileQueryService(AuditStatusTrakingFileRepository auditStatusTrakingFileRepository) {
        this.auditStatusTrakingFileRepository = auditStatusTrakingFileRepository;
    }

    /**
     * Return a {@link List} of {@link AuditStatusTrakingFile} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AuditStatusTrakingFile> findByCriteria(AuditStatusTrakingFileCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AuditStatusTrakingFile> specification = createSpecification(criteria);
        return auditStatusTrakingFileRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link AuditStatusTrakingFile} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AuditStatusTrakingFile> findByCriteria(AuditStatusTrakingFileCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AuditStatusTrakingFile> specification = createSpecification(criteria);
        return auditStatusTrakingFileRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AuditStatusTrakingFileCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AuditStatusTrakingFile> specification = createSpecification(criteria);
        return auditStatusTrakingFileRepository.count(specification);
    }

    /**
     * Function to convert {@link AuditStatusTrakingFileCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AuditStatusTrakingFile> createSpecification(AuditStatusTrakingFileCriteria criteria) {
        Specification<AuditStatusTrakingFile> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AuditStatusTrakingFile_.id));
            }
            if (criteria.getTrackId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTrackId(), AuditStatusTrakingFile_.trackId));
            }
            if (criteria.getFileId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFileId(), AuditStatusTrakingFile_.fileId));
            }
            if (criteria.getFileName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFileName(), AuditStatusTrakingFile_.fileName));
            }
        }
        return specification;
    }
}
