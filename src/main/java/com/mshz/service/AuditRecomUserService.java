package com.mshz.service;

import com.mshz.domain.AuditRecomUser;
import com.mshz.domain.AuditRecommendation;
import com.mshz.repository.AuditRecomUserRepository;
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
 * Service Implementation for managing {@link AuditRecomUser}.
 */
@Service
@Transactional
public class AuditRecomUserService {

    private final Logger log = LoggerFactory.getLogger(AuditRecomUserService.class);

    private final AuditRecomUserRepository auditRecomUserRepository;

    private final AuditNotifService auditNotifService;

    public AuditRecomUserService(AuditRecomUserRepository auditRecomUserRepository,
        AuditNotifService auditNotifService) {
        this.auditRecomUserRepository = auditRecomUserRepository;
        this.auditNotifService = auditNotifService;
    }

    /**
     * Save a auditRecomUser.
     *
     * @param auditRecomUser the entity to save.
     * @return the persisted entity.
     */
    public AuditRecomUser save(AuditRecomUser auditRecomUser) {
        log.debug("Request to save AuditRecomUser : {}", auditRecomUser);
        auditRecomUser = auditRecomUserRepository.save(auditRecomUser);
        auditNotifService.sendRecomNotifToRecomUser(auditRecomUser);
        return auditRecomUser;
    }

    /**
     * Get all the auditRecomUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AuditRecomUser> findAll(Pageable pageable) {
        log.debug("Request to get all AuditRecomUsers");
        return auditRecomUserRepository.findAll(pageable);
    }


    /**
     * Get one auditRecomUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AuditRecomUser> findOne(Long id) {
        log.debug("Request to get AuditRecomUser : {}", id);
        return auditRecomUserRepository.findById(id);
    }

    /**
     * Delete the auditRecomUser by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AuditRecomUser : {}", id);
        auditRecomUserRepository.deleteById(id);
    }

    @Async
    public void copyFromTo(AuditRecommendation model, AuditRecommendation recom) {
       try {
            if(model != null && model.getId() != null && recom.getId() != null && recom.getId() != null){
                List<AuditRecomUser> users = auditRecomUserRepository.findByRecomId(model.getId());
                if(!users.isEmpty()){
                    List<AuditRecomUser> copies = new ArrayList<>();
                    for (AuditRecomUser user : users) {
                        AuditRecomUser copy = new AuditRecomUser();
                        copy.setRecomId(recom.getId());
                        copy.setRole(user.getRole());
                        copy.setUserEmail(user.getUserEmail());
                        copy.setUserFullName(user.getUserFullName());
                        copy.setUserId(user.getUserId());
                        copies.add(copy);
                    }
                    copies = auditRecomUserRepository.saveAll(copies);
                    if(!copies.isEmpty()){
                        auditNotifService.sendRecommandationStatusChangeNote(recom);
                    }
                }
            }
       } catch (Exception e) {
           e.printStackTrace();
           log.error("error {}", e.getMessage());
       }
    }

}
