package com.mshz.service;

import com.mshz.domain.ControlMaturity;
import com.mshz.repository.ControlMaturityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ControlMaturity}.
 */
@Service
@Transactional
public class ControlMaturityService {

    private final Logger log = LoggerFactory.getLogger(ControlMaturityService.class);

    private final ControlMaturityRepository controlMaturityRepository;

    public ControlMaturityService(ControlMaturityRepository controlMaturityRepository) {
        this.controlMaturityRepository = controlMaturityRepository;
    }

    /**
     * Save a controlMaturity.
     *
     * @param controlMaturity the entity to save.
     * @return the persisted entity.
     */
    public ControlMaturity save(ControlMaturity controlMaturity) {
        log.debug("Request to save ControlMaturity : {}", controlMaturity);
        return controlMaturityRepository.save(controlMaturity);
    }

    /**
     * Get all the controlMaturities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ControlMaturity> findAll(Pageable pageable) {
        log.debug("Request to get all ControlMaturities");
        return controlMaturityRepository.findAll(pageable);
    }


    /**
     * Get one controlMaturity by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ControlMaturity> findOne(Long id) {
        log.debug("Request to get ControlMaturity : {}", id);
        return controlMaturityRepository.findById(id);
    }

    /**
     * Delete the controlMaturity by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ControlMaturity : {}", id);
        controlMaturityRepository.deleteById(id);
    }
}
