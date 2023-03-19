package com.mshz.web.rest;

import com.mshz.domain.AuditRecomUser;
import com.mshz.service.AuditRecomUserService;
import com.mshz.web.rest.errors.BadRequestAlertException;
import com.mshz.service.dto.AuditRecomUserCriteria;
import com.mshz.service.AuditRecomUserQueryService;

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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.mshz.domain.AuditRecomUser}.
 */
@RestController
@RequestMapping("/api")
public class AuditRecomUserResource {

    private final Logger log = LoggerFactory.getLogger(AuditRecomUserResource.class);

    private static final String ENTITY_NAME = "microrisqueAuditRecomUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AuditRecomUserService auditRecomUserService;

    private final AuditRecomUserQueryService auditRecomUserQueryService;

    public AuditRecomUserResource(AuditRecomUserService auditRecomUserService, AuditRecomUserQueryService auditRecomUserQueryService) {
        this.auditRecomUserService = auditRecomUserService;
        this.auditRecomUserQueryService = auditRecomUserQueryService;
    }

    /**
     * {@code POST  /audit-recom-users} : Create a new auditRecomUser.
     *
     * @param auditRecomUser the auditRecomUser to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new auditRecomUser, or with status {@code 400 (Bad Request)} if the auditRecomUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/audit-recom-users")
    public ResponseEntity<AuditRecomUser> createAuditRecomUser(@RequestBody AuditRecomUser auditRecomUser) throws URISyntaxException {
        log.debug("REST request to save AuditRecomUser : {}", auditRecomUser);
        if (auditRecomUser.getId() != null) {
            throw new BadRequestAlertException("A new auditRecomUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AuditRecomUser result = auditRecomUserService.save(auditRecomUser);
        return ResponseEntity.created(new URI("/api/audit-recom-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /audit-recom-users} : Updates an existing auditRecomUser.
     *
     * @param auditRecomUser the auditRecomUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated auditRecomUser,
     * or with status {@code 400 (Bad Request)} if the auditRecomUser is not valid,
     * or with status {@code 500 (Internal Server Error)} if the auditRecomUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/audit-recom-users")
    public ResponseEntity<AuditRecomUser> updateAuditRecomUser(@RequestBody AuditRecomUser auditRecomUser) throws URISyntaxException {
        log.debug("REST request to update AuditRecomUser : {}", auditRecomUser);
        if (auditRecomUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AuditRecomUser result = auditRecomUserService.save(auditRecomUser);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, auditRecomUser.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /audit-recom-users} : get all the auditRecomUsers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of auditRecomUsers in body.
     */
    @GetMapping("/audit-recom-users")
    public ResponseEntity<List<AuditRecomUser>> getAllAuditRecomUsers(AuditRecomUserCriteria criteria, Pageable pageable) {
        log.debug("REST request to get AuditRecomUsers by criteria: {}", criteria);
        Page<AuditRecomUser> page = auditRecomUserQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /audit-recom-users/count} : count all the auditRecomUsers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/audit-recom-users/count")
    public ResponseEntity<Long> countAuditRecomUsers(AuditRecomUserCriteria criteria) {
        log.debug("REST request to count AuditRecomUsers by criteria: {}", criteria);
        return ResponseEntity.ok().body(auditRecomUserQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /audit-recom-users/:id} : get the "id" auditRecomUser.
     *
     * @param id the id of the auditRecomUser to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the auditRecomUser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/audit-recom-users/{id}")
    public ResponseEntity<AuditRecomUser> getAuditRecomUser(@PathVariable Long id) {
        log.debug("REST request to get AuditRecomUser : {}", id);
        Optional<AuditRecomUser> auditRecomUser = auditRecomUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(auditRecomUser);
    }

    /**
     * {@code DELETE  /audit-recom-users/:id} : delete the "id" auditRecomUser.
     *
     * @param id the id of the auditRecomUser to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/audit-recom-users/{id}")
    public ResponseEntity<Void> deleteAuditRecomUser(@PathVariable Long id) {
        log.debug("REST request to delete AuditRecomUser : {}", id);
        auditRecomUserService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
