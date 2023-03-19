package com.mshz.service;

import com.mshz.domain.RiskType;
import com.mshz.repository.RiskTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link RiskType}.
 */
@Service
@Transactional
public class RiskTypeService {

    private final Logger log = LoggerFactory.getLogger(RiskTypeService.class);

    private final RiskTypeRepository riskTypeRepository;

    public RiskTypeService(RiskTypeRepository riskTypeRepository) {
        this.riskTypeRepository = riskTypeRepository;
    }

    /**
     * Save a riskType.
     *
     * @param riskType the entity to save.
     * @return the persisted entity.
     */
    public RiskType save(RiskType riskType) {
        log.debug("Request to save RiskType : {}", riskType);
        return riskTypeRepository.save(riskType);
    }

    /**
     * Get all the riskTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RiskType> findAll(Pageable pageable) {
        log.debug("Request to get all RiskTypes");
        return riskTypeRepository.findAll(pageable);
    }


    /**
     * Get one riskType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RiskType> findOne(Long id) {
        log.debug("Request to get RiskType : {}", id);
        return riskTypeRepository.findById(id);
    }

    /**
     * Delete the riskType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete RiskType : {}", id);
        riskTypeRepository.deleteById(id);
    }
}
