package com.mshz.web.rest;

import com.mshz.domain.AuditCycle;
import com.mshz.service.AuditCycleService;
import com.mshz.web.rest.errors.BadRequestAlertException;
import com.mshz.service.dto.AuditCycleCriteria;
import com.mshz.service.AuditCycleQueryService;

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
 * REST controller for managing {@link com.mshz.domain.AuditCycle}.
 */
@RestController
@RequestMapping("/api")
public class AuditCycleResource {

    private final Logger log = LoggerFactory.getLogger(AuditCycleResource.class);

    private static final String ENTITY_NAME = "microrisqueAuditCycle";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AuditCycleService auditCycleService;

    private final AuditCycleQueryService auditCycleQueryService;

    public AuditCycleResource(AuditCycleService auditCycleService, AuditCycleQueryService auditCycleQueryService) {
        this.auditCycleService = auditCycleService;
        this.auditCycleQueryService = auditCycleQueryService;
    }

    /**
     * {@code POST  /audit-cycles} : Create a new auditCycle.
     *
     * @param auditCycle the auditCycle to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new auditCycle, or with status {@code 400 (Bad Request)} if the auditCycle has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/audit-cycles")
    public ResponseEntity<AuditCycle> createAuditCycle(@Valid @RequestBody AuditCycle auditCycle) throws URISyntaxException {
        log.debug("REST request to save AuditCycle : {}", auditCycle);
        if (auditCycle.getId() != null) {
            throw new BadRequestAlertException("A new auditCycle cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AuditCycle result = auditCycleService.save(auditCycle);
        return ResponseEntity.created(new URI("/api/audit-cycles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /audit-cycles} : Updates an existing auditCycle.
     *
     * @param auditCycle the auditCycle to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated auditCycle,
     * or with status {@code 400 (Bad Request)} if the auditCycle is not valid,
     * or with status {@code 500 (Internal Server Error)} if the auditCycle couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/audit-cycles")
    public ResponseEntity<AuditCycle> updateAuditCycle(@Valid @RequestBody AuditCycle auditCycle) throws URISyntaxException {
        log.debug("REST request to update AuditCycle : {}", auditCycle);
        if (auditCycle.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AuditCycle result = auditCycleService.save(auditCycle);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, auditCycle.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /audit-cycles} : get all the auditCycles.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of auditCycles in body.
     */
    @GetMapping("/audit-cycles")
    public ResponseEntity<List<AuditCycle>> getAllAuditCycles(AuditCycleCriteria criteria, Pageable pageable) {
        log.debug("REST request to get AuditCycles by criteria: {}", criteria);
        Page<AuditCycle> page = auditCycleQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /audit-cycles/count} : count all the auditCycles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/audit-cycles/count")
    public ResponseEntity<Long> countAuditCycles(AuditCycleCriteria criteria) {
        log.debug("REST request to count AuditCycles by criteria: {}", criteria);
        return ResponseEntity.ok().body(auditCycleQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /audit-cycles/:id} : get the "id" auditCycle.
     *
     * @param id the id of the auditCycle to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the auditCycle, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/audit-cycles/{id}")
    public ResponseEntity<AuditCycle> getAuditCycle(@PathVariable Long id) {
        log.debug("REST request to get AuditCycle : {}", id);
        Optional<AuditCycle> auditCycle = auditCycleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(auditCycle);
    }

    /**
     * {@code DELETE  /audit-cycles/:id} : delete the "id" auditCycle.
     *
     * @param id the id of the auditCycle to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/audit-cycles/{id}")
    public ResponseEntity<Void> deleteAuditCycle(@PathVariable Long id) {
        log.debug("REST request to delete AuditCycle : {}", id);
        auditCycleService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
