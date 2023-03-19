package com.mshz.repository;

import com.mshz.domain.Control;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Control entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ControlRepository extends JpaRepository<Control, Long>, JpaSpecificationExecutor<Control> {
}
