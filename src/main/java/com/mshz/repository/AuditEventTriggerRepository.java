package com.mshz.repository;

import com.mshz.domain.AuditEventTrigger;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the AuditEventTrigger entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AuditEventTriggerRepository extends JpaRepository<AuditEventTrigger, Long>, JpaSpecificationExecutor<AuditEventTrigger> {

    List<AuditEventTrigger> findByNextStartAtBetweenAndDisabledAndAuditIsNotNull(Instant instant, Instant instant2,
            Boolean disable);
}
