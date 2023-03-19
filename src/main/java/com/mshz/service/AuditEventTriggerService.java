package com.mshz.service;

import com.mshz.domain.Audit;
import com.mshz.domain.AuditEventTrigger;
import com.mshz.domain.enumeration.AuditEventRecurrence;
import com.mshz.repository.AuditEventTriggerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link AuditEventTrigger}.
 */
@Service
@Transactional
public class AuditEventTriggerService {

    private final Logger log = LoggerFactory.getLogger(AuditEventTriggerService.class);

    private final AuditEventTriggerRepository auditEventTriggerRepository;

    private final AuditService auditService;

    public AuditEventTriggerService(AuditEventTriggerRepository auditEventTriggerRepository,
        AuditService auditService) {
        this.auditEventTriggerRepository = auditEventTriggerRepository;
        this.auditService = auditService;
    }

    /**
     * Save a auditEventTrigger.
     *
     * @param auditEventTrigger the entity to save.
     * @return the persisted entity.
     */
    public AuditEventTrigger save(AuditEventTrigger auditEventTrigger) {
        log.debug("Request to save AuditEventTrigger : {}", auditEventTrigger);
        if(auditEventTrigger != null && auditEventTrigger.getId() == null){
            auditEventTrigger.setDisabled(Boolean.FALSE);
            auditEventTrigger.setCreatedAt((new Date()).toInstant());
        }
        if(auditEventTrigger != null && auditEventTrigger.getStartCount() == null)
            auditEventTrigger.setStartCount(0);
        return auditEventTriggerRepository.save(auditEventTrigger);
    }

    /**
     * Get all the auditEventTriggers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AuditEventTrigger> findAll(Pageable pageable) {
        log.debug("Request to get all AuditEventTriggers");
        return auditEventTriggerRepository.findAll(pageable);
    }


    /**
     * Get one auditEventTrigger by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AuditEventTrigger> findOne(Long id) {
        log.debug("Request to get AuditEventTrigger : {}", id);
        return auditEventTriggerRepository.findById(id);
    }

    /**
     * Delete the auditEventTrigger by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AuditEventTrigger : {}", id);
        auditEventTriggerRepository.deleteById(id);
    }
    
    public void autoCreateSheduledInstances(){
        Instant instant = Instant.now();

        LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();

        Instant start = localDate.atTime(LocalTime.MIN)
                        .atZone(ZoneId.systemDefault()).toInstant();

        Instant end = localDate.atTime(LocalTime.MAX)
                        .atZone(ZoneId.systemDefault()).toInstant();

        List<AuditEventTrigger> events = auditEventTriggerRepository
            .findByNextStartAtBetweenAndDisabledAndAuditIsNotNull(start,end, Boolean.FALSE);
        log.debug("findedEvents {} at {} ", events, instant);
        for (AuditEventTrigger event : events) {
            String title = event.getStartCount() != null && event.getStartCount().intValue() > 0 ?
                             event.getName().concat("( "+(event.getStartCount().intValue() +1) + " )") :
                             event.getName();
           Audit audit = auditService.createFromModelId(title, event.getAudit().getId());
           if(audit != null){
               if(event.getFirstStartedAt() == null)
                    event.setFirstStartedAt(instant);
                event = setNextShudeledOnAfter(event);
                Integer startCount = event.getStartCount() != null ? (event.getStartCount().intValue() +1) : 1;
                event.setStartCount(startCount);
               auditEventTriggerRepository.save(event);
           }
        }
        
    }

    public AuditEventTrigger setNextShudeledOnAfter(AuditEventTrigger event){
        if(event != null){
            if(event.getNextStartAt() != null){
                ZonedDateTime zdt = event.getNextStartAt().atZone(ZoneId.systemDefault());
                if(event.getRecurrence() != null && event.getRecurrence() != AuditEventRecurrence.ONCE){
                    if(event.getRecurrence() == AuditEventRecurrence.EVERY_WEEK_ON_DAY){
                        event.setNextStartAt(zdt.plusDays(7).toInstant());
                    }else if(event.getRecurrence() == AuditEventRecurrence.EVERY_YEAR_ON_DATE){
                        int month = zdt.getMonthValue();
                        int dayOfMonth = zdt.getDayOfMonth();
                        event.setNextStartAt(zdt.plusYears(1).withMonth(month)
                                    .withDayOfMonth(dayOfMonth).toInstant());
                    }else if(event.getRecurrence() == AuditEventRecurrence.WEEK){
                        if(zdt.getDayOfWeek() == DayOfWeek.FRIDAY)
                            event.setNextStartAt(zdt.plusDays(3).toInstant());
                        else if(zdt.getDayOfWeek() == DayOfWeek.SATURDAY)
                            event.setNextStartAt(zdt.plusDays(2).toInstant());
                        else
                            event.setNextStartAt(zdt.plusDays(1).toInstant());
                    }else if(event.getRecurrence() == AuditEventRecurrence.EVERY_MONTH_OF_DAY_POSITION){
                       event.setNextStartAt(getNextDateByDayPos(zdt));
                    }else if(event.getRecurrence() == AuditEventRecurrence.EVERY_MONTH){
                        ZonedDateTime nextZdt = null;
                        int monthPlusNumber = 1;
                        // lopp is nessaray if net date has not dayofMonths
                        while(nextZdt == null){
                            try {
                                nextZdt = zdt.plusMonths(monthPlusNumber)
                                    .withHour(zdt.getHour())
                                    .withMinute(zdt.getMinute())
                                    .withSecond(zdt.getSecond())
                                    .withDayOfMonth(zdt.getDayOfMonth());
                                monthPlusNumber++;
                            } catch (Exception e) {
                                log.error("error {}", e.getMessage());
                                e.printStackTrace();
                            }
                        }
                        event.setNextStartAt(nextZdt.toInstant());
                    }else{
                        event.setNextStartAt(zdt.plusDays(1).toInstant());
                    }
                }else{
                    event.setNextStartAt(null);
                }
            }else{
                if(event.getFirstStartedAt() != null)
                    event.setNextStartAt(event.getFirstStartedAt());
            }
        }
        return event;
    }

    private Instant getNextDateByDayPos(ZonedDateTime zdt){
        if(zdt != null){
            int dayPos = 0;
            
            // find day postion
            ZonedDateTime monthZdt = ZonedDateTime.now(ZoneId.systemDefault())
                .withYear(zdt.getYear())
                .withMonth(zdt.getMonthValue())
                .withDayOfMonth(1);

            while(monthZdt.getMonthValue() == zdt.getMonthValue()){
                if(monthZdt.getDayOfWeek() == zdt.getDayOfWeek())
                    dayPos++;
                if(monthZdt.getDayOfMonth() == zdt.getDayOfMonth())
                    break;
                else
                    monthZdt = monthZdt.plusDays(1); // incrementation
            }

            // find next date by day pos
            ZonedDateTime nextMothZdt = ZonedDateTime.now(ZoneId.systemDefault())
                .withYear(zdt.getYear())
                .withMonth(zdt.getMonthValue() +1) // next month
                .withDayOfMonth(1)
                .withHour(0)
                .withMinute(0)
                .withSecond(0);

            int currentMonth = nextMothZdt.getMonthValue();

            int currentPos = 0;

            while(dayPos != 0){
                // cpt reinitialization
                if(nextMothZdt.getMonthValue() != currentMonth){
                    currentPos = 0; 
                    currentMonth = nextMothZdt.getMonthValue();
                }
                // finding day by position
                if(nextMothZdt.getDayOfWeek() == zdt.getDayOfWeek())
                    currentPos++;
                
                if(currentPos == dayPos)
                    break;
                else
                    nextMothZdt = nextMothZdt.plusDays(1); // incrementation
            }

            return nextMothZdt
                    .withHour(zdt.getHour())
                    .withMinute(zdt.getMinute())
                    .withSecond(zdt.getSecond())
                    .toInstant();

        }
        return null;
    }
}
