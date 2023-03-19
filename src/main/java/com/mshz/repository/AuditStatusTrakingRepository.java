package com.mshz.repository;

import com.mshz.domain.AuditStatusTraking;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the AuditStatusTraking entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AuditStatusTrakingRepository extends JpaRepository<AuditStatusTraking, Long>, JpaSpecificationExecutor<AuditStatusTraking> {
}
