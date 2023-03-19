package com.mshz.webflux;

import com.mshz.security.SecurityUtils;
import com.mshz.webflux.dto.NotifDTO;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GatewayClientService {

    private final Logger logger = LoggerFactory.getLogger(GatewayClientService.class);

    private final String URL_PREFIX = "//";

    @Value("${services.gateway:microgateway}")
    private String GATEWAY_SERVICE_NAME;

    private final String NOTIFICATION_PATH = "/topic/api/notify";

    @Autowired
    private RestTemplate restTemplate;
    
    public void sendNotificationToGateway(NotifDTO notification){
        try {
            logger.debug("sending notification to gateway {} ",notification);
            // passe loged login to notification
            notification.setSenderLogin(SecurityUtils.getCurrentUserLogin().orElse(null));

            // remove logged user id on notification tagets
            Long loggedUserId = SecurityUtils.getUserId().orElse(null);
            logger.debug("loggedUserId {} ", loggedUserId);
            /* if(loggedUserId != null){
                List<Long> targetsWithoutLoggedUser = notification.getTargetUserIds()
                        .stream()
                        .filter(id -> id != null && !id.equals(loggedUserId))
                        .collect(Collectors.toList());
                notification.setTargetUserIds(targetsWithoutLoggedUser);
            } */

            // fomate api resouces uri
            String url = String.format("%s%s%s", URL_PREFIX, GATEWAY_SERVICE_NAME, NOTIFICATION_PATH).toString();

            // sending request
            ResponseEntity<Void> response = restTemplate.postForEntity(url, notification,Void.class);

            logger.debug("status code {}", response.getStatusCodeValue());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

}
