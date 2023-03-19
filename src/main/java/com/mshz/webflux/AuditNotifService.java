package com.mshz.webflux;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.mshz.domain.Audit;
import com.mshz.domain.AuditRecomUser;
import com.mshz.domain.AuditRecommendation;
import com.mshz.domain.AuditUser;
import com.mshz.domain.enumeration.AuditUserRole;
import com.mshz.repository.AuditRecomUserRepository;
import com.mshz.repository.AuditRecommendationRepository;
import com.mshz.repository.AuditRepository;
import com.mshz.repository.AuditUserRepository;
import com.mshz.webflux.dto.NotifAction;
import com.mshz.webflux.dto.NotifDTO;
import com.mshz.webflux.dto.NotifEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AuditNotifService {

    Logger logger = LoggerFactory.getLogger(AuditNotifService.class);
    
    private final GatewayClientService gatewayClientService;

    private final AuditRepository auditRepository;

    private final AuditUserRepository auditUserRepository;

    private final AuditRecomUserRepository auditRecomUserRepository;

    private final AuditRecommendationRepository recommendationRepository;


    public AuditNotifService(
        GatewayClientService gatewayClientService,
        AuditRepository auditRepository,
        AuditUserRepository auditUserRepository,
        AuditRecomUserRepository auditRecomUserRepository,
        AuditRecommendationRepository recommendationRepository){
        this.gatewayClientService = gatewayClientService;
        this.auditRepository = auditRepository;
        this.auditUserRepository = auditUserRepository;
        this.auditRecomUserRepository = auditRecomUserRepository;
        this.recommendationRepository = recommendationRepository;
    }

    @Async
    public void sendAuditStatusChangeNote(Audit audit){
        logger.info("sending status change notif for audit: {}", audit);
        if(audit != null && audit.getId() != null){
            List<AuditUser> users = auditUserRepository.findByAuditId(audit.getId());
            switch(audit.getStatus()){
                case EXECUTED:
                    users = users.stream()
                        .filter(a -> a.getRole() == AuditUserRole.SUBMITOR || a.getRole() == AuditUserRole.VALIDATOR)
                        .collect(Collectors.toList());
                    break;
                case SUBMITTED:
                    users = users.stream()
                        .filter(a -> a.getRole() == AuditUserRole.SUBMITOR || a.getRole() == AuditUserRole.VALIDATOR)
                        .collect(Collectors.toList());
                    break;
                case COMPLETED:
                    users = users.stream()
                        .filter(t -> t.getRole() == AuditUserRole.VALIDATOR)
                        .distinct().collect(Collectors.toList());
                    break;
                default:
                    // no thing
            }
            createAndSendAuditNotification(audit, users);
        }
    }

    @Async
    public void sendAuditNotifToUser(AuditUser auditUser){
        if(auditUser != null && auditUser.getUserId() != null && auditUser.getAuditId() != null){
            List<AuditUser> exists = auditUserRepository
                .findByAuditIdAndUserId(auditUser.getAuditId(), auditUser.getUserId());
            if(exists == null || exists.isEmpty()){
                Audit audit = auditRepository.findById(auditUser.getAuditId()).orElse(null);
                if(audit != null){
                    List<AuditUser> targets = new ArrayList<>();
                    targets.add(auditUser);
                    createAndSendAuditNotification(audit, targets);
                }
            }
        }
    }


    @Async
    public void sendRecommandationStatusChangeNote(AuditRecommendation recommendation){
        logger.info("sending status change notif for recommendation: {}", recommendation);
        if(recommendation != null && recommendation.getId() != null){
            List<AuditRecomUser> users = auditRecomUserRepository.findByRecomId(recommendation.getId());
            switch(recommendation.getStatus()){
                case EXECUTED:
                    users = users.stream()
                        .filter(r -> r.getRole() == AuditUserRole.SUBMITOR || r.getRole() == AuditUserRole.VALIDATOR).collect(Collectors.toList());
                    break;
                case SUBMITTED:
                    users = users.stream()
                        .filter(r -> r.getRole() == AuditUserRole.SUBMITOR || r.getRole() == AuditUserRole.VALIDATOR)
                        .collect(Collectors.toList());
                    break;
                case COMPLETED:
                    users = users.stream()
                        .filter(r -> r.getRole() == AuditUserRole.VALIDATOR).collect(Collectors.toList());
                    break;
                default:
                    // nothing
            } // swith end

            createAndSendRecommendationNotif(recommendation, users, true);
        }
    }

    @Async
    public void sendRecomNotifToRecomUser(AuditRecomUser recomUser){
        if(recomUser != null && recomUser.getRecomId() != null && recomUser.getUserId() != null){
            List<AuditRecomUser> exists = auditRecomUserRepository
                .findByRecomIdAndUserId(recomUser.getRecomId(), recomUser.getUserId());
            if(exists == null || exists.isEmpty()){
                AuditRecommendation recommendation = recommendationRepository.findById(recomUser.getRecomId()).orElse(null);
                if(recommendation != null){
                    List<AuditRecomUser> targets = new ArrayList<>();
                    targets.add(recomUser);
                    createAndSendRecommendationNotif(recommendation, targets, false);
                }
            }
        }
    }

    private void createAndSendAuditNotification(Audit audit,  List<AuditUser> users){
        if(audit != null && users != null && !users.isEmpty()){
            List<Long> targetUserIds = users.stream()
                .filter(u -> u.getUserId() != null)
                .map(u -> u.getUserId().longValue())
                .sorted().distinct().map(uid -> Long.valueOf(uid)).collect(Collectors.toList());

            NotifAction action = null;
            switch(audit.getStatus()){
                case CANCELED:
                    action = NotifAction.AUDIT_CANCELED;
                    break;
                case EXECUTED:
                    action = NotifAction.AUDIT_EXECUTED;
                    break;
                case SUBMITTED:
                    action = NotifAction.AUDIT_SUBMITTED;
                    break;
                case COMPLETED:
                    action = NotifAction.AUDIT_COMPLETED;
                    break;
                case STARTED:
                    action = NotifAction.AUDIT_STARTED;
                    break;
                case INITIAL:
                    action = NotifAction.AUDIT_INITILIZED;
                    break;
                default:
                    action = null;
            } // swith end

            if(!targetUserIds.isEmpty()){
                NotifDTO note = new NotifDTO();
                List<NotifEntity> entities = new ArrayList<>();
                entities.add(new NotifEntity(audit.getId(), audit.getTitle()));
                note.setEntities(entities);
                note.setAction(action);
                note.setTargetUserIds(targetUserIds);
                gatewayClientService.sendNotificationToGateway(note);
            }
        }
    }

    private void createAndSendRecommendationNotif(AuditRecommendation recommendation, List<AuditRecomUser> users, boolean withAuditUser){
        if(recommendation != null && recommendation.getId() != null){
            boolean sendToResponsableOlso = false;
            List<Long> targetUserIds = new ArrayList<>();
            /* if(recommendation.getAuditId() != null && withAuditUser){
                // add all audit users to targets
                List<AuditUser> auditUsers = auditUserRepository.findByAuditId(recommendation.getAuditId());
                for (AuditUser auditUser : auditUsers) {
                    if(auditUser.getUserId() != null)
                        targetUserIds.add(auditUser.getUserId());
                }
            } */
            
            // // add recommandations users to targets
            if(users != null && !users.isEmpty()){
                for (AuditRecomUser user : users) {
                    if(user != null && user.getUserId() != null)
                        targetUserIds.add(user.getUserId());
                }
            }

            NotifAction action = null;
            switch(recommendation.getStatus()){
                case CANCELED:
                    action = NotifAction.RECOM_CANCELED;
                    sendToResponsableOlso = true;
                    break;
                case EXECUTED:
                    action = NotifAction.RECOM_EXECUTED;
                    break;
                case SUBMITTED:
                    action = NotifAction.RECOM_SUBMITTED;
                    break;
                case COMPLETED:
                    action = NotifAction.RECOM_COMPLETED;
                    sendToResponsableOlso = true;
                    break;
                case STARTED:
                    action = NotifAction.RECOM_STARTED;
                    sendToResponsableOlso = true;
                    break;
                case INITIAL:
                    action = NotifAction.RECOM_INITILIZED;
                    sendToResponsableOlso = true;
                    break;
                default:
                    action = null;
            } // swith end

            // adding responsable to notif targets
            /* if(recommendation.getResponsableId() != null && sendToResponsableOlso &&
                 !targetUserIds.contains(recommendation.getResponsableId())){
                targetUserIds.add(recommendation.getResponsableId());
            } */
 
            if(!targetUserIds.isEmpty()){
                // distinct targets ids
                targetUserIds = targetUserIds.stream()
                .filter(id -> id != null).map(id -> id.longValue()).sorted()
                .distinct().map(id -> Long.valueOf(id)).collect(Collectors.toList());

                // prepare and send notifications
                Audit audit = auditRepository.findById(recommendation.getAuditId()).orElse(null);
                NotifDTO note = new NotifDTO();
                List<NotifEntity> entities = new ArrayList<>();
                entities.add(new NotifEntity(audit.getId(), audit.getTitle()));
                note.setEntities(entities);
                note.setAction(action);
                note.setTargetUserIds(targetUserIds);
                gatewayClientService.sendNotificationToGateway(note);
            }
            
        }
    }

}
