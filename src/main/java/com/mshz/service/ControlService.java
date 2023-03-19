package com.mshz.service;

import com.mshz.domain.Control;
import com.mshz.repository.ControlRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Control}.
 */
@Service
@Transactional
public class ControlService {

    private final Logger log = LoggerFactory.getLogger(ControlService.class);

    private final ControlRepository controlRepository;

    public ControlService(ControlRepository controlRepository) {
        this.controlRepository = controlRepository;
    }

    /**
     * Save a control.
     *
     * @param control the entity to save.
     * @return the persisted entity.
     */
    public Control save(Control control) {
        log.debug("Request to save Control : {}", control);
        return controlRepository.save(control);
    }

    /**
     * Get all the controls.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Control> findAll(Pageable pageable) {
        log.debug("Request to get all Controls");
        return controlRepository.findAll(pageable);
    }


    /**
     * Get one control by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Control> findOne(Long id) {
        log.debug("Request to get Control : {}", id);
        return controlRepository.findById(id);
    }

    /**
     * Delete the control by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Control : {}", id);
        controlRepository.deleteById(id);
    }
}
