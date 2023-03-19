package com.mshz.repository;

import com.mshz.domain.ControlMaturity;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ControlMaturity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ControlMaturityRepository extends JpaRepository<ControlMaturity, Long>, JpaSpecificationExecutor<ControlMaturity> {
}
