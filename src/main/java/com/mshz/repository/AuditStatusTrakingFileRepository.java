package com.mshz.repository;

import com.mshz.domain.AuditStatusTrakingFile;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the AuditStatusTrakingFile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AuditStatusTrakingFileRepository extends JpaRepository<AuditStatusTrakingFile, Long>, JpaSpecificationExecutor<AuditStatusTrakingFile> {
}
