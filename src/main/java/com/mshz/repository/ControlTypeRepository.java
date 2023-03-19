package com.mshz.repository;

import com.mshz.domain.ControlType;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ControlType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ControlTypeRepository extends JpaRepository<ControlType, Long>, JpaSpecificationExecutor<ControlType> {
}
