package com.mshz.web.rest;

import com.mshz.domain.ControlType;
import com.mshz.service.ControlTypeService;
import com.mshz.web.rest.errors.BadRequestAlertException;
import com.mshz.service.dto.ControlTypeCriteria;
import com.mshz.service.ControlTypeQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.mshz.domain.ControlType}.
 */
@RestController
@RequestMapping("/api")
public class ControlTypeResource {

    private final Logger log = LoggerFactory.getLogger(ControlTypeResource.class);

    private static final String ENTITY_NAME = "microrisqueControlType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ControlTypeService controlTypeService;

    private final ControlTypeQueryService controlTypeQueryService;

    public ControlTypeResource(ControlTypeService controlTypeService, ControlTypeQueryService controlTypeQueryService) {
        this.controlTypeService = controlTypeService;
        this.controlTypeQueryService = controlTypeQueryService;
    }

    /**
     * {@code POST  /control-types} : Create a new controlType.
     *
     * @param controlType the controlType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new controlType, or with status {@code 400 (Bad Request)} if the controlType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/control-types")
    public ResponseEntity<ControlType> createControlType(@Valid @RequestBody ControlType controlType) throws URISyntaxException {
        log.debug("REST request to save ControlType : {}", controlType);
        if (controlType.getId() != null) {
            throw new BadRequestAlertException("A new controlType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ControlType result = controlTypeService.save(controlType);
        return ResponseEntity.created(new URI("/api/control-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /control-types} : Updates an existing controlType.
     *
     * @param controlType the controlType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated controlType,
     * or with status {@code 400 (Bad Request)} if the controlType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the controlType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/control-types")
    public ResponseEntity<ControlType> updateControlType(@Valid @RequestBody ControlType controlType) throws URISyntaxException {
        log.debug("REST request to update ControlType : {}", controlType);
        if (controlType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ControlType result = controlTypeService.save(controlType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, controlType.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /control-types} : get all the controlTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of controlTypes in body.
     */
    @GetMapping("/control-types")
    public ResponseEntity<List<ControlType>> getAllControlTypes(ControlTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ControlTypes by criteria: {}", criteria);
        Page<ControlType> page = controlTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /control-types/count} : count all the controlTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/control-types/count")
    public ResponseEntity<Long> countControlTypes(ControlTypeCriteria criteria) {
        log.debug("REST request to count ControlTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(controlTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /control-types/:id} : get the "id" controlType.
     *
     * @param id the id of the controlType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the controlType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/control-types/{id}")
    public ResponseEntity<ControlType> getControlType(@PathVariable Long id) {
        log.debug("REST request to get ControlType : {}", id);
        Optional<ControlType> controlType = controlTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(controlType);
    }

    /**
     * {@code DELETE  /control-types/:id} : delete the "id" controlType.
     *
     * @param id the id of the controlType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/control-types/{id}")
    public ResponseEntity<Void> deleteControlType(@PathVariable Long id) {
        log.debug("REST request to delete ControlType : {}", id);
        controlTypeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
