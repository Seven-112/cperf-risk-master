package com.mshz.service;

import com.mshz.domain.Audit;
import com.mshz.repository.AuditRepository;
import com.mshz.webflux.AuditNotifService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

import com.mshz.domain.enumeration.AuditStatus;
import com.mshz.domain.enumeration.AuditUserRole;

/**
 * Service Implementation for managing {@link Audit}.
 */
@Service
@Transactional
public class AuditService {

    private final Logger log = LoggerFactory.getLogger(AuditService.class);

    private final AuditRepository auditRepository;

    private final AuditNotifService notifService;

    private final AuditUserService auditUserService;

    private final AuditRecommendationService recommendationService;

    public AuditService(AuditRepository auditRepository,AuditNotifService notifService,
        AuditUserService auditUserService, AuditRecommendationService recommendationService) {
        this.auditRepository = auditRepository;
        this.notifService = notifService;
        this.auditUserService = auditUserService;
        this.recommendationService = recommendationService;
    }

    /**
     * Save a audit.
     *
     * @param audit the entity to save.
     * @return the persisted entity.
     */
    public Audit save(Audit audit) {
        log.debug("Request to save Audit : {}", audit);
        audit = normalizeDatesIntervalAndStatusIfIsNecessary(audit);
        if(audit.getStatus() == AuditStatus.COMPLETED && audit.getExecutedAt() == null)
            audit.setExecutedAt(Instant.now());
        audit = auditRepository.save(audit);
        notifService.sendAuditStatusChangeNote(audit);
        return audit;
    }

    /**
     * Get all the audits.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Audit> findAll(Pageable pageable) {
        log.debug("Request to get all Audits");
        Page<Audit> page = auditRepository.findAll(pageable);
        Page<Audit>  pageImpl = new PageImpl<>(
            sortAudits(page.getContent()),
            page.getPageable(),
            page.getTotalElements()
        );
        return pageImpl;
    }


    /**
     * Get one audit by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Audit> findOne(Long id) {
        log.debug("Request to get Audit : {}", id);
        return auditRepository.findById(id);
    }

    /**
     * Delete the audit by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Audit : {}", id);
        auditRepository.deleteById(id);
    }

    public Audit normalizeDatesIntervalAndStatusIfIsNecessary(Audit audit){
        if(audit != null){
            // setting defult sart and end date if is necessary
            Instant now = Instant.now();
            if(audit.getStartDate() == null)
                audit.setStartDate(now);
            if(audit.getEndDate() == null)
                audit.setEndDate(now.atZone(ZoneId.systemDefault()).plusDays(1).toInstant());
            
            // normailize date interval
            if(audit.getStartDate().isAfter(audit.getEndDate())){
                Instant startDate = audit.getEndDate();
                audit.setEndDate(audit.getStartDate());
                audit.setStartDate(startDate);
            }

            // normlize status
            if(audit.getStatus() == null)
                audit.setStatus(AuditStatus.INITIAL);
            if(audit.getStartDate().compareTo(now) <=0 
                && audit.getEndDate().compareTo(now) >=0
                 && audit.getStatus() == AuditStatus.INITIAL){
                audit.setStatus(AuditStatus.STARTED);
            }
        }
        return audit;
    }

    public Page<Audit> findByUserIdAndRoleAndStatus(Long userId, AuditUserRole role, AuditStatus status,
            Pageable pageable) {
        Page<Audit> page = auditRepository.findByUserIdAndRoleAndStatus(userId, role, status, pageable);
        Page<Audit>  pageImpl = new PageImpl<>(
            sortAudits(page.getContent()),
            page.getPageable(),
            page.getTotalElements()
        );
        return pageImpl;
    }

    public Page<Audit> findByUserIdAndStatus(Long userId, AuditStatus status, Pageable pageable) {
        Page<Audit> page = auditRepository.findByUserIdAndStatus(userId,status, pageable);
        Page<Audit>  pageImpl = new PageImpl<>(
            sortAudits(page.getContent()),
            page.getPageable(),
            page.getTotalElements()
        );
        return pageImpl;
    }

    public Page<Audit> findByUserId(Long userId, Pageable pageable) {
        Page<Audit> page = auditRepository.findByUserId(userId,pageable);
        Page<Audit>  pageImpl = new PageImpl<>(
            sortAudits(page.getContent()),
            page.getPageable(),
            page.getTotalElements()
        );
        return pageImpl;
    }

    /** 
     * ===================================
     * ======= BEGIN SORTING UTILS METHODS =====
     * ===================================
     */

    public List<Audit> sortAuditsyIds(List<Long> ids){
        List<Audit> audits = new ArrayList<>();
        if(ids != null && !ids.isEmpty()){
            audits = auditRepository.findByIdIn(ids);
            return sortAudits(audits);
        }
        return audits;
    }

    public List<Audit> sortAudits(List<Audit> audits){
        if(audits != null && audits.size() >=2){

            /**
             * a will placed before b if sorted comparator is negative
             * b will placed before a if sorted comparator is positive
             * no sorting if sorted comparator is zero
             */

            return audits.stream()
                // sorting by ids
                .sorted((a,b) -> sortById(a,b))
                // sorting by started tasks
                .sorted((a,b) -> sortByStatus(a,b))
                // sorting by chrono
                .sorted((a,b) -> sortByChrono(a,b))
                .collect(Collectors.toList());
        }

        return audits;
    }

    private int sortById(Audit aa, Audit ab){
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

    private int sortByStatus(Audit aa, Audit ab){
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

    private int sortByChrono(Audit aa, Audit ab){
        if(aa != null && ab != null){
            try {
                boolean aaHasValidChrono = aa.getStartDate() != null && (aa.getEndDate() != null || aa.getExecutedAt() != null);
                boolean abHasValidChrono = ab.getStartDate() != null && (ab.getEndDate() != null || ab.getExecutedAt() != null);

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
                    Duration da = Duration.between(now, aa.getEndDate());
                    Duration db = Duration.between(now, ab.getEndDate());
                    return da.compareTo(db); 
                }
                // end  sorting by excecced chrono

                if(aa.getEndDate() != null && ab.getEndDate() != null )
                    return aa.getEndDate().compareTo(ab.getEndDate());
            } catch (Exception e) {
                //TODO: handle exception
            }
        }
        return 0;
    }

    private boolean isChronoExceced(Audit audit){
        if(audit != null && audit.getStartDate() != null && audit.getEndDate() != null){
            Instant instant = audit.getExecutedAt() != null ? audit.getExecutedAt() : Instant.now();
            return instant.compareTo(audit.getEndDate()) > 0;
        }
        return false;
    }

    /** 
     * ==================================
     * ==== END SORTING UTILS METHODS ===
     * ==================================
     */

    public Audit createFromModelId(String title, Long id) {
        Audit model = auditRepository.findByIdAndStatusNot(id, AuditStatus.CANCELED);
        if(model != null){
            // cloning audit
            Instant now = Instant.now();
            Audit audit = new Audit();
            audit.setCycle(model.getCycle());
            audit.startDate(now);
            // setting end date
            if(model.getStartDate() != null && model.getEndDate() != null){
                Duration d = Duration.between(model.getStartDate(), model.getEndDate());
                audit.setEndDate(now.plusMillis(d.toMillis()));
            }
            audit.setProcessCategoryId(model.getProcessCategoryId());
            audit.setProcessCategoryName(model.getProcessCategoryName());
            audit.setProcessId(model.getProcessId());
            audit.setProcessName(model.getProcessName());
            audit.setRiskId(model.getRiskId());
            audit.setRiskLevel(model.getRiskLevel());
            audit.setRiskName(model.getRiskName());
            audit.setStatus(AuditStatus.STARTED);
            audit.setTitle(title != null ? title : "");
            audit.setType(model.getType());
            audit = auditRepository.save(audit);
            if(audit.getId() != null){
                auditUserService.copyFromTo(model, audit);
                recommendationService.copyFromTo(model, audit);
            }
            return audit;
        }
        return null;
    }
}
