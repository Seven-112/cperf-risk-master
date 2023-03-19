package com.mshz.repository;

import com.mshz.domain.AuditRecommendationFile;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the AuditRecommendationFile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AuditRecommendationFileRepository extends JpaRepository<AuditRecommendationFile, Long>, JpaSpecificationExecutor<AuditRecommendationFile> {

    List<AuditRecommendationFile> findByRecommendationId(Long id);
}
