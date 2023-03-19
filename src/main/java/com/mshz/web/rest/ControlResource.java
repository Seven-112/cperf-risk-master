package com.mshz.web.rest;

import com.mshz.domain.Control;
import com.mshz.service.ControlService;
import com.mshz.web.rest.errors.BadRequestAlertException;
import com.mshz.service.dto.ControlCriteria;
import com.mshz.service.ControlQueryService;

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
 * REST controller for managing {@link com.mshz.domain.Control}.
 */
@RestController
@RequestMapping("/api")
public class ControlResource {

    private final Logger log = LoggerFactory.getLogger(ControlResource.class);

    private static final String ENTITY_NAME = "microrisqueControl";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ControlService controlService;

    private final ControlQueryService controlQueryService;

    public ControlResource(ControlService controlService, ControlQueryService controlQueryService) {
        this.controlService = controlService;
        this.controlQueryService = controlQueryService;
    }

    /**
     * {@code POST  /controls} : Create a new control.
     *
     * @param control the control to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new control, or with status {@code 400 (Bad Request)} if the control has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/controls")
    public ResponseEntity<Control> createControl(@Valid @RequestBody Control control) throws URISyntaxException {
        log.debug("REST request to save Control : {}", control);
        if (control.getId() != null) {
            throw new BadRequestAlertException("A new control cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Control result = controlService.save(control);
        return ResponseEntity.created(new URI("/api/controls/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /controls} : Updates an existing control.
     *
     * @param control the control to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated control,
     * or with status {@code 400 (Bad Request)} if the control is not valid,
     * or with status {@code 500 (Internal Server Error)} if the control couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/controls")
    public ResponseEntity<Control> updateControl(@Valid @RequestBody Control control) throws URISyntaxException {
        log.debug("REST request to update Control : {}", control);
        if (control.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Control result = controlService.save(control);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, control.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /controls} : get all the controls.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of controls in body.
     */
    @GetMapping("/controls")
    public ResponseEntity<List<Control>> getAllControls(ControlCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Controls by criteria: {}", criteria);
        Page<Control> page = controlQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /controls/count} : count all the controls.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/controls/count")
    public ResponseEntity<Long> countControls(ControlCriteria criteria) {
        log.debug("REST request to count Controls by criteria: {}", criteria);
        return ResponseEntity.ok().body(controlQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /controls/:id} : get the "id" control.
     *
     * @param id the id of the control to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the control, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/controls/{id}")
    public ResponseEntity<Control> getControl(@PathVariable Long id) {
        log.debug("REST request to get Control : {}", id);
        Optional<Control> control = controlService.findOne(id);
        return ResponseUtil.wrapOrNotFound(control);
    }

    /**
     * {@code DELETE  /controls/:id} : delete the "id" control.
     *
     * @param id the id of the control to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/controls/{id}")
    public ResponseEntity<Void> deleteControl(@PathVariable Long id) {
        log.debug("REST request to delete Control : {}", id);
        controlService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
