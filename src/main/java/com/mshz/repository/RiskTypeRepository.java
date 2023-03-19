package com.mshz.repository;

import com.mshz.domain.RiskType;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the RiskType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RiskTypeRepository extends JpaRepository<RiskType, Long>, JpaSpecificationExecutor<RiskType> {
}
