package com.mshz.repository;

import com.mshz.domain.AuditCycle;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the AuditCycle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AuditCycleRepository extends JpaRepository<AuditCycle, Long>, JpaSpecificationExecutor<AuditCycle> {
}
