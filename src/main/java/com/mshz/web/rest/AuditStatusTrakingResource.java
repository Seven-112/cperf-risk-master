package com.mshz.web.rest;

import com.mshz.domain.AuditStatusTraking;
import com.mshz.service.AuditStatusTrakingService;
import com.mshz.web.rest.errors.BadRequestAlertException;
import com.mshz.service.dto.AuditStatusTrakingCriteria;
import com.mshz.service.AuditStatusTrakingQueryService;

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
 * REST controller for managing {@link com.mshz.domain.AuditStatusTraking}.
 */
@RestController
@RequestMapping("/api")
public class AuditStatusTrakingResource {

    private final Logger log = LoggerFactory.getLogger(AuditStatusTrakingResource.class);

    private static final String ENTITY_NAME = "microrisqueAuditStatusTraking";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AuditStatusTrakingService auditStatusTrakingService;

    private final AuditStatusTrakingQueryService auditStatusTrakingQueryService;

    public AuditStatusTrakingResource(AuditStatusTrakingService auditStatusTrakingService, AuditStatusTrakingQueryService auditStatusTrakingQueryService) {
        this.auditStatusTrakingService = auditStatusTrakingService;
        this.auditStatusTrakingQueryService = auditStatusTrakingQueryService;
    }

    /**
     * {@code POST  /audit-status-trakings} : Create a new auditStatusTraking.
     *
     * @param auditStatusTraking the auditStatusTraking to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new auditStatusTraking, or with status {@code 400 (Bad Request)} if the auditStatusTraking has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/audit-status-trakings")
    public ResponseEntity<AuditStatusTraking> createAuditStatusTraking(@Valid @RequestBody AuditStatusTraking auditStatusTraking) throws URISyntaxException {
        log.debug("REST request to save AuditStatusTraking : {}", auditStatusTraking);
        if (auditStatusTraking.getId() != null) {
            throw new BadRequestAlertException("A new auditStatusTraking cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AuditStatusTraking result = auditStatusTrakingService.save(auditStatusTraking);
        return ResponseEntity.created(new URI("/api/audit-status-trakings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /audit-status-trakings} : Updates an existing auditStatusTraking.
     *
     * @param auditStatusTraking the auditStatusTraking to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated auditStatusTraking,
     * or with status {@code 400 (Bad Request)} if the auditStatusTraking is not valid,
     * or with status {@code 500 (Internal Server Error)} if the auditStatusTraking couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/audit-status-trakings")
    public ResponseEntity<AuditStatusTraking> updateAuditStatusTraking(@Valid @RequestBody AuditStatusTraking auditStatusTraking) throws URISyntaxException {
        log.debug("REST request to update AuditStatusTraking : {}", auditStatusTraking);
        if (auditStatusTraking.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AuditStatusTraking result = auditStatusTrakingService.save(auditStatusTraking);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, auditStatusTraking.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /audit-status-trakings} : get all the auditStatusTrakings.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of auditStatusTrakings in body.
     */
    @GetMapping("/audit-status-trakings")
    public ResponseEntity<List<AuditStatusTraking>> getAllAuditStatusTrakings(AuditStatusTrakingCriteria criteria, Pageable pageable) {
        log.debug("REST request to get AuditStatusTrakings by criteria: {}", criteria);
        Page<AuditStatusTraking> page = auditStatusTrakingQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /audit-status-trakings/count} : count all the auditStatusTrakings.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/audit-status-trakings/count")
    public ResponseEntity<Long> countAuditStatusTrakings(AuditStatusTrakingCriteria criteria) {
        log.debug("REST request to count AuditStatusTrakings by criteria: {}", criteria);
        return ResponseEntity.ok().body(auditStatusTrakingQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /audit-status-trakings/:id} : get the "id" auditStatusTraking.
     *
     * @param id the id of the auditStatusTraking to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the auditStatusTraking, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/audit-status-trakings/{id}")
    public ResponseEntity<AuditStatusTraking> getAuditStatusTraking(@PathVariable Long id) {
        log.debug("REST request to get AuditStatusTraking : {}", id);
        Optional<AuditStatusTraking> auditStatusTraking = auditStatusTrakingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(auditStatusTraking);
    }

    /**
     * {@code DELETE  /audit-status-trakings/:id} : delete the "id" auditStatusTraking.
     *
     * @param id the id of the auditStatusTraking to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/audit-status-trakings/{id}")
    public ResponseEntity<Void> deleteAuditStatusTraking(@PathVariable Long id) {
        log.debug("REST request to delete AuditStatusTraking : {}", id);
        auditStatusTrakingService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
