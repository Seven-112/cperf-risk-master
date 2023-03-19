package com.mshz.repository;

import java.util.List;

import com.mshz.domain.AuditRecomUser;
import com.mshz.domain.AuditRecommendation;
import com.mshz.domain.enumeration.AuditStatus;
import com.mshz.domain.enumeration.AuditUserRole;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the AuditRecommendation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AuditRecommendationRepository extends JpaRepository<AuditRecommendation, Long>, JpaSpecificationExecutor<AuditRecommendation> {

    @Query("select distinct a from AuditRecommendation a "
            +"left join AuditRecomUser au ON a.id=au.recomId "
            +"where au.userId=:uid and au.role=:role and a.status =:status")
    Page<AuditRecommendation>  findByUserIdAndRoleAndStatus(@Param("uid") Long userId, @Param("role") AuditUserRole role, @Param("status") AuditStatus status, Pageable pageable);

    @Query("select distinct a from AuditRecommendation a "
            +"left join AuditRecomUser au ON a.id=au.recomId "
            +"where au.userId=:uid and a.status =:status")
    Page<AuditRecommendation> findByUserIdAndStatus(@Param("uid") Long userId, @Param("status") AuditStatus status, Pageable pageable);


    @Query("select distinct a from AuditRecommendation a "
            +"left join AuditRecomUser au ON a.id=au.recomId "
            +"where au.userId=:uid")
    Page<AuditRecommendation> findByUserId(@Param("uid") Long userId, Pageable pageable);

    List<AuditRecommendation> findByIdIn(List<Long> ids);

    List<AuditRecommendation> findByAuditIdAndStatusNot(Long id, AuditStatus canceled);
}
