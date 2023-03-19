package com.mshz.service;

import com.mshz.domain.Audit;
import com.mshz.domain.AuditUser;
import com.mshz.repository.AuditUserRepository;
import com.mshz.webflux.AuditNotifService;

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
 * Service Implementation for managing {@link AuditUser}.
 */
@Service
@Transactional
public class AuditUserService {

    private final Logger log = LoggerFactory.getLogger(AuditUserService.class);

    private final AuditUserRepository auditUserRepository;
    
    private final AuditNotifService auditNotifService;

    public AuditUserService(AuditUserRepository auditUserRepository,AuditNotifService auditNotifService) {
        this.auditUserRepository = auditUserRepository;
        this.auditNotifService = auditNotifService;
    }

    /**
     * Save a auditUser.
     *
     * @param auditUser the entity to save.
     * @return the persisted entity.
     */
    public AuditUser save(AuditUser auditUser) {
        log.debug("Request to save AuditUser : {}", auditUser);
        boolean isNew = auditUser != null && auditUser.getId() == null;
        auditUser = auditUserRepository.save(auditUser);
        if(isNew)
            auditNotifService.sendAuditNotifToUser(auditUser);
        return auditUser;
    }

    /**
     * Get all the auditUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AuditUser> findAll(Pageable pageable) {
        log.debug("Request to get all AuditUsers");
        return auditUserRepository.findAll(pageable);
    }


    /**
     * Get one auditUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AuditUser> findOne(Long id) {
        log.debug("Request to get AuditUser : {}", id);
        return auditUserRepository.findById(id);
    }

    /**
     * Delete the auditUser by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AuditUser : {}", id);
        auditUserRepository.deleteById(id);
    }

    @Async
    public void copyFromTo(Audit model, Audit audit) {
        try {
            if(model != null && model.getId() != null && audit != null && audit.getId() != null){
                List<AuditUser> users = auditUserRepository.findByAuditId(model.getId());
                List<AuditUser> copies = new ArrayList<>();
                log.debug("users to copy {} audit id {}", users, audit.getId());
                for (AuditUser auditUser : users) {
                    AuditUser copy = new AuditUser();
                    copy.setAuditId(audit.getId());
                    copy.setRole(auditUser.getRole());
                    copy.setUserEmail(auditUser.getUserEmail());
                    copy.setUserFullName(auditUser.getUserFullName());
                    copy.setUserId(auditUser.getUserId());
                    copies.add(copy);
                }
    
                copies = auditUserRepository.saveAll(copies);
                
                if(!copies.isEmpty())
                    auditNotifService.sendAuditStatusChangeNote(audit);
    
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("error {}", e.getMessage());
        }
    }
}
