package com.mshz.web.rest;

import com.mshz.domain.AuditEventTrigger;
import com.mshz.service.AuditEventTriggerService;
import com.mshz.web.rest.errors.BadRequestAlertException;
import com.mshz.service.dto.AuditEventTriggerCriteria;
import com.mshz.service.AuditEventTriggerQueryService;

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
 * REST controller for managing {@link com.mshz.domain.AuditEventTrigger}.
 */
@RestController
@RequestMapping("/api")
public class AuditEventTriggerResource {

    private final Logger log = LoggerFactory.getLogger(AuditEventTriggerResource.class);

    private static final String ENTITY_NAME = "microrisqueAuditEventTrigger";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AuditEventTriggerService auditEventTriggerService;

    private final AuditEventTriggerQueryService auditEventTriggerQueryService;

    public AuditEventTriggerResource(AuditEventTriggerService auditEventTriggerService, AuditEventTriggerQueryService auditEventTriggerQueryService) {
        this.auditEventTriggerService = auditEventTriggerService;
        this.auditEventTriggerQueryService = auditEventTriggerQueryService;
    }

    /**
     * {@code POST  /audit-event-triggers} : Create a new auditEventTrigger.
     *
     * @param auditEventTrigger the auditEventTrigger to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new auditEventTrigger, or with status {@code 400 (Bad Request)} if the auditEventTrigger has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/audit-event-triggers")
    public ResponseEntity<AuditEventTrigger> createAuditEventTrigger(@Valid @RequestBody AuditEventTrigger auditEventTrigger) throws URISyntaxException {
        log.debug("REST request to save AuditEventTrigger : {}", auditEventTrigger);
        if (auditEventTrigger.getId() != null) {
            throw new BadRequestAlertException("A new auditEventTrigger cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AuditEventTrigger result = auditEventTriggerService.save(auditEventTrigger);
        return ResponseEntity.created(new URI("/api/audit-event-triggers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /audit-event-triggers} : Updates an existing auditEventTrigger.
     *
     * @param auditEventTrigger the auditEventTrigger to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated auditEventTrigger,
     * or with status {@code 400 (Bad Request)} if the auditEventTrigger is not valid,
     * or with status {@code 500 (Internal Server Error)} if the auditEventTrigger couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/audit-event-triggers")
    public ResponseEntity<AuditEventTrigger> updateAuditEventTrigger(@Valid @RequestBody AuditEventTrigger auditEventTrigger) throws URISyntaxException {
        log.debug("REST request to update AuditEventTrigger : {}", auditEventTrigger);
        if (auditEventTrigger.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AuditEventTrigger result = auditEventTriggerService.save(auditEventTrigger);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, auditEventTrigger.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /audit-event-triggers} : get all the auditEventTriggers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of auditEventTriggers in body.
     */
    @GetMapping("/audit-event-triggers")
    public ResponseEntity<List<AuditEventTrigger>> getAllAuditEventTriggers(AuditEventTriggerCriteria criteria, Pageable pageable) {
        log.debug("REST request to get AuditEventTriggers by criteria: {}", criteria);
        Page<AuditEventTrigger> page = auditEventTriggerQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /audit-event-triggers/count} : count all the auditEventTriggers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/audit-event-triggers/count")
    public ResponseEntity<Long> countAuditEventTriggers(AuditEventTriggerCriteria criteria) {
        log.debug("REST request to count AuditEventTriggers by criteria: {}", criteria);
        return ResponseEntity.ok().body(auditEventTriggerQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /audit-event-triggers/:id} : get the "id" auditEventTrigger.
     *
     * @param id the id of the auditEventTrigger to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the auditEventTrigger, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/audit-event-triggers/{id}")
    public ResponseEntity<AuditEventTrigger> getAuditEventTrigger(@PathVariable Long id) {
        log.debug("REST request to get AuditEventTrigger : {}", id);
        Optional<AuditEventTrigger> auditEventTrigger = auditEventTriggerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(auditEventTrigger);
    }

    /**
     * {@code DELETE  /audit-event-triggers/:id} : delete the "id" auditEventTrigger.
     *
     * @param id the id of the auditEventTrigger to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/audit-event-triggers/{id}")
    public ResponseEntity<Void> deleteAuditEventTrigger(@PathVariable Long id) {
        log.debug("REST request to delete AuditEventTrigger : {}", id);
        auditEventTriggerService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
