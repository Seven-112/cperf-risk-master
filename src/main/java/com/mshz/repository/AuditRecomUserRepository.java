package com.mshz.repository;

import java.util.List;

import com.mshz.domain.AuditRecomUser;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the AuditRecomUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AuditRecomUserRepository extends JpaRepository<AuditRecomUser, Long>, JpaSpecificationExecutor<AuditRecomUser> {

    List<AuditRecomUser> findByRecomId(Long id);

    List<AuditRecomUser> findByRecomIdAndUserId(Long recomId, Long userId);
}
