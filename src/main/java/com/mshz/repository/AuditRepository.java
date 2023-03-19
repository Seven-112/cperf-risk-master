package com.mshz.repository;

import java.time.Instant;
import java.util.List;

import com.mshz.domain.Audit;
import com.mshz.domain.enumeration.AuditStatus;
import com.mshz.domain.enumeration.AuditUserRole;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Audit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AuditRepository extends JpaRepository<Audit, Long>, JpaSpecificationExecutor<Audit> {

    @Query("select a from Audit a where :time between a.startDate and a.endDate and a.status =:status")
    List<Audit> getStartabaleAtInstant(@Param("time") Instant instant, @Param("status") AuditStatus initial);


    @Query("select distinct a from Audit a "
            +"left join AuditUser au ON a.id=au.auditId "
            +"where au.userId=:uid and au.role=:role and a.status =:status")
    Page<Audit> findByUserIdAndRoleAndStatus(@Param("uid") Long userId, @Param("role") AuditUserRole role, @Param("status") AuditStatus status, Pageable pageable);


    @Query("select distinct a from Audit a "
            +"left join AuditUser au ON a.id=au.auditId "
            +"where au.userId=:uid and a.status =:status")
    Page<Audit> findByUserIdAndStatus(@Param("uid") Long userId, @Param("status") AuditStatus status, Pageable pageable);


    @Query("select distinct a from Audit a "
            +"left join AuditUser au ON a.id=au.auditId "
            +"where au.userId=:uid")
    Page<Audit> findByUserId(@Param("uid") Long userId, Pageable pageable);


    List<Audit> findByIdIn(List<Long> ids);


    Audit findByIdAndStatusNot(Long id, AuditStatus canceled);
}
