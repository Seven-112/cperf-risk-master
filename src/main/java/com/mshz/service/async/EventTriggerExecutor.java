package com.mshz.service.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.mshz.service.AuditEventTriggerService;

@Service
public class EventTriggerExecutor {

    Logger log = LoggerFactory.getLogger(EventTriggerExecutor.class);

    private final AuditEventTriggerService eventTriggerService;

    public EventTriggerExecutor(AuditEventTriggerService eventTriggerService){
        this.eventTriggerService = eventTriggerService;
    }

    @Async
    @Scheduled(cron = "${sheduling.job.cron.event-trigger}")
    public void autoCreatePlannedInstances(){
        log.info("event trigger intance auto-ceated");
        eventTriggerService.autoCreateSheduledInstances();
    }
}
