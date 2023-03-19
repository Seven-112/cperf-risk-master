package com.mshz.service;

import com.mshz.domain.Risk;
import com.mshz.repository.RiskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Risk}.
 */
@Service
@Transactional
public class RiskService {

    private final Logger log = LoggerFactory.getLogger(RiskService.class);

    private final RiskRepository riskRepository;

    public RiskService(RiskRepository riskRepository) {
        this.riskRepository = riskRepository;
    }

    /**
     * Save a risk.
     *
     * @param risk the entity to save.
     * @return the persisted entity.
     */
    public Risk save(Risk risk) {
        log.debug("Request to save Risk : {}", risk);
        return riskRepository.save(risk);
    }

    /**
     * Get all the risks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Risk> findAll(Pageable pageable) {
        log.debug("Request to get all Risks");
        return riskRepository.findAll(pageable);
    }


    /**
     * Get one risk by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Risk> findOne(Long id) {
        log.debug("Request to get Risk : {}", id);
        return riskRepository.findById(id);
    }

    /**
     * Delete the risk by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Risk : {}", id);
        riskRepository.deleteById(id);
    }
}
