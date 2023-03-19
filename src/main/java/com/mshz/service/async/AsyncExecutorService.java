package com.mshz.service.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class AsyncExecutorService {

    private final Logger log = LoggerFactory.getLogger(AsyncExecutorService.class);

    private final AsyncUtilService utilService;

    public AsyncExecutorService( AsyncUtilService utilService){
        this.utilService = utilService;
    }

    @Async
    @Scheduled(fixedDelay = 30000, initialDelay = 10000) // 10 seconds and initialize on 10 seconds
    public void autoPlayAudits(){
        try {
            utilService.autoPlayAudits();
        } catch (Exception e) {
            e.printStackTrace();
            log.debug(e.getMessage());
        }
    }
    
}
