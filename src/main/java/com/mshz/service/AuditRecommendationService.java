package com.mshz.service;

import com.mshz.domain.Audit;
import com.mshz.domain.AuditRecommendation;
import com.mshz.domain.enumeration.AuditStatus;
import com.mshz.domain.enumeration.AuditUserRole;
import com.mshz.repository.AuditRecommendationRepository;
import com.mshz.webflux.AuditNotifService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link AuditRecommendation}.
 */
@Service
@Transactional
public class AuditRecommendationService {

    private final Logger log = LoggerFactory.getLogger(AuditRecommendationService.class);

    private final AuditRecommendationRepository auditRecommendationRepository;

    private final AuditNotifService notifService;

    private final AuditRecomUserService recomUserService;

    private final AuditRecommendationFileService fileService;

    public AuditRecommendationService(AuditRecommendationRepository auditRecommendationRepository,
        AuditNotifService notifService, AuditRecomUserService recomUserService,
         AuditRecommendationFileService fileService) {
        this.auditRecommendationRepository = auditRecommendationRepository;
        this.notifService = notifService;
        this.recomUserService = recomUserService;
        this.fileService = fileService;
    }

    /**
     * Save a auditRecommendation.
     *
     * @param auditRecommendation the entity to save.
     * @return the persisted entity.
     */
    public AuditRecommendation save(AuditRecommendation auditRecommendation) {
        log.debug("Request to save AuditRecommendation : {}", auditRecommendation);
        if(auditRecommendation != null && auditRecommendation.getId() == null){
            auditRecommendation.setEditAt(Instant.now());
            auditRecommendation.setStatus(AuditStatus.STARTED);
        }
        if(auditRecommendation.getStatus() == AuditStatus.COMPLETED && auditRecommendation.getExecutedAt() == null)
            auditRecommendation.setExecutedAt(Instant.now());
        auditRecommendation = auditRecommendationRepository.save(auditRecommendation);
        notifService.sendRecommandationStatusChangeNote(auditRecommendation);
        return auditRecommendation;
    }

    /**
     * Get all the auditRecommendations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AuditRecommendation> findAll(Pageable pageable) {
        log.debug("Request to get all AuditRecommendations");
        Page<AuditRecommendation> page = auditRecommendationRepository.findAll(pageable);
        Page<AuditRecommendation> pageImpl = new PageImpl<>(
            sortRecoms(page.getContent()),
            page.getPageable(),
            page.getTotalElements()
        );
        return pageImpl;
    }


    /**
     * Get one auditRecommendation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AuditRecommendation> findOne(Long id) {
        log.debug("Request to get AuditRecommendation : {}", id);
        return auditRecommendationRepository.findById(id);
    }

    /**
     * Delete the auditRecommendation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AuditRecommendation : {}", id);
        auditRecommendationRepository.deleteById(id);
    }

    public Page<AuditRecommendation> findByUserIdAndRoleAndStatus(Long userId, AuditUserRole role, AuditStatus status,
            Pageable pageable) {
        Page<AuditRecommendation> page = auditRecommendationRepository
                .findByUserIdAndRoleAndStatus(userId, role, status, pageable);
        Page<AuditRecommendation> pageImpl = new PageImpl<>(
            sortRecoms(page.getContent()),
            page.getPageable(),
            page.getTotalElements()
        );
        return pageImpl;
    }

    public Page<AuditRecommendation> findByUserIdAndStatus(Long userId, AuditStatus status, Pageable pageable) {
        Page<AuditRecommendation> page = auditRecommendationRepository.findByUserIdAndStatus(userId,status, pageable);
        Page<AuditRecommendation> pageImpl = new PageImpl<>(
            sortRecoms(page.getContent()),
            page.getPageable(),
            page.getTotalElements()
        );
        return pageImpl;
    }

    public Page<AuditRecommendation> findByUserId(Long userId, Pageable pageable) {
        Page<AuditRecommendation> page = auditRecommendationRepository.findByUserId(userId,pageable);
        Page<AuditRecommendation> pageImpl = new PageImpl<>(
            sortRecoms(page.getContent()),
            page.getPageable(),
            page.getTotalElements()
        );
        return pageImpl;
    }

    /** 
     * ===================================
     * ======= BEGIN SORTNG UTILS METHODS =====
     * ===================================
     */

    public List<AuditRecommendation> sortRecomsyIds(List<Long> ids){
        List<AuditRecommendation> recoms = new ArrayList<>();
        if(ids != null && !ids.isEmpty()){
            recoms = auditRecommendationRepository.findByIdIn(ids);
            return sortRecoms(recoms);
        }
        return recoms;
    }

    public List<AuditRecommendation> sortRecoms(List<AuditRecommendation> recoms){
        if(recoms != null && recoms.size() >=2){

            /**
             * a will placed before b if sorted comparator is negative
             * b will placed before a if sorted comparator is positive
             * no sorting if sorted comparator is zero
             */

            return recoms.stream()
                // sorting by ids
                .sorted((a,b) -> sortById(a,b))
                // sorting by started tasks
                .sorted((a,b) -> sortByStatus(a,b))
                // sorting by chrono
                .sorted((a,b) -> sortByChrono(a,b))
                .collect(Collectors.toList());
        }

        return recoms;
    }

    private int sortById(AuditRecommendation aa, AuditRecommendation ab){
        if(aa != null && ab != null){
            try {
                if(aa.getId() == null && ab.getId() != null)
                    return 1; // tb avant ta
                if(aa.getId() != null && ab.getId() == null)
                    return -1; // ta avant tb
                if(aa.getId() != null && ab.getId() != null)
                    return ab.getId().intValue() - aa.getId().intValue();
            } catch (Exception e) {
                //TODO: handle exception
            }
        }
        return 0;
    }

    private int sortByStatus(AuditRecommendation aa, AuditRecommendation ab){
        // if task is started will render first
        if(aa != null && ab != null){
            try {
                List<AuditStatus> startedStatus = Arrays
                    .asList(AuditStatus.EXECUTED, AuditStatus.STARTED, AuditStatus.SUBMITTED);
                
                boolean taIsStarted = aa.getStatus() != null && startedStatus.contains(aa.getStatus());
                boolean tbIsStarted = ab.getStatus() != null && startedStatus.contains(ab.getStatus());

                if(!taIsStarted && tbIsStarted)
                    return 1; // tb avant ta
                if(taIsStarted && !tbIsStarted)
                    return -1; // ta avant tb
            } catch (Exception e) {
                //TODO: handle exception
            }
        }
        return 0;
    }

    private int sortByChrono(AuditRecommendation aa, AuditRecommendation ab){
        if(aa != null && ab != null){
            try {
                boolean aaHasValidChrono = aa.getEditAt() != null && (aa.getDateLimit() != null || aa.getExecutedAt() != null);
                boolean abHasValidChrono = ab.getEditAt() != null && (ab.getDateLimit() != null || ab.getExecutedAt() != null);

                if(!aaHasValidChrono && !abHasValidChrono)
                    return 0;

                if(!aaHasValidChrono && abHasValidChrono)
                    return 1; // tb avant ta

                if(aaHasValidChrono && !abHasValidChrono)
                    return -1; // ta avant tb
                    
                if(aa.getStatus() != AuditStatus.CANCELED && ab.getStatus() == AuditStatus.CANCELED)
                    return -1; // tb avant ta

                if(aa.getStatus() == AuditStatus.CANCELED && ab.getStatus() != AuditStatus.CANCELED)
                    return 1; // ta avant tb

                if(aa.getExecutedAt() == null && ab.getExecutedAt() != null)
                    return -1; // tb avant ta

                if(aa.getExecutedAt() != null && ab.getExecutedAt() == null)
                    return 1; // ta avant tb

                // sorting by excecced chrono
                if(isChronoExceced(aa) && !isChronoExceced(ab))
                    return -1;
                if(!isChronoExceced(aa) && isChronoExceced(ab))
                    return 1;
                if(isChronoExceced(aa) && isChronoExceced(ab)){
                    Instant now = Instant.now();
                    Duration da = Duration.between(now, aa.getDateLimit());
                    Duration db = Duration.between(now, ab.getDateLimit());
                    return da.compareTo(db); 
                }
                // end  sorting by excecced chrono

                if(aa.getDateLimit() != null && ab.getDateLimit() != null)
                    return aa.getDateLimit().compareTo(ab.getDateLimit());
            } catch (Exception e) {
                //TODO: handle exception
            }
        }
        return 0;
    }

    private boolean isChronoExceced(AuditRecommendation recom){
        if(recom != null && recom.getEditAt() != null && recom.getDateLimit() != null){
            Instant instant = recom.getExecutedAt() != null ? recom.getExecutedAt() : Instant.now();
            return instant.compareTo(recom.getDateLimit()) > 0;
        }
        return false;
    }

    /** 
     * ==================================
     * ==== END SOTRTING UTILS METHODS ===
     * ==================================
     */

    @Async
    public void copyFromTo(Audit model, Audit audit) {
        try {
            if(model != null && model.getId() != null && audit != null && audit.getId() != null){
                List<AuditRecommendation> recoms = auditRecommendationRepository
                                .findByAuditIdAndStatusNot(model.getId(), AuditStatus.CANCELED);
                if(!recoms.isEmpty()){
                    Instant now = Instant.now();
                    for (AuditRecommendation recom : recoms) {
                        AuditRecommendation copy = new AuditRecommendation();
                        copy.setAuditId(audit.getId());
                        copy.setAuditorEmail(recom.getAuditorEmail());
                        copy.setAuditorId(recom.getAuditorId());
                        copy.setAuditorName(recom.getAuditorName());
                        copy.setContent(recom.getContent());
                        copy.setEditAt(now);
                        if(recom.getDateLimit() != null && recom.getEditAt() != null){
                            Duration d = Duration.between(recom.getEditAt(), recom.getDateLimit());
                            copy.setDateLimit(now.plusMillis(d.toMillis()));
                        }else{
                            copy.setDateLimit(now.atZone(ZoneId.systemDefault()).plusDays(1).toInstant());
                        }
                        copy.setEntityId(recom.getEntityId());
                        copy.setEntiyName(recom.getEntiyName());
                        copy.setResponsableEmail(recom.getResponsableEmail());
                        copy.setResponsableId(recom.getResponsableId());
                        copy.setResponsableName(recom.getResponsableName());
                        copy.setStatus(AuditStatus.STARTED);
                        copy = auditRecommendationRepository.save(copy);
                        if(copy != null && copy.getId() != null){
                            recomUserService.copyFromTo(recom, copy);
                            fileService.copyFromTo(recom, copy);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("error {}", e.getMessage());
        }
    }
}
