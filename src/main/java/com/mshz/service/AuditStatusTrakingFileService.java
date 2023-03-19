package com.mshz.service;

import com.mshz.domain.AuditStatusTrakingFile;
import com.mshz.repository.AuditStatusTrakingFileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link AuditStatusTrakingFile}.
 */
@Service
@Transactional
public class AuditStatusTrakingFileService {

    private final Logger log = LoggerFactory.getLogger(AuditStatusTrakingFileService.class);

    private final AuditStatusTrakingFileRepository auditStatusTrakingFileRepository;

    public AuditStatusTrakingFileService(AuditStatusTrakingFileRepository auditStatusTrakingFileRepository) {
        this.auditStatusTrakingFileRepository = auditStatusTrakingFileRepository;
    }

    /**
     * Save a auditStatusTrakingFile.
     *
     * @param auditStatusTrakingFile the entity to save.
     * @return the persisted entity.
     */
    public AuditStatusTrakingFile save(AuditStatusTrakingFile auditStatusTrakingFile) {
        log.debug("Request to save AuditStatusTrakingFile : {}", auditStatusTrakingFile);
        return auditStatusTrakingFileRepository.save(auditStatusTrakingFile);
    }

    /**
     * Get all the auditStatusTrakingFiles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AuditStatusTrakingFile> findAll(Pageable pageable) {
        log.debug("Request to get all AuditStatusTrakingFiles");
        return auditStatusTrakingFileRepository.findAll(pageable);
    }


    /**
     * Get one auditStatusTrakingFile by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AuditStatusTrakingFile> findOne(Long id) {
        log.debug("Request to get AuditStatusTrakingFile : {}", id);
        return auditStatusTrakingFileRepository.findById(id);
    }

    /**
     * Delete the auditStatusTrakingFile by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AuditStatusTrakingFile : {}", id);
        auditStatusTrakingFileRepository.deleteById(id);
    }
}
