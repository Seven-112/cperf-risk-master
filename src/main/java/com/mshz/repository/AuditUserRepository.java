package com.mshz.repository;

import java.util.List;

import com.mshz.domain.AuditUser;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the AuditUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AuditUserRepository extends JpaRepository<AuditUser, Long>, JpaSpecificationExecutor<AuditUser> {

    List<AuditUser> findByAuditId(Long id);

    List<AuditUser> findByAuditIdAndUserId(Long auditId, Long userId);
}
