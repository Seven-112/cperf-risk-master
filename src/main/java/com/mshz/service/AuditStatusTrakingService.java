package com.mshz.service;

import com.mshz.domain.AuditStatusTraking;
import com.mshz.repository.AuditStatusTrakingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link AuditStatusTraking}.
 */
@Service
@Transactional
public class AuditStatusTrakingService {

    private final Logger log = LoggerFactory.getLogger(AuditStatusTrakingService.class);

    private final AuditStatusTrakingRepository auditStatusTrakingRepository;

    public AuditStatusTrakingService(AuditStatusTrakingRepository auditStatusTrakingRepository) {
        this.auditStatusTrakingRepository = auditStatusTrakingRepository;
    }

    /**
     * Save a auditStatusTraking.
     *
     * @param auditStatusTraking the entity to save.
     * @return the persisted entity.
     */
    public AuditStatusTraking save(AuditStatusTraking auditStatusTraking) {
        log.debug("Request to save AuditStatusTraking : {}", auditStatusTraking);
        return auditStatusTrakingRepository.save(auditStatusTraking);
    }

    /**
     * Get all the auditStatusTrakings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AuditStatusTraking> findAll(Pageable pageable) {
        log.debug("Request to get all AuditStatusTrakings");
        return auditStatusTrakingRepository.findAll(pageable);
    }


    /**
     * Get one auditStatusTraking by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AuditStatusTraking> findOne(Long id) {
        log.debug("Request to get AuditStatusTraking : {}", id);
        return auditStatusTrakingRepository.findById(id);
    }

    /**
     * Delete the auditStatusTraking by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AuditStatusTraking : {}", id);
        auditStatusTrakingRepository.deleteById(id);
    }
}
