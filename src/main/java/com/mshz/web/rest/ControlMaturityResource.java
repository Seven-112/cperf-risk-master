package com.mshz.web.rest;

import com.mshz.domain.ControlMaturity;
import com.mshz.service.ControlMaturityService;
import com.mshz.web.rest.errors.BadRequestAlertException;
import com.mshz.service.dto.ControlMaturityCriteria;
import com.mshz.service.ControlMaturityQueryService;

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
 * REST controller for managing {@link com.mshz.domain.ControlMaturity}.
 */
@RestController
@RequestMapping("/api")
public class ControlMaturityResource {

    private final Logger log = LoggerFactory.getLogger(ControlMaturityResource.class);

    private static final String ENTITY_NAME = "microrisqueControlMaturity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ControlMaturityService controlMaturityService;

    private final ControlMaturityQueryService controlMaturityQueryService;

    public ControlMaturityResource(ControlMaturityService controlMaturityService, ControlMaturityQueryService controlMaturityQueryService) {
        this.controlMaturityService = controlMaturityService;
        this.controlMaturityQueryService = controlMaturityQueryService;
    }

    /**
     * {@code POST  /control-maturities} : Create a new controlMaturity.
     *
     * @param controlMaturity the controlMaturity to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new controlMaturity, or with status {@code 400 (Bad Request)} if the controlMaturity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/control-maturities")
    public ResponseEntity<ControlMaturity> createControlMaturity(@Valid @RequestBody ControlMaturity controlMaturity) throws URISyntaxException {
        log.debug("REST request to save ControlMaturity : {}", controlMaturity);
        if (controlMaturity.getId() != null) {
            throw new BadRequestAlertException("A new controlMaturity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ControlMaturity result = controlMaturityService.save(controlMaturity);
        return ResponseEntity.created(new URI("/api/control-maturities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /control-maturities} : Updates an existing controlMaturity.
     *
     * @param controlMaturity the controlMaturity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated controlMaturity,
     * or with status {@code 400 (Bad Request)} if the controlMaturity is not valid,
     * or with status {@code 500 (Internal Server Error)} if the controlMaturity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/control-maturities")
    public ResponseEntity<ControlMaturity> updateControlMaturity(@Valid @RequestBody ControlMaturity controlMaturity) throws URISyntaxException {
        log.debug("REST request to update ControlMaturity : {}", controlMaturity);
        if (controlMaturity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ControlMaturity result = controlMaturityService.save(controlMaturity);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, controlMaturity.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /control-maturities} : get all the controlMaturities.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of controlMaturities in body.
     */
    @GetMapping("/control-maturities")
    public ResponseEntity<List<ControlMaturity>> getAllControlMaturities(ControlMaturityCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ControlMaturities by criteria: {}", criteria);
        Page<ControlMaturity> page = controlMaturityQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /control-maturities/count} : count all the controlMaturities.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/control-maturities/count")
    public ResponseEntity<Long> countControlMaturities(ControlMaturityCriteria criteria) {
        log.debug("REST request to count ControlMaturities by criteria: {}", criteria);
        return ResponseEntity.ok().body(controlMaturityQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /control-maturities/:id} : get the "id" controlMaturity.
     *
     * @param id the id of the controlMaturity to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the controlMaturity, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/control-maturities/{id}")
    public ResponseEntity<ControlMaturity> getControlMaturity(@PathVariable Long id) {
        log.debug("REST request to get ControlMaturity : {}", id);
        Optional<ControlMaturity> controlMaturity = controlMaturityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(controlMaturity);
    }

    /**
     * {@code DELETE  /control-maturities/:id} : delete the "id" controlMaturity.
     *
     * @param id the id of the controlMaturity to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/control-maturities/{id}")
    public ResponseEntity<Void> deleteControlMaturity(@PathVariable Long id) {
        log.debug("REST request to delete ControlMaturity : {}", id);
        controlMaturityService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
