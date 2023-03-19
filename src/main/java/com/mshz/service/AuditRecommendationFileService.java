package com.mshz.service;

import com.mshz.domain.AuditRecommendation;
import com.mshz.domain.AuditRecommendationFile;
import com.mshz.repository.AuditRecommendationFileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link AuditRecommendationFile}.
 */
@Service
@Transactional
public class AuditRecommendationFileService {

    private final Logger log = LoggerFactory.getLogger(AuditRecommendationFileService.class);

    private final AuditRecommendationFileRepository auditRecommendationFileRepository;

    public AuditRecommendationFileService(AuditRecommendationFileRepository auditRecommendationFileRepository) {
        this.auditRecommendationFileRepository = auditRecommendationFileRepository;
    }

    /**
     * Save a auditRecommendationFile.
     *
     * @param auditRecommendationFile the entity to save.
     * @return the persisted entity.
     */
    public AuditRecommendationFile save(AuditRecommendationFile auditRecommendationFile) {
        log.debug("Request to save AuditRecommendationFile : {}", auditRecommendationFile);
        return auditRecommendationFileRepository.save(auditRecommendationFile);
    }

    /**
     * Get all the auditRecommendationFiles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AuditRecommendationFile> findAll(Pageable pageable) {
        log.debug("Request to get all AuditRecommendationFiles");
        return auditRecommendationFileRepository.findAll(pageable);
    }


    /**
     * Get one auditRecommendationFile by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AuditRecommendationFile> findOne(Long id) {
        log.debug("Request to get AuditRecommendationFile : {}", id);
        return auditRecommendationFileRepository.findById(id);
    }

    /**
     * Delete the auditRecommendationFile by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AuditRecommendationFile : {}", id);
        auditRecommendationFileRepository.deleteById(id);
    }

    @Async
    public void copyFromTo(AuditRecommendation model, AuditRecommendation recom) {
        try {
            if(model != null && model.getId() != null && recom.getId() != null && recom.getId() != null){
                List<AuditRecommendationFile> files = auditRecommendationFileRepository.findByRecommendationId(model.getId());
                if(!files.isEmpty()){
                    List<AuditRecommendationFile> copies = new ArrayList<>();
                    for (AuditRecommendationFile file : files) {
                        AuditRecommendationFile copy = new AuditRecommendationFile();
                        copy.setFileId(file.getFileId());
                        copy.setFileName(file.getFileName());
                        copy.setRecommendationId(recom.getId());
                        copies.add(copy);
                    }

                    auditRecommendationFileRepository.saveAll(copies);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("error {}", e.getMessage());
        }
    }
}
