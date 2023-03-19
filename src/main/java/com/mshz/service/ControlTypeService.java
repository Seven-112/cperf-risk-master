package com.mshz.service;

import com.mshz.domain.ControlType;
import com.mshz.repository.ControlTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ControlType}.
 */
@Service
@Transactional
public class ControlTypeService {

    private final Logger log = LoggerFactory.getLogger(ControlTypeService.class);

    private final ControlTypeRepository controlTypeRepository;

    public ControlTypeService(ControlTypeRepository controlTypeRepository) {
        this.controlTypeRepository = controlTypeRepository;
    }

    /**
     * Save a controlType.
     *
     * @param controlType the entity to save.
     * @return the persisted entity.
     */
    public ControlType save(ControlType controlType) {
        log.debug("Request to save ControlType : {}", controlType);
        return controlTypeRepository.save(controlType);
    }

    /**
     * Get all the controlTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ControlType> findAll(Pageable pageable) {
        log.debug("Request to get all ControlTypes");
        return controlTypeRepository.findAll(pageable);
    }


    /**
     * Get one controlType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ControlType> findOne(Long id) {
        log.debug("Request to get ControlType : {}", id);
        return controlTypeRepository.findById(id);
    }

    /**
     * Delete the controlType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ControlType : {}", id);
        controlTypeRepository.deleteById(id);
    }
}
