package com.mshz.service;

import com.mshz.domain.AuditCycle;
import com.mshz.repository.AuditCycleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link AuditCycle}.
 */
@Service
@Transactional
public class AuditCycleService {

    private final Logger log = LoggerFactory.getLogger(AuditCycleService.class);

    private final AuditCycleRepository auditCycleRepository;

    public AuditCycleService(AuditCycleRepository auditCycleRepository) {
        this.auditCycleRepository = auditCycleRepository;
    }

    /**
     * Save a auditCycle.
     *
     * @param auditCycle the entity to save.
     * @return the persisted entity.
     */
    public AuditCycle save(AuditCycle auditCycle) {
        log.debug("Request to save AuditCycle : {}", auditCycle);
        return auditCycleRepository.save(auditCycle);
    }

    /**
     * Get all the auditCycles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AuditCycle> findAll(Pageable pageable) {
        log.debug("Request to get all AuditCycles");
        return auditCycleRepository.findAll(pageable);
    }


    /**
     * Get one auditCycle by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AuditCycle> findOne(Long id) {
        log.debug("Request to get AuditCycle : {}", id);
        return auditCycleRepository.findById(id);
    }

    /**
     * Delete the auditCycle by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AuditCycle : {}", id);
        auditCycleRepository.deleteById(id);
    }
}
