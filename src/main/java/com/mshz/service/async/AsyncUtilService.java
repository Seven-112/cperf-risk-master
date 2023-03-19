package com.mshz.service.async;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;

import com.mshz.domain.Audit;
import com.mshz.domain.enumeration.AuditStatus;
import com.mshz.repository.AuditRepository;
import com.mshz.webflux.AuditNotifService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AsyncUtilService {

    private final Logger log = LoggerFactory.getLogger(AsyncUtilService.class);
    
    private final AuditRepository auditRepository;

    private final AuditNotifService notifService;

    public AsyncUtilService(AuditRepository auditRepository,AuditNotifService notifService){
        this.auditRepository = auditRepository;
        this.notifService = notifService;
    }
    
    public void autoPlayAudits(){
        try {
            Instant now = Instant.now();
            List<Audit> audits = auditRepository.getStartabaleAtInstant(now, AuditStatus.INITIAL);
            for (Audit audit : audits) {
                audit.setStatus(AuditStatus.STARTED);
                audit = auditRepository.saveAndFlush(audit);
                notifService.sendAuditStatusChangeNote(audit);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.debug(e.getMessage());
        }
    }
}
