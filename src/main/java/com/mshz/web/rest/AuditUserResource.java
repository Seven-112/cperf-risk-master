package com.mshz.web.rest;

import com.mshz.domain.AuditUser;
import com.mshz.service.AuditUserService;
import com.mshz.web.rest.errors.BadRequestAlertException;
import com.mshz.service.dto.AuditUserCriteria;
import com.mshz.service.AuditUserQueryService;

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
 * REST controller for managing {@link com.mshz.domain.AuditUser}.
 */
@RestController
@RequestMapping("/api")
public class AuditUserResource {

    private final Logger log = LoggerFactory.getLogger(AuditUserResource.class);

    private static final String ENTITY_NAME = "microrisqueAuditUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AuditUserService auditUserService;

    private final AuditUserQueryService auditUserQueryService;

    public AuditUserResource(AuditUserService auditUserService, AuditUserQueryService auditUserQueryService) {
        this.auditUserService = auditUserService;
        this.auditUserQueryService = auditUserQueryService;
    }

    /**
     * {@code POST  /audit-users} : Create a new auditUser.
     *
     * @param auditUser the auditUser to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new auditUser, or with status {@code 400 (Bad Request)} if the auditUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/audit-users")
    public ResponseEntity<AuditUser> createAuditUser(@RequestBody AuditUser auditUser) throws URISyntaxException {
        log.debug("REST request to save AuditUser : {}", auditUser);
        if (auditUser.getId() != null) {
            throw new BadRequestAlertException("A new auditUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AuditUser result = auditUserService.save(auditUser);
        return ResponseEntity.created(new URI("/api/audit-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /audit-users} : Updates an existing auditUser.
     *
     * @param auditUser the auditUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated auditUser,
     * or with status {@code 400 (Bad Request)} if the auditUser is not valid,
     * or with status {@code 500 (Internal Server Error)} if the auditUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/audit-users")
    public ResponseEntity<AuditUser> updateAuditUser(@RequestBody AuditUser auditUser) throws URISyntaxException {
        log.debug("REST request to update AuditUser : {}", auditUser);
        if (auditUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AuditUser result = auditUserService.save(auditUser);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, auditUser.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /audit-users} : get all the auditUsers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of auditUsers in body.
     */
    @GetMapping("/audit-users")
    public ResponseEntity<List<AuditUser>> getAllAuditUsers(AuditUserCriteria criteria, Pageable pageable) {
        log.debug("REST request to get AuditUsers by criteria: {}", criteria);
        Page<AuditUser> page = auditUserQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /audit-users/count} : count all the auditUsers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/audit-users/count")
    public ResponseEntity<Long> countAuditUsers(AuditUserCriteria criteria) {
        log.debug("REST request to count AuditUsers by criteria: {}", criteria);
        return ResponseEntity.ok().body(auditUserQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /audit-users/:id} : get the "id" auditUser.
     *
     * @param id the id of the auditUser to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the auditUser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/audit-users/{id}")
    public ResponseEntity<AuditUser> getAuditUser(@PathVariable Long id) {
        log.debug("REST request to get AuditUser : {}", id);
        Optional<AuditUser> auditUser = auditUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(auditUser);
    }

    /**
     * {@code DELETE  /audit-users/:id} : delete the "id" auditUser.
     *
     * @param id the id of the auditUser to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/audit-users/{id}")
    public ResponseEntity<Void> deleteAuditUser(@PathVariable Long id) {
        log.debug("REST request to delete AuditUser : {}", id);
        auditUserService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
